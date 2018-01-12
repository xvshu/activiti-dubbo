package com.eloancn.framework.activiti.redis.handler.impl;

import com.alibaba.fastjson.JSON;
import com.eloancn.framework.activiti.redis.client.ActivitiEventConsumer;
import com.eloancn.framework.activiti.redis.handler.ActivitiEventHandler;
import com.eloancn.framework.activiti.redis.model.ActivitiEventModel;
import com.eloancn.framework.activiti.redis.utils.EventType;
import com.eloancn.framework.activiti.util.WorkflowUtils;
import com.eloancn.framework.activiti.util.error.Preconditions;
import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xvshu on 2017/11/10.
 */
@Component
public class ActivitiStartHandler implements ActivitiEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ActivitiStartHandler.class);


    @Autowired
    private IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;


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
            Preconditions.checkNotNullOrEmpty(eventModel.getNextUserIds(),"nextUserIds is empty");

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

            logger.info("=ActivitiStartHandler.variables=>", JSON.toJSONString(eventModel.getVariables()));

            //TODO 与组织机构平台交互转换用户id


            // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
            identityService.setAuthenticatedUserId(eventModel.getUserId());

            logger.info("=ActivitiStartHandler=> setAuthenticatedUserId spen[{}]ms;",System.currentTimeMillis()-timeBegin);
            timeBegin= System.currentTimeMillis();

            processInstance = runtimeService.startProcessInstanceByKey(eventModel.getProcessDefinitionKey(), eventModel.getBusinessKey(), eventModel.getVariables());
            logger.info("=ActivitiStartHandler=>start process of {key={}, bkey={}, pid={}, variables={}}", new Object[]{"leave", eventModel.getBusinessKey(), processInstance.getId(), eventModel.getVariables()});

            logger.info("=ActivitiStartHandler=> startProcessInstanceByKey spen[{}]ms;",System.currentTimeMillis()-timeBegin);
            timeBegin= System.currentTimeMillis();

            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

            logger.info("=ActivitiStartHandler=> createTaskQuery spen[{}]ms;",System.currentTimeMillis()-timeBegin);
            timeBegin= System.currentTimeMillis();

            for(int i = 0;i<tasks.size();i++){
                Task oneTask = tasks.get(i);
                if(oneTask.getAssignee()==null || oneTask.getAssignee().length()==0){
                    if(eventModel.getNextUserIds().size()==1){
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

