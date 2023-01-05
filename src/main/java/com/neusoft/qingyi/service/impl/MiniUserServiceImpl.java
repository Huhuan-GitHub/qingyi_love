package com.neusoft.qingyi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.qingyi.pojo.MiniUser;
import com.neusoft.qingyi.service.MiniUserService;
import com.neusoft.qingyi.mapper.MiniUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 29600
 * @description 针对表【t_mini_user】的数据库操作Service实现
 * @createDate 2022-11-12 23:10:04
 */
@Service
public class MiniUserServiceImpl extends ServiceImpl<MiniUserMapper, MiniUser>
        implements MiniUserService {

    @Resource
    private MiniUserMapper miniUserMapper;

    @Override
    public MiniUser getMiniUserHomePage(Integer miniId) {
        return miniUserMapper.selectMiniUserHomePage(miniId);
    }
}




