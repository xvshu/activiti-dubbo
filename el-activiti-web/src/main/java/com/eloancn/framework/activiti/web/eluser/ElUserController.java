package com.eloancn.framework.activiti.web.eluser;

import com.eloancn.framework.activiti.user.ActivitiUserServiceFactory;
import com.eloancn.framework.activiti.util.Page;
import com.eloancn.nback.system.vo.SysQuartersVO;
import com.eloancn.nback.system.vo.SysUserNewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Administrator on 2019-02-19.
 */
@Controller
@RequestMapping(value = "/el/user")
public class ElUserController {

    @Autowired
    ActivitiUserServiceFactory activitiUserServiceFactory;

    /**
     * 模糊查询用户
     *
     */
    @RequestMapping(value = "search/list")
    @ResponseBody
    public List<SysUserNewVO> searchUser(String searchWord, HttpSession session, HttpServletRequest request) {

        if(searchWord==null || searchWord.trim().length()==0){
            return null;
        }
        return  activitiUserServiceFactory.getUserService().queryUserByNamLike(searchWord);

    }

    /**
     * 模糊查询岗位
     *
     */
    @RequestMapping(value = "quas/search/list")
    @ResponseBody
    public List<SysQuartersVO> searchQuas(String searchWord, HttpSession session, HttpServletRequest request) {

        if(searchWord==null || searchWord.trim().length()==0){
            return null;
        }
        return  activitiUserServiceFactory.getUserService().queryQuartersByNamLike(searchWord);

    }

}


