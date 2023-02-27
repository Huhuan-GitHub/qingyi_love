package com.neusoft.qingyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.qingyi.common.ResultUtils;
import com.neusoft.qingyi.pojo.MiniUser;
import com.neusoft.qingyi.pojo.MiniUserAttention;
import com.neusoft.qingyi.service.MiniUserAttentionService;
import com.neusoft.qingyi.mapper.MiniUserAttentionMapper;
import com.neusoft.qingyi.util.RedisUtils;
import com.neusoft.qingyi.util.ResponseResult;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private MiniUserAttentionMapper miniUserAttentionMapper;

    /**
     * 关注小程序用户
     *
     * @param miniUserAttention
     * @return
     */
    @Transactional
    @Override
    public ResponseResult<?> attentionMiniUser(MiniUserAttention miniUserAttention) {
        String attentionOpenid = miniUserAttention.getAttentionOpenid();
        String attentionedOpenid = miniUserAttention.getAttentionedOpenid();
        boolean res = false;
        String key = RedisUtils.USER_ATTENTION_PREFIX + attentionOpenid;
        Boolean isAttentioned = stringRedisTemplate.opsForSet().isMember(key, attentionedOpenid);
        if (isAttentioned.booleanValue()) {
            return ResultUtils.success(2);
        }
        // 查询关注记录
        MiniUserAttention attentionRecord = query().eq("attention_openid", attentionOpenid).eq("attentioned_openid", attentionedOpenid).one();
        // 关注记录写入缓存
        stringRedisTemplate.opsForSet().add(key, attentionedOpenid);
        if (attentionRecord == null) {
            // 没有记录，进行新增操作
            MiniUserAttention newMiniUserAttention = new MiniUserAttention();
            newMiniUserAttention.setAttentionOpenid(attentionOpenid);
            newMiniUserAttention.setAttentionedOpenid(attentionedOpenid);
            newMiniUserAttention.setAttentionTime(new Date());
            newMiniUserAttention.setCancelAttentionTime(null);
            newMiniUserAttention.setIsCancelAttention(0);
            res = save(newMiniUserAttention);
        } else if (attentionRecord.getIsCancelAttention() == 1) {
            // 查询到了关注记录，但是记录是取消关注的，那么就进行更新操作即可
            attentionRecord.setIsCancelAttention(0);
            attentionRecord.setAttentionTime(new Date());
            res = update(attentionRecord, null);
        }
        if (res) {
            return ResultUtils.success(1);
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




