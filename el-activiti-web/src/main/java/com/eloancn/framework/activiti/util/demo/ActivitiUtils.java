package com.eloancn.framework.activiti.util.demo;

import com.eloancn.organ.dto.UserDto;
import org.activiti.engine.identity.User;

import javax.servlet.http.HttpSession;

/**
 * Created by xvshu on 2017/11/1.
 */
public class ActivitiUtils {

//    private static String USERID = "DEMO_USER_ID";

    public static final String USER = "user";

    /**
     * 设置用户到session
     *
     * @param session
     * @param userID
     */
    public static void saveUserIdToSession(HttpSession session, String userID) {
        session.setAttribute(USER, userID);

    }



    /**
     * 设置用户到session
     *
     * @param session
     * @param user
     */
    public static void saveUserToSession(HttpSession session, Object user) {
        session.setAttribute(USER, user);
    }


    /**
     * 从Session获取当前用户信息
     *
     * @param session
     * @return
     */
    public static String getUserIDFromSession(HttpSession session) {
        Object attribute = session.getAttribute(USER);
        return attribute == null ? null : (String) attribute;
    }

    /**
     * 从Session获取当前用户信息
     *
     * @param session
     * @return
     */
    public static UserDto getUserFromSession(HttpSession session) {
        Object attribute = session.getAttribute(USER);
        return attribute == null ? null : (UserDto) attribute;
    }
}
