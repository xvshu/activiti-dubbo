package com.eloancn.framework.activiti.service.activiti;

import com.eloancn.framework.activiti.util.WorkflowUtils;
import org.activiti.engine.*;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 工作流跟踪相关Service
 *
 * @author xvshu
 */
@Component
public class WorkflowTraceService {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RuntimeService runtimeService;

    @Autowired
    protected TaskService taskService;

    @Autowired
    protected RepositoryService repositoryService;

    @Autowired
    protected IdentityService identityService;

    @Autowired
    protected HistoryService historyService;

    /**
     * 流程跟踪图
     *
     * @param processInstanceId 流程实例ID
     * @return 封装了各种节点信息
     */
    public List<Map<String, Object>> traceProcess(String processInstanceId) throws Exception {
        Execution execution = runtimeService.createExecutionQuery().executionId(processInstanceId).singleResult();//执行实例
        Object property = PropertyUtils.getProperty(execution, "activityId");
        String activityId = "";
        if (property != null) {
            activityId = property.toString();
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
                .singleResult();

        List<HistoricTaskInstance> tasks=historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).finished().list();
//        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();


        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
        List<ActivityImpl> activitiList = processDefinition.getActivities();//获得当前任务的所有节点

        List<Map<String, Object>> activityInfos = new ArrayList<Map<String, Object>>();
        for (ActivityImpl activity : activitiList) {

            boolean currentActiviti = false;
            String id = activity.getId();
            HistoricTaskInstance taskForActivity = null;
            for(HistoricTaskInstance oneT : tasks){
                if(oneT.getTaskDefinitionKey().equals(id)){
                    taskForActivity=oneT;
                    break;
                }
            }

            // 当前节点
            if (id.equals(activityId)) {
                currentActiviti = true;
            }

            Map<String, Object> activityImageInfo = packageSingleActivitiInfo(activity, processInstance, currentActiviti,taskForActivity);

            activityInfos.add(activityImageInfo);
        }

        return activityInfos;
    }

    /**
     * 封装输出信息，包括：当前节点的X、Y坐标、变量信息、任务类型、任务描述
     *
     * @param activity
     * @param processInstance
     * @param currentActiviti
     * @return
     */
    private Map<String, Object> packageSingleActivitiInfo(ActivityImpl activity, ProcessInstance processInstance,
                                                          boolean currentActiviti,HistoricTaskInstance task) throws Exception {
        Map<String, Object> vars = new HashMap<String, Object>();
        Map<String, Object> activityInfo = new HashMap<String, Object>();
        activityInfo.put("currentActiviti", currentActiviti);
        setPosition(activity, activityInfo);
        setWidthAndHeight(activity, activityInfo);

        Map<String, Object> properties = activity.getProperties();
//        Map<String, Object> acvars = activity.getVariables();
        vars.put("任务类型", WorkflowUtils.parseToZhType(properties.get("type").toString()));
        Map<String, Object> acvars = new HashMap<>();
        if(task!=null){
            List<HistoricVariableInstance> listVars = historyService.createHistoricVariableInstanceQuery().taskId(task.getId()).list();
            for(HistoricVariableInstance onevar : listVars){
                acvars.put(onevar.getVariableName(),onevar.getValue());
            }
        }

        if(acvars!=null){
            vars.put("是否通过", WorkflowUtils.parseToZhType(acvars.get("pass")==null ? "暂无":((Boolean)acvars.get("pass"))==true?"通过":"驳回"));
            vars.put("审核意见", WorkflowUtils.parseToZhType(acvars.get("msg")==null ? "暂无":acvars.get("msg").toString()));

        }

        ActivityBehavior activityBehavior = activity.getActivityBehavior();
        logger.debug("activityBehavior={}", activityBehavior);
        if (activityBehavior instanceof UserTaskActivityBehavior) {

            Task currentTask = null;

			/*
             * 当前节点的task
			 */
            if (currentActiviti) {
                currentTask = getCurrentTaskInfo(processInstance);
            }

			/*
			 * 当前任务的分配角色
			 */
            UserTaskActivityBehavior userTaskActivityBehavior = (UserTaskActivityBehavior) activityBehavior;
            TaskDefinition taskDefinition = userTaskActivityBehavior.getTaskDefinition();
            Set<Expression> candidateGroupIdExpressions = taskDefinition.getCandidateGroupIdExpressions();
            if (!candidateGroupIdExpressions.isEmpty()) {

                // 任务的处理角色
                setTaskGroup(vars, candidateGroupIdExpressions);

                // 当前处理人
                if (currentTask != null) {
                    setCurrentTaskAssignee(vars, currentTask);
                }
            }
        }

        vars.put("节点说明", properties.get("documentation"));

        String description = activity.getProcessDefinition().getDescription();
        vars.put("描述", description);

        logger.debug("trace variables: {}", vars);
        activityInfo.put("vars", vars);
        return activityInfo;
    }

    private void setTaskGroup(Map<String, Object> vars, Set<Expression> candidateGroupIdExpressions) {
        String roles = "[";
        for (Expression expression : candidateGroupIdExpressions) {
            String expressionText = expression.getExpressionText();
            roles += expressionText +",";
        }
        roles=roles.substring(0,roles.length()-1)+"]";
        vars.put("任务所属岗位code", roles);
    }

    /**
     * 设置当前处理人信息
     *
     * @param vars
     * @param currentTask
     */
    private void setCurrentTaskAssignee(Map<String, Object> vars, Task currentTask) {
        String assignee = currentTask.getAssignee();
//        if (assignee != null) {
//            User assigneeUser = identityService.createUserQuery().userId(assignee).singleResult();
//            String userInfo = assigneeUser.getFirstName() + " " + assigneeUser.getLastName();
//            vars.put("当前处理人", assignee);
//        }
        vars.put("当前处理人", assignee);
    }

    /**
     * 获取当前节点信息
     *
     * @param processInstance
     * @return
     */
    private Task getCurrentTaskInfo(ProcessInstance processInstance) {
        Task currentTask = null;
        try {
            String activitiId = (String) PropertyUtils.getProperty(processInstance, "activityId");
            logger.debug("current activity id: {}", activitiId);

            currentTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDefinitionKey(activitiId)
                    .singleResult();
            logger.debug("current task for processInstance: {}", ToStringBuilder.reflectionToString(currentTask));

        } catch (Exception e) {
            logger.error("can not get property activityId from processInstance: {}", processInstance);
        }
        return currentTask;
    }

    /**
     * 设置宽度、高度属性
     *
     * @param activity
     * @param activityInfo
     */
    private void setWidthAndHeight(ActivityImpl activity, Map<String, Object> activityInfo) {
        activityInfo.put("width", activity.getWidth());
        activityInfo.put("height", activity.getHeight());
    }

    /**
     * 设置坐标位置
     *
     * @param activity
     * @param activityInfo
     */
    private void setPosition(ActivityImpl activity, Map<String, Object> activityInfo) {
        activityInfo.put("x", activity.getX());
        activityInfo.put("y", activity.getY());
    }
}
