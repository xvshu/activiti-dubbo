package com.eloancn.framework.activiti.redis.handler.impl;

import com.alibaba.fastjson.JSON;
import com.eloancn.framework.activiti.redis.client.ActivitiEventConsumer;
import com.eloancn.framework.activiti.redis.handler.ActivitiEventHandler;
import com.eloancn.framework.activiti.redis.model.ActivitiEventModel;
import com.eloancn.framework.activiti.redis.utils.EventType;
import com.eloancn.framework.activiti.user.ActivitiUserServiceFactory;
import com.eloancn.framework.activiti.util.WorkflowUtils;
import com.eloancn.framework.activiti.util.error.Preconditions;
import com.eloancn.organ.dto.UserDto;
import com.google.common.collect.Lists;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cmd.GetIdentityLinksForTaskCmd;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by xvshu on 2017/11/10.
 */
@Component
public class ActivitiStartHandler implements ActivitiEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ActivitiStartHandler.class);




    @Autowired
    private IdentityService identityService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ActivitiUserServiceFactory activitiUserServiceFactory;


    @Override
    public void doHandler(ActivitiEventModel eventModel) {
        logger.info("=ActivitiStartHandler=> begin");

        long timeBegin= System.currentTimeMillis();
        ProcessInstance processInstance = null;
        try {

            //TODO 检查参数
            Preconditions.checkNotNull(eventModel,"eventModel is null");
            Preconditions.checkNotNull(eventModel.getProcessDefinitionKey(),"processDefinitionKey is null");
            Preconditions.checkNotNull(eventModel.getUserId(),"userId is null");
            Preconditions.checkNotNull(eventModel.getBusinessKey(),"businessKey is null");

            if(eventModel.getVariables() == null){
                eventModel.setVariables(new HashMap());
            }

            Map<String, Object> variables = eventModel.getVariables();
            if(eventModel.getDepartment()!=null){
                variables.put(WorkflowUtils.departmentKey,eventModel.getDepartment());
            }
            if(eventModel.getArea()!=null){
                variables.put(WorkflowUtils.areaKey,eventModel.getArea());
            }

            variables.put("assigneeList",eventModel.getNextUserIds());
            eventModel.setVariables(variables);

            logger.info("=ActivitiStartHandler.variables=>{}", JSON.toJSONString(eventModel.getVariables()));


            // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
            identityService.setAuthenticatedUserId(eventModel.getUserId());

            logger.info("=ActivitiStartHandler=> setAuthenticatedUserId spen[{}]ms;",System.currentTimeMillis()-timeBegin);
            timeBegin= System.currentTimeMillis();

            processInstance = runtimeService.startProcessInstanceByKey(eventModel.getProcessDefinitionKey(), eventModel.getBusinessKey(), eventModel.getVariables());
            logger.info("=ActivitiStartHandler=>start process of {key={}, bkey={}, pid={}, variables={}}", new Object[]{"leave", eventModel.getBusinessKey(), processInstance.getId(), eventModel.getVariables()});

            logger.info("=ActivitiStartHandler=> startProcessInstanceByKey spen[{}]ms;",System.currentTimeMillis()-timeBegin);
            timeBegin= System.currentTimeMillis();

            //查询下一级待处理任务
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

            logger.info("=ActivitiStartHandler=> createTaskQuery spen[{}]ms;",System.currentTimeMillis()-timeBegin);
            timeBegin= System.currentTimeMillis();


            BpmnModel model = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
            Collection<FlowElement> flowElements=null;
            if(model != null) {
                flowElements = model.getMainProcess().getFlowElements();

            }


            //遍历每个任务，分配处理人
            for(int i = 0;i<tasks.size();i++){
                Task oneTask = tasks.get(i);

                //获取当前任务定义对象
                FlowElement thisTask = null;
                for(FlowElement fe : flowElements) {
                    if(fe.getId().equals(oneTask.getTaskDefinitionKey())){
                        thisTask=fe;
                        break;
                    }
                }

                //获取当前任务的待处理人
                List<String> candidateGroups = new ArrayList<String>();
                try{
                    if(thisTask!=null){
                        UserTask userTask = (UserTask)thisTask;
                        candidateGroups = userTask.getCandidateGroups();
                    }
                }catch (Exception ex){
                    logger.error("=ActivitiStartHandler=> change usertask error :",ex);
                }

                //判断当前任务定义是否有处理人，如果已经指定固定处理人则不做处理。
                if(oneTask.getAssignee()==null || oneTask.getAssignee().length()==0){
                    //如果没有下级处理人，则进行交互
                    if(eventModel.getNextUserIds()==null || eventModel.getNextUserIds().size()<1){
                        //判断是否有用户标识，如果有用户标识则进行用户标识查找
                        if(candidateGroups!=null
                                && candidateGroups.size()>0  ){

                            logger.info("=ActivitiStartHandler=> has candidateGroups task id :{} group:{}",oneTask.getId(),JSON.toJSONString(candidateGroups));

                            List<String> nextList = Lists.newArrayList();

                            // 根据用户标识读取用户
                            nextList = activitiUserServiceFactory.getUserService().getUsersByMark(candidateGroups,eventModel.getUserId());
                            eventModel.setNextUserIds(nextList);
                        }else{
                            //获取上级id
                            List<String> nextList = activitiUserServiceFactory.getUserService().getSuperiorUsers(eventModel.getUserId());
                            eventModel.setNextUserIds(nextList);
                        }

                    }

                    //如果传入了下一级处理人，如果只有一个，则这个人签收这个任务
                    //如果有多人处理人，则多个人为处理人，等待被签收
                    if(eventModel.getNextUserIds()!=null && eventModel.getNextUserIds().size()==1){
                        logger.info("=ActivitiStartHandler=> taskService.claim taskId:{} userId:{}",oneTask.getId(),eventModel.getNextUserIds().get(0));
                        taskService.claim(oneTask.getId(),eventModel.getNextUserIds().get(0) );
                    }else{
                        for(String nextUser:eventModel.getNextUserIds()){
                            logger.info("=ActivitiStartHandler=> taskService.addCandidateUser taskId:{} userId:{}",oneTask.getId(),nextUser);
                            taskService.addCandidateUser(oneTask.getId(),nextUser);
                        }
                    }
                }
            }

            logger.info("=ActivitiStartHandler=> end for spen[{}]ms;",System.currentTimeMillis()-timeBegin);
        }catch (Throwable th){
            logger.error("=ActivitiStartHandler=> error :",th);
            throw  th;
        }finally {
            identityService.setAuthenticatedUserId(null);
        }

    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.STARTWORKFLOW);
    }
}

