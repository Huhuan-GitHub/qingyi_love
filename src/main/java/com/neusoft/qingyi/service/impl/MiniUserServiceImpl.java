package com.neusoft.qingyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.qingyi.common.ErrorCode;
import com.neusoft.qingyi.common.ResultUtils;
import com.neusoft.qingyi.mapper.MiniUserAttentionMapper;
import com.neusoft.qingyi.pojo.MiniUser;
import com.neusoft.qingyi.pojo.MiniUserAttention;
import com.neusoft.qingyi.qingyiexception.QingYiException;
import com.neusoft.qingyi.service.MiniUserService;
import com.neusoft.qingyi.mapper.MiniUserMapper;
import com.neusoft.qingyi.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 29600
 * @description 针对表【t_mini_user】的数据库操作Service实现
 * @createDate 2022-11-12 23:10:04
 */
@Service
public class MiniUserServiceImpl extends ServiceImpl<MiniUserMapper, MiniUser>
        implements MiniUserService {

    @Value("${wx.APPID}")
    private String appid;

    @Value("${wx.SECRET}")
    private String secret;

    @Value("${wx.CODE_2_URL}")
    private String codeUrl;

    @Value("${wx.user.temp-avatar-url}")
    private String tempAvatarUrl;

    @Value("${wx.user.temp-username}")
    private String tempUsername;

    @Value("${wx.user.temp-backgroundImage}")
    private String tempBackgroundImage;

    @Value("${mini-user.avatar-path}")
    private String miniUserAvatarPath;

    @Value("${mini-user.avatar-root-folder}")
    private String rootFolder;

    @Value("${server.url}")
    private String serverUrl;

    @Value("${server.port}")
    private String serverPort;

    @Resource
    private MiniUserMapper miniUserMapper;

    @Resource
    private MiniUserAttentionMapper miniUserAttentionMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public MiniUser getMiniUserHomePage(Integer miniId) {
        return miniUserMapper.selectMiniUserHomePage(miniId);
    }

    @Override
    public MiniUser getMiniUserHomePage(String openid) {
        return miniUserMapper.selectMiniUserHomePageByOpenid(openid);
    }

    @Override
    public MiniUser getMiniUserByOpenid(String openid) {
        return miniUserMapper.selectOne(new QueryWrapper<MiniUser>().eq("openid", openid));
    }

    @Override
    public MiniUser getMiniUserAttentionList(String openid) {
        return miniUserMapper.selectMiniUserAttentionList(openid);
    }

    @Override
    public String getOpenid(String jsCode) {
        return MiniUserUtils.getOpenid(codeUrl, appid, secret, jsCode);
    }

    @Override
    public MiniUser login(MiniUser miniUser) {
        MiniUser user = miniUserMapper.selectOne(new QueryWrapper<MiniUser>().eq("openid", miniUser.getOpenid()));
        if (user == null) {
            miniUser.setAvatar(tempAvatarUrl);
            miniUser.setUsername(tempUsername);
            miniUser.setBackgroundImage(tempBackgroundImage);
            miniUser.setCreateTime(new Date());
            miniUser.setIsDeleted(0);
            miniUserMapper.insert(miniUser);
            return miniUser;
        } else {
            return user;
        }
    }

    @Override
    public MiniUser updateMiniUserUsername(String openid, String username) {
        int res = miniUserMapper.updateMiniUserUsernameByOpenid(openid, username);
        if (res <= -1) {
            throw new QingYiException(ErrorCode.OPERATION_ERROR);
        } else {
            return miniUserMapper.selectOne(new QueryWrapper<MiniUser>().eq("openid", openid));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MiniUser updateMiniUserUsernameAndAvatar(String openid, String username, MultipartFile avatar) {
        // 先写入头像
        String folderPath = miniUserAvatarPath + "\\";
        if (FileUtils.folderExists(folderPath)) {
            // 如果指定文件夹路径存在或者创建成功后，才执行写入操作
            // 写入图片
            String fileName = openid + ".png";
            String filePath = folderPath + fileName;
            try {
                FileUtils.writePostsImg(filePath, avatar.getInputStream());
                String resUrl = serverUrl + ":" + serverPort + "/" + rootFolder + "/" + fileName;
                MiniUser user = miniUserMapper.selectOne(new QueryWrapper<MiniUser>().eq("openid", openid));
                user.setUsername(username);
                user.setAvatar(resUrl);
                int update = miniUserMapper.update(user, null);
                if (update <= -1) {
                    throw new QingYiException(ErrorCode.OPERATION_ERROR);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return miniUserMapper.selectOne(new QueryWrapper<MiniUser>().eq("openid", openid));
    }

    @Override
    public ResponseResult<?> queryMiniUserAttentionSize(String openid) {
        String key = RedisUtils.USER_ATTENTION_PREFIX + openid;
        return ResultUtils.success(stringRedisTemplate.opsForSet().size(key));
    }

    @Override
    public ResponseResult<?> queryMiniUserAttentionedSize(String openid) {
        long res = 0L;
        Set<String> keys = stringRedisTemplate.keys(RedisUtils.USER_ATTENTION_PREFIX + "*");
        if (keys == null) {
            return ResultUtils.success(0);
        }
        for (String key : keys) {
            if (Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(key, openid))) {
                res++;
            }
        }
        return ResultUtils.success(res);
    }

    @Override
    public ResponseResult<?> queryFriendSize(String openid) {
        String selfKey = RedisUtils.USER_ATTENTION_PREFIX + openid;
        Set<String> selfAttentionSet = stringRedisTemplate.opsForSet().members(selfKey);
        if (selfAttentionSet == null) {
            return ResultUtils.success(0);
        }
        long res = 0;
        for (String member : selfAttentionSet) {
            Set<String> attentionSet = stringRedisTemplate.opsForSet().members(RedisUtils.USER_ATTENTION_PREFIX + member);
            if (attentionSet != null && attentionSet.contains(openid)) {
                res++;
            }
        }
        return ResultUtils.success(res);
    }

    @Override
    public ResponseResult<?> queryAttentionList(String openid, long pageNo, long pageSize) {
        return ResultUtils.success(miniUserMapper.queryMiniUserAttentionList(openid, (pageNo - 1) * pageSize, pageSize));
    }

    @Transactional
    @Override
    public ResponseResult<?> cancelAttention(MiniUserAttention miniUserAttention) {
        if (miniUserAttention == null) {
            throw new QingYiException(ErrorCode.PARAMS_ERROR);
        }
        String attentionOpenid = miniUserAttention.getAttentionOpenid();
        String attenionedOpenid = miniUserAttention.getAttentionedOpenid();
        String key = RedisUtils.USER_ATTENTION_PREFIX + attentionOpenid;
        // 先删除数据库里的数据
        int base_res = miniUserAttentionMapper.deleteAttention(attentionOpenid, attenionedOpenid, new Date());
//        int base_res = miniUserAttentionMapper.delete(new QueryWrapper<MiniUserAttention>().eq("attention_openid", attentionOpenid).eq("attentioned_openid", attenionedOpenid));
        if (base_res >= 1) {
            // 数据库删除成功后才删除缓存
            stringRedisTemplate.opsForSet().remove(key, attenionedOpenid);
            return ResultUtils.success("取消关注成功！");
        } else {
            throw new QingYiException(ErrorCode.OPERATION_ERROR);
        }
    }

    @Override
    public ResponseResult<?> cancelAttention(Integer id) {
        if (id == null) {
            throw new QingYiException(ErrorCode.PARAMS_ERROR);
        }
        MiniUserAttention miniUserAttention = miniUserAttentionMapper.selectById(id);
        if (miniUserAttention == null) {
            throw new QingYiException(ErrorCode.PARAMS_ERROR);
        }
        miniUserAttention.setIsCancelAttention(1);
        miniUserAttention.setCancelAttentionTime(new Date());
        int updateRes = miniUserAttentionMapper.updateById(miniUserAttention);
//        int updateRes = miniUserAttentionMapper.update(miniUserAttention, new QueryWrapper<>());
        if (updateRes >= 1) {
            String attentionOpenid = miniUserAttention.getAttentionOpenid();
            String attenionedOpenid = miniUserAttention.getAttentionedOpenid();
            String key = RedisUtils.USER_ATTENTION_PREFIX + attentionOpenid;
            // 数据库删除成功后才删除缓存
            stringRedisTemplate.opsForSet().remove(key, attenionedOpenid);
            return ResultUtils.success("取消关注成功！");
        } else {
            throw new QingYiException(ErrorCode.OPERATION_ERROR);
        }
    }

    @Override
    public ResponseResult<?> queryMiniUserFansList(String openid, long pageNo, long pageSize) {
        List<MiniUserAttention> miniUserAttentions = miniUserAttentionMapper.queryMiniUserFansList(openid, (pageNo - 1) * pageSize, pageSize);
        String key = RedisUtils.USER_ATTENTION_PREFIX + openid;
        Set<String> members = stringRedisTemplate.opsForSet().members(key);
        if (members != null) {
            for (MiniUserAttention miniUserAttention : miniUserAttentions) {
                String attentionOpenid = miniUserAttention.getAttentionOpenid();
                if (members.contains(attentionOpenid)) {
                    miniUserAttention.setOppositeisAttention(true);
                }
            }
        }
        return ResultUtils.success(miniUserAttentions);
    }

    @Override
    public ResponseResult<?> queryMiniUserFriendList(String openid, long pageNo, long pageSize) {
        String key = RedisUtils.USER_ATTENTION_PREFIX + openid;
        // 获取自己关注的openid集合
        Set<String> myAttentionOpenidSet = stringRedisTemplate.opsForSet().members(key);
        // 如果自己没有关注的人，那么就不可能有好友，直接返回
        if (myAttentionOpenidSet == null) {
            return ResultUtils.success();
        }
        Set<String> attenionOpenidSet = new HashSet<>(myAttentionOpenidSet.size());
        for (String myAttentionOpenid : myAttentionOpenidSet) {
            Set<String> members = stringRedisTemplate.opsForSet().members(RedisUtils.USER_ATTENTION_PREFIX + myAttentionOpenid);
            if (members != null && members.contains(openid)) {
                attenionOpenidSet.add(myAttentionOpenid);
            }
        }
        // 如果没有查询到共同好友，直接返回空
        if (attenionOpenidSet.isEmpty()) {
            return ResultUtils.success();
        }
        return ResultUtils.success(miniUserAttentionMapper.mybatisTest(attenionOpenidSet, openid));
    }

    @Override
    public ResponseResult<?> queryMiniUserAttentionState(String resourceOpenid, String targetOpenid) {
        if (resourceOpenid == null || targetOpenid == null) {
            throw new QingYiException(ErrorCode.PARAMS_ERROR);
        }
        Set<String> resourceSet = stringRedisTemplate.opsForSet().members(RedisUtils.USER_ATTENTION_PREFIX + resourceOpenid);
        Set<String> targetSet = stringRedisTemplate.opsForSet().members(RedisUtils.USER_ATTENTION_PREFIX + targetOpenid);
        // 如果都没有关注人，那么肯定不是共同关注
        if (resourceSet == null || targetSet == null) {
            return ResultUtils.success(false);
        }
        // 判断是否互相关注
        return ResultUtils.success(resourceSet.contains(targetOpenid) && targetSet.contains(resourceOpenid));
    }
}
