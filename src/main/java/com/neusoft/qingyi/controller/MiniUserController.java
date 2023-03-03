package com.neusoft.qingyi.controller;

import com.neusoft.qingyi.common.ErrorCode;
import com.neusoft.qingyi.common.ResultUtils;
import com.neusoft.qingyi.mapper.MiniUserAttentionMapper;
import com.neusoft.qingyi.pojo.MiniUser;
import com.neusoft.qingyi.pojo.MiniUserAttention;
import com.neusoft.qingyi.qingyiexception.QingYiException;
import com.neusoft.qingyi.service.MiniUserAttentionService;
import com.neusoft.qingyi.service.MiniUserService;
import com.neusoft.qingyi.util.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

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

    @ApiOperation("获取其他用户的主页信息接口")
    @GetMapping("/getMiniUserHomePageDetails")
    public ResponseResult<?> getMiniUserHomePageDetails(@RequestParam("miniId") Integer miniId) {
        if (miniId == null) {
            throw new QingYiException(ErrorCode.PARAMS_ERROR);
        }
        MiniUser miniUserHomePage = miniUserService.getMiniUserHomePage(miniId);
        return ResultUtils.success(miniUserHomePage);
    }

    @ApiOperation("根据openid获取主页信息接口")
    @GetMapping("/getMiniUserHomePageDetailsByOpenid")
    public ResponseResult<?> getMiniUserHomePageDetailsByOpenid(@RequestParam("openid") String openid) {
        if (openid == null) {
            throw new QingYiException(ErrorCode.PARAMS_ERROR);
        }
        MiniUser miniUserHomePage = miniUserService.getMiniUserHomePage(openid);
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
        return miniUserAttentionService.attentionMiniUser(miniUserAttention);
    }

    @ApiOperation("根据id取消关注接口")
    @PostMapping("/cancelAttention/{id}")
    public ResponseResult<?> cancelAttention(@PathVariable(value = "id") Integer id) {
        return miniUserService.cancelAttention(id);
    }

    @ApiOperation("获取小程序用户的关注数量")
    @GetMapping("/getAttentionSize")
    public ResponseResult<?> getAttentionSize(@RequestParam("openid") String openid) {
        return miniUserService.queryMiniUserAttentionSize(openid);
    }

    @ApiOperation("获取小程序用户被关注的数量")
    @GetMapping("/getAttentionedSize")
    public ResponseResult<?> getAttentionedSize(@RequestParam("openid") String openid) {
        return miniUserService.queryMiniUserAttentionedSize(openid);
    }

    @ApiOperation("获取小程序用户好友的数量")
    @GetMapping("/friendIntersectionSize")
    public ResponseResult<?> friendIntersectionSize(@RequestParam("openid") String openid) {
        return miniUserService.queryFriendSize(openid);
    }

    @ApiOperation("获取小程序用户关注列表")
    @GetMapping("/getMyAttentionList")
    public ResponseResult<?> getMyAttentionList(@RequestParam("openid") String openid, long pageNo, long pageSize) {
        return miniUserService.queryAttentionList(openid, pageNo, pageSize);
    }

    @ApiOperation("获取小程序用户粉丝列表")
    @GetMapping("/getMiniUserFansList")
    public ResponseResult<?> getMiniUserFansList(@RequestParam("openid") String openid, long pageNo, long pageSize) {
        return miniUserService.queryMiniUserFansList(openid, pageNo, pageSize);
    }

    @ApiOperation("获取小程序用户好友列表")
    @GetMapping("/getMiniUserFriendList")
    public ResponseResult<?> getMiniUserFriendList(@RequestParam("openid") String openid, long pageNo, long pageSize) {
        return miniUserService.queryMiniUserFriendList(openid, pageNo, pageSize);
    }

    @ApiOperation("判断两个小程序用户之间是否互相关注")
    @GetMapping("/whetherMiniUsersEachOtherAttention")
    public ResponseResult<?> getMiniUsersAttentionState(@Param("resourceOpenid") String resourceOpenid, @Param("targetOpenid") String targetOpenid) {
        return miniUserService.queryMiniUserAttentionState(resourceOpenid, targetOpenid);
    }
}
