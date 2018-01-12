package com.eloancn.framework.activiti.web.organ;

import com.eloancn.framework.sevice.api.ResultDTO;
import com.eloancn.organ.api.RightService;
import com.eloancn.organ.common.ResultOrganCode;
import com.eloancn.organ.dto.RightDto;
import com.eloancn.organ.dto.TreeNodeDto;
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
 * 权限管理
 * @author : qinxf
 * @date : 2017/11/30
 */
@Controller
@RequestMapping("/right")
public class RightController {

    private static final Logger logger = LoggerFactory.getLogger(RightController.class);

    @Autowired
    private RightService rightService;

    /**
     * 添加权限
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public ResultDTO<RightDto> add(RightDto rightDto){
        logger.info("RightController.add rightDto:{}",rightDto);
        ResultDTO<RightDto> resultDTO = rightService.insertRight(rightDto);
        logger.info("RightController.add rightDto:{},Result:{}",rightDto,resultDTO);
        return resultDTO;
    }

    /**
     * 修改权限
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public ResultDTO<Integer> update(RightDto rightDto) {
        logger.info("RightController.update RightDto:{}", rightDto);
        ResultDTO<Integer> resultDTO = new ResultDTO<>();
        if (null==rightDto || null==rightDto.getId()){
            resultDTO.setCode(ResultOrganCode.PARAM_CHECK_FAIL.getCode());
            resultDTO.setMessage(ResultOrganCode.PARAM_CHECK_FAIL.getMessage());
            return resultDTO;
        }

        resultDTO = rightService.updateRight(rightDto);
        logger.info("RightController.update RightDto:{}，Result:{}", rightDto,resultDTO);

        return resultDTO;
    }

    @RequestMapping(value = "/view/loadAll")
    public ModelAndView redirectJsp(HttpSession session, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/organ/right_list");
        return mav;
    }

    /**
     * 加载权限树
     */
    @RequestMapping(value = "/loadAll")
    @ResponseBody
    public ResultDTO<List<TreeNodeDto>> loadAll(){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("status",1);
        ResultDTO<List<TreeNodeDto>> resultDTO  = rightService.getAllRights(paramMap);
        logger.info("RightController.loadALl Result:{}",resultDTO);
        return resultDTO;
    }

    /**
     * 删除权限
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public ResultDTO<Integer> delete(RightDto rightDto){
        logger.info("RightController.delete RightDto:{}",rightDto);
        ResultDTO<Integer> resultDTO = new ResultDTO<>();
        if (null==rightDto.getId() || 0>=rightDto.getId()
                || null==rightDto.getStatus()){
            resultDTO.setCode(ResultOrganCode.PARAM_CHECK_FAIL.getCode());
            resultDTO.setMessage(ResultOrganCode.PARAM_CHECK_FAIL.getMessage());
            return resultDTO;
        }

        resultDTO = rightService.updateRight(rightDto);
        logger.info("RightController.delete rightDto:{},Result:{}", rightDto,resultDTO);

        return resultDTO;
    }

}
