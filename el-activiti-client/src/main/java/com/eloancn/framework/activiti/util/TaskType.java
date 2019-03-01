package com.eloancn.framework.activiti.util;

/**
 * Created by xvshu on 2018/2/27.
 */
public enum  TaskType {
    ActivitiTask("activiti_task"),
    AlermTask("alerm_task");

    private String type;
    TaskType(String type){
        this.type=type;
    }

    public String getType() {
        return type;
    }
}
