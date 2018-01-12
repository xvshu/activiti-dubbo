package com.eloancn.framework.activiti.entity.workflow;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;

import java.util.Date;

/**
 * Created by xvshu on 2017/11/27.
 */
public class ELTask {


    protected String owner;
    protected String assignee;
    protected String initialAssignee;
    protected String parentTaskId;
    protected String name;
    protected String description;
    protected String localizedDescription;
    protected int priority=50;
    protected Date createTime; // The time when the task has been created
    protected Date dueDate;
    protected int suspensionState = SuspensionState.ACTIVE.getStateCode();
    protected String category;
    protected String executionId;
    protected String processInstanceId;
    protected String processDefinitionId;
    protected String taskDefinitionKey;
    protected String formKey;
    protected String tenantId = ProcessEngineConfiguration.NO_TENANT_ID;
    protected String id = null;

    public static ELTask convert(Task taskEntity) {
        if (taskEntity == null) {
            return null;
        }
        ELTask eLTask = new ELTask();
        eLTask.setOwner(taskEntity.getOwner());
        eLTask.setAssignee(taskEntity.getAssignee());
        eLTask.setParentTaskId(taskEntity.getParentTaskId());
        eLTask.setName(taskEntity.getName());
        eLTask.setDescription(taskEntity.getDescription());
         eLTask.setPriority(taskEntity.getPriority());
        eLTask.setCreateTime(taskEntity.getCreateTime());
        eLTask.setDueDate(taskEntity.getDueDate());
        eLTask.setCategory(taskEntity.getCategory());
        eLTask.setExecutionId(taskEntity.getExecutionId());
        eLTask.setProcessInstanceId(taskEntity.getProcessInstanceId());
        eLTask.setProcessDefinitionId(taskEntity.getProcessDefinitionId());
        eLTask.setTaskDefinitionKey(taskEntity.getTaskDefinitionKey());
        eLTask.setFormKey(taskEntity.getFormKey());
        eLTask.setTenantId(taskEntity.getTenantId());
        eLTask.setId(taskEntity.getId());
        return eLTask;
    }


    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getInitialAssignee() {
        return initialAssignee;
    }

    public void setInitialAssignee(String initialAssignee) {
        this.initialAssignee = initialAssignee;
    }

    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocalizedDescription() {
        return localizedDescription;
    }

    public void setLocalizedDescription(String localizedDescription) {
        this.localizedDescription = localizedDescription;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getSuspensionState() {
        return suspensionState;
    }

    public void setSuspensionState(int suspensionState) {
        this.suspensionState = suspensionState;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }
}
