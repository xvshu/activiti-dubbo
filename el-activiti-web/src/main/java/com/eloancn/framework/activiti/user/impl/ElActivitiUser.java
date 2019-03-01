package com.eloancn.framework.activiti.user.impl;


import com.eloancn.framework.activiti.user.IActivitiUser;
import com.eloancn.nback.system.api.SysQuartersAPI;
import com.eloancn.nback.system.api.SysUserAreaAPI;
import com.eloancn.nback.system.api.SysUserAreaNewAPI;
import com.eloancn.nback.system.api.SysUserNewAPI;
import com.eloancn.nback.system.vo.SysQuartersVO;
import com.eloancn.nback.system.vo.SysUserAreaVO;
import com.eloancn.nback.system.vo.SysUserNewVO;
import com.eloancn.nback.tools.page.NbackPagination;
import com.eloancn.nback.tools.result.NbackResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/1/22 0022.
 */
@Service
public class ElActivitiUser implements IActivitiUser {

    @Autowired
    private SysUserNewAPI sysUserNewAPI;

    @Autowired
    private SysQuartersAPI sysQuartersAPI;

    private SysUserAreaNewAPI sysUserAreaNewAPI;

    /**
     * 根据当前任务获取上级用户
     * @param uid
     * @return
     */
    @Override
    public List<String> getSuperiorUsers(String uid) {
        List<String> result = new ArrayList<>();
        try {
            NbackResult<SysUserNewVO> resultUser = sysUserNewAPI.queryByUserId(Integer.valueOf(uid), null);
            if (resultUser != null && resultUser.getResult() != null && resultUser.getResult().getParentId() != null) {
                result.add(resultUser.getResult().getParentId().toString());
            } else {
                result = defaultUser();
            }
        }catch (Exception ex){
            ex.printStackTrace();
            result = defaultUser();
        }
        return result;
    }

    /**
     * 根据职位标识用户
     * @param mark
     * @return
     */
    @Override
    public List<String> getUsersByMark(List<String> mark,String taskUserId) {
        List<String> result = new ArrayList<>();
        for(String imark:mark){
            Map<String, Object> param = new HashMap<>();
            param.put("quartersId",imark);
            NbackResult<NbackPagination<SysUserAreaVO>> result_sys =sysUserAreaNewAPI.queryUserAreaPageList(param,1,10000,null);
            if(result_sys!=null && result_sys.getResult()!=null
                    && result_sys.getResult().getData()!=null
                    && result_sys.getResult().getData().size()>0){
                for(SysUserAreaVO oneUser : result_sys.getResult().getData()){
                    result.add(oneUser.getUserId().toString());
                }

            }
        }
        if(result.size()==0){
            result=defaultUser();
        }
        return result;
    }

    private List<String> defaultUser(){
        List<String> result = new ArrayList<>();
        result.add("1");
        return result;
    }

    /**
     * 模糊查询用户信息
     * @param queryWord
     * @return
     */
    @Override
    public List<SysUserNewVO> queryUserByNamLike(String queryWord){
        if(queryWord==null || queryWord.length()==0){
            return null;
        }

        List<SysUserNewVO> result = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("realNameLike",queryWord);
        try {
            NbackResult<NbackPagination<SysUserNewVO>> userSysResult = sysUserNewAPI.getPageListSysUserNew(param,1,1000,null);
            if(userSysResult!=null && userSysResult.getResult()!=null && userSysResult.getResult().getData()!=null){
                result = userSysResult.getResult().getData();
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return result;
        }
        return result;
    }

    /**
     * 模糊查询岗位信息
     * @param queryWord
     * @return
     */
    @Override
    public List<SysQuartersVO> queryQuartersByNamLike(String queryWord){
        if(queryWord==null || queryWord.length()==0){
            return null;
        }

        List<SysQuartersVO> result = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("nameLike",queryWord);
        try {
            NbackResult<NbackPagination<SysQuartersVO>> quartersSysResult = sysQuartersAPI.queryPageList(param,1,1000,null);
            if(quartersSysResult!=null && quartersSysResult.getResult()!=null && quartersSysResult.getResult().getData()!=null){
                result = quartersSysResult.getResult().getData();
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return result;
        }
        return result;
    }
}
