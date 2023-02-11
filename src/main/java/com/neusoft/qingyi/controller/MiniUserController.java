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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/miniUser")
public class MiniUserController {

    @Resource
    private MiniUserAttentionService miniUserAttentionService;

    @Resource
    private MiniUserService miniUserService;

    @ApiOperation("小程序用户更新信息接口")
    @PostMapping("/updateMiniUserInfo")
    public ResponseResult<?> updateMiniUserInfo(HttpServletRequest request, @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        String openid = request.getParameter("openid");
        String username = request.getParameter("username");
        if (openid == null || username == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        // 如果上传头像为空，只修改了”昵称“
        if (avatar == null) {
            MiniUser miniUser = miniUserService.updateMiniUserUsername(openid, username);
            return ResultUtils.success(miniUser);
        } else {
            // 否则：既修改了”昵称“，也修改了头像
            return ResultUtils.success(miniUserService.updateMiniUserUsernameAndAvatar(openid, username, avatar));
        }
    }

    @ApiOperation("小程序用户换取openid接口")
    @GetMapping("/getOpenid")
    public ResponseResult<?> getOpenid(@RequestParam("jsCode") String jsCode) {
        if (jsCode == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(miniUserService.getOpenid(jsCode));
    }

    @ApiOperation("小程序用户匿名登录接口")
    @PostMapping("/login")
    public ResponseResult<?> login(@RequestBody MiniUser miniUser) {
        if (miniUser == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(miniUserService.login(miniUser));
    }

    @ApiOperation("获取小程序用户关注列表")
    @GetMapping("/getMiniUserAttentionList")
    public ResponseResult<?> getMiniUserAttentionList(@RequestParam("openid") String openid) {
        if (openid == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(miniUserService.getMiniUserAttentionList(openid));
    }

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
        if (cancel_res == null) {
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
        return ResultUtils.success(miniUserHomePage);
    }

    @ApiOperation("根据openid获取小程序用户信息")
    @GetMapping("/getMiniUserInfoByOpenid")
    public ResponseResult<?> getMiniUserInfoByOpenid(@RequestParam("openid") String openid) {
        if (openid == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(miniUserService.getMiniUserByOpenid(openid));
    }
}
