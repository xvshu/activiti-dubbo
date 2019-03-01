package com.eloancn.framework.activiti.conf;

import org.springframework.stereotype.Service;

/**
 * Created by xvshu on 2017/11/28.
 */
public class AdminUsers {
    private String adminUserId="admin";

    public String getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }
}
