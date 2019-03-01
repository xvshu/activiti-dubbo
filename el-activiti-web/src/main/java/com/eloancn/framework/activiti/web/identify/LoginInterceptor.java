package com.eloancn.framework.activiti.web.identify;

import com.eloancn.framework.activiti.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xvshu on 2017/12/19.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    private List<String> postUrl = Arrays.asList("login","logon","logout","js","css","statics","common","img","kindeditor","ui_component","img/activiti","workflow");

    /**
     * 在业务处理器处理请求之前被调用
     * 如果返回false
     *     从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
     * 如果返回true
     *    执行下一个拦截器,直到所有的拦截器都执行完毕
     *    再执行被拦截的Controller
     *    然后进入拦截器链,
     *    从最后一个拦截器往回执行所有的postHandle()
     *    接着再从最后一个拦截器往回执行所有的afterCompletion()
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        log.debug("preHandle=>");
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String url = requestUri.substring(contextPath.length());

        log.debug("preHandle=>requestUri:"+requestUri);
        log.debug("preHandle=>contextPath:"+contextPath);
        log.debug("preHandle=>url:"+url);

        for(String onePostUrl : postUrl){
            if(url.indexOf(onePostUrl)>=0 ){
                return true;
            }
        }


        String userID =   UserUtil.getUserIDFromSession(request.getSession());
        if(userID == null){
            log.info("preHandle=>Interceptor not login");
            request.getRequestDispatcher("/login").forward(request, response);
            return false;
        }else {
            return true;
        }
    }




}
