package com.eloancn.framework.activiti.web.workflow;

import com.alibaba.fastjson.JSON;
import com.eloancn.framework.activiti.entity.workflow.ELProcessInstance;
import com.eloancn.framework.activiti.util.Page;
import com.eloancn.framework.activiti.util.PageUtil;
import com.eloancn.framework.activiti.util.ProcessDefinitionCache;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.impl.cmd.GetIdentityLinksForProcessInstanceCmd;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/workflow/processinstance")
public class ProcessInstanceController {

    protected Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private RuntimeService runtimeService;


    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "running")
    public ModelAndView running(Model model, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/workflow/running-manage");
        Page<ProcessInstance> page = new Page<ProcessInstance>(PageUtil.PAGE_SIZE);
        int[] pageParams = PageUtil.init(page, request);

        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        List<ProcessInstance> list = processInstanceQuery.listPage(pageParams[0], pageParams[1]);
        page.setResult(list);
        page.setTotalCount(processInstanceQuery.count());
        mav.addObject("page", page);
        return mav;
    }

    @RequestMapping(value = "running/findall")
    @ResponseBody
    public Page<ELProcessInstance> runningAll(Integer page, Integer rows ,Model model, HttpServletRequest request) {

        logger.info("=running.findall=> page:{} rows:{}",page,rows);

        if(page==null){
            page=1;
        }
        if(rows==null){
            rows=10;
        }

        Page<ELProcessInstance> pageResult = new Page<ELProcessInstance>(PageUtil.PAGE_SIZE);

        pageResult.setPageNo(page);
        pageResult.setPageSize(rows);

        ProcessDefinitionCache.setRepositoryService(repositoryService);

        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();

        List<ProcessInstance> processInstances = processInstanceQuery.listPage(pageResult.getFirst() - 1, pageResult.getPageSize());



        List<ELProcessInstance> elProcessInstances = new ArrayList<ELProcessInstance>();
        for(ProcessInstance oneP : processInstances){
            ELProcessInstance oneE = ELProcessInstance.covert(oneP);
            List<IdentityLink> links = runtimeService.getIdentityLinksForProcessInstance(oneP.getId());
            for(IdentityLink oneLink:links){
                if(oneLink.getType().equals(IdentityLinkType.STARTER)){
                    oneE.setStartUserId(oneLink.getUserId());
                    break;
                }
            }
            elProcessInstances.add(oneE);
        }

        pageResult.setResult(elProcessInstances);
        pageResult.setTotalCount(processInstanceQuery.count());
        logger.info("=running.findall=>{}", JSON.toJSONString(pageResult));

        return pageResult;
    }

    @RequestMapping(value = "finished")
    public ModelAndView finished(Model model, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/workflow/finished-manage");
        return mav;
    }

    @RequestMapping(value = "finished/findall")
    @ResponseBody
    public Page<HistoricProcessInstance> finishedall(Integer page, Integer rows , HttpServletRequest request) {
        logger.info("=finished.finishedall=> page:{} rows:{}",page,rows);

        if(page==null){
            page=1;
        }
        if(rows==null){
            rows=10;
        }

        Page<HistoricProcessInstance> pageResult = new Page<HistoricProcessInstance>(PageUtil.PAGE_SIZE);
        pageResult.setPageNo(page);
        pageResult.setPageSize(rows);

        HistoricProcessInstanceQuery processInstanceQuery = historyService.createHistoricProcessInstanceQuery().finished();
        List<HistoricProcessInstance> list = processInstanceQuery.listPage(pageResult.getFirst() - 1, pageResult.getPageSize());

        pageResult.setResult(list);
        pageResult.setTotalCount(processInstanceQuery.count());
        logger.info("=finished.finishedall=>{}", JSON.toJSONString(pageResult));

        return pageResult;
    }

    /**
     * 挂起、激活流程实例
     */
    @RequestMapping(value = "update/{state}/{processInstanceId}",produces="text/html;charset=UTF-8")
    @ResponseBody
    public String updateState(@PathVariable("state") String state, @PathVariable("processInstanceId") String processInstanceId,
                              RedirectAttributes redirectAttributes) {
        String resultStr = "";
        if (state.equals("active")) {
            logger.info("=updateState=> active processInstanceId:"+processInstanceId);
            runtimeService.activateProcessInstanceById(processInstanceId);
            resultStr= "已激活ID为[" + processInstanceId + "]的流程实例。";
        } else if (state.equals("suspend")) {
            logger.info("=updateState=> suspend processInstanceId:"+processInstanceId);
            runtimeService.suspendProcessInstanceById(processInstanceId);
            resultStr= "已挂起ID为[" + processInstanceId + "]的流程实例。";
        }else if(state.equals("delete")){
            logger.info("=updateState=> delete processInstanceId:"+processInstanceId);
            runtimeService.deleteProcessInstance(processInstanceId,"管理员删除");
            resultStr="已刪除ID为[" + processInstanceId + "]的流程实例。";
        }else if(state.equals("deletehi")){
            logger.info("=updateState=> delete processInstanceId:"+processInstanceId);
            historyService.deleteHistoricProcessInstance(processInstanceId);
            resultStr="已刪除ID为[" + processInstanceId + "]的流程实例。";
        }else{
            logger.info("=updateState=>do nothing status is {} processInstanceId:{}",state,processInstanceId);
        }
        return resultStr;
    }

    /**
     * 挂起、激活流程实例
     */
    @RequestMapping(value = "addusers",produces="text/html;charset=UTF-8")
    @ResponseBody
    public String addusers(  String folwID,String addUsers,
                            RedirectAttributes redirectAttributes) {

        logger.info("=addusers=> folwID:{} addUsers:{}",folwID,addUsers);
        String result="";
        String errorTadkIds = "";
        String[] userIDs = addUsers.split("\\|");
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(folwID).list();
        for(int i = 0;i<tasks.size();i++){
            Task oneTask = tasks.get(i);
            if(oneTask.getAssignee()==null || oneTask.getAssignee().length()==0){
                for(String nextUser:userIDs){
                    logger.info("=ProcessInstanceController=>addCandidateUser taskService.addCandidateUser taskId:{} userId:{}",oneTask.getId(),nextUser);
                    taskService.addCandidateUser(oneTask.getId(),nextUser);
                }
            }else{
                errorTadkIds = errorTadkIds + "," + oneTask.getId();
            }
        }
        result = "已处理ID为[" + folwID + "]的流程实例。";
        if(errorTadkIds!=null && errorTadkIds.length()>0){
            result = result + "其中有些任务已经指定办理人，无法增加候选人，任务id为：["+errorTadkIds+"]";
        }

        return result;
    }

    /**
     * 挂起、激活流程实例
     */
    @RequestMapping(value = "modifyuser",produces="text/html;charset=UTF-8")
    @ResponseBody
    public String modifyuser(  String folwID,String modifyUsers,
                             RedirectAttributes redirectAttributes) {

        logger.info("=modifyuser=> folwID:{} modifyUsers:{}",folwID,modifyUsers);

        String result="";
        String[] userIDs = modifyUsers.split("\\|");
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(folwID).list();
        for(int i = 0;i<tasks.size();i++){
            Task oneTask = tasks.get(i);

            logger.info("=ProcessInstanceController=>modifyAssignee taskService.setAssignee taskId:{} userId:{}",oneTask.getId(),userIDs[0]);
            taskService.setAssignee(oneTask.getId(),userIDs[0]);

        }
        result = "已处理ID为[" + folwID + "]的流程实例。新办理人id为["+userIDs[0]+"]";

        return result;
    }

    /**
     * 挂起、激活流程实例
     */
    @RequestMapping(value = "deletehi/{processInstanceId}",produces="text/html;charset=UTF-8")
    @ResponseBody
    public String deletehi( @PathVariable("processInstanceId") String processInstanceId,
                              RedirectAttributes redirectAttributes) {

        logger.info("=updateState=> delete processInstanceId:"+processInstanceId);
        historyService.deleteHistoricProcessInstance(processInstanceId);
        String result = "已刪除ID为[" + processInstanceId + "]的流程实例。";

        return result;
    }
}
