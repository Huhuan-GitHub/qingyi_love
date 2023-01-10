package com.neusoft.qingyi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.qingyi.mapper.MiniUserMapper;
import com.neusoft.qingyi.mapper.PostsCommentCountMapper;
import com.neusoft.qingyi.myenum.ResponseCode;
import com.neusoft.qingyi.pojo.MiniUser;
import com.neusoft.qingyi.pojo.PostsComment;
import com.neusoft.qingyi.qingyiexception.QingYiException;
import com.neusoft.qingyi.service.PostsCommentService;
import com.neusoft.qingyi.mapper.PostsCommentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 29600
 * @description 针对表【t_posts_comment】的数据库操作Service实现
 * @createDate 2023-01-01 21:34:56
 */
@Service
@Slf4j
public class PostsCommentServiceImpl extends ServiceImpl<PostsCommentMapper, PostsComment>
        implements PostsCommentService {

    @Resource
    private PostsCommentMapper postsCommentMapper;

    @Resource
    private PostsCommentCountMapper postsCommentCountMapper;

    @Resource
    private MiniUserMapper miniUserMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    synchronized public PostsComment miniUserCommentPosts(PostsComment postsComment) {
        if (postsComment == null) {
            throw new QingYiException(ResponseCode.PARAMS_ERROR);
        }
        Integer cParentId = postsComment.getCParentId();
        // 如果为空，表示是帖子的根评论，设置为-1
        if (cParentId == null) {
            postsComment.setCParentId(-1);
        } else {
            // 否则就是评论的回复
            postsComment.setCParentId(cParentId);
        }
        postsComment.setCommentDate(new Date());
        int res = postsCommentMapper.insert(postsComment);
        if (res <= 0) {
            throw new QingYiException(ResponseCode.OPERATION_ERROR);
        }
        log.info("评论写入评论表成功");
        // 插入成功后，拿到帖子评论的主键，评论数量自增1
        int pId = postsComment.getPId();
        String openid = postsComment.getOpenid();
        // 暂时先不进行数据库点赞数量的处理
        // TODO:如果数据库中有数据，则执行新增+1即可
//        Integer incrRes = postsCommentCountMapper.commentCountIncrByPid(pId);
        // TODO:数据库中没有评论内容时，执行新增操作
        /*if (incrRes <= 0) {
            throw new QingYiException(ResponseCode.OPERATION_ERROR);
        }*/
        // 设置评论人的信息
        MiniUser commentMiniUser = miniUserMapper.selectMiniUserByOpenid(openid);
        postsComment.setCommentMiniUser(commentMiniUser);
        // 设置这条评论是回复谁的
        MiniUser replyMiniUser = postsCommentMapper.selectCommentReplyParentMiniUser(cParentId);
        postsComment.setReplyMiniUser(replyMiniUser);
        return postsComment;
    }
}




