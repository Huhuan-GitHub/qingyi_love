package com.neusoft.qingyi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.qingyi.common.ErrorCode;
import com.neusoft.qingyi.common.ResultUtils;
import com.neusoft.qingyi.pojo.Posts;
import com.neusoft.qingyi.service.MiniUserService;
import com.neusoft.qingyi.service.PostsService;
import com.neusoft.qingyi.util.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
    public ResponseResult<?> postsPageTest(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, @RequestParam(value = "openid", required = false) String openid) {
        if (currentPage <= 0 || pageSize <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(postsService.getPostsPage(currentPage, pageSize, openid));
    }

    @ApiOperation(value = "滚动分页获取帖子")
    @GetMapping("")
    public ResponseResult<?> queryPosts(@RequestParam("lastId") Long max, @RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        return postsService.queryPosts(max,offset);
    }

    @ApiOperation(value = "获取帖子详情接口")
    @GetMapping("/getPostsDetails")
    public ResponseResult<?> getPostsDetails(@RequestParam("pId") Integer pId, @RequestParam(value = "openid", required = false) String openid) {
        if (pId == null) {
            return new ResponseResult<>(400, "参数不正确");
        }
        Posts postsDetails = postsService.getPostsDetails(pId, openid);
        return new ResponseResult<>(200, "获取帖子详情成功！", postsDetails);
    }

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
            return ResultUtils.success(postsService.getPostsDetails(posts.getPId(), null));
        } else {
            return ResultUtils.success(ErrorCode.SYSTEM_ERROR);
        }
    }

    @ApiOperation("删除帖子接口")
    @PostMapping("/deletePost")
    public synchronized ResponseResult<?> deletePost(@RequestBody Map<String, Integer> map) {
        if (map.get("pId") == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(postsService.remove(new QueryWrapper<Posts>().eq("p_id", map.get("pId"))));
    }
}
