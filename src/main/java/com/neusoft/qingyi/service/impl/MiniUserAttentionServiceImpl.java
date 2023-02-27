package com.neusoft.qingyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.qingyi.common.ResultUtils;
import com.neusoft.qingyi.pojo.MiniUser;
import com.neusoft.qingyi.pojo.MiniUserAttention;
import com.neusoft.qingyi.service.MiniUserAttentionService;
import com.neusoft.qingyi.mapper.MiniUserAttentionMapper;
import com.neusoft.qingyi.util.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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
    public ResponseResult<?> attentionMiniUser(MiniUserAttention miniUserAttention) {
        String attentionOpenid = miniUserAttention.getAttentionOpenid();
        String attentionedOpenid = miniUserAttention.getAttentionedOpenid();
        boolean res = false;
        // 查询关注记录
        MiniUserAttention attentionRecord = query().eq("attention_openid", attentionOpenid).eq("attentioned_openid", attentionedOpenid).one();
        // 查询到了关注记录，但是记录是取消关注的，那么就进行更新操作即可
        if (attentionRecord != null && attentionRecord.getIsCancelAttention() == 1) {
            attentionRecord.setIsCancelAttention(0);
            res = update(attentionRecord, null);
        } else {
            // 没有记录，进行新增操作
            MiniUserAttention newMiniUserAttention = new MiniUserAttention();
            newMiniUserAttention.setAttentionOpenid(attentionOpenid);
            newMiniUserAttention.setAttentionedOpenid(attentionedOpenid);
            newMiniUserAttention.setAttentionTime(new Date());
            newMiniUserAttention.setCancelAttentionTime(null);
            newMiniUserAttention.setIsCancelAttention(0);
            res = save(newMiniUserAttention);
        }
        // TODO:删除缓存中的关注数据
        if (res) {
            return ResultUtils.success("关注成功");
        } else {
            return ResultUtils.fail("关注失败");
        }
    }

    @Override
    public MiniUserAttention cancelAttentionMiniUser(MiniUserAttention miniUserAttention) {
        MiniUserAttention attention_record = miniUserAttentionMapper.selectOne(new QueryWrapper<MiniUserAttention>().eq("attentioned_openid", miniUserAttention.getAttentionedOpenid()).eq("attention_openid", miniUserAttention.getAttentionOpenid()));
        // 没有查询到关注记录，说明未关注，直接返回处理成功即可
        if (attention_record == null) {
            return miniUserAttention;
        } else {
            // 查询到了关注记录，将关注信息更新未关注
            attention_record.setIsCancelAttention(1);
            attention_record.setCancelAttentionTime(new Date());
            miniUserAttentionMapper.update(attention_record, null);
        }
        return miniUserAttentionMapper.selectOne(new QueryWrapper<MiniUserAttention>().eq("attentioned_openid", miniUserAttention.getAttentionedOpenid()).eq("attention_openid", miniUserAttention.getAttentionOpenid()));
    }

}




