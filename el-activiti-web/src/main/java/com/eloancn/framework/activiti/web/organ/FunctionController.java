package com.eloancn.framework.activiti.web.organ;

import com.eloancn.framework.sevice.api.ResultDTO;
import com.eloancn.organ.api.FunctionService;
import com.eloancn.organ.dto.FunctionDto;
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
 * 职务功能
 * @author : CJT
 * @date : 2017/11/2
 */
@Controller
@RequestMapping("/function")
public class FunctionController {

    private static final Logger logger = LoggerFactory.getLogger(FunctionController.class);

    @Autowired
    private FunctionService functionOrganService;

    /**
     * 添加职务
     * @param functionDto 职务信息
     * @return 返回处理结果
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public ResultDTO<FunctionDto> add(FunctionDto functionDto){
        logger.info("FunctionController.add functionDto:{}",functionDto);
        ResultDTO<FunctionDto> resultDTO = functionOrganService.insertFunction(functionDto);
        logger.info("FunctionController.add functionDto:{},Result:{}",functionDto,resultDTO);
        return resultDTO;
    }

    /**
     * 修改职务信息
     * @param functionDto 职务信息
     * @return 返回处理结果
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public ResultDTO<Integer> update(FunctionDto functionDto) {
        logger.info("FunctionController.update FunctionDto:{}", functionDto);
        ResultDTO<Integer> resultDTO = new ResultDTO<>();
        if (null==functionDto || null==functionDto.getId()){
            resultDTO.setCode(ResultOrganCode.PARAM_CHECK_FAIL.getCode());
            resultDTO.setMessage(ResultOrganCode.PARAM_CHECK_FAIL.getMessage());
            return resultDTO;
        }

        resultDTO = functionOrganService.updateFunction(functionDto);
        logger.info("FunctionController.update FunctionDto:{}，Result:{}", functionDto,resultDTO);

        return resultDTO;
    }

    @RequestMapping(value = "/view/loadAll")
    public ModelAndView redirectJsp(HttpSession session, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/organ/functionManage");
        return mav;
    }

    /**
     * 加载职务树
     */
    @RequestMapping(value = "/loadAll")
    @ResponseBody
    public ResultDTO<List<TreeNodeDto>> loadAll(){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("status",1);
        ResultDTO<List<TreeNodeDto>> resultDTO  = functionOrganService.getAllFunctions(paramMap);
        logger.info("FunctionController.loadALl Result:{}",resultDTO);
        return resultDTO;
    }

    /**
     * 删除职务
     * @param functionDto 职务信息
     * @return 返回处理结果
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public ResultDTO<Integer> delete(FunctionDto functionDto){
        logger.info("FunctionController.delete FunctionDto:{}",functionDto);
        ResultDTO<Integer> resultDTO = new ResultDTO<>();
        if (null==functionDto.getId() || 0>=functionDto.getId()
                || null==functionDto.getStatus()){
            resultDTO.setCode(ResultOrganCode.PARAM_CHECK_FAIL.getCode());
            resultDTO.setMessage(ResultOrganCode.PARAM_CHECK_FAIL.getMessage());
            return resultDTO;
        }

        resultDTO = functionOrganService.updateFunction(functionDto);
        logger.info("FunctionController.delete FunctionDto:{},Result:{}", functionDto,resultDTO);

        return resultDTO;
    }

}
