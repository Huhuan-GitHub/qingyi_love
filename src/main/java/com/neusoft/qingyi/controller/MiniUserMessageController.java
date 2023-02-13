package com.neusoft.qingyi.controller;

import com.neusoft.qingyi.common.ErrorCode;
import com.neusoft.qingyi.common.ResultUtils;
import com.neusoft.qingyi.service.MiniUserChatMessageService;
import com.neusoft.qingyi.util.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
}
