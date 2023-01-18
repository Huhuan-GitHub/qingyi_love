package com.neusoft.qingyi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.qingyi.common.ErrorCode;
import com.neusoft.qingyi.common.ResultUtils;
import com.neusoft.qingyi.pojo.Posts;
import com.neusoft.qingyi.service.MiniUserService;
import com.neusoft.qingyi.service.PostsService;
import com.neusoft.qingyi.util.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostsController {
    @Resource
    private PostsService postsService;

    @Resource
    private MiniUserService miniUserService;
    @ApiOperation(value = "获取分页帖子接口")
    @GetMapping("/getPostsPage")
    public ResponseResult<?> postsPageTest(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize) {
        if (currentPage <= 0 || pageSize <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(postsService.getPostsPage(currentPage, pageSize));
    }

    @ApiOperation(value = "获取分页帖子接口")
    @GetMapping("/getPostsByPage")
    public ResponseResult<?> getPostsByPage(@RequestParam(required = false, name = "openid") String openid, @RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize) {
        Map<String, Object> pageParamsMap = new HashMap<>();
        pageParamsMap.put("currentPage", currentPage);
        pageParamsMap.put("pageSize", pageSize);
        pageParamsMap.put("openid", openid);
        List<Posts> res = postsService.getPostsByPage(pageParamsMap);
        return new ResponseResult<>(200, "查询成功！", res);
    }

    @ApiOperation(value = "获取帖子详情接口")
    @GetMapping("/getPostsDetails")
    public ResponseResult<?> getPostsDetails(@RequestParam("pId") Integer pId, @RequestParam("openid") String openid) {
        if (pId == null || openid == null) {
            return new ResponseResult<>(400, "参数不正确");
        }
        Posts postsDetails = postsService.getPostsDetails(pId, openid);
        return new ResponseResult<>(200, "获取帖子详情成功！", postsDetails);
    }

//    @ApiOperation("上传帖子接口")
//    @RequestMapping(value = "/addPosts", method = RequestMethod.POST)
//    public ResponseResult<?> addPosts(@RequestBody Posts posts) {
//        // 是否匿名，暂时不用
//        // Boolean notReveal = Boolean.valueOf(request.getParameter("notReveal"));
//        if (posts.getOpenid() == null || posts.getOpenid().equals("")) {
//            return new ResponseResult<>(403, "没有登录或权限！");
//        }
//        if (posts.getContent() == null || posts.getContent().equals("")) {
//            return new ResponseResult<>(400, "发布帖子的内容为空！");
//        }
//        // 设置发帖时间参数->发送帖子
//        posts.setSendTime(new Date());
//        Posts resPosts = postsService.addPosts(posts);
//
//        if (resPosts.getPId() <= 0 || resPosts.getPId() == null || resPosts.getOpenid() == null || resPosts.getOpenid().equals("")) {
//            return new ResponseResult<>(400, "帖子发布失败！");
//        } else {
//            return new ResponseResult<>(200, "帖子发布成功！", resPosts.getPId());
//        }
//    }

    @ApiOperation("根据帖子主键获取帖子")
    @GetMapping("/toPostsDetails")
    public ResponseResult<?> toPostsDetails(@RequestParam Integer pId) {
        if (pId == null) {
            return new ResponseResult<>(400, "帖子主键为空");
        }
        Posts posts = postsService.getPostsById(pId);
        if (posts != null) {
            return new ResponseResult<>(200, "根据帖子主键获取帖子成功", posts);
        } else {
            return new ResponseResult<>(400, "根据帖子主键获取到的帖子为空", null);
        }
    }

    @ApiOperation("发布帖子接口")
    @PostMapping("/publicPosts")
    public ResponseResult<?> publicPosts(HttpServletRequest request, @RequestParam(value = "img", required = false) MultipartFile[] img) {
        String content = request.getParameter("content");
        String openid = request.getParameter("openid");
        Integer tId = Integer.valueOf(request.getParameter("tid"));
        Integer notReveal = Integer.valueOf(request.getParameter("notReveal"));
        if (content == null || openid == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        // 构建帖子对象
        Posts posts = new Posts(null, new Date(), content, openid, tId, notReveal);
        // 上传帖子
        boolean saveRes = postsService.publicPosts(posts, img);
        if (saveRes) {
            return ResultUtils.success(posts);
        } else {
            return ResultUtils.success(ErrorCode.SYSTEM_ERROR);
        }
    }
}
