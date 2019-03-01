package com.eloancn.framework.activiti.facade;

import com.alibaba.fastjson.JSON;
import com.eloancn.framework.activiti.dto.WorkFlowDto;
import com.eloancn.framework.activiti.redis.task.ActivitiTaskCache;
import com.eloancn.framework.activiti.util.Page;
import com.eloancn.framework.activiti.TaskResult;
import com.eloancn.framework.activiti.redis.client.ActivitiEventProducer;
import com.eloancn.framework.activiti.redis.model.ActivitiEventModel;
import com.eloancn.framework.activiti.redis.utils.EventType;
import com.eloancn.framework.activiti.service.ElActivitiService;
import com.eloancn.framework.activiti.util.PageUtil;
import com.eloancn.framework.activiti.util.TaskType;
import com.eloancn.framework.activiti.util.activiti.TaskComparator;
import com.eloancn.framework.activiti.util.error.Preconditions;
import com.eloancn.organ.common.BusCodeEnum;
import com.eloancn.organ.dto.UserDto;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.VariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by xvshu on 2017/10/30.
 */
@Service("elActivitiService")
public class ElActiviti implements ElActivitiService {

    private static Logger logger = LoggerFactory.getLogger(ElActiviti.class);

    @Autowired
    private IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;


    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ActivitiEventProducer activitiEventProducer;

    @Autowired
    private ActivitiTaskCache activitiTaskCache;



    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 启动流程
     *
     */
    @Override
    public void startWorkflow(WorkFlowDto workFlowDto) {
        startWorkflow(workFlowDto.getProcessDefinitionKey(),workFlowDto.getUserId(),workFlowDto.getBusinessKey(), workFlowDto.getNextUserIds(),workFlowDto.getVariables(),workFlowDto.getBusCodeEnum());
    }


    /**
     * 启动流程
     *
     */
    @Override
    public void startWorkflow(String processDefinitionKey,String userId,String businessKey, List<String> nextUserIds,Map<String, Object> variables,BusCodeEnum busCodeEnum) {


        logger.info("=startWorkflow=>processDefinitionKey:{} userId:{} businessKey:{} variables:{} nextUserIds:{}",processDefinitionKey,userId,businessKey,variables, JSON.toJSONString(nextUserIds));
        //校验参数
        Preconditions.checkNotNull(processDefinitionKey,"processDefinitionKey is null");
        Preconditions.checkNotNull(userId,"userId is null");
        //转换新旧用户id



        if(nextUserIds!=null){
            nextUserIds = new ArrayList<>();
        }

        ActivitiEventModel activitiEventModel = new ActivitiEventModel();
        activitiEventModel.setEventType(EventType.STARTWORKFLOW);
        activitiEventModel.setProcessDefinitionKey(processDefinitionKey);
        activitiEventModel.setUserId(userId);
        activitiEventModel.setBusinessKey(businessKey);
        activitiEventModel.setNextUserIds(nextUserIds);
        activitiEventModel.setVariables(variables);
        activitiEventModel.setBusCodeEnum(busCodeEnum);


        try {
            activitiEventProducer.fireEvent(activitiEventModel);
        }catch (Throwable th){
            logger.error("=startWorkflow=> error :",th);
            throw  th;
        }

    }

