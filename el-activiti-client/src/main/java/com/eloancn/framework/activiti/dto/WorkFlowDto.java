package com.eloancn.framework.activiti.dto;

import com.eloancn.organ.common.BusCodeEnum;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by xvshu on 2018/1/25.
 */
public class WorkFlowDto implements Serializable {
    /**
     * 流程定义key
     */
    private String processDefinitionKey;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 启动流程用户id
     */
    private String userId;

    /**
     * 流程业务唯一key
     */
    private String businessKey;

    /**
     * 下一个执行人的用户id
     */
    private List<String> nextUserIds;

    /**
     * 流程变量 Map<String, Object>
     */
    private Map<String, Object> variables;

    /**
     * 系统标识
     */
    private BusCodeEnum busCodeEnum;

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public List<String> getNextUserIds() {
        return nextUserIds;
    }

    public void setNextUserIds(List<String> nextUserIds) {
        this.nextUserIds = nextUserIds;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public BusCodeEnum getBusCodeEnum() {
        return busCodeEnum;
    }

    public void setBusCodeEnum(BusCodeEnum busCodeEnum) {
        this.busCodeEnum = busCodeEnum;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * 启动流程
     * @param processDefinitionKey 流程定义key（必须）
     * @param userId 启动流程用户id（必须）
     * @param businessKey 流程业务唯一key（必须）
     * @param nextUserIds 下一个执行人的用户id
     * @param variables 流程变量 Map<String, Object>
     * @param busCodeEnum 系统标识
     * @return
     */
    public WorkFlowDto(String processDefinitionKey, String taskId, String userId, String businessKey, List<String> nextUserIds, Map<String, Object> variables, BusCodeEnum busCodeEnum) {
        this.processDefinitionKey = processDefinitionKey;
        this.taskId = taskId;
        this.userId = userId;
        this.businessKey = businessKey;
        this.nextUserIds = nextUserIds;
        this.variables = variables;
        this.busCodeEnum = busCodeEnum;
    }
}
