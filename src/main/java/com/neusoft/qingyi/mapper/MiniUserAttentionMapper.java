package com.neusoft.qingyi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.qingyi.pojo.MiniUser;
import com.neusoft.qingyi.pojo.MiniUserAttention;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author 29600
 * @description 针对表【t_mini_user_attention】的数据库操作Mapper
 * @createDate 2022-12-29 20:47:15
 * @Entity com.neusoft.qingyi.pojo.MiniUserAttention
 */
public interface MiniUserAttentionMapper extends BaseMapper<MiniUserAttention> {
    int deleteAttention(@Param("attentionOpenid") String attentionOpenid, @Param("attenionedOpenid") String attenionedOpenid, @Param("date") Date date);

    List<MiniUserAttention> queryMiniUserFansList(@Param("openid") String openid, @Param("pageNo") long pageNo, @Param("pageSize") long pageSize);

    List<MiniUserAttention> mybatisTest(Set<String> set, @Param("attentionOpenid") String attentionOpenid);
}




