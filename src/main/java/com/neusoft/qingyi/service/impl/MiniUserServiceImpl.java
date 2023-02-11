package com.neusoft.qingyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.qingyi.common.ErrorCode;
import com.neusoft.qingyi.pojo.MiniUser;
import com.neusoft.qingyi.qingyiexception.QingYiException;
import com.neusoft.qingyi.service.MiniUserService;
import com.neusoft.qingyi.mapper.MiniUserMapper;
import com.neusoft.qingyi.util.FileUtils;
import com.neusoft.qingyi.util.MiniUserUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public MiniUser getMiniUserHomePage(Integer miniId) {
        return miniUserMapper.selectMiniUserHomePage(miniId);
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
}




