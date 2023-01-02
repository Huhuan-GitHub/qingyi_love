package com.neusoft.qingyi.controller;

import com.neusoft.qingyi.mapper.PostsCommentMapper;
import com.neusoft.qingyi.myenum.ResponseCode;
import com.neusoft.qingyi.pojo.PostsComment;
import com.neusoft.qingyi.qingyiexception.QingYiException;
import com.neusoft.qingyi.service.PostsCommentService;
import com.neusoft.qingyi.util.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "帖子评论接口")
@RequestMapping("/postsComment")
public class PostsCommentController {

    @Resource
    private PostsCommentService postsCommentService;
    @Resource
    private PostsCommentMapper postsCommentMapper;

    @ApiOperation("分页获取帖子评论列表接口")
    @GetMapping("/getPostsCommentListByPage")
    public ResponseResult<?> getPostsCommentListByPage(@RequestParam Integer currentPage, @RequestParam Integer pageSize, @RequestParam Integer pId) {
        if (currentPage == null || pageSize == null) {
            return new ResponseResult<>(ResponseCode.POSTS_COMMENT_PAGE_PARAMS_ERROR.getCode(), ResponseCode.POSTS_COMMENT_PAGE_PARAMS_ERROR.getMsg());
        }
        return null;
//        List<PostsComment> postsCommentList = postsCommentService.getPostsCommentByPage(currentPage, pageSize, pId);
//        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMsg(), postsCommentList);
    }

    @ApiOperation("评论帖子接口")
    @PostMapping("/replyPostsComment")
    public ResponseResult<?> replyPostsComment(@RequestBody PostsComment postsComment) {
        if (postsComment == null) {
            throw new QingYiException(ResponseCode.PARAMS_ERROR);
        }
        PostsComment newPostsComment = postsCommentService.miniUserCommentPosts(postsComment);
        return new ResponseResult<>(200, "评论成功", newPostsComment);
    }
}
