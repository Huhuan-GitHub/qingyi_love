package com.neusoft.qingyi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.qingyi.myenum.ResponseCode;
import com.neusoft.qingyi.pojo.Tag;
import com.neusoft.qingyi.qingyiexception.QingYiException;
import com.neusoft.qingyi.service.TagService;
import com.neusoft.qingyi.util.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Resource
    private TagService tagService;

    @ApiOperation("获取所有帖子标签接口")
    @GetMapping("/getAllTag")
    public ResponseResult<?> getAllTag() {
        List<Tag> tagList = tagService.list();
        return new ResponseResult<>(200, "获取所有帖子标签成功", tagList);
    }

    @ApiOperation("分页获取帖子标签")
    @GetMapping("/getPostsTagByPage")
    public ResponseResult<?> getPostsTagByPage(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        // 处理请求参数错误异常
        if (pageSize == null || pageNo == null) {
            throw new QingYiException(ResponseCode.PARAMS_ERROR);
        }
        Page<Tag> tagPage = new Page<>(pageNo, pageSize);
        Page<Tag> page = tagService.page(tagPage);
        List<Tag> records = page.getRecords();
        return new ResponseResult<>(200, "分页获取帖子标签成功", records);
    }
}
