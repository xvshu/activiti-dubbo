package com.eloancn.framework.activiti.redis.task;


import com.eloancn.framework.activiti.redis.utils.RedisKeyUtil;
import com.eloancn.framework.activiti.util.error.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by xvshu on 2017/11/10.
 */
@Service
public class AlermTaskCache {

    private static final Logger logger = LoggerFactory.getLogger(AlermTaskCache.class);

    @Resource(name = "redisTemplate")
    private HashOperations hashOperations;

    @Resource(name = "redisTemplate")
    private RedisOperations redisOperations;

    public void initTaskPage(String key,Object values){
        Preconditions.checkNotNull(key,"ActivitiTaskCache.initTaskList key is null");
        Preconditions.checkNotNull(values,"ActivitiTaskCache.initTaskList values is null");
        hashOperations.put(RedisKeyUtil.getAlermKey(),key,values);
        expire(RedisKeyUtil.getAlermKey());
    }

    public boolean containKey(String key){
        Preconditions.checkNotNull(key,"ActivitiTaskCache.initTaskList key is null");
        return hashOperations.hasKey(RedisKeyUtil.getAlermKey(),key);
    }



    public void deleteTask(String key){
        Preconditions.checkNotNull(key,"ActivitiTaskCache.deleteTaskList key is null");
        hashOperations.delete(RedisKeyUtil.getAlermKey(),key);
    }

    public Object getTaskPage(String key){
        Preconditions.checkNotNull(key,"ActivitiTaskCache.getTaskList key is null");
        return hashOperations.get(RedisKeyUtil.getAlermKey(),key);
    }


    private boolean expire(String key){
        return redisOperations.expire(key,RedisKeyUtil.getAlermKeyExpire(), TimeUnit.SECONDS);
    }

    public void deleteAllTask(){
        redisOperations.delete(RedisKeyUtil.getAlermKey());
    }
}
