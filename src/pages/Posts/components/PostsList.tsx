import React, {useEffect, useState} from 'react'
import ProCard from "@ant-design/pro-card";
import {LikeOutlined, MessageOutlined} from '@ant-design/icons';
import {
  Avatar,
  Button,
  Divider,
  Drawer,
  Image,
  Input,
  List,
  message,
  Popover,
  Space,
  Spin,
  Tag,
  Typography
} from 'antd';
import {PostsSimpleType} from "@/models/posts";
import {PostsImg} from "@/models/postsImg";
import {PostTag} from "@/models/postTag";
import {Link} from 'umi';
import {PostsComment} from "@/models/postsComment";
import {getPostsDetails} from "@/services/posts";
import minimatch from "minimatch";
import {replyPostsComment} from "@/services/postsComment";

const {Text} = Typography;
const PostTags = ({tag}: { tag: PostTag }) => {
  return (
    <div>
      <Tag key={tag.tid} color={tag.bgColor}>{tag.text}</Tag>
    </div>
  )
}
const RightImg = ({imgUrl}: { imgUrl: string }) => {
  return (
    <Image width={200} src={imgUrl} alt="帖子图片"/>
  )
}
const CommentLink: React.FC<{ text: string, size?: number, openid: string }> = ({text, size = 12, openid}) => {
  return (
    <Link to={`/miniUser/center/${openid}`} style={{fontSize: size}}>
      {text}
    </Link>
  )
}
const PostCommentReply: React.FC<{ reply: PostsComment[] }> = ({reply}) => {
  const [replyContent, setReplyContent] = useState<string>('');
  const [openReplyInput, setOpenReplyInput] = useState<boolean>(false);
  const handleOpenChange = (newOpen: boolean) => {
    setOpenReplyInput(newOpen);
  };
  const hideReplyInput = () => {
    setOpenReplyInput(false);
  }
  const sendReply = async () => {
    const params: { pId: number; openid: string; cParentId: number; comment: string } = {
      pId: reply[0].pId,
      openid: 'olG-q5aFDk6wc4tR446WUp3Gct1U',//TODO:评论人的身份：假设该openid为官方openid
      cParentId: reply[0].cParentId,
      comment: replyContent,
    }
    const res = await replyPostsComment(params);
    if (res.data) {
      console.log(res.data.data)
      setTimeout(() => {
        hideReplyInput();
        message.success("评论成功")
      }, 500)
    } else {
      message.error('操作失败，请重试');
    }
    console.log(reply);
  }
  return (
    <>
      <Space align={"start"} direction={"vertical"}>
        {reply.map((item) => <div key={item.cId}>
          <Space align={"start"} direction={"vertical"}>
            <Space align={"center"} size={48}>
              <Space size={4} align={"center"}>
                <Avatar key={item.cId} size={"small"} src={<Image
                  src={item.replyMiniUser.avatar}/>}/>
                <CommentLink text={item.commentMiniUser.username} openid={item.commentMiniUser.openid}/>
                <Text>
                  {'>'}
                </Text>
                <CommentLink text={item.replyMiniUser.username} openid={item.replyMiniUser.openid}/>
              </Space>
              <Typography.Text style={{color: "gray", fontSize: '12px'}}>
                {item.commentDate}
              </Typography.Text>
            </Space>
            <Typography.Text>{item.comment}</Typography.Text>
            <Popover open={openReplyInput} onOpenChange={handleOpenChange} placement="bottomLeft" title={
              <Space>
                <Input size={"small"} placeholder={"输入回复"} value={replyContent} onChange={e => {
                  setReplyContent(e.target.value);
                }}/>
                <Button type={"primary"} size={"small"} onClick={() => sendReply()}>发送</Button>
              </Space>
            } trigger="click">
              <Typography.Link>
                回复
              </Typography.Link>
            </Popover>
          </Space>
          {item.postsCommentList.length === 0 ? <></> : <PostCommentReply reply={item.postsCommentList}/>}
        </div>)}
      </Space>
    </>
  )
}
let refreshComment = false;
const Reply: React.FC<{ pid: number }> = (props) => {
  const [replyContent, setReplyContent] = useState<string>('');
  const [openReplyInput, setOpenReplyInput] = useState<boolean>(false);
  const handleOpenChange = (newOpen: boolean) => {
    setOpenReplyInput(newOpen);
  };
  const hideReplyInput = () => {
    setOpenReplyInput(false);
  }
  const sendReply = async () => {
    const params: { pId: number; openid: string; comment: string } = {
      pId: props.pid,
      openid: 'olG-q5aFDk6wc4tR446WUp3Gct1U',//TODO:评论人的身份：假设该openid为官方openid
      comment: replyContent,
    }
    const res = await replyPostsComment(params);
    if (res.data) {
      console.log(res.data.data)
      hideReplyInput();
      message.success("评论成功");
      refreshComment = !refreshComment;
    } else {
      message.error('操作失败，请重试');
    }
  }
  return (
    <Popover open={openReplyInput} onOpenChange={handleOpenChange} placement="bottomLeft" title={
      <Space>
        <Input size={"small"} placeholder={"输入回复"} value={replyContent} onChange={e => {
          setReplyContent(e.target.value);
        }}/>
        <Button type={"primary"} size={"small"} onClick={() => sendReply()}>发送</Button>
      </Space>
    } trigger="click">
      <Button type={"primary"} icon={<MessageOutlined/>}>评论</Button>
    </Popover>
  )
}
const PostCommentItem: React.FC<{ comment: PostsComment }> = ({comment}) => {
  return (
    <>
      <Space align={"start"}>
        <Avatar
          src={<Image src={comment.commentMiniUser.avatar}/>}/>
        <Space align={"start"} direction={"vertical"}>
          <CommentLink text={comment.commentMiniUser.username} size={14} openid={comment.commentMiniUser.openid}/>
          <Typography.Text>{comment.comment}</Typography.Text>
          <PostCommentReply reply={comment.postsCommentList}/>
          <Space align={"center"} size={260}>
            <div>
              <Typography.Text>{comment.commentDate}</Typography.Text>
            </div>
            <Space>
              <MessageOutlined style={{cursor: 'pointer'}}/>
            </Space>
          </Space>
        </Space>
      </Space>
    </>
  )
}
const PostComment: React.FC<{ refresh: boolean, pid: number }> = (props) => {
  const [loading, setLoading] = useState<boolean>(true);
  const [commentList, setCommentList] = useState<PostsComment[]>();
  const loadCommentList = async () => {
    const res = await getPostsDetails(props.pid);
    if (res.data) {
      setCommentList(res.data.data.postsCommentList);
      console.log(commentList);
    } else {
      message.error("加载失败，请刷新重试");
    }
    setLoading(false);
  }
  useEffect(() => {
    loadCommentList();
  }, [loading, props.refresh])
  return (
    <>
      {/*帖子评论抽屉*/}
      <Space align={"start"} wrap size={[0, 24]}>
        {commentList?.map((item, index) => <PostCommentItem key={index} comment={item}/>)}
      </Space>
    </>
  )
}
const PostsList: React.FC<{ postList: any, spin: boolean }> = ({postList, spin}) => {
  const [showCommentDrawer, setShowCommentDrawer] = useState<boolean>(false);
  const [currentPid, setCurrentPid] = useState<number>(0);
  return (
    <ProCard>
      <Spin spinning={spin}>
        <List
          itemLayout="vertical"
          size="default"
          pagination={{
            onChange: (page) => {
              console.log(page);
            },
            pageSize: 10,
          }}
          dataSource={postList}
          renderItem={(item: PostsSimpleType) => (
            <List.Item
              key={item.pid}
              actions={[
                <>
                  <Space split={<Divider type="vertical"/>}>
                    <Space style={{cursor: "pointer"}} key={"like"}>
                      {React.createElement(LikeOutlined)}
                      {item.currentPostsLikeCount.toString()}
                    </Space>
                    <Space style={{cursor: "pointer"}} key={"comment"} onClick={() => {
                      setCurrentPid(item.pid)
                      setShowCommentDrawer(true)
                    }}>
                      {React.createElement(MessageOutlined)}
                      {item.currentPostsCommentCount.toString()}
                    </Space>
                  </Space>
                </>
              ]}
            >
              <Space direction="vertical" size={[10, 12]}>
                <List.Item.Meta
                  title={
                    <Typography.Link ellipsis={true}>
                      {item.content}
                    </Typography.Link>
                  }
                  description={<PostTags tag={item.tag}/>}
                />
                {item.content}
                {/*用户信息*/}
                <div>
                  <Space>
                    <Avatar
                      size="small"
                      src={<Image src={item.miniUser.avatar} alt={"用户头像"}/>}
                    />
                    <Link to={`/miniUser/center/${item.miniUser.openid}`}>{item.miniUser.username}</Link>
                    <Text style={{fontSize: '14px'}} strong={true}>发布于</Text>
                    <Text style={{fontSize: '12px'}} type={"secondary"}>{item.sendTime}</Text>
                  </Space>
                </div>
                {/*图片列表组件*/}
                <div>
                  <Space size={[12, 6]} wrap>
                    {item.postsImgList.map((img: PostsImg, index) => <RightImg key={index} imgUrl={img.imgUrl}/>)}
                  </Space>
                </div>
              </Space>
            </List.Item>
          )}
        >
          <Drawer onClose={() => {
            setShowCommentDrawer(false)
          }}
                  open={showCommentDrawer}
                  closable={false}
                  title={"评论列表"}
                  extra={<Reply pid={currentPid}/>}
                  width={520}
          >
            <PostComment refresh={showCommentDrawer} pid={currentPid}/>
          </Drawer>
        </List>
      </Spin>
    </ProCard>
  )
}

export default PostsList;
