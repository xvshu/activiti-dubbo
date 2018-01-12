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
import org.apache.commons.collections.map.HashedMap;
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
public class ActivitiNoteComoleteHandler implements ActivitiEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ActivitiNoteComoleteHandler.class);

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;


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

            eventModel.getVariables().put("assigneeList",eventModel.getNextUserIds());
            taskService.complete(eventModel.getTaskId(), eventModel.getVariables());

            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();


            for(int i = 0;i<tasks.size();i++){
                Task oneTask = tasks.get(i);
                if(oneTask.getAssignee()==null || oneTask.getAssignee().length()==0){
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

