package com.eloancn.framework.activiti.service.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * Created by xvshu on 2017/11/22.
 */
public class MQActivitiService implements JavaDelegate {

    MQActivitiService(){};

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        //流程id
        execution.getProcessInstanceId();

    }
}
