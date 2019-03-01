package com.eloancn.framework.activiti.service;

import com.eloancn.framework.activiti.dto.WorkFlowDto;
import com.eloancn.framework.activiti.util.Page;
import com.eloancn.framework.activiti.TaskResult;
import com.eloancn.organ.common.BusCodeEnum;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.VariableInstance;

import java.util.List;
import java.util.Map;

/**
 * 工作流引擎对外服务接口
 * Created by 许恕 on 2017/10/30.
 */
public interface ElActivitiService {

    /**
     * 启动流程
     * @param processDefinitionKey 流程定义key（必须）
     * @param userId 启动流程用户id（必须）
     * @param businessKey 流程业务唯一key（必须）
     * @param nextUserId 下一个执行人的用户id
     * @param variables 流程变量 Map<String, Object>
     * @param busCodeEnum 系统标识
     * @return
     */
    public void startWorkflow(String processDefinitionKey,String userId,String businessKey, List<String> nextUserId,Map<String, Object> variables,BusCodeEnum busCodeEnum) ;

    /**
     * 启动流程
     * @param workFlowDto 任务参数对象
     *  processDefinitionKey 流程定义key（必须）
     *  userId 启动流程用户id（必须）
     *  businessKey 流程业务唯一key（必须）
     *  nextUserId 下一个执行人的用户id
     *  variables 流程变量 Map<String, Object>
     *  busCodeEnum 系统标识
     * @return
     */
    public void startWorkflow(WorkFlowDto workFlowDto) ;

    /**
     *
     * @param userId 用户id
     * @param page 分页信息
     * @param busCodeEnum 系统标识
     * @return
     */
    public Page<TaskResult> findTodoTasks(String userId,Page page,BusCodeEnum busCodeEnum);

    /**
     * chakan
     * @param processDefinitionKey 流程定义key
     * @param userId 用户ID
     * @return
     */
    public Page<TaskResult> findTodoTasks(List<String> processDefinitionKey,String userId,Page page,BusCodeEnum busCodeEnum);


    /**
     * 读取运行中流程
     * @param processDefinitionKey 流程定义key（必须）
     * @param userID 用户id（必须）
     * @param page 分页参数
     * @return
     */
    public Page<TaskResult> findRunningProcessInstaces(String processDefinitionKey,String userID, Page page,BusCodeEnum busCodeEnum);

    /**
     * 读取已经结束的流程
     * @param processDefinitionKey 流程定义key（必须）
     * @param userID 用户id（必须）
     * @param page 分页参数
     * @return
     */
    public Page<TaskResult> findFinishedProcessInstaces(String processDefinitionKey, String userID, Page page,BusCodeEnum busCodeEnum) ;

    /**
     * 签收任务
     * @param taskId 任务节点id
     * @param userId 签收人id
     * @return
     */
    public String claim(String taskId,String userId,BusCodeEnum busCodeEnum) ;

    /**
     * 完成某次任务节点
     * @param taskId 任务节点id（必须）
     * @param variables 任务节点参数Map<String, Object>
     * @param nextUserId 下一个执行人的用户id（必须）
     * @return
     */
    public void nodeComplete(String userID,String taskId, Map<String, Object> variables,List<String> nextUserId,BusCodeEnum busCodeEnum) ;

    /**
     * 完成某次任务节点
     * @param workFlowDto 流程参数对象
     *  userID 流程处理人
     *  taskId 任务节点id（必须）
     *  variables 任务节点参数Map<String, Object>
     *  nextUserId 下一个执行人的用户id（必须）
     *  busCodeEnum 系统标识
     * @return
     */
    public void nodeComplete(WorkFlowDto workFlowDto) ;

    /**
     * 完成某次任务节点
     *
     *  userID 流程处理人
     *  taskId 任务节点id（必须）
     * @return
     */
    public void msgComplete(String userID,String msgId,BusCodeEnum busCodeEnum) ;

    /**
     * 完成某次任务节点
     * @param workFlowDto 流程参数对象
     *  userID 流程处理人
     *  taskId 任务节点id（必须）
     *  variables 任务节点参数Map<String, Object>
     *  nextUserId 下一个执行人的用户id（必须）
     *  busCodeEnum 系统标识
     * @return
     */
    public void msgComplete(WorkFlowDto workFlowDto) ;

    /**
     * 查询某流程历史记录
     * @param processInstanceId 流程实例id
     * @return
     */
    public List<HistoricTaskInstance> queryHistoricTask(String processInstanceId) ;


    /**
     * 获取流程变量
     * @param processInstanceId 流程id
     * @return
     */
    public Map<String, Object> getVariables(String processInstanceId);


}
