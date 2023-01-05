package com.neusoft.qingyi.service;

import com.neusoft.qingyi.pojo.MiniUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author 29600
 * @description 针对表【t_mini_user】的数据库操作Service
 * @createDate 2022-11-12 23:10:04
 */
public interface MiniUserService extends IService<MiniUser> {
    MiniUser getMiniUserHomePage(Integer miniId);
}
