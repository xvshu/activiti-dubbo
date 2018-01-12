package com.eloancn.framework.activiti.web.organ;

import com.eloancn.framework.sevice.api.ResultDTO;
import com.eloancn.organ.api.DepartmentService;
import com.eloancn.organ.dto.DepartmentDto;
import com.eloancn.organ.common.ResultOrganCode;
import com.eloancn.organ.dto.TreeNodeDto;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门功能
 * @author : CJT
 * @date : 2017/11/2
 */
@Controller
@RequestMapping("/department")
public class DepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private DepartmentService departmentOrganService;

    /**
     * 添加部门
     * @param departmentDto 参数
     * @return 返回处理结果
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public ResultDTO<DepartmentDto> add(DepartmentDto departmentDto){
        logger.info("DepartmentController.add DepartmentDto:{}", departmentDto);
        ResultDTO<DepartmentDto> resultDTO = new ResultDTO<>();
        if (StringUtils.isBlank(departmentDto.getName()) || null==departmentDto.getPid() || StringUtils.isBlank(departmentDto.getParentCode())){
            resultDTO.setCode(ResultOrganCode.PARAM_CHECK_FAIL.getCode());
            resultDTO.setMessage(ResultOrganCode.PARAM_CHECK_FAIL.getMessage());
            return resultDTO;
        }

        resultDTO = departmentOrganService.insertDepartment(departmentDto);
        logger.info("DepartmentController.add DepartmentDto:{}，Result:{}", departmentDto,resultDTO);

        return resultDTO;
    }

    /**
     * 修改部门信息
     * @param departmentDto 部门信息
     * @return 返回处理结果
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public ResultDTO<Integer> update(DepartmentDto departmentDto){
        logger.info("DepartmentController.update DepartmentDto:{}", departmentDto);
        ResultDTO<Integer> resultDTO = new ResultDTO<>();
        if (null==departmentDto || null==departmentDto.getId()){
            resultDTO.setCode(ResultOrganCode.PARAM_CHECK_FAIL.getCode());
            resultDTO.setMessage(ResultOrganCode.PARAM_CHECK_FAIL.getMessage());
            return resultDTO;
        }

        resultDTO = departmentOrganService.updateDepartment(departmentDto);
        logger.info("DepartmentController.update DepartmentDto:{},Result:{}", departmentDto,resultDTO);

        return resultDTO;
    }

    /**
     * 删除部门
     * @param departmentDto 部门信息
     * @return 返回处理结果
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public ResultDTO<Integer> delete(DepartmentDto departmentDto){
        logger.info("DepartmentController.delete departmentDto:{}", departmentDto);
        ResultDTO<Integer> resultDTO = new ResultDTO<>();
        if (null==departmentDto.getId() || 0>=departmentDto.getId()
                || null==departmentDto.getStatus()){
            resultDTO.setCode(ResultOrganCode.PARAM_CHECK_FAIL.getCode());
            resultDTO.setMessage(ResultOrganCode.PARAM_CHECK_FAIL.getMessage());
            return resultDTO;
        }

        resultDTO = departmentOrganService.updateDepartment(departmentDto);
        logger.info("DepartmentController.delete departmentDto:{},Result:{}", departmentDto,resultDTO);

        return resultDTO;
    }

    @RequestMapping(value = "/view/loadAll")
    public ModelAndView redirectJsp(HttpSession session, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/organ/departmentManage");
        return mav;
    }

    /**
     * 加载所有部门信息
     * @return 返回结果
     */
    @RequestMapping(value = "/loadAll")
    @ResponseBody
    public ResultDTO<List<TreeNodeDto>> loadAll(){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("status",1);
        ResultDTO<List<TreeNodeDto>> resultDTO  = departmentOrganService.getAllDepartment(paramMap);
        logger.info("DepartmentController.loadALl Result:{}",resultDTO);
        return resultDTO;
    }

}
