package com.neusoft.qingyi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.qingyi.pojo.MiniUser;
import com.neusoft.qingyi.pojo.MiniUserAttention;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 29600
 * @description 针对表【t_mini_user】的数据库操作Mapper
 * @createDate 2022-11-12 23:10:04
 * @Entity com.neusoft.qingyi.pojo.MiniUser
 */
public interface MiniUserMapper extends BaseMapper<MiniUser> {
    List<MiniUser> selectByOpenid(@Param("openid") String openid);

    MiniUser selectMiniUserByOpenid(@Param("openid") String openid);

    MiniUser selectMiniUserHomePage(@Param("miniId") Integer miniId);

    MiniUser selectMiniUserThreeData(@Param("openid") String openid);

    MiniUser selectMiniUserAttentionList(@Param("openid") String openid);

    Integer updateMiniUserUsernameByOpenid(@Param("openid") String openid, @Param("username") String username);

    List<MiniUserAttention> queryMiniUserAttentionList(@Param("openid") String openid, @Param("pageNo") long pageNo, @Param("pageSize") long pageSize);
}




