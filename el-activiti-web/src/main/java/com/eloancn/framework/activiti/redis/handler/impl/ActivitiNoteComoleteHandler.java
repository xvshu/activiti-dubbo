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
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by xvshu on 2017/11/10.
 */
@Component
public class ActivitiNoteComoleteHandler implements ActivitiEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ActivitiNoteComoleteHandler.class);

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;


    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ActivitiUserServiceFactory activitiUserServiceFactory;


    @Override
    public void doHandler(ActivitiEventModel eventModel) {

        long timeBegin= System.currentTimeMillis();
        try {
            Preconditions.checkNotNull(eventModel,"eventModel is null");
            Preconditions.checkNotNull(eventModel.getTaskId(),"taskid is null");

            Task nowTask = taskService.createTaskQuery().taskId(eventModel.getTaskId()).singleResult();
            String processInstanceId = nowTask.getProcessInstanceId();

            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            Map<String, Object> pvariables = processInstance.getProcessVariables();
            if(pvariables==null){
                pvariables = new HashedMap();
            }

            if(eventModel.getVariables()==null){
                eventModel.setVariables(pvariables);
            }else{
                Map<String, Object> cvariables = eventModel.getVariables();
                pvariables.putAll(cvariables);
                eventModel.setVariables(pvariables);
            }
            logger.info("=ActivitiNoteComoleteHandler.pvariables=>", JSON.toJSONString(pvariables));

            //完成当前节点
            if(eventModel.getNextUserIds()!=null){
                eventModel.getVariables().put("assigneeList",eventModel.getNextUserIds());
            }
            if(eventModel.getVariables()!=null
                    && eventModel.getVariables().size()>0){
                taskService.setVariablesLocal(eventModel.getTaskId(),eventModel.getVariables());
            }
            taskService.complete(eventModel.getTaskId(), eventModel.getVariables());


            BpmnModel model = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
            Collection<FlowElement> flowElements=null;
            if(model != null) {
                flowElements = model.getMainProcess().getFlowElements();
            }

            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();



            for(int i = 0;i<tasks.size();i++){
                Task oneTask = tasks.get(i);

                //获取当前task节点设计
                FlowElement thisTask = null;
                for(FlowElement fe : flowElements) {
                    if(fe.getId().equals(oneTask.getTaskDefinitionKey())){
                        thisTask=fe;
                        break;
                    }
                }

                //查询当前节点候选组（人员标识）
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
                            //获取某标记的人员id，交互区域
                            logger.info("=ActivitiStartHandler=>task id :{} group:{}",oneTask.getId(),JSON.toJSONString(candidateGroups));

                            List<String> nextList = Lists.newArrayList();

                            // 根据用户标识读取用户
                            nextList = activitiUserServiceFactory.getUserService().getUsersByMark(candidateGroups,eventModel.getUserId());
                            eventModel.setNextUserIds(nextList);

                        }else{
                            //todo:获取上级id
                            List<String> nextList = activitiUserServiceFactory.getUserService().getSuperiorUsers(eventModel.getUserId());
                            eventModel.setNextUserIds(nextList);
                        }

                    }

                    //如果传入了下一级处理人，如果只有一个，则这个人签收这个任务
                    //如果有多人处理人，则多个人为处理人，等待被签收
                    if(eventModel.getNextUserIds().size()==1){
                        logger.info("=ActivitiNoteComoleteHandler=> taskService.claim taskId:{} userId:{}",oneTask.getId(),eventModel.getNextUserIds().get(0));
                        taskService.claim(oneTask.getId(),eventModel.getNextUserIds().get(0) );
                    }else{
                        for(String nextUser:eventModel.getNextUserIds()){
                            logger.info("=ActivitiNoteComoleteHandler=> taskService.addCandidateUser taskId:{} userId:{}",oneTask.getId(),nextUser);
                            taskService.addCandidateUser(oneTask.getId(),nextUser);
                        }
                    }
                }
            }

            logger.info("=ActivitiNoteComoleteHandler=> spen[{}]ms;",System.currentTimeMillis()-timeBegin);
        } catch (Exception e) {
            logger.error("=ActivitiNoteComoleteHandler=>error on complete task {}, variables={}", new Object[]{eventModel.getTaskId(), eventModel.getVariables(), e});
            logger.info("=ActivitiNoteComoleteHandler=> spen[{}]ms;",System.currentTimeMillis()-timeBegin);
        }

    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.NODECOMPLETE);
    }
}

