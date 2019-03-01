package com.eloancn.framework.activiti.web.workflow;

import java.io.ByteArrayInputStream;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eloancn.framework.activiti.util.Page;
import com.eloancn.framework.activiti.util.PageUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 流程模型控制器
 *
 * @author xvshu
 */
@Controller
@RequestMapping(value = "/workflow/model")
public class ModelController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    RepositoryService repositoryService;

    /**
     * 模型列表
     */
    @RequestMapping(value = "list")
    public ModelAndView modelList(HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("workflow/model-list");
        return mav;
    }


    /**
     * 模型列表
     */
    @RequestMapping(value = "list/findall")
    @ResponseBody
    public Page<Model> findall(Integer page, Integer rows ,HttpServletRequest request) {

        logger.info("=ModelController.findall=> page:{} rows:{}",page,rows);
        if(page==null){
            page=1;
        }
        if(rows==null){
            rows=10;
        }

        Page<Model> pageResult = new Page<Model>(PageUtil.PAGE_SIZE);
        pageResult.setPageNo(page);
        pageResult.setPageSize(rows);

        ModelQuery modelQuery = repositoryService.createModelQuery().orderByCreateTime().desc();
        List<Model> list = modelQuery.listPage(pageResult.getFirst() - 1, pageResult.getPageSize());
        pageResult.setTotalCount(modelQuery.count());
        pageResult.setResult(list);
        return pageResult;
    }
    /**
     * 创建模型
     */
    @RequestMapping(value = "create", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
    @ResponseBody
    public String create(@RequestParam("name") String name, @RequestParam("key") String key, @RequestParam("description") String description,
                       HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
        String result="";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);
            Model modelData = repositoryService.newModel();

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            description = StringUtils.defaultString(description);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName(name);

            //todo：做唯一性验证
            modelData.setKey(StringUtils.defaultString(key));
            //查询key下流程
            List<Model> modelDataCheck = repositoryService.createModelQuery().modelKey(key).list();

            if(modelDataCheck!=null && modelDataCheck.size()>0){
                redirectAttributes.addFlashAttribute("message", "创建失败，key 重复请修改！");
                return "创建失败，key 重复请修改！";
            }

            repositoryService.saveModel(modelData);
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));

//            response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelData.getId());

            result="创建成功,modeid为:"+modelData.getId();
        } catch (Exception e) {
            logger.error("创建模型失败：", e);
            return "创建模型失败,error:"+e.getMessage();
        }
        return result;
    }

    /**
     * 根据Model部署流程
     */
    @RequestMapping(value = "deploy/{modelId}",produces="text/html;charset=UTF-8")
    @ResponseBody
    public String deploy(@PathVariable("modelId") String modelId, RedirectAttributes redirectAttributes) {
        String result ="";
        try {
            Model modelData = repositoryService.getModel(modelId);
            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));

            ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().processDefinitionKey(modelData.getKey());
//            List<ProcessDefinition> processDefinitionList = processDefinitionQuery.list();
//
//            if(processDefinitionList!=null && processDefinitionList.size()>0){
//                return "部署失败，相同key流程已经部署！";
//            }

            byte[] bpmnBytes = null;

            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            bpmnBytes = new BpmnXMLConverter().convertToXML(model);

            logger.info("xml is {}",new String(bpmnBytes,"UTF-8"));

            String processName = modelData.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment().name(modelData.getName()).addString(processName, new String(bpmnBytes,"UTF-8")).deploy();
            result="部署成功，部署ID=" + deployment.getId();
        } catch (Exception e) {
            logger.error("根据模型部署流程失败：modelId={}", modelId, e);
            result="部署失败，错误信息：" + e.getMessage();
        }
        return result;
    }

    /**
     * 导出model对象为指定类型
     *
     * @param modelId 模型ID
     * @param type    导出文件类型(bpmn\json)
     */
    @RequestMapping(value = "export/{modelId}/{type}")
    public void export(@PathVariable("modelId") String modelId,
                       @PathVariable("type") String type,
                       HttpServletResponse response) {
        try {
            Model modelData = repositoryService.getModel(modelId);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            byte[] modelEditorSource = repositoryService.getModelEditorSource(modelData.getId());

            JsonNode editorNode = new ObjectMapper().readTree(modelEditorSource);
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);

            // 处理异常
            if (bpmnModel.getMainProcess() == null) {
                response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
                response.getOutputStream().println("no main process, can't export for type: " + type);
                response.flushBuffer();
                return;
            }

            String filename = "";
            byte[] exportBytes = null;

            String mainProcessId = bpmnModel.getMainProcess().getId();

            if (type.equals("bpmn")) {

                BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
                exportBytes = xmlConverter.convertToXML(bpmnModel);

                filename = mainProcessId + ".bpmn20.xml";
            } else if (type.equals("json")) {

                exportBytes = modelEditorSource;
                filename = mainProcessId + ".json";

            }

            ByteArrayInputStream in = new ByteArrayInputStream(exportBytes);
            IOUtils.copy(in, response.getOutputStream());

            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.flushBuffer();
        } catch (Exception e) {
            logger.error("导出model的xml文件失败：modelId={}, type={}", modelId, type, e);
        }
    }

    @RequestMapping(value = "delete/{modelId}",produces="text/html;charset=UTF-8")
    @ResponseBody
    public String delete(@PathVariable("modelId") String modelId, RedirectAttributes redirectAttributes) {

        String result = "";
        Model modelData = repositoryService.getModel(modelId);
        ProcessDefinitionQuery processDefinitionQuery = null;
        if(modelData.getKey()!=null){
            processDefinitionQuery = repositoryService.createProcessDefinitionQuery().processDefinitionKey(modelData.getKey());

        }else{
            processDefinitionQuery = repositoryService.createProcessDefinitionQuery().processDefinitionId(modelData.getId());
        }
        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.list();

        if(processDefinitionList!=null && processDefinitionList.size()>0){
            return "删除失败，相同key流程已经部署使用！";
        }

        repositoryService.deleteModel(modelId);

        return "删除成功！";
    }

}
