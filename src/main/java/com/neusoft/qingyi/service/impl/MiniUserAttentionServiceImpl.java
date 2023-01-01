package com.neusoft.qingyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.qingyi.pojo.MiniUserAttention;
import com.neusoft.qingyi.service.MiniUserAttentionService;
import com.neusoft.qingyi.mapper.MiniUserAttentionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 29600
 * @description 针对表【t_mini_user_attention】的数据库操作Service实现
 * @createDate 2022-12-29 20:47:15
 */
@Service
@Transactional
public class MiniUserAttentionServiceImpl extends ServiceImpl<MiniUserAttentionMapper, MiniUserAttention>
        implements MiniUserAttentionService {

    @Resource
    private MiniUserAttentionMapper miniUserAttentionMapper;

    /**
     * 关注小程序用户
     *
     * @param miniUserAttention
     * @return
     */
    @Override
    public MiniUserAttention attentionMiniUser(MiniUserAttention miniUserAttention) {
        MiniUserAttention attention_record = miniUserAttentionMapper.selectOne(new QueryWrapper<MiniUserAttention>().eq("attentioned_openid", miniUserAttention.getAttentionedOpenid()).eq("attention_openid", miniUserAttention.getAttentionOpenid()));
        miniUserAttention.setAttentionTime(new Date());
        miniUserAttention.setIsCancelAttention(0);
        // 没有查询到关注记录，执行插入操作
        if (attention_record == null) {
            miniUserAttentionMapper.insert(miniUserAttention);
        } else {
            miniUserAttentionMapper.update(miniUserAttention, null);
        }
        return miniUserAttentionMapper.selectOne(new QueryWrapper<MiniUserAttention>().eq("attentioned_openid", miniUserAttention.getAttentionedOpenid()).eq("attention_openid", miniUserAttention.getAttentionOpenid()));
    }

    @Override
    public int cancelAttentionMiniUser(MiniUserAttention miniUserAttention) {
        MiniUserAttention attention_record = miniUserAttentionMapper.selectOne(new QueryWrapper<MiniUserAttention>().eq("id", miniUserAttention.getId()));
        // 没有查询到关注记录，说明未关注，直接返回处理成功即可
        if (attention_record == null) {
            return 1;
        } else {
            // 查询到了关注记录，将关注信息更新未关注
            miniUserAttention.setIsCancelAttention(1);
            miniUserAttention.setCancelAttentionTime(new Date());
            return miniUserAttentionMapper.update(miniUserAttention, null);
        }
    }
}




