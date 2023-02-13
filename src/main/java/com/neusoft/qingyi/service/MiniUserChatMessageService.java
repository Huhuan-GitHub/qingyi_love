package com.neusoft.qingyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.qingyi.pojo.MiniUserChatMessage;

import java.util.List;

public interface MiniUserChatMessageService extends IService<MiniUserChatMessage> {
    List<MiniUserChatMessage> getMiniUserChatMessageList(String openid);
}
