package com.eloancn.framework.activiti.web.workflow;

import com.alibaba.fastjson.JSON;
import com.eloancn.framework.activiti.TaskResult;
import com.eloancn.framework.activiti.dto.WorkFlowDto;
import com.eloancn.framework.activiti.facade.ElActiviti;
import com.eloancn.framework.activiti.util.Page;
import com.eloancn.framework.activiti.util.PageUtil;
import com.eloancn.framework.activiti.util.Variable;
import com.eloancn.framework.activiti.util.demo.ActivitiUtils;
import com.eloancn.organ.common.BusCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * &#x8bf7;&#x5047;&#x63a7;&#x5236;&#x5668;&#xff0c;&#x5305;&#x542b;&#x4fdd;&#x5b58;&#x3001;&#x542f;&#x52a8;&#x6d41;&#x7a0b;
 *
 * @author xvshu
 */
@Controller
@RequestMapping(value = "/img/activiti/")
public class ActivitImgShowController {

    private Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 读取运行中的流程实例
     *
     * @return
     */
    @RequestMapping(value = "runningImg")
    public ModelAndView runningImg(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/workflow/img_running");
        String pi_id = request.getParameter("pi_id");
        String pi_processDefinitionId = request.getParameter("pi_processDefinitionId");
        logger.info("ActivitImgShowController runningImg pi_id:{} pi_processDefinitionId:{}",String.valueOf(pi_id),String.valueOf(pi_processDefinitionId));
        mav.addObject("pi_id", pi_id);
        mav.addObject("pi_processDefinitionId", pi_processDefinitionId);
        return mav;
    }




}
