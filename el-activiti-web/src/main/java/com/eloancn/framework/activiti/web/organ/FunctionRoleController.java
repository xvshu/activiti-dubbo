package com.eloancn.framework.activiti.web.organ;

import com.eloancn.framework.sevice.api.ResultDTO;
import com.eloancn.organ.api.FunctionRoleService;
import com.eloancn.organ.dto.FunctionRoleDto;
import com.eloancn.organ.dto.RoleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 职务角色关系
 * @author : CJT
 * @date : 2017/11/22
 */
@Controller
@RequestMapping("/funRole")
public class FunctionRoleController {

    private static final Logger logger = LoggerFactory.getLogger(FunctionRoleController.class);

    @Autowired
    private FunctionRoleService funRoleOrganService;

    /**
     * 根据职务ID获取角色信息
     * @param funId
     * @return
     */
    @RequestMapping(value = "/searchRolesByFunId")
    @ResponseBody
    public ResultDTO<List<RoleDto>> searchRolesByFunId(Integer funId){
        logger.info("FunctionRoleController.searchRolesByFunId funId:{}",funId);
        return funRoleOrganService.searchRolesByFunId(funId);
    }

    /**
     * 删除记录
     */
    @RequestMapping(value = "/delRecord")
    @ResponseBody
    public ResultDTO<Integer> delRecord(FunctionRoleDto functionRoleDto){
        logger.info("FunctionRoleController.delRecord FunctionRoleDto:{}",functionRoleDto);
        return funRoleOrganService.delRecord(functionRoleDto);
    }

}
