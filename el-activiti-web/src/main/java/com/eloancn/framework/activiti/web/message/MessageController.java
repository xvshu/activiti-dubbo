package com.eloancn.framework.activiti.web.message;

import com.eloancn.framework.sevice.api.PageParsDTO;
import com.eloancn.framework.sevice.api.PageResultDTO;
import com.eloancn.framework.sevice.api.ResultDTO;
import com.eloancn.message.api.MessageRecService;
import com.eloancn.message.api.MessageSendService;
import com.eloancn.message.constant.ResultCode;
import com.eloancn.message.dto.MessageRecDto;
import com.eloancn.message.dto.MessageSendDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author : CJT
 * @date : 2017/12/5
 */
@Controller
@RequestMapping("/message")
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageSendService messageSendService;
    @Autowired
    private MessageRecService messageRecService;

    /**
     * 发送消息页面
     */
    @RequestMapping(value = "/view/send")
    public ModelAndView sendJsp(HttpSession session, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/message/sendMessage");
        return mav;
    }

    /**
     * 发送消息
     */
    @RequestMapping(value = "/send")
    @ResponseBody
    public ResultDTO<Integer> send(MessageSendDto messageSendDto){
        logger.info("MessageController.send MessageSendDto:{}",messageSendDto);
        ResultDTO<Integer> resultDTO = messageSendService.insertMessageSend(messageSendDto);
        logger.info("MessageController.send MessageSendDto:{},Result:{}",messageSendDto,resultDTO);
        return resultDTO;
    }

    /**
     * 消息列表页面（接收者）
     */
    @RequestMapping(value = "/view/loadAll")
    public ModelAndView loadAllJsp(HttpSession session, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/message/messageList");
        return mav;
    }

    /**
     * 加载消息记录（接收者）
     */
    @RequestMapping(value = "/loadAll")
    @ResponseBody
    public ResultDTO<PageResultDTO<MessageRecDto>> loadAll(PageParsDTO<MessageRecDto> pageParsDTO, Integer rows, MessageRecDto messageRecDto){
        logger.info("MessageController.loadAll PageParsDTO:{},rows:{},MessageRecDto:{}",pageParsDTO,rows,messageRecDto);
        if (null!=rows && 0<rows){
            pageParsDTO.setLimit(rows);
        }else{
            pageParsDTO.setPage(0);
            pageParsDTO.setLimit(0);
        }
        if (null!=messageRecDto){
            pageParsDTO.setParam(messageRecDto);
        }
        ResultDTO<PageResultDTO<MessageRecDto>> resultDTO = messageRecService.searchMessageRec(pageParsDTO);
        logger.info("MessageController.loadAll PageParsDTO:{},rows:{},paramMap:{},MessageRecDto:{}",pageParsDTO,rows,messageRecDto,resultDTO);
        return resultDTO;
    }

    /**
     * 加载首页信息数据
     */
    @RequestMapping(value = "/searchMessageRecByUidForIndex")
    @ResponseBody
    public ResultDTO<PageResultDTO<MessageRecDto>> searchMessageRecByUidForIndex(Integer uid){
        logger.info("MessageController.searchMessageRecByUidForIndex Uid:{}",uid);
        ResultDTO<PageResultDTO<MessageRecDto>> resultDTO = messageRecService.searchMessageRecByUidForIndex(uid);
        logger.info("MessageController.searchMessageRecByUidForIndex Uid:{},Result:{}",uid,resultDTO);
        return resultDTO;
    }

    /**
     * 更新接受记录
     */
    @RequestMapping(value = "/updateMessageRec")
    @ResponseBody
    public ResultDTO<Integer> updateMessageRec(MessageRecDto messageRecDto){
        logger.info("MessageController.updateMessageRec MessageRecDto:{}",messageRecDto);
        ResultDTO<Integer> resultDTO = messageRecService.updateMessageRec(messageRecDto);
        logger.info("MessageController.updateMessageRec MessageRecDto:{},Result:{}",messageRecDto,resultDTO);
        return resultDTO;
    }

    /**
     * 公告列表页面（接收者）
     */
    @RequestMapping(value = "/view/loadAllNotice")
    public ModelAndView loadAllNoticeJsp(HttpSession session, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/message/messageNoticeList");
        return mav;
    }

    /**
     * 消息发送记录页面
     */
    @RequestMapping(value = "/view/sendMessageListJsp")
    public ModelAndView sendMessageListJsp(HttpSession session, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/message/sendMessageList");
        return mav;
    }

    /**
     * 加载发送记录
     */
    @RequestMapping(value = "/loadSendMessage")
    @ResponseBody
    public ResultDTO<PageResultDTO<MessageSendDto>> loadSendMessage(PageParsDTO<MessageSendDto> pageParsDTO, Integer rows, MessageSendDto messageSendDto){
        logger.info("MessageController.loadAllNotice PageParsDTO:{},rows:{},MessageSendDto:{}",pageParsDTO,rows,messageSendDto);
        if (null!=rows && 0<rows){
            pageParsDTO.setLimit(rows);
        }else{
            pageParsDTO.setPage(0);
            pageParsDTO.setLimit(0);
        }
        if (null!=messageSendDto){
            pageParsDTO.setParam(messageSendDto);
        }
        pageParsDTO.setSort("createTime.DESC");
        ResultDTO<PageResultDTO<MessageSendDto>> resultDTO = messageSendService.searchMessageSend(pageParsDTO);
        logger.info("MessageController.loadAllNotice PageParsDTO:{},rows:{},paramMap:{},MessageSendDto:{}",pageParsDTO,rows,messageSendDto,resultDTO);
        return resultDTO;
    }

    /**
     * 加载首页公告数据
     */
    @RequestMapping(value = "/searchNoticeMessageForIndex")
    @ResponseBody
    public ResultDTO<PageResultDTO<MessageSendDto>> searchNoticeMessageForIndex(MessageSendDto messageSendDto,HttpSession session){
        logger.info("MessageController.searchNoticeMessageForIndex MessageSendDto:{} HttpSession:{}",messageSendDto,session);
        PageParsDTO<MessageSendDto> pageParsDTO = new PageParsDTO<>();
        pageParsDTO.setPage(0);
        pageParsDTO.setLimit(5);
        pageParsDTO.setParam(messageSendDto);
        pageParsDTO.setSort("createTime.DESC");
        ResultDTO<PageResultDTO<MessageSendDto>> resultDTO = messageSendService.searchMessageSend(pageParsDTO);
        logger.info("MessageController.searchNoticeMessageForIndex MessageSendDto:{} HttpSession:{},Result:{}",messageSendDto,session,resultDTO);
        return resultDTO;
    }


}
