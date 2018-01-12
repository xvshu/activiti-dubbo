package com.eloancn.framework.activiti.util.demo;

import org.activiti.engine.identity.User;

import javax.servlet.http.HttpSession;

/**
 * Created by xvshu on 2017/11/1.
 */
public class ActivitiUtils {

    private static String USERID = "DEMO_USER_ID";

    /**
     * 设置用户到session
     *
     * @param session
     * @param userID
     */
    public static void saveUserToSession(HttpSession session, String userID) {
        session.setAttribute(USERID, userID);
    }


    /**
     * 从Session获取当前用户信息
     *
     * @param session
     * @return
     */
    public static String getUserIDFromSession(HttpSession session) {
        Object attribute = session.getAttribute(USERID);
        return attribute == null ? null : (String) attribute;
    }
}
