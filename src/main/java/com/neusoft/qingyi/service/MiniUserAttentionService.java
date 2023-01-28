package com.neusoft.qingyi.service;

import com.neusoft.qingyi.pojo.MiniUserAttention;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 29600
* @description 针对表【t_mini_user_attention】的数据库操作Service
* @createDate 2022-12-29 20:47:15
*/
public interface MiniUserAttentionService extends IService<MiniUserAttention> {
    MiniUserAttention attentionMiniUser(MiniUserAttention miniUserAttention);

    MiniUserAttention cancelAttentionMiniUser(MiniUserAttention miniUserAttention);
}
