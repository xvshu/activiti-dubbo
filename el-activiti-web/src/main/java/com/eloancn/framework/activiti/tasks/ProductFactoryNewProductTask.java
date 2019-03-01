package com.eloancn.framework.activiti.tasks;

import com.eloancn.backProductFactory.api.product.ProductApi;
import com.eloancn.backProductFactory.common.enums.ProductStatusEnum;
import com.eloancn.backProductFactory.dto.ResponseResult;
import com.eloancn.backProductFactory.dto.product.ProductDto;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2019/1/22 0022.
 */
@Service("productFactoryNewProductTask")
public class ProductFactoryNewProductTask implements Serializable {
    private static Logger logger = LoggerFactory.getLogger(ProductFactoryNewProductTask.class);


    public void execute(DelegateExecution delegateExecution,ProductApi productApi) throws Exception {
        logger.info("ProductFactoryNewProductTask begin！");
        Boolean val = (Boolean)delegateExecution.getVariable("pass");
//        Boolean val = (Boolean) pass.getValue(delegateExecution);
        logger.info("=ProductFactoryNewProductTask=>business:{} prossId:{} pass:{}",delegateExecution.getBusinessKey(),delegateExecution.getProcessInstanceId(),val.toString());

        //修改产品状态为审核中
        ProductDto pactDto = new ProductDto();
        pactDto.setId(Long.valueOf(delegateExecution.getBusinessKey()));
        pactDto.setUpdateTime(new Date());
        pactDto.setLastMender("workflow");
        //审核通过，则更新产品状态为待发布否则为待审核
        if(val){
            pactDto.setStatus(ProductStatusEnum.product_status__publishing.getStatus());
        }else{
            pactDto.setStatus(ProductStatusEnum.product_status__create.getStatus());
        }
        ResponseResult<Integer> responseResult = productApi.update(pactDto);


        logger.info("ProductFactoryNewProductTask end！");

    }
}
