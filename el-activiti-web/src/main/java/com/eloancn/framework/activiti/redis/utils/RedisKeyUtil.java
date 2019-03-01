package com.eloancn.framework.activiti.redis.utils;

/**
 * Created by xvshu on 2017/11/9.
 */
public class RedisKeyUtil {
    private final static String EVENT_KEY = "PF_XS_FRM_ACTIVITI_ASYNC_EVENT_KEY";
    private final static String TASK_KEY = "PF_XS_FRM_ACTIVITI_ASYNC_TASK_KEY";
    private final static String ALERM_KEY="PF_XS_FRM_ACTIVITI_ASYNC_ALERM_KEY";

    private final static long TASK_KEY_EXPIRE = 10;
    private final static long ALERM_KEY_EXPIRE = 10;

    public static String getEventQueueKey(){
        return EVENT_KEY;
    }

    public static String getTaskKey() {
        return TASK_KEY;
    }

    public static String getAlermKey() {
        return ALERM_KEY;
    }

    public static long getTaskKeyExpire() {
        return TASK_KEY_EXPIRE;
    }

    public static long getAlermKeyExpire() {
        return ALERM_KEY_EXPIRE;
    }
}
