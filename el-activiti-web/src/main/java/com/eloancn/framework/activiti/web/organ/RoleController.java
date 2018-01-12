package com.eloancn.framework.activiti.web.organ;

import com.alibaba.fastjson.JSONObject;
import com.eloancn.framework.sevice.api.PageParsDTO;
import com.eloancn.framework.sevice.api.ResultDTO;
import com.eloancn.organ.api.RoleService;
import com.eloancn.organ.common.ResultOrganCode;
import com.eloancn.organ.dto.PageInfoDto;
import com.eloancn.organ.dto.RoleDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 角色信息
 * @author : CJT
 * @date : 2017/11/17
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    private Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleOrganService;
    
    /**
     * 添加角色记录
     * @param roleDto 角色信息
     * @return 返回结果
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResultDTO<Integer> add(RoleDto roleDto){
        logger.info("RoleController.add RoleDto:{}",roleDto);
        ResultDTO<Integer> resultDTO = new ResultDTO<>();
        if (StringUtils.isBlank(roleDto.getName())){
            resultDTO.setCode(ResultOrganCode.PARAM_CHECK_FAIL.getCode());
            resultDTO.setMessage(ResultOrganCode.PARAM_CHECK_FAIL.getMessage());
            return resultDTO;
        }

        resultDTO = roleOrganService.insertRole(roleDto);
        logger.info("RoleController.add RoleDto:{},Result:{}",roleDto,resultDTO);

        return resultDTO;
    }

    /**
     * 修改角色信息
     * @param roleDto 角色信息
     * @return 返回结果
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResultDTO<Integer> update(RoleDto roleDto){
        logger.info("RoleController.update RoleDto:{}",roleDto);
        ResultDTO<Integer> resultDTO = new ResultDTO<>();
        if ( roleDto.getId() == null || 0>=roleDto.getId()){
            resultDTO.setCode(ResultOrganCode.PARAM_CHECK_FAIL.getCode());
            resultDTO.setMessage(ResultOrganCode.PARAM_CHECK_FAIL.getMessage());
            return resultDTO;
        }

        resultDTO = roleOrganService.updateRole(roleDto);
        logger.info("RoleController.update RoleDto:{},Result:{}",roleDto,resultDTO);

        return resultDTO;
    }

    @RequestMapping(value = "/view/loadAll")
    public ModelAndView redirectJsp(HttpSession session, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/organ/roleList");
        return mav;
    }

    /**
     * 查询角色记录
     * @param pageParsDTO 分页信息
     * @param rows 每页行数
     * @param paramMap 角色查询信息
     * @return 返回查询结果
     */
    @RequestMapping("/loadAll")
    @ResponseBody
    public ResultDTO<PageInfoDto<RoleDto>> loadAll(PageParsDTO pageParsDTO, Integer rows, String paramMap){
        logger.info("RoleController.loadAll PageParsDTO:{},rows:{},paramMap:{}",pageParsDTO,rows,paramMap);
        if (null!=rows && 0<rows){
            pageParsDTO.setLimit(rows);
        }else{
            pageParsDTO.setPage(0);
            pageParsDTO.setLimit(0);
        }
        if (!StringUtils.isBlank(paramMap)){
            Map map = (Map) JSONObject.parse(paramMap);
            pageParsDTO.setParam(map);
        }
        ResultDTO<PageInfoDto<RoleDto>>  resultDTO = roleOrganService.searchRoles(pageParsDTO);
        logger.info("RoleController.loadAll PageParsDTO:{},rows:{},paramMap:{},Result:{}",pageParsDTO,rows,paramMap,resultDTO);

        return resultDTO;
    }
}
