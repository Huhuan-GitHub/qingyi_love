package com.neusoft.qingyi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.qingyi.mapper.MiniUserChatMessageMapper;
import com.neusoft.qingyi.pojo.MiniUserChatMessage;
import com.neusoft.qingyi.service.MiniUserChatMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MiniUserChatMessageServiceImpl extends ServiceImpl<MiniUserChatMessageMapper, MiniUserChatMessage>
        implements MiniUserChatMessageService {
    @Resource
    private MiniUserChatMessageMapper miniUserChatMessageMapper;

    @Override
    public List<MiniUserChatMessage> getMiniUserChatMessageList(String openid) {
        return miniUserChatMessageMapper.selectMiniUserChatMessageList(openid);
    }
}
