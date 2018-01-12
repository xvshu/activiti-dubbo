package com.eloancn.framework.activiti.model;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.Date;


/**
 * Created by xvshu on 2017/11/20.
 */
public class ELProcessInstance {

    protected String id = null;
    protected String startUserId=null;
    protected String activityId;
    protected String processInstanceId;
    protected String superExecutionId;
    protected String tenantId = ProcessEngineConfiguration.NO_TENANT_ID;
    protected String name;
    protected String nowActiviNoteName;
    protected String description;
    protected String localizedName;
    protected String localizedDescription;
    protected Date lockTime;
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected String deploymentId;
    protected String processDefinitionName;
    protected Integer processDefinitionVersion;
    protected String businessKey;
    protected String parentId;
    protected int suspensionState = SuspensionState.ACTIVE.getStateCode();
    public void setSuspensionState(boolean suspensionState) {
        if(suspensionState){
            this.suspensionState= SuspensionState.SUSPENDED.getStateCode();
        }else{
            this.suspensionState= SuspensionState.ACTIVE.getStateCode();
        }
    }

    public static ELProcessInstance covert(ProcessInstance processInstance) {
        if (processInstance == null) {
            return null;
        }
        ELProcessInstance eLProcessInstance = new ELProcessInstance();
        eLProcessInstance.setTenantId(processInstance.getTenantId());
        eLProcessInstance.setName(processInstance.getName());
        eLProcessInstance.setDescription(processInstance.getDescription());
        eLProcessInstance.setLocalizedName(processInstance.getLocalizedName());
        eLProcessInstance.setLocalizedDescription(processInstance.getLocalizedDescription());
        eLProcessInstance.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        eLProcessInstance.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
        eLProcessInstance.setProcessDefinitionName(processInstance.getProcessDefinitionName());
        eLProcessInstance.setProcessDefinitionVersion(processInstance.getProcessDefinitionVersion());
        eLProcessInstance.setDeploymentId(processInstance.getDeploymentId());
        eLProcessInstance.setActivityId(processInstance.getActivityId());
        eLProcessInstance.setProcessInstanceId(processInstance.getProcessInstanceId());
        eLProcessInstance.setBusinessKey(processInstance.getBusinessKey());
        eLProcessInstance.setParentId(processInstance.getParentId());
        eLProcessInstance.setSuperExecutionId(processInstance.getSuperExecutionId());
        eLProcessInstance.setSuspensionState(processInstance.isSuspended());
        eLProcessInstance.setId(processInstance.getId());
        return eLProcessInstance;
    }


    public int getSuspensionState() {
        return suspensionState;
    }

    public void setSuspensionState(int suspensionState) {
        this.suspensionState = suspensionState;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    public Integer getProcessDefinitionVersion() {
        return processDefinitionVersion;
    }

    public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
        this.processDefinitionVersion = processDefinitionVersion;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getSuperExecutionId() {
        return superExecutionId;
    }

    public void setSuperExecutionId(String superExecutionId) {
        this.superExecutionId = superExecutionId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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

    public String getLocalizedName() {
        return localizedName;
    }

    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }

    public String getLocalizedDescription() {
        return localizedDescription;
    }

    public void setLocalizedDescription(String localizedDescription) {
        this.localizedDescription = localizedDescription;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getNowActiviNoteName() {
        return nowActiviNoteName;
    }

    public void setNowActiviNoteName(String nowActiviNoteName) {
        this.nowActiviNoteName = nowActiviNoteName;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }
}
