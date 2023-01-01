package com.neusoft.qingyi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.neusoft.qingyi.pojo.MiniUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author 29600
 * @description 针对表【t_mini_user】的数据库操作Mapper
 * @createDate 2022-11-12 23:10:04
 * @Entity com.neusoft.qingyi.pojo.MiniUser
 */
public interface MiniUserMapper extends BaseMapper<MiniUser> {
    List<MiniUser> selectByOpenid(@Param("openid") String openid);

    MiniUser selectMiniUserByOpenid(@Param("openid")String openid);
}




