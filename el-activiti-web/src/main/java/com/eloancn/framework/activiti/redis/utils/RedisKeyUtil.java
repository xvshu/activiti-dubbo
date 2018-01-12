package com.eloancn.framework.activiti.redis.utils;

/**
 * Created by xvshu on 2017/11/9.
 */
public class RedisKeyUtil {
    private final static String EVENT_KEY = "FRM_ASYNC_EVENT_KEY";

    public static String getEventQueueKey(){
        return EVENT_KEY;
    }
}
