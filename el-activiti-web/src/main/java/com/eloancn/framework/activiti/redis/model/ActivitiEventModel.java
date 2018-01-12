package com.eloancn.framework.activiti.redis.model;

import com.eloancn.framework.activiti.redis.utils.EventType;
import com.eloancn.organ.common.BusCodeEnum;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by xvshu on 2017/11/9.
 */
public class ActivitiEventModel implements Serializable {

    /**
     * 初始化
     * @param eventType  * 事件类型：
     * STARTWORKFLOW(0), //启动流程
     * NODECOMPLETE(1);  //处理流程
     *
     * @param processDefinitionKey 流程定义key
     * @param userId 当前处理人id
     * @param businessKey 业务key
     * @param department  部门id
     * @param area  区域id
     * @param nextUserIds  下一批处理的人
     * @param variables 传入流程的参数集合
     * @param taskId 当前处理taskid
     * @param busCodeEnum 系统标识
     */
    public ActivitiEventModel(EventType eventType, String processDefinitionKey, String userId, String businessKey, String department, String area, List<String> nextUserIds, Map<String, Object> variables, String taskId, BusCodeEnum busCodeEnum) {
        this.eventType = eventType;
        this.processDefinitionKey = processDefinitionKey;
        this.userId = userId;
        this.businessKey = businessKey;
        this.department = department;
        this.area = area;
        this.nextUserIds = nextUserIds;
        this.variables = variables;
        this.taskId = taskId;
        this.busCodeEnum = busCodeEnum;
    }

    public ActivitiEventModel() {
    }

    /**
     * 事件类型：
     * STARTWORKFLOW(0), //启动流程
     * NODECOMPLETE(1);  //处理流程
     */
    private EventType eventType;

    /**
     * 流程定义key
     */
    private String processDefinitionKey;

    /**
     * 当前处理人id
     */
    private String userId;

    /**
     * 业务key
     */
    private String businessKey;

    /**
     * 部门id
     */
    private String department;

    /**
     * 区域id
     */
    private String area;

    /**
     * 下一批处理的人
     */
    private List<String> nextUserIds;

    /**
     * 传入流程的参数集合
     */
    private Map<String, Object> variables;

    /**
     * 当前处理taskid
     */
    private String taskId;

    /**
     * 系统标识
     */
    private BusCodeEnum busCodeEnum;

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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
}
