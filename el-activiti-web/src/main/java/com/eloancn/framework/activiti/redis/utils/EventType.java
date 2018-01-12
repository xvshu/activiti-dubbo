package com.eloancn.framework.activiti.redis.utils;

/**
 * Created by xvshu on 2017/11/9.
 */
public enum EventType {


    STARTWORKFLOW(0), //启动流程
    NODECOMPLETE(1);  //处理流程


    private int value;
    public int getValue(){
        return value;
    }

    EventType(int value){
        this.value = value;
    }

}
