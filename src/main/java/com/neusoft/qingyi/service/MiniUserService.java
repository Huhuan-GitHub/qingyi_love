package com.neusoft.qingyi.service;

import com.neusoft.qingyi.pojo.MiniUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.qingyi.pojo.MiniUserAttention;
import com.neusoft.qingyi.util.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author 29600
 * @description 针对表【t_mini_user】的数据库操作Service
 * @createDate 2022-11-12 23:10:04
 */
public interface MiniUserService extends IService<MiniUser> {
    MiniUser getMiniUserHomePage(Integer miniId);

    MiniUser getMiniUserHomePage(String openid);

    MiniUser getMiniUserByOpenid(String openid);

    MiniUser getMiniUserAttentionList(String openid);

    String getOpenid(String jsCode);

    MiniUser login(MiniUser miniUser);

    MiniUser updateMiniUserUsername(String openid, String username);

    MiniUser updateMiniUserUsernameAndAvatar(String openid, String username, MultipartFile avatar);

    ResponseResult<?> queryMiniUserAttentionSize(String openid);

    ResponseResult<?> queryMiniUserAttentionedSize(String openid);

    ResponseResult<?> queryFriendSize(String openid);

    ResponseResult<?> queryAttentionList(String openid, long pageNo, long pageSize);

    ResponseResult<?> cancelAttention(MiniUserAttention miniUserAttention);

    ResponseResult<?> cancelAttention(Integer id);

    ResponseResult<?> queryMiniUserFansList(String openid, long pageNo, long pageSize);

    ResponseResult<?> queryMiniUserFriendList(String openid, long pageNo, long pageSize);

    ResponseResult<?> queryMiniUserAttentionState(String resourceOpenid, String targetOpenid);
}
