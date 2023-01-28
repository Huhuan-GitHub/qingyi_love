package com.neusoft.qingyi.controller;

import com.neusoft.qingyi.common.ErrorCode;
import com.neusoft.qingyi.common.ResultUtils;
import com.neusoft.qingyi.pojo.PostsLike;
import com.neusoft.qingyi.service.PostsLikeService;
import com.neusoft.qingyi.util.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/postsLike")
public class PostsLikeController {

    @Resource
    private PostsLikeService postsLikeService;

    @ApiOperation("用户点赞/取消点赞帖子接口")
    @PostMapping("/like")
    public ResponseResult<?> like(@RequestBody PostsLike postsLike) {
        if (postsLike == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        // 点赞帖子
        postsLikeService.likePost(postsLike);
        return ResultUtils.success(1);
    }
}
