package com.eloancn.framework.activiti.web.organ;

import com.alibaba.fastjson.JSONObject;
import com.eloancn.framework.sevice.api.PageParsDTO;
import com.eloancn.framework.sevice.api.ResultDTO;
import com.eloancn.organ.api.UserService;
import com.eloancn.organ.dto.PageInfoDto;
import com.eloancn.organ.common.ResultOrganCode;
import com.eloancn.organ.dto.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author : CJT
 * @date : 2017/11/2
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userOrganService;

    /**
     * 添加用户
     * @param userDto 用户参数
     * @return 操作结果
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public ResultDTO<Integer> add(UserDto userDto){

        logger.info("UserController.add UserDto:{}", userDto);

        ResultDTO<Integer> resultDTO = new ResultDTO<>();
        if (null==userDto || StringUtils.isBlank(userDto.getName())
                || null==userDto.getUserType()){
            resultDTO.setCode(ResultOrganCode.PARAM_CHECK_FAIL.getCode());
            resultDTO.setMessage(ResultOrganCode.PARAM_CHECK_FAIL.getMessage());
            return resultDTO;
        }
        resultDTO = userOrganService.insertUser(userDto);
        logger.info("UserController.add UserDto:{},resultDTO:{}", userDto,resultDTO);
        return resultDTO;
    }

    @RequestMapping(value = "/view/loadAll")
    public ModelAndView redirectJsp(HttpSession session, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/organ/userList");
        return mav;
    }

    /**
     * 加载所有用户（分页）
     * @param pageParsDTO 分页信息
     * @param rows 每页条数
     * @param paramMap 查询条件
     * @return 查询结果
     */
    @RequestMapping(value = "/loadAll")
    @ResponseBody
    public ResultDTO<PageInfoDto<UserDto>> loadAll(PageParsDTO pageParsDTO,Integer rows,String paramMap){
        logger.info("UserController.loadAll PageParsDTO:{},Rows:{},paramMap:{} ", pageParsDTO,rows,paramMap);
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
        ResultDTO<PageInfoDto<UserDto>> resultDTO = userOrganService.searchUser(pageParsDTO);
        logger.info("UserController.loadAll PageParsDTO:{},Rows:{},paramMap:{},Result:{} ", pageParsDTO,rows,paramMap,resultDTO);
        return resultDTO;
    }

    /**
     * 禁用用户
     * @param id 用户ID
     * @param status 用户状态
     * @return 结果
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public ResultDTO<Integer> delete(Integer id,Integer status){
        logger.info("UserController.delete Id:{},status:{}",id,status);
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setStatus(status);
        ResultDTO<Integer> resultDTO = userOrganService.updateUser(userDto);
        logger.info("UserController.delete Id:{},status:{},Result:{}",id,status,resultDTO);
        return resultDTO;
    }

    /**
     * 修改用户
     * @param userDto 用户信息
     * @return 返回修改结果
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public ResultDTO<Integer> update(UserDto userDto){
        logger.info("UserController.update UserDto:{}",userDto);
        ResultDTO<Integer> resultDTO = new ResultDTO<>();
        if (null==userDto.getId() || 0>=userDto.getId()){
            resultDTO.setCode(ResultOrganCode.PARAM_CHECK_FAIL.getCode());
            resultDTO.setMessage(ResultOrganCode.PARAM_CHECK_FAIL.getMessage());
            return resultDTO;
        }
        resultDTO = userOrganService.updateUser(userDto);
        logger.info("UserController.update UserDto:{},Result:{}",userDto,resultDTO);
        return resultDTO;
    }

}
