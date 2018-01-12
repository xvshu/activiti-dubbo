package com.eloancn.framework.activiti.web.organ;

import com.eloancn.framework.sevice.api.ResultDTO;
import com.eloancn.organ.api.RoleRightService;
import com.eloancn.organ.dto.RoleRightDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 角色权限信息
 * @author : qinxf
 * @date : 2017/12/08
 */
@Controller
@RequestMapping("/role/right")
public class RoleRightController {

    private Logger logger = LoggerFactory.getLogger(RoleRightController.class);

    @Autowired
    private RoleRightService roleRightService;
    
    /**
     * 获取角色的权限信息
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResultDTO<List<RoleRightDto>> list(Integer roleId){
        logger.info("RoleRightController.list roleId:{}",roleId);
        ResultDTO<List<RoleRightDto>> result = roleRightService.getCheckRights(roleId);

        return result;
    }

    /**
     * 修改权限信息
     * @param roleId
     * @param rightIds
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResultDTO<Integer> update(Integer roleId,String rightIds){
        logger.info("RoleRightController.update roleId:{},rightIds:{}",roleId,rightIds);
        return roleRightService.updateRights(roleId,rightIds);
    }
}
