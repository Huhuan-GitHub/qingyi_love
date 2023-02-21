package com.neusoft.qingyi.controller;

import com.neusoft.qingyi.common.ErrorCode;
import com.neusoft.qingyi.common.ResultUtils;
import com.neusoft.qingyi.service.MiniUserChatMessageService;
import com.neusoft.qingyi.util.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/message")
@Api("小程序聊天系统接口")
public class MiniUserMessageController {
    @Resource
    private MiniUserChatMessageService miniUserChatMessageService;

    @ApiOperation("获取小程序用户聊天列表")
    @GetMapping("/list")
    public ResponseResult<?> list(@RequestParam("openid") String openid) {
        if (openid == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(miniUserChatMessageService.getMiniUserChatMessageList(openid));
    }

    @ApiOperation("小程序用户查看消息详情接口")
    @PostMapping("/viewMessage")
    public ResponseResult<?> viewMessage(@RequestBody Map<String, String> messageMap) {
        String sendOpenid = messageMap.get("sendOpenid");
        String receiveOpenid = messageMap.get("receiveOpenid");
        if (sendOpenid == null || receiveOpenid == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(miniUserChatMessageService.viewMessage(sendOpenid, receiveOpenid));
    }
}
