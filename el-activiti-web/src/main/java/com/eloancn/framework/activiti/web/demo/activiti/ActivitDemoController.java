package com.eloancn.framework.activiti.web.demo.activiti;

import com.alibaba.fastjson.JSON;
import com.eloancn.framework.activiti.TaskResult;
import com.eloancn.framework.activiti.entity.workflow.ELProcessInstance;
import com.eloancn.framework.activiti.entity.workflow.ELTask;
import com.eloancn.framework.activiti.facade.ElActiviti;
import com.eloancn.framework.activiti.util.Page;
import com.eloancn.framework.activiti.util.PageUtil;
import com.eloancn.framework.activiti.util.ProcessDefinitionCache;
import com.eloancn.framework.activiti.util.Variable;
import com.eloancn.framework.activiti.util.demo.ActivitiUtils;
import com.eloancn.organ.common.BusCodeEnum;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * &#x8bf7;&#x5047;&#x63a7;&#x5236;&#x5668;&#xff0c;&#x5305;&#x542b;&#x4fdd;&#x5b58;&#x3001;&#x542f;&#x52a8;&#x6d41;&#x7a0b;
 *
 * @author xvshu
 */
@Controller
@RequestMapping(value = "/demo/activiti")
public class ActivitDemoController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final String processDefinitionKey = "test-xvshu";

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ElActiviti elActiviti;

    @RequestMapping(value = {"apply", ""})
    public String createForm(Model model) {
        return "/demo/activiti/apply";
    }

    /**
     * 启动流程demo
     */
    @RequestMapping(value = "start", method = RequestMethod.POST)
    public String startWorkflow(RedirectAttributes redirectAttributes, HttpSession session,String nextUsers) {
        logger.info("=ActivitDemoController.startWorkflow=> nextUsers:{}",nextUsers);

        try {
            String[] nUsers =null;
            String userId = ActivitiUtils.getUserIDFromSession(session);
            if(nextUsers!=null &&nextUsers.length()>0){
                nUsers = nextUsers.split(",");
            }
            List<String> nextUserIds = new ArrayList<>();
            nextUserIds.addAll(Arrays.asList(nUsers));
            elActiviti.startWorkflow(processDefinitionKey,userId,String.valueOf(System.currentTimeMillis()),nextUserIds, null, BusCodeEnum.LI_CAI);
            redirectAttributes.addFlashAttribute("message", "流程已提交");
        } catch (Exception e) {
            if (e.getMessage().indexOf("no processes deployed with key") != -1) {
                logger.warn("没有部署流程!", e);
                redirectAttributes.addFlashAttribute("error", "没有部署流程，请在[工作流]->[流程管理]页面点击<重新部署流程>");
            } else {
                logger.error("启动请假流程失败：", e);
                redirectAttributes.addFlashAttribute("error", "系统内部错误！");
            }
        }
        return "redirect:/demo/activiti/apply";
    }

    /**
     * 设置流程demo操作人
     */
    @RequestMapping(value = "demouser")
    public ModelAndView demoUser(RedirectAttributes redirectAttributes, HttpSession session) {
        ModelAndView mav = new ModelAndView("/demo/activiti/setdemouser");
        return mav;
    }


    /**
     * 设置流程demo操作人
     */
    @RequestMapping(value = "setdemouser", method = RequestMethod.POST)
    public String setDemoUser(String userId, RedirectAttributes redirectAttributes, HttpSession session) {

        try {
            ActivitiUtils.saveUserToSession(session, userId);
            logger.info("=ActivitDemoController.setDemoUser=> userid:{}",userId);
            redirectAttributes.addFlashAttribute("message", "流程示例用户已更新，用户ID：" + String.valueOf(userId));
        }catch (Exception ex){
            redirectAttributes.addFlashAttribute("message", "出现错误：" + ex.getMessage());
            logger.error("=ActivitDemoController.setDemoUser=>",ex);
        }

        return "redirect:/demo/activiti/demouser";
    }

    /**
     * 任务列表
     *
     */
    @RequestMapping(value = "list/task")
    public ModelAndView taskList(HttpSession session, HttpServletRequest request) {

        String userId = ActivitiUtils.getUserIDFromSession(session);

        ModelAndView mav = new ModelAndView("/demo/activiti/taskList");
        Page<TaskResult> page = new Page<TaskResult>(PageUtil.PAGE_SIZE);
        PageUtil.init(page, request);
        List<String> processDefinitionKeys = new ArrayList<String>();
        processDefinitionKeys.add(processDefinitionKey);
        page =  elActiviti.findTodoTasks(processDefinitionKeys,userId,page,BusCodeEnum.LI_CAI);
        mav.addObject("page", page);
        return mav;
    }

    /**
     * 任务列表
     *
     */
    @RequestMapping(value = "list/taskall")
    public ModelAndView taskListAll(HttpSession session, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/demo/activiti/all-task");
        return mav;
    }


    /**
     * 任务列表
     *
     */
    @RequestMapping(value = "list/taskall/findall")
    @ResponseBody
    public Page<ActivitResult> taskAllPageFind(Integer page, Integer rows ,HttpSession session, HttpServletRequest request) {
        logger.info("=finished.finishedall=> page:{} rows:{}",page,rows);
        String userId = ActivitiUtils.getUserIDFromSession(session);

        if(page==null){
            page=1;
        }
        if(rows==null){
            rows=10;
        }

        Page<TaskResult> pageResult = new Page<TaskResult>(PageUtil.PAGE_SIZE);
        pageResult.setPageNo(page);
        pageResult.setPageSize(rows);
        pageResult = elActiviti.findTodoTasks(userId,pageResult,BusCodeEnum.LI_CAI);
        List<TaskResult>  results = pageResult.getResult();
        List<ActivitResult> resultsfic = new ArrayList<>();
        ProcessDefinitionCache.setRepositoryService(repositoryService);

        for(TaskResult taskResult:results){
            ActivitResult activitResult = new ActivitResult();
            ELProcessInstance elProcessInstance = ELProcessInstance.covert(taskResult.getProcessInstance());
            activitResult.setElProcessInstance(elProcessInstance);
            activitResult.setTask(ELTask.convert(taskResult.getTask()));
            resultsfic.add(activitResult);
        }

        Page<ActivitResult> pageResultFix = new Page<ActivitResult>(PageUtil.PAGE_SIZE);
        pageResultFix.setPageNo(page);
        pageResultFix.setPageSize(rows);
        pageResultFix.setResult(resultsfic);
        pageResultFix.setTotalCount(pageResult.getTotalCount());
        logger.info("=findTodoTasks=> result:{} ",JSON.toJSONString(pageResultFix));
        return pageResultFix;
    }

    public class ActivitResult{
        private ELProcessInstance elProcessInstance;
        private ELTask task;

        public ELProcessInstance getElProcessInstance() {
            return elProcessInstance;
        }

        public void setElProcessInstance(ELProcessInstance elProcessInstance) {
            this.elProcessInstance = elProcessInstance;
        }

        public ELTask getTask() {
            return task;
        }

        public void setTask(ELTask task) {
            this.task = task;
        }
    }

    /**
     * 读取运行中的流程实例
     *
     * @return
     */
    @RequestMapping(value = "list/running")
    public ModelAndView runningList(HttpSession session,HttpServletRequest request) {

        String userId = ActivitiUtils.getUserIDFromSession(session);

        ModelAndView mav = new ModelAndView("/demo/activiti/running");
        Page<TaskResult> page = new Page<TaskResult>(PageUtil.PAGE_SIZE);
        PageUtil.init(page, request);
        page = elActiviti.findRunningProcessInstaces(processDefinitionKey,userId, page,BusCodeEnum.LI_CAI);



        mav.addObject("page", page);
        return mav;
    }

    /**
     * 读取结束的流程实例
     *
     * @return
     */
    @RequestMapping(value = "list/finished")
    public ModelAndView finishedList(HttpSession session,HttpServletRequest request) {

        String userId = ActivitiUtils.getUserIDFromSession(session);
        ModelAndView mav = new ModelAndView("/demo/activiti/finished");
        Page<TaskResult> page = new Page<TaskResult>(PageUtil.PAGE_SIZE);
        PageUtil.init(page, request);
        page  = elActiviti.findFinishedProcessInstaces(processDefinitionKey,userId,page,BusCodeEnum.LI_CAI);

        mav.addObject("page", page);
        return mav;
    }

    /**
     * 签收任务
     */
    @RequestMapping(value = "task/claim/{id}")
    public String claim(@PathVariable("id") String taskId, HttpSession session, RedirectAttributes redirectAttributes) {
        String userId = ActivitiUtils.getUserIDFromSession(session);
        elActiviti.claim(taskId, userId,BusCodeEnum.LI_CAI);
        redirectAttributes.addFlashAttribute("message", "任务已签收");
        return "redirect:/demo/activiti/list/task";
    }

    /**
     * 签收任务
     */
    @RequestMapping(value = "task/claimWF/{id}")
    @ResponseBody
    public String claimWF(@PathVariable("id") String taskId, HttpSession session, RedirectAttributes redirectAttributes) {
        String userId = ActivitiUtils.getUserIDFromSession(session);
        elActiviti.claim(taskId, userId,BusCodeEnum.LI_CAI);
        return "任务已签收";
    }

    /**
     * 完成任务
     *
     * @return
     */
    @RequestMapping(value = "complete/{id}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String complete(@PathVariable("id") String taskId, Variable var,HttpSession session) {
        try {
            Map<String, Object> variables = var.getVariableMap();
            String userId = ActivitiUtils.getUserIDFromSession(session);
            String nextUserId = (String)variables.get("nextUserId");

            String[] nUsers =null;
            if(nextUserId!=null &&nextUserId.length()>0){
                nUsers = nextUserId.split(",");
            }
            List<String> nextUserIds = new ArrayList<>();
            if(nUsers!=null && nUsers.length>0){
                nextUserIds.addAll(Arrays.asList(nUsers));
            }

            logger.info("=ActivitDemoController.complete=>taskId:{} nextUserId:{} variables:{}",taskId,JSON.toJSONString(nextUserId), JSON.toJSONString(variables));

            elActiviti.nodeComplete(userId,taskId, variables,nextUserIds,BusCodeEnum.LI_CAI);
            return "success";
        } catch (Exception e) {
            logger.error("=ActivitDemoController.complete=>error on complete task {}, variables={}", new Object[]{taskId, var.getVariableMap(), e});
            return "error";
        }
    }



    /**
     * 完成任务
     *
     * @return
     */
    @RequestMapping(value = "completeWf", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String completeWF(String taskId, boolean isPass, Variable var, HttpSession session) {
        logger.info("=completeWF=> taskId:{} isPass:{}",taskId,isPass);
        try {
            Map<String, Object> variables = new HashedMap();
            String userId = ActivitiUtils.getUserIDFromSession(session);
            String nextUserId = (String)variables.get("nextUserId");

            String[] nUsers =null;
            if(nextUserId!=null &&nextUserId.length()>0){
                nUsers = nextUserId.split(",");
            }
            List<String> nextUserIds = new ArrayList<>();
            if(nUsers!=null && nUsers.length>0){
                nextUserIds.addAll(Arrays.asList(nUsers));
            }

            variables.put("pass",isPass);

            logger.info("=ActivitDemoController.complete=>taskId:{} nextUserId:{} variables:{}",taskId,JSON.toJSONString(nextUserId), JSON.toJSONString(variables));

            elActiviti.nodeComplete(userId,taskId, variables,nextUserIds,BusCodeEnum.LI_CAI);
            return "办理成功！";
        } catch (Exception e) {
            logger.error("=ActivitDemoController.complete=>error on complete task {}, variables={}", new Object[]{taskId, var.getVariableMap(), e});
            return "出现错误，请联系管理员";
        }
    }


}
