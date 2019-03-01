package com.eloancn.framework.activiti.web.workflow;

import com.alibaba.fastjson.JSON;
import com.eloancn.framework.activiti.cmd.JumpActivityCmd;
import com.eloancn.framework.activiti.entity.workflow.ELProcessDefinitionEntity;
import com.eloancn.framework.activiti.service.activiti.WorkflowProcessDefinitionService;
import com.eloancn.framework.activiti.service.activiti.WorkflowTraceService;
import com.eloancn.framework.activiti.util.*;
import com.eloancn.framework.sevice.api.ResultDTO;
import com.eloancn.organ.api.DepartmentService;
import com.eloancn.organ.api.UserService;
import com.eloancn.organ.dto.TreeNodeDto;
import com.eloancn.organ.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * 流程管理控制器
 *
 * @author xvshu
 */
@Controller
@RequestMapping(value = "/workflow/user")
public class WorkFlowUserController {

    protected Logger logger = LoggerFactory.getLogger(getClass());



    @RequestMapping(value = "/department/loadAll")
    @ResponseBody
    public ResultDTO<List<TreeNodeDto>> loadAllDepartment(){
        logger.info("=WorkFlowUserController=>loadAllDepartment ");

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("status",1);
        ResultDTO<List<TreeNodeDto>> resultDTO  =null ;
        logger.info("=WorkFlowUserController=> Result:{}",JSON.toJSONString(resultDTO));
        return resultDTO;
    }

    @RequestMapping(value = "/user/loadAll")
    @ResponseBody
    public ResultDTO<List<UserDto>> loadAllUser(String depId){
        logger.info("=WorkFlowUserController=>loadAllUser depId:{}",depId);

        try{
            if(depId!=null){
                depId = java.net.URLDecoder.decode(depId,"UTF-8");
            }
        }catch(Exception ex){
            logger.error("=WorkFlowUserController=>loadAllUser decode depid:{} error",depId,ex);
        }
        logger.info("=WorkFlowUserController=>loadAllUser decode depId:{}",depId);

        //TODO:根据部门id获取用户
        UserDto searchDto = new UserDto();
        searchDto.setDepCode(depId);
        ResultDTO<List<UserDto>> resultDTO  = null;
        logger.info("=WorkFlowUserController=>loadAllUser depId:{} Result:{}",depId,JSON.toJSONString(resultDTO));
        return resultDTO;
    }
}