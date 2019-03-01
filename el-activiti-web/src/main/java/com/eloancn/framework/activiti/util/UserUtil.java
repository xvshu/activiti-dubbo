package com.eloancn.framework.activiti.util;

import com.eloancn.organ.dto.UserDto;
import org.activiti.engine.identity.User;

import javax.servlet.http.HttpSession;

/**
 * 用户工具类
 *
 * @author xvshu
 */
public class UserUtil {

    private static String USERID = "DEMO_USER_ID";

    private static final String USER = "user";

    private static final String SYS_CODE="unified_config_001";

    /**
     * 设置用户到session
     *
     * @param session
     * @param user
     */
    public static void saveUserToSession(HttpSession session, User user) {
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

    /**
     * 获取当前登录用户名
     * @param session
     * @return
     */
    public static String getUserNameFromSession(HttpSession session) {
        UserDto userFromSession = getUserFromSession(session);
        return userFromSession==null?null:userFromSession.getName();

    }

    public static String getSysCode() {
        return SYS_CODE;
    }
}
