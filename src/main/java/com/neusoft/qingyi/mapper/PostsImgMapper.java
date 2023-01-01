package com.neusoft.qingyi.mapper;

import com.neusoft.qingyi.pojo.PostsImg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 29600
* @description 针对表【t_posts_img】的数据库操作Mapper
* @createDate 2022-11-12 23:10:04
* @Entity com.neusoft.qingyi.pojo.PostsImg
*/
public interface PostsImgMapper extends BaseMapper<PostsImg> {
    List<PostsImg> selectByPId(@Param("pid") Integer pid);
}