    /**
     * 查询所有待办任务
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TaskResult> findTodoTasks(String userId,Page page,BusCodeEnum busCodeEnum) {
        //校验参数
        Preconditions.checkNotNull(userId,"userId is null");
        Preconditions.checkNotNull(page,"page is null");
        Preconditions.checkNotNull(page.getPageSize(),"page.size is null");
        Preconditions.checkNotNull(page.getPageNo(),"page.no is null");

        Page<TaskResult> resultPage = page;
        resultPage = getTasks(userId,page);
        logger.info("=findTodoTasks=> end time:{};", new Date());
        return resultPage;
    }

    //组合所有的activiti与报警待办-无流程key限制
    private Page<TaskResult> getTasks(String userId,Page page){
        page = getTaskFromDb(null,userId,page);
        return page ;
    }
    //组合所有的activiti与报警待办-有流程key限制
    private Page<TaskResult> getTasks(List<String> processDefinitionKeys,String userId,Page page){
        page= getTaskFromDb(processDefinitionKeys,userId,page);
        return page ;
    }

    //从db层面获取任务
    private Page<TaskResult> getTaskFromDb(List<String> processDefinitionKeys,String userId,Page page){
        //获取activiti任务
        List<TaskResult> results = getTaskFromActiviti( processDefinitionKeys,userId);

        //安创建时间排序
        Collections.sort(results,new TaskComparator());
        int count = results.size();
        //人工分页
        results= PageUtil.page(results,page.getPageNo(),page.getPageSize());
        page.setResult(results);
        page.setTotalCount(count);
        return page;
    }

    private List<TaskResult> getTaskFromActiviti(List<String> processDefinitionKeys,String userId){

        String key="";
        if(processDefinitionKeys!=null){
            key = JSON.toJSONString(processDefinitionKeys)+userId;
        }else{
            key = userId;
        }

        if(activitiTaskCache.containKey(key)){
            return (List<TaskResult>)activitiTaskCache.getTaskPage(key);
        }


        // 根据当前人的ID查询
        TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateOrAssigned(userId);
        if(processDefinitionKeys!=null){
            taskQuery = taskQuery.processDefinitionKeyIn(processDefinitionKeys);
        }

        List<Task> tasks = taskQuery.list();

        List<TaskResult> results = new ArrayList<>();

        // 根据流程的业务ID查询实体并关联
        for (Task task : tasks) {
            String processInstanceId = task.getProcessInstanceId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
            if (processInstance == null) {
                continue;
            }
            String businessKey = processInstance.getBusinessKey();
            if (businessKey == null) {
                continue;
            }

            TaskResult result = new TaskResult();
            result.setTask(task);
            result.setProcessInstance(processInstance);
            result.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
            result.setTaskType(TaskType.ActivitiTask);
            results.add(result);
        }




        //结果放入缓存
        if(results!=null){
            activitiTaskCache.deleteTask(key);
            activitiTaskCache.initTaskPage(key,results);
        }
        return results;
    }

    /**
     * 查询所有待办任务
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TaskResult> findTodoTasks(List<String> processDefinitionKeys, String userId, Page page, BusCodeEnum busCodeEnum) {
        //校验参数
        Preconditions.checkNotNull(processDefinitionKeys,"processDefinitionKeys is null");
        Preconditions.checkNotNull(userId,"userId is null");
        Preconditions.checkNotNull(page,"page is null");
        Preconditions.checkNotNull(page.getPageSize(),"page.size is null");
        Preconditions.checkNotNull(page.getPageNo(),"page.no is null");

        Page<TaskResult> resultPage = page;
        resultPage = getTasks(processDefinitionKeys,userId,page);
        logger.info("=findTodoTasks=> end time:{};", new Date());

        return resultPage;
    }

    /**
     * 查询流程定义对象
     *
     * @param processDefinitionId 流程定义ID
     * @return
     */
    protected ProcessDefinition getProcessDefinition(String processDefinitionId) {

        long timeBegin= System.currentTimeMillis();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();

        logger.info("=getProcessDefinition=> spen[{}]ms;",System.currentTimeMillis()-timeBegin);


        return processDefinition;


    }


    /**
     * 读取运行中的流程
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TaskResult> findRunningProcessInstaces(String processDefinitionKey,String userID, Page page,BusCodeEnum busCodeEnum) {
        //校验参数
        Preconditions.checkNotNull(processDefinitionKey,"userId is null");
        Preconditions.checkNotNull(userID,"userId is null");
        Preconditions.checkNotNull(page,"page is null");
        Preconditions.checkNotNull(page.getPageSize(),"page.size is null");
        Preconditions.checkNotNull(page.getPageNo(),"page.no is null");


        int[] pageParams = initPageParm(page);

        long timeBegin= System.currentTimeMillis();

        List<TaskResult> taskResults = new ArrayList<TaskResult>();
        ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).active().involvedUser(userID).orderByProcessInstanceId().desc();
        List<ProcessInstance> list = query.listPage(pageParams[0], pageParams[1]);
        // 关联业务实体
        for (ProcessInstance processInstance : list) {
            String businessKey = processInstance.getBusinessKey();
            if (businessKey == null) {
                continue;
            }
            TaskResult result = new TaskResult();
            result.setProcessInstance(processInstance);
            result.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));


            // 设置当前任务信息
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().orderByTaskCreateTime().desc().listPage(0, 1);
            result.setTask(tasks.get(0));
            taskResults.add(result);
        }

        page.setResult(taskResults);
        page.setTotalCount(query.count());

        logger.info("=findRunningProcessInstaces=> spen[{}]ms;",System.currentTimeMillis()-timeBegin);


        return page;
    }

    /**
     * 读取已结束中的流程
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TaskResult> findFinishedProcessInstaces(String processDefinitionKey, String userID, Page page,BusCodeEnum busCodeEnum) {
        //校验参数
        Preconditions.checkNotNull(processDefinitionKey,"userId is null");
        Preconditions.checkNotNull(userID,"userId is null");
        Preconditions.checkNotNull(page,"page is null");
        Preconditions.checkNotNull(page.getPageSize(),"page.size is null");
        Preconditions.checkNotNull(page.getPageNo(),"page.no is null");


        int[] pageParams = initPageParm(page);
        Page<TaskResult> resultPage = page;
        long timeBegin= System.currentTimeMillis();

        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery().processDefinitionKey(processDefinitionKey).finished().involvedUser(userID).orderByProcessInstanceEndTime().desc();
        List<HistoricProcessInstance> list = query.listPage(pageParams[0], pageParams[1]);
        logger.info("=findFinishedProcessInstaces=> listPage[firstResult:{},maxResults:{}];",pageParams[0], pageParams[1]);

        List<TaskResult> taskResults = new ArrayList<>();
        // 关联业务实体
        for (HistoricProcessInstance historicProcessInstance : list) {
            String businessKey = historicProcessInstance.getBusinessKey();
            TaskResult result = new TaskResult();
            result.setProcessDefinition(getProcessDefinition(historicProcessInstance.getProcessDefinitionId()));
            result.setHistoricProcessInstance(historicProcessInstance);
            taskResults.add(result);
        }

        resultPage.setResult(taskResults);
        resultPage.setTotalCount(query.count());
        logger.info("=findFinishedProcessInstaces=> spen[{}]ms;",System.currentTimeMillis()-timeBegin);
        return resultPage;
    }

    /**
     * 签收任务
     */
    @Override
    public String claim(String taskId,String userId,BusCodeEnum busCodeEnum) {
        //校验参数
        Preconditions.checkNotNull(taskId,"taskId is null");
        Preconditions.checkNotNull(userId,"userId is null");



        long timeBegin= System.currentTimeMillis();

        taskService.claim(taskId, userId);
        activitiTaskCache.deleteAllTask();

        logger.info("=claim=> spen[{}]ms;",System.currentTimeMillis()-timeBegin);

        return "success";
    }

