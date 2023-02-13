package com.neusoft.qingyi.controller;

import com.neusoft.qingyi.service.PostsImgService;
import com.neusoft.qingyi.util.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/postsImg")
public class PostsImgController {
    @Resource
    private PostsImgService postsImgService;

    @ApiOperation("将图片写入缓存")
    @PostMapping("/addPostsImg")
    public ResponseResult addPostsImg(HttpServletRequest request,
                                      @RequestParam("file") MultipartFile files) throws IOException {
        String openid = request.getParameter("openid");
        Integer pId = Integer.valueOf(request.getParameter("pId"));
        if (pId == null || pId.equals("")) {
            return new ResponseResult<>(400, "pId不能为空", -1);
        }
        if (openid == null || openid.equals("")) {
            return new ResponseResult<>(400, "openid不能为空", -1);
        }
        if (files == null) {
            return new ResponseResult<>(400, "上传文件为空", -1);
        }
        int res;
        res = postsImgService.addPostsImg(pId, openid, files);
        if (res <= 0) {
            return new ResponseResult<>(400, "帖子图片写入失败！", -1);
        } else {
            return new ResponseResult<>(200, "帖子图片写入成功！");
        }
    }

    
    @PostMapping("/addPostsImgToCache")
    public ResponseResult addPostsImgToCache(@RequestParam("files") MultipartFile[] files) throws IOException {
        for (MultipartFile file : files) {
            postsImgService.addPostsImg(1, "openid", file);
        }
        return new ResponseResult<>(200, "写入缓存成功！");
    }
}
