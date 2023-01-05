package com.neusoft.qingyi.controller;

import com.neusoft.qingyi.myenum.ResponseCode;
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
            return new ResponseResult<>(400, "请求参数错误");
        }
        MiniUserAttention attention_res = miniUserAttentionService.attentionMiniUser(miniUserAttention);
        if (attention_res == null) {
            return new ResponseResult<>(500, "关注失败");
        } else {
            return new ResponseResult<>(200, "关注成功", attention_res);
        }
    }

    @ApiOperation("取消关注小程序用户接口")
    @PostMapping("/cancelAttentionMiniUser")
    public ResponseResult<?> cancelAttentionMiniUser(@RequestBody MiniUserAttention miniUserAttention) {
        if (miniUserAttention == null) {
            return new ResponseResult<>(400, "请求参数错误");
        }
        int cancel_res = miniUserAttentionService.cancelAttentionMiniUser(miniUserAttention);
        if (cancel_res >= 1) {
            return new ResponseResult<>(200, "取消成功");
        } else {
            return new ResponseResult<>(500, "取消失败");
        }
    }

    @ApiOperation("获取其他用户的主页信息接口")
    @GetMapping("/getMiniUserHomePageDetails")
    public ResponseResult<?> getMiniUserHomePageDetails(@RequestParam("miniId") Integer miniId) {
        if (miniId == null) {
            throw new QingYiException(ResponseCode.PARAMS_ERROR);
        }
        MiniUser miniUserHomePage = miniUserService.getMiniUserHomePage(miniId);
        return new ResponseResult<>(200, "请求成功", miniUserHomePage);
    }
}