    /**
     * 完成单个节点任务
     * @return
     */
    @Override
    public void nodeComplete(WorkFlowDto workFlowDto) {
        nodeComplete(workFlowDto.getUserId(),workFlowDto.getTaskId(),workFlowDto.getVariables(),workFlowDto.getNextUserIds(),workFlowDto.getBusCodeEnum());
    }

    /**
     * 完成单个节点任务
     * @return
     */
    @Override
    public void nodeComplete(String userID,String taskId, Map<String, Object> variables,List<String> nextUserIds,BusCodeEnum busCodeEnum) {

        //校验参数
        Preconditions.checkNotNull(taskId,"taskID is null");
        Preconditions.checkNotNull(userID,"userId is null");


        if(nextUserIds!=null){
            nextUserIds = new ArrayList<>();
        }

        ActivitiEventModel activitiEventModel = new ActivitiEventModel();
        activitiEventModel.setEventType(EventType.NODECOMPLETE);
        activitiEventModel.setTaskId(taskId);
        activitiEventModel.setNextUserIds(nextUserIds);
        activitiEventModel.setVariables(variables);
        activitiEventModel.setBusCodeEnum(busCodeEnum);

        try {
            activitiEventProducer.fireEvent(activitiEventModel);
            activitiTaskCache.deleteAllTask();
        } catch (Exception e) {
            logger.error("error on complete task {}, variables={}", new Object[]{taskId, variables, e});
            throw e;
        }
    }

    @Override
    public void msgComplete(String userID, String msgId, BusCodeEnum busCodeEnum) {
        logger.info("=msgComplete=>userID:{},msgId:{}",userID,msgId);

    }

    @Override
    public void msgComplete(WorkFlowDto workFlowDto) {
        msgComplete(workFlowDto.getUserId(),workFlowDto.getTaskId(),workFlowDto.getBusCodeEnum());
    }

    /**
     * 某一次流程的执行经历的多少任务
     */
    @Override
    public List<HistoricTaskInstance> queryHistoricTask(String processInstanceId) {
        Preconditions.checkNotNull(processInstanceId,"processInstanceId is null");
        long timeBegin= System.currentTimeMillis();

        List<HistoricTaskInstance> list = processEngine.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
        if (list != null && list.size() > 0) {
            for (HistoricTaskInstance hti : list) {
                System.out.print("taskId:" + hti.getId()+"，");
                System.out.print("name:" + hti.getName()+"，");
                System.out.print("pdId:" + hti.getProcessDefinitionId()+"，");
                System.out.print("assignee:" + hti.getAssignee()+"，");
            }
        }

        logger.info("=queryHistoricTask=> spen[{}]ms;",System.currentTimeMillis()-timeBegin);


        return list;
    }

    @Override
    public Map<String, Object> getVariables(String processInstanceId) {
        Map<String, Object> result = new HashedMap();
        try{
            result = runtimeService.getVariables(processInstanceId);
        }catch (Exception ex){
            result = new HashedMap();
        }
        return result;
    }

    private int[]  initPageParm(Page page){
        //校验参数
        Preconditions.checkNotNull(page,"page is null");
        Preconditions.checkNotNull(page.getPageSize(),"page.size is null");
        Preconditions.checkNotNull(page.getPageNo(),"page.no is null");

        int firstResult = page.getFirst() - 1;
        int maxResults = page.getPageSize();
        return new int[]{firstResult, maxResults};
    }





}
