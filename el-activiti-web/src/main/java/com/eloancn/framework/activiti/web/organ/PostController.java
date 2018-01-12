package com.eloancn.framework.activiti.web.organ;

import com.alibaba.fastjson.JSONObject;
import com.eloancn.framework.sevice.api.PageParsDTO;
import com.eloancn.framework.sevice.api.ResultDTO;
import com.eloancn.organ.api.PostService;
import com.eloancn.organ.dto.PageInfoDto;
import com.eloancn.organ.dto.PostDto;
import com.eloancn.organ.common.ResultOrganCode;
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
 * 职位功能
 * @author : CJT
 * @date : 2017/11/10
 */
@Controller
@RequestMapping("/post")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postOrganService;

    /**
     * 添加职位记录
     * @param postDto 职位信息
     * @return 返回结果
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResultDTO<Integer> add(PostDto postDto){
        logger.info("PostController.add PostDto:{}",postDto);
        ResultDTO<Integer> resultDTO = new ResultDTO<>();
        if (StringUtils.isBlank(postDto.getName())){
            resultDTO.setCode(ResultOrganCode.PARAM_CHECK_FAIL.getCode());
            resultDTO.setMessage(ResultOrganCode.PARAM_CHECK_FAIL.getMessage());
            return resultDTO;
        }

        resultDTO = postOrganService.insertPost(postDto);
        logger.info("PostController.add PostDto:{},Result:{}",postDto,resultDTO);

        return resultDTO;
    }

    /**
     * 修改职位信息
     * @param postDto 职位信息
     * @return 返回结果
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResultDTO<Integer> update(PostDto postDto){
        logger.info("PostController.update postDto:{}",postDto);
        ResultDTO<Integer> resultDTO = new ResultDTO<>();
        if ( postDto.getId() == null || 0>=postDto.getId()){
            resultDTO.setCode(ResultOrganCode.PARAM_CHECK_FAIL.getCode());
            resultDTO.setMessage(ResultOrganCode.PARAM_CHECK_FAIL.getMessage());
            return resultDTO;
        }

        resultDTO = postOrganService.updatePost(postDto);
        logger.info("PostController.update postDto:{},Result:{}",postDto,resultDTO);

        return resultDTO;
    }

    @RequestMapping(value = "/view/loadAll")
    public ModelAndView redirectJsp(HttpSession session, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/organ/postList");
        return mav;
    }

    /**
     * 查询职位记录
     * @param pageParsDTO 分页信息
     * @param rows 每页行数
     * @param paramMap 职位查询信息
     * @return 返回查询结果
     */
    @RequestMapping("/loadAll")
    @ResponseBody
    public ResultDTO<PageInfoDto<PostDto>> loadAll(PageParsDTO pageParsDTO, Integer rows, String paramMap){
        logger.info("PostController.loadAll PageParsDTO:{},rows:{},paramMap:{}",pageParsDTO,rows,paramMap);
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
        ResultDTO<PageInfoDto<PostDto>>  resultDTO = postOrganService.searchPost(pageParsDTO);
        logger.info("PostController.loadAll PageParsDTO:{},rows:{},paramMap:{},Result:{}",pageParsDTO,rows,paramMap,resultDTO);

        return resultDTO;
    }

}
