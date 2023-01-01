package com.neusoft.qingyi.controller;

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
        if (postsLike.getOpenid() == null || postsLike.getP_id() == null) {
            return new ResponseResult<>(400, "openid或Pid为空", -1);
        }
        // 点赞/取消点赞帖子
        Integer res = postsLikeService.likeOrUnLikePosts(postsLike);
        if(res==null){
            return new ResponseResult<>(500, "点赞/取消点赞失败");
        }
        if (res >= 0) {
            return new ResponseResult<>(200, "点赞/取消点赞成功", res);
        } else {
            return new ResponseResult<>(500, "点赞/取消点赞失败");
        }
    }
}
