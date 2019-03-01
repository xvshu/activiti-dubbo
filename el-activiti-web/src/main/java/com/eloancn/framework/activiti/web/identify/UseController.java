package com.eloancn.framework.activiti.web.identify;

import com.eloancn.framework.activiti.util.UserUtil;
import com.eloancn.framework.activiti.util.demo.ActivitiUtils;
import com.eloancn.framework.sevice.api.ResultDTO;
import com.eloancn.organ.api.UserService;
import com.eloancn.organ.common.OrganStatusEnum;
import com.eloancn.organ.dto.UserDto;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 用户相关控制器
 *
 * @author xvshu
 */
@Controller
@RequestMapping("/user")
public class UseController {

    private static Logger logger = LoggerFactory.getLogger(UseController.class);

    // Activiti Identify Service
    private IdentityService identityService;



    /**
     * 登录系统
     *
     * @param userName
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "/logon")
    public String logon(@RequestParam("username") String userName, @RequestParam("password") String password, HttpSession session) {
        logger.debug("logon request: {username={}, password={}}", userName, password);

        boolean checkPassword =false;

        //todo:查询用户

        if(userName != null && userName.equals("admin")
                && password != null && password.equals("admin") ){
            checkPassword=true;
            ActivitiUtils.saveUserIdToSession(session, String.valueOf(userName));
        }

        if (checkPassword) {
            return "redirect:/main/index";
        }
        return  "";
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "/login";
    }

    @Autowired
    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }




}
