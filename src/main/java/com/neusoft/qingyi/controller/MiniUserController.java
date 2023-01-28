package com.neusoft.qingyi.controller;

import com.neusoft.qingyi.common.ErrorCode;
import com.neusoft.qingyi.common.ResultUtils;
import com.neusoft.qingyi.pojo.MiniUser;
import com.neusoft.qingyi.pojo.MiniUserAttention;
import com.neusoft.qingyi.qingyiexception.QingYiException;
import com.neusoft.qingyi.service.MiniUserAttentionService;
import com.neusoft.qingyi.service.MiniUserService;
import com.neusoft.qingyi.util.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/miniUser")
public class MiniUserController {

    @Resource
    private MiniUserAttentionService miniUserAttentionService;

    @Resource
    private MiniUserService miniUserService;

    @ApiOperation("关注小程序用户接口")
    @PostMapping("/attentionMiniUser")
    public ResponseResult<?> attentionMiniUser(@RequestBody MiniUserAttention miniUserAttention) {
        if (miniUserAttention == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        MiniUserAttention attention_res = miniUserAttentionService.attentionMiniUser(miniUserAttention);
        if (attention_res == null) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
        } else {
            return ResultUtils.success(attention_res);
        }
    }

    @ApiOperation("取消关注小程序用户接口")
    @PostMapping("/cancelAttentionMiniUser")
    public ResponseResult<?> cancelAttentionMiniUser(@RequestBody MiniUserAttention miniUserAttention) {
        if (miniUserAttention == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        MiniUserAttention cancel_res = miniUserAttentionService.cancelAttentionMiniUser(miniUserAttention);
        if (cancel_res==null) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
        } else {
            cancel_res.setIsCancelAttention(1);
            return ResultUtils.success(cancel_res);
        }
    }

    @ApiOperation("获取其他用户的主页信息接口")
    @GetMapping("/getMiniUserHomePageDetails")
    public ResponseResult<?> getMiniUserHomePageDetails(@RequestParam("miniId") Integer miniId) {
        if (miniId == null) {
            throw new QingYiException(ErrorCode.PARAMS_ERROR);
        }
        MiniUser miniUserHomePage = miniUserService.getMiniUserHomePage(miniId);
        return new ResponseResult<>(200, "请求成功", miniUserHomePage);
    }
}
