package com.eloancn.framework.activiti.web.configmap;

import com.alibaba.fastjson.JSON;
import com.el.config.api.ConfigMapService;
import com.el.config.dto.ConfigMapDto;
import com.eloancn.framework.activiti.util.Page;
import com.eloancn.framework.activiti.util.PageUtil;
import com.eloancn.framework.sevice.api.PageParsDTO;
import com.eloancn.framework.sevice.api.PageResultDTO;
import com.eloancn.framework.sevice.api.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : CJT
 * @date : 2017/11/2
 */
@Controller
@RequestMapping("/disconf")
public class DisconfController {

    private static final Logger logger = LoggerFactory.getLogger(DisconfController.class);


    /**
     * 模型列表
     */
    @RequestMapping(value = "index")
    public ModelAndView modelList(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("disconf/dis_index");
        return mav;
    }



}
