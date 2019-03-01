package com.eloancn.framework.activiti.redis.task;


import com.alibaba.fastjson.JSONObject;
import com.eloancn.framework.activiti.redis.model.ActivitiEventModel;
import com.eloancn.framework.activiti.redis.utils.RedisKeyUtil;
import com.eloancn.framework.activiti.util.error.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by xvshu on 2017/11/10.
 */
@Service
public class ActivitiTaskCache {

    private static final Logger logger = LoggerFactory.getLogger(ActivitiTaskCache.class);

    @Resource(name = "redisTemplate")
    private HashOperations hashOperations;

    @Resource(name = "redisTemplate")
    private RedisOperations redisOperations;

    public void initTaskPage(String key,Object values){
        Preconditions.checkNotNull(key,"ActivitiTaskCache.initTaskList key is null");
        Preconditions.checkNotNull(values,"ActivitiTaskCache.initTaskList values is null");
        hashOperations.put(RedisKeyUtil.getTaskKey(),key,values);
        expire(RedisKeyUtil.getTaskKey());
    }

    public boolean containKey(String key){
        Preconditions.checkNotNull(key,"ActivitiTaskCache.initTaskList key is null");
        return hashOperations.hasKey(RedisKeyUtil.getTaskKey(),key);
    }



    public void deleteTask(String key){
        Preconditions.checkNotNull(key,"ActivitiTaskCache.deleteTaskList key is null");
        hashOperations.delete(RedisKeyUtil.getTaskKey(),key);
    }

    public Object getTaskPage(String key){
        Preconditions.checkNotNull(key,"ActivitiTaskCache.getTaskList key is null");
        return hashOperations.get(RedisKeyUtil.getTaskKey(),key);
    }


    private boolean expire(String key){
        return redisOperations.expire(key,RedisKeyUtil.getTaskKeyExpire(), TimeUnit.SECONDS);
    }

    public void deleteAllTask(){
        redisOperations.delete(RedisKeyUtil.getTaskKey());
    }
}
