package com.eloancn.framework.activiti.redis.handler;

import com.eloancn.framework.activiti.redis.model.ActivitiEventModel;
import com.eloancn.framework.activiti.redis.utils.EventType;

import java.util.List;

/**
 * Created by xvshu on 2017/11/10.
 */
public interface  ActivitiEventHandler {
    //处理事件
    void doHandler(ActivitiEventModel eventModel);
    //获取关注事件的类型
    List<EventType> getSupportEventType();
}
