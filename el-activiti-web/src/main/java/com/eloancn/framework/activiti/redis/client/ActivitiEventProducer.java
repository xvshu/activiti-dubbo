package com.eloancn.framework.activiti.redis.client;


import com.alibaba.fastjson.JSONObject;
import com.eloancn.framework.activiti.redis.model.ActivitiEventModel;
import com.eloancn.framework.activiti.redis.utils.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by xvshu on 2017/11/10.
 */
@Service
public class ActivitiEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(ActivitiEventProducer.class);

    @Resource(name = "redisTemplate")
    private ListOperations listOperations;

    public boolean fireEvent(ActivitiEventModel activitiEventModel){
        try {
            //序列化
            String json = JSONObject.toJSONString(activitiEventModel);
            //产生key
            String activitiListKey = RedisKeyUtil.getEventQueueKey();
            //放入工作队列中
            listOperations.leftPush(activitiListKey, json);
            return true;
        }catch (Exception e){
            logger.error("=ActivitiEventProducer=> fireEvent 异常 ："+e.getMessage());
            return false;
        }

    }

}
