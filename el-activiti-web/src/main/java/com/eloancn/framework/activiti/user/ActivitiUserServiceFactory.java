package com.eloancn.framework.activiti.user;

import com.eloancn.framework.activiti.user.impl.ElActivitiUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2019/1/22 0022.
 */
@Service
public class ActivitiUserServiceFactory {

    @Autowired
    private ElActivitiUser elActivitiUser;

    public IActivitiUser getUserService(){
         return elActivitiUser;
    }
}
