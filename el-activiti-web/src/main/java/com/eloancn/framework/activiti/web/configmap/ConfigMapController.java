package com.eloancn.framework.activiti.web.configmap;

import com.alibaba.fastjson.JSON;
import com.el.config.api.ConfigMapService;
import com.el.config.dto.ConfigMapDto;
import com.eloancn.framework.activiti.util.Page;
import com.eloancn.framework.activiti.util.PageUtil;
import com.eloancn.framework.sevice.api.PageParsDTO;
import com.eloancn.framework.sevice.api.PageResultDTO;
import com.eloancn.framework.sevice.api.ResultDTO;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author : CJT
 * @date : 2017/11/2
 */
@Controller
@RequestMapping("/configmap")
public class ConfigMapController {

    private static final Logger logger = LoggerFactory.getLogger(ConfigMapController.class);

    @Autowired
    private ConfigMapService configMapService;


    /**
     * 模型列表
     */
    @RequestMapping(value = "list")
    public ModelAndView modelList(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("configmap/configmap-list");
        return mav;
    }

    /**
     * 模型列表
     */
    @RequestMapping(value = "findall")
    @ResponseBody
    public Page<ConfigMapDto> findall(Integer page, Integer rows , String configName,HttpServletRequest request) {

        logger.info("=ConfigMapController.findall=> page:{} rows:{} configName:{}",page,rows,String.valueOf(configName));
        if(page==null){
            page=1;
        }
        if(rows==null){
            rows=10;
        }

        Page<ConfigMapDto> pageResult = new Page<ConfigMapDto>(PageUtil.PAGE_SIZE);
        pageResult.setPageNo(page);
        pageResult.setPageSize(rows);

        PageParsDTO pageParsDTO= new PageParsDTO();
        pageParsDTO.setPage(page);
        pageParsDTO.setLimit(rows);
        ConfigMapDto searchDto = new ConfigMapDto();
        if(configName!=null && configName.length()>0){
            searchDto.setConfigName(configName);
        }
        PageResultDTO<ConfigMapDto> pageResultDTO = configMapService.getConfigMapPage(searchDto,pageParsDTO);

        if(pageResultDTO!=null && pageResultDTO.getItems()!=null){
            pageResult.setTotalCount(pageResultDTO.getTotalCount());
            pageResult.setResult(pageResultDTO.getItems());
        }

        return pageResult;
    }

    /**
     * 模型列表
     */
    @RequestMapping(value = "insert")
    @ResponseBody
    public String insert(ConfigMapDto configMapDto, HttpServletRequest request) {

        logger.info("=ConfigMapController.insert=> configMapDto:{}", JSON.toJSONString(configMapDto));
        String result = "添加失败";
        try {
            ResultDTO<ConfigMapDto> resultCheck = configMapService.getConfigMapByName(configMapDto.getConfigName());

            if (resultCheck != null && resultCheck.getData() != null) {
                result = "配置名称重复，请重新添加";
            } else {
                ResultDTO<Integer> resultDTO = configMapService.insertConfigMap(configMapDto);
                if (resultDTO != null && resultDTO.getData() != null) {
                    result = "添加成功";
                }
            }
        }catch (Exception ex){
            logger.error("=ConfigMapController.insert=>error",ex);
            result="操作失败，请联系管理员";
        }

        return result ;
    }

    /**
     * 模型列表
     */
    @RequestMapping(value = "edit")
    @ResponseBody
    public String edit(ConfigMapDto configMapDto, HttpServletRequest request) {

        logger.info("=ConfigMapController.edit=> configMapDto:{}", JSON.toJSONString(configMapDto));

        ResultDTO<Integer> resultDTO = configMapService.updateConfigMap(configMapDto);
        String result = "修改失败";
        if(resultDTO!=null && resultDTO.getData()!=null){
            result = "修改成功";
        }

        return result ;
    }

    /**
     * 模型列表
     */
    @RequestMapping(value = "delete/{configName}",produces="text/html;charset=UTF-8")
    @ResponseBody
    public String delete(@PathVariable("configName")String configName, HttpServletRequest request) {

        logger.info("=ConfigMapController.delete=> configName:{}", String.valueOf(configName));

        ResultDTO<Integer> resultDTO = configMapService.deleteConfigMapByName(configName);
        String result = "删除失败";
        if(resultDTO!=null && resultDTO.getData()!=null){
            result = "删除成功";
        }

        return result ;
    }

}
