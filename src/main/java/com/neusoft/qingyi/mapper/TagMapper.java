package com.neusoft.qingyi.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.neusoft.qingyi.pojo.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 29600
* @description 针对表【t_tag】的数据库操作Mapper
* @createDate 2022-11-12 23:10:04
* @Entity com.neusoft.qingyi.pojo.Tag
*/
public interface TagMapper extends BaseMapper<Tag> {
    List<Tag> selectAllByTId(@Param("tId") Integer tId);
}




