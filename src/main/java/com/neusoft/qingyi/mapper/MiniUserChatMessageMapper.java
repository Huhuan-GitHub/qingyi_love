package com.neusoft.qingyi.mapper;

import com.neusoft.qingyi.pojo.MiniUserChatMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 29600
 * @description 针对表【t_mini_user_chat_message】的数据库操作Mapper
 * @createDate 2023-02-13 20:18:30
 * @Entity com.neusoft.qingyi.pojo.MiniUserChatMessage
 */
public interface MiniUserChatMessageMapper extends BaseMapper<MiniUserChatMessage> {

    List<MiniUserChatMessage> selectMiniUserChatMessageList(@Param("send_openid") String send_openid);
}




