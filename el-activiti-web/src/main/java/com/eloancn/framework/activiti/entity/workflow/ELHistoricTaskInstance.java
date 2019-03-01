package com.eloancn.framework.activiti.entity.workflow;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;

/**
 * Created by Administrator on 2019/1/31 0031.
 */
public class ELHistoricTaskInstance extends HistoricTaskInstanceEntity {
    private String msg ;
    private Boolean pass;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getPass() {
        return pass;
    }

    public void setPass(Boolean pass) {
        this.pass = pass;
    }


    public static ELHistoricTaskInstance cover(HistoricTaskInstanceEntity input) {
        if (input == null) {
            return null;
        }
        ELHistoricTaskInstance eLHistoricTaskInstance = new ELHistoricTaskInstance();
        eLHistoricTaskInstance.setExecutionId(input.getExecutionId());
        eLHistoricTaskInstance.setName(input.getName());
        eLHistoricTaskInstance.setParentTaskId(input.getParentTaskId());
        eLHistoricTaskInstance.setDescription(input.getDescription());
        eLHistoricTaskInstance.setOwner(input.getOwner());
        eLHistoricTaskInstance.setAssignee(input.getAssignee());
        eLHistoricTaskInstance.setTaskDefinitionKey(input.getTaskDefinitionKey());
        eLHistoricTaskInstance.setFormKey(input.getFormKey());
        eLHistoricTaskInstance.setPriority(input.getPriority());
        eLHistoricTaskInstance.setDueDate(input.getDueDate());
        eLHistoricTaskInstance.setClaimTime(input.getClaimTime());
        eLHistoricTaskInstance.setCategory(input.getCategory());
        eLHistoricTaskInstance.setTenantId(input.getTenantId());
        eLHistoricTaskInstance.setQueryVariables(input.getQueryVariables());
        eLHistoricTaskInstance.setId(input.getId());
        eLHistoricTaskInstance.setProcessInstanceId(input.getProcessInstanceId());
        eLHistoricTaskInstance.setProcessDefinitionId(input.getProcessDefinitionId());
        eLHistoricTaskInstance.setProcessDefinitionKey(input.getProcessDefinitionKey());
        eLHistoricTaskInstance.setProcessDefinitionName(input.getProcessDefinitionName());
        eLHistoricTaskInstance.setProcessDefinitionVersion(input.getProcessDefinitionVersion());
        eLHistoricTaskInstance.setDeploymentId(input.getDeploymentId());
        eLHistoricTaskInstance.setStartTime(input.getStartTime());
        eLHistoricTaskInstance.setEndTime(input.getEndTime());
        eLHistoricTaskInstance.setDurationInMillis(input.getDurationInMillis());
        eLHistoricTaskInstance.setDeleteReason(input.getDeleteReason());
        return eLHistoricTaskInstance;
    }
}
