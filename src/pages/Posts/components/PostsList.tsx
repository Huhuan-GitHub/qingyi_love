import React, {useEffect, useState} from 'react'
import ProCard from "@ant-design/pro-card";
import {
  DeleteOutlined,
  EyeInvisibleOutlined,
  LikeOutlined,
  MessageOutlined,
  MoreOutlined,
  WarningOutlined,
  CaretRightOutlined
} from '@ant-design/icons';
import {
  Avatar,
  Button,
  Divider,
  Drawer, Empty,
  Image,
  Input,
  List,
  message, Popconfirm,
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
import {deletePostByPid, getPostsDetails} from "@/services/posts";
import {deletePostsComment, replyPostsComment} from "@/services/postsComment";
import {PostsContext} from "@/pages/Posts";

const UpdateCommentContext = React.createContext({
  update: () => {
  }
});
const {Text} = Typography;
const PostTags = ({tag}: { tag: PostTag }) => {
  return (
    <div>
      {tag === null ? <Tag>推荐</Tag> : <Tag key={tag.tid} color={tag.bgColor}>{tag.text}</Tag>}
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

const Reply: React.FC<{ pid: number, triggerComponentName: string, cParentId?: number }> = (props) => {
  const [replyContent, setReplyContent] = useState<string>('');
  const [openReplyInput, setOpenReplyInput] = useState<boolean>(false);
  const handleOpenChange = (newOpen: boolean) => {
    setOpenReplyInput(newOpen);
  };
  const hideReplyInput = () => {
    setOpenReplyInput(false);
  }
  const sendReply = async () => {
    const params: { pId: number; openid: string; comment: string, cParentId?: number } = {
      pId: props.pid,
      openid: 'olG-q5aFDk6wc4tR446WUp3Gct1U',//TODO:评论人的身份：假设该openid为官方openid
      comment: replyContent,
      cParentId: props.cParentId
    }
    const res = await replyPostsComment(params);
    if (res.data) {
      console.log(res.data.data)
      hideReplyInput();
      message.success("评论成功");
      setReplyContent('')
      return true;
    } else {
      message.error('操作失败，请重试');
      return false;
    }
  }
  return (
    <>
      <UpdateCommentContext.Consumer>
        {context => {
          let actionComponent;
          if (props.triggerComponentName === 'button') {
            actionComponent = <Button type={"primary"} icon={<MessageOutlined/>}>评论</Button>
          } else if (props.triggerComponentName === 'link') {
            actionComponent = <Typography.Link onClick={() => {
            }}>回复</Typography.Link>
          } else if (props.triggerComponentName === 'icon') {
            actionComponent = <MessageOutlined onClick={() => {
            }}/>
          }
          return (
            <Popover open={openReplyInput} onOpenChange={handleOpenChange} placement="bottomLeft" title={
              <Space>
                <Input size={"small"} placeholder={"输入回复"} value={replyContent} onChange={e => {
                  setReplyContent(e.target.value);
                }}/>
                <Button type={"primary"} size={"small"}
                        onClick={() => {
                          sendReply().then(() => {
                            context.update()
                          }).catch((err: any) => {
                            console.log(err)
                          })
                        }}>发送</Button>
              </Space>
            } trigger="click">
              {actionComponent}
            </Popover>
          )
        }}
      </UpdateCommentContext.Consumer>
    </>
  )
}
const PostCommentReply: React.FC<{ reply: PostsComment[] }> = ({reply}) => {
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
                  <CaretRightOutlined style={{color: "gray"}}/>
                </Text>
                <CommentLink text={item.replyMiniUser.username} openid={item.replyMiniUser.openid}/>
              </Space>
              <Typography.Text style={{color: "gray", fontSize: '12px'}}>
                {item.commentDate}
              </Typography.Text>
            </Space>
            <Typography.Text>{item.comment}</Typography.Text>
            <Reply pid={item.pId} cParentId={item.cParentId} triggerComponentName={"link"}/>
          </Space>
          {item.postsCommentList.length === 0 ? <></> : <PostCommentReply reply={item.postsCommentList}/>}
        </div>)}
      </Space>
    </>
  )
}
const PostCommentItem: React.FC<{ comment: PostsComment }> = ({comment}) => {
  const confirmDeleteComment = async () => {
    const res = await deletePostsComment({cId: comment.cId})
    if (res.data) {
      message.success("删除评论成功")
      return true;
    } else {
      message.error("删除评论失败")
      return false;
    }
  }
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
              <Reply
                cParentId={comment.cId}
                pid={comment.pId}
                triggerComponentName={"icon"}/>
              <UpdateCommentContext.Consumer>
                {context => {
                  return (
                    <Popconfirm title={"确认删除评论？"}
                                onConfirm={() => {
                                  confirmDeleteComment()
                                    .then(() => {
                                      context.update()
                                    }).catch(() => {
                                    message.error("删除评论失败！")
                                  })
                                }} placement="left"
                                icon={<WarningOutlined style={{color: "red"}}/>}>
                      <DeleteOutlined style={{cursor: "pointer"}}/>
                    </Popconfirm>
                  )
                }}
              </UpdateCommentContext.Consumer>
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

      {commentList?.length === 0 ? <Empty/> : <Space align={"start"} wrap size={[0, 24]}>
        {commentList?.map((item, index) => <PostCommentItem key={index} comment={item}/>)}
      </Space>}
    </>
  )
}

