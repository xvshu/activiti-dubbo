package com.eloancn.framework.activiti.service.user;

import com.alibaba.fastjson.JSON;
import com.eloancn.framework.activiti.conf.AdminUsers;
import com.eloancn.framework.activiti.facade.ElActiviti;
import com.eloancn.framework.activiti.util.error.Preconditions;
import com.eloancn.framework.sevice.api.MessageStatus;
import com.eloancn.framework.sevice.api.ResultDTO;
import com.eloancn.organ.api.UserService;
import com.eloancn.organ.common.BusCodeEnum;
import com.eloancn.organ.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by xvshu on 2017/11/15.
 */
@Service
public class UserChangeService {
    private static Logger logger = LoggerFactory.getLogger(UserChangeService.class);

    @Autowired
    private UserService userOrganService;

    @Autowired
    private AdminUsers adminUsers;

    /**
     * 转换用户id（将用户id转换为架构平台组织结构内用户新id）
     * @param oldUserId 老用户id
     * @param busCodeEnum 所在系统
     * @return
     */
    public String changeUserID(String oldUserId, BusCodeEnum busCodeEnum){
        //参数校验
        Preconditions.checkNotNull(oldUserId,"oldUserId is null");
        Preconditions.checkNotNull(busCodeEnum,"elSystem is null");

        List<String> oldUsers = new ArrayList<>();
        oldUsers.add(oldUserId);
        String newUserId = null;
        ResultDTO<Map<String, UserDto>> resultDTO = userOrganService.convertUser(oldUsers, busCodeEnum);
        Preconditions.checkNotNull(resultDTO,"userOrgan.convertUser resultDTO is null oldUserId:"+oldUserId);
        Preconditions.checkNotNull(resultDTO.getData(),"userOrgan.convertUser resultDTO.Data is null oldUserId:"+oldUserId);
        Preconditions.checkNotNull(resultDTO.getData().get(oldUserId),"userOrgan.convertUser resultDTO.Data."+oldUserId+"  is null");
        logger.info("=changeUserID=> userOrganService.convertUser oldUserId:{} busCodeEnum:{} resultDTO:{}",oldUserId, JSON.toJSON(busCodeEnum),JSON.toJSONString(resultDTO));

        if(resultDTO!=null && resultDTO.getData()!=null && resultDTO.getData().get(oldUserId)!=null){
            UserDto userDto = resultDTO.getData().get(oldUserId);
            newUserId=userDto.getId()==null?null:String.valueOf(userDto.getId());
        }
        Preconditions.checkNotNull(newUserId,"userOrgan.convertUser newUserId is null oldid:"+oldUserId);
        return newUserId;
    }

    /**
     * 批量转换用户id（将用户id转换为架构平台组织结构内用户新id）
     * @param oldUserIds 老用户id集合
     * @param busCodeEnum 所在系统
     * @return
     */
    public List<String> changeUserIDBatch(List<String> oldUserIds, BusCodeEnum busCodeEnum){
        //参数校验
        Preconditions.checkNotNullOrEmpty(oldUserIds,"oldUserIds is Empty");
        Preconditions.checkNotNull(busCodeEnum,"elSystem is null");

        List<String> newUserId = new ArrayList<>();
        ResultDTO<Map<String, UserDto>> resultDTO = userOrganService.convertUser(oldUserIds, busCodeEnum);
        Preconditions.checkNotNull(resultDTO,"userOrgan.convertUser resultDTO is null");
        Preconditions.checkNotNull(resultDTO.getData(),"userOrgan.convertUser resultDTO.Data is null");
        Preconditions.checkNotFalse(resultDTO.getData().values().size()==oldUserIds.size(),"userOrgan.convertUser resultDTO.Data.size < oldUserIds.size ");
        logger.info("=changeUserIDBatch=> userOrganService.convertUser oldUserIds:{} busCodeEnum:{} resultDTO:{}",JSON.toJSONString(oldUserIds), JSON.toJSON(busCodeEnum),JSON.toJSONString(resultDTO));

        for(UserDto oneUser:resultDTO.getData().values()){
            newUserId.add(String.valueOf(oneUser.getId()));
        }

        return newUserId;
    }


    /**
     * 批量转换用户id（将用户id转换为架构平台组织结构内用户新id）
     * @param userId 员工id
     * @return
     */
    public List<String> getParentUserID(String userId){
        //参数校验
        Preconditions.checkNotNullOrEmpty(userId,"oldUserIds is Empty");

        List<String> parentUserIds = new ArrayList<>();
        ResultDTO<List<UserDto>> resultDTO = userOrganService.findUserUpper(Integer.valueOf(userId));
        Preconditions.checkNotNull(resultDTO,"userOrgan.findUserUpper resultDTO is null childrenId:"+userId);
        Preconditions.checkNotFalse(resultDTO.getStatus()==MessageStatus.SUCCESS.getValue(),"userOrgan.findUserUpper resultDTO is null childrenId:"+userId);

        if(resultDTO.getData()==null || resultDTO.getData().size()<1){
            parentUserIds.add(adminUsers.getAdminUserId());
        }else{
            logger.info("=getParentUserID=> userOrganService.findUserUpper userId:{} resultDTO:{}",userId,JSON.toJSONString(resultDTO));
            for(UserDto oneU : resultDTO.getData()){
                parentUserIds.add(String.valueOf(oneU.getId()));
            }
        }
        Preconditions.checkNotNullOrEmpty(parentUserIds," parentUserIds is Empty childrenId:"+ userId);


        return parentUserIds;
    }

}
