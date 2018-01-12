package com.eloancn.framework.activiti.web.organ;

import com.eloancn.framework.sevice.api.ResultDTO;
import com.eloancn.organ.api.AreaService;
import com.eloancn.organ.dto.AreaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : CJT
 * @date : 2017/11/3
 */
@Controller
@RequestMapping("/area")
public class AreaController {

    private static final Logger logger = LoggerFactory.getLogger(AreaController.class);

    @Autowired
    private AreaService areaOrganService;

    /**
     * 根据Pid获取区域记录
     * @param areaDto
     * @return
     */
    @RequestMapping(value = "/loadRecord")
    @ResponseBody
    public ResultDTO<List<AreaDto>> loadRecord(AreaDto areaDto){
        logger.info("AreaController.loadRecord AreaDto:{}",areaDto);
        ResultDTO<List<AreaDto>> resultDTO = areaOrganService.searchArea(areaDto);
        logger.info("AreaController.loadRecord AreaDto:{},Result:{}",areaDto,resultDTO);

        return resultDTO;
    }

}