const PostsList: React.FC<{ postList: any, spin: boolean }> = ({postList, spin}) => {
  const [showCommentDrawer, setShowCommentDrawer] = useState<boolean>(false);
  const [currentPid, setCurrentPid] = useState<number>(0);
  const [refreshComment, setRefreshComment] = useState<boolean>(true);
  const updateComment = React.useCallback(() => {
    setRefreshComment(!refreshComment)
  }, [refreshComment])
  const deletePost = async (pid: number) => {
    const res = await deletePostByPid({pId: pid});
    if (res.data) {
      message.success("帖子删除成功");
      return true;
    } else {
      message.error("帖子删除失败！")
      return false;
    }
  }
  const loadMore = (
    <div
      style={{
        textAlign: 'center',
        marginTop: 12,
        height: 32,
        lineHeight: '32px',
      }}
    >
      <PostsContext.Consumer>
        {context => {
          return (
            <Button type={"primary"} onClick={() => {
              context.getMore()
            }}>加载更多</Button>
          )
        }}
      </PostsContext.Consumer>
    </div>
  )
  return (
    <>
      <ProCard>
        <Spin spinning={spin}>
          <List
            itemLayout="vertical"
            size="default"
            loadMore={loadMore}
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
                        setRefreshComment(!refreshComment)
                      }}>
                        {React.createElement(MessageOutlined)}
                        {item.currentPostsCommentCount.toString()}
                      </Space>
                      <Popover content={
                        <PostsContext.Consumer>
                          {context => {
                            return (
                              <Space>
                                <Popconfirm
                                  title="删除帖子"
                                  description="确认删除该帖子？"
                                  okText="确认"
                                  cancelText="取消"
                                  onConfirm={async () => {
                                    await deletePost(item.pid)
                                    await context.update();
                                  }}
                                  trigger={"hover"}
                                >
                                  <DeleteOutlined onClick={() => {
                                  }} style={{cursor: "pointer"}}/>
                                </Popconfirm>
                                <EyeInvisibleOutlined style={{cursor: "pointer"}}/>
                              </Space>
                            )
                          }}
                        </PostsContext.Consumer>
                      }>
                        <Space onClick={() => {
                        }} style={{cursor: "pointer"}} key={"more"}>
                          {React.createElement(MoreOutlined)}
                        </Space>
                      </Popover>
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
          </List>
          <UpdateCommentContext.Provider value={{update: updateComment}}>
            <Drawer onClose={() => {
              setShowCommentDrawer(false)
            }}
                    open={showCommentDrawer}
                    closable={false}
                    title={"评论列表"}
                    extra={<Reply

                      pid={currentPid} triggerComponentName={"button"}/>}
                    width={520}
            >
              <PostComment refresh={refreshComment} pid={currentPid}/>
            </Drawer>
          </UpdateCommentContext.Provider>
        </Spin>
      </ProCard>
    </>
  )
}

export default PostsList;
