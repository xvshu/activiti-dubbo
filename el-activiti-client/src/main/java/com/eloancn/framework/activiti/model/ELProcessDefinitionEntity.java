package com.eloancn.framework.activiti.model;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.repository.ProcessDefinition;

/**
 * Created by xvshu on 2017/11/20.
 */
public class ELProcessDefinitionEntity{

    protected String id;
    protected String name;
    protected String key;
    protected int revision = 1;
    protected String description;
    protected int version;
    protected String category;
    protected String deploymentId;
    protected String resourceName;
    protected String tenantId = ProcessEngineConfiguration.NO_TENANT_ID;
    protected String diagramResourceName;
    protected int suspensionState = SuspensionState.ACTIVE.getStateCode();


    public void setSuspensionState(boolean suspensionState) {
        if(suspensionState){
            this.suspensionState= SuspensionState.SUSPENDED.getStateCode();
        }else{
            this.suspensionState= SuspensionState.ACTIVE.getStateCode();
        }
    }

    public static ELProcessDefinitionEntity covert(ProcessDefinition processDefinition) {
        if (processDefinition == null) {
            return null;
        }
        ELProcessDefinitionEntity eLProcessDefinitionEntity = new ELProcessDefinitionEntity();
        eLProcessDefinitionEntity.setKey(processDefinition.getKey());
        eLProcessDefinitionEntity.setVersion(processDefinition.getVersion());
        eLProcessDefinitionEntity.setCategory(processDefinition.getCategory());
        eLProcessDefinitionEntity.setDeploymentId(processDefinition.getDeploymentId());
        eLProcessDefinitionEntity.setResourceName(processDefinition.getResourceName());
        eLProcessDefinitionEntity.setTenantId(processDefinition.getTenantId());
        eLProcessDefinitionEntity.setDiagramResourceName(processDefinition.getDiagramResourceName());
        eLProcessDefinitionEntity.setName(processDefinition.getName());
        eLProcessDefinitionEntity.setKey(processDefinition.getKey());
        eLProcessDefinitionEntity.setDescription(processDefinition.getDescription());
        eLProcessDefinitionEntity.setId(processDefinition.getId());
        eLProcessDefinitionEntity.setSuspensionState(processDefinition.isSuspended());
        return eLProcessDefinitionEntity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getDiagramResourceName() {
        return diagramResourceName;
    }

    public void setDiagramResourceName(String diagramResourceName) {
        this.diagramResourceName = diagramResourceName;
    }

    public int getSuspensionState() {
        return suspensionState;
    }

    public void setSuspensionState(int suspensionState) {
        this.suspensionState = suspensionState;
    }
}
