package com.eloancn.framework.activiti.user;

import com.eloancn.nback.system.vo.SysQuartersVO;
import com.eloancn.nback.system.vo.SysUserNewVO;

import java.util.List;

/**
 * Created by Administrator on 2019/1/22 0022.
 */
public interface IActivitiUser {
    public List<String> getSuperiorUsers(String uid);
    public List<String> getUsersByMark(List<String> mark,String taskUserId);
    /**
     * 模糊查询用户信息
     * @param queryWord
     * @return
     */
    public List<SysUserNewVO> queryUserByNamLike(String queryWord);

    /**
     * 模糊查询岗位信息
     * @param queryWord
     * @return
     */
    public List<SysQuartersVO> queryQuartersByNamLike(String queryWord);
}
