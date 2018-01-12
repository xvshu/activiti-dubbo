package com.eloancn.framework.activiti.redis.client;

import com.alibaba.fastjson.JSON;
import com.eloancn.framework.activiti.redis.handler.ActivitiEventHandler;
import com.eloancn.framework.activiti.redis.handler.impl.ActivitiStartHandler;
import com.eloancn.framework.activiti.redis.model.ActivitiEventModel;
import com.eloancn.framework.activiti.redis.utils.EventType;
import com.eloancn.framework.activiti.redis.utils.RedisKeyUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by xvshu on 2017/11/10.
 */
@Service
public class ActivitiEventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(ActivitiEventConsumer.class);

    private ApplicationContext applicationContext;
    //存储各种type事件对应的Handler处理类
    private Map<EventType, List<ActivitiEventHandler>> config = new HashMap<>();

    @Resource(name = "redisTemplate")
    private ListOperations listOperations;

    @Override
    public void afterPropertiesSet() throws Exception {
        //获取所有实现EventHandler的类
        Map<String, ActivitiEventHandler> eventHandlerMap = applicationContext.getBeansOfType(ActivitiEventHandler.class);
        if (eventHandlerMap != null){
            for (Map.Entry<String, ActivitiEventHandler> entry : eventHandlerMap.entrySet()){
                //遍历每个EventHandler，将这个EventHandler放到集合对应的type中
                ActivitiEventHandler eventHandler = entry.getValue();
                //获取每个EventHandler所关注的事件类型
                List<EventType> eventTypes = eventHandler.getSupportEventType();

                for (EventType type : eventTypes){
                    //初始化的时候config为空
                    if (!config.containsKey(type)){
                        config.put(type, new ArrayList<ActivitiEventHandler>());
                    }
                    //将Handler放入指定type中
                    config.get(type).add(eventHandler);

                }
            }
        }

        //启动线程从工作队列中取出事件进行处理
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){
                    String key = RedisKeyUtil.getEventQueueKey();
                    //获取存储的(经过序列化的)事件event
                    String jsonEvent = (String)listOperations.rightPop(key,0, TimeUnit.SECONDS);

                    if (jsonEvent.equals(key)){
                        continue;
                    }

                    ActivitiEventModel eventModel = JSON.parseObject(jsonEvent, ActivitiEventModel.class);

                    if (!config.containsKey(eventModel.getEventType())){
                        logger.error("不能识别的事件！");
                        continue;
                    }
                    //获取关注过该事件eventModel的handler,进行处理
                    for (ActivitiEventHandler handler : config.get(eventModel.getEventType())){
                        handler.doHandler(eventModel);
                    }

                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


}
