import type {PostsImg} from "@/models/postsImg";
import type {PostTag} from '@/models/postTag'
import {MiniUser} from "@/models/miniUser";
import {PostsComment} from "@/models/postsComment";

export interface PostsSimpleType {
  pid: number;
  content: string,
  postsImgList: PostsImg[],
  tag: PostTag,
  currentPostsLikeCount: number,
  currentPostsCommentCount: number,
  sendTime: string,
  miniUser: MiniUser,
  postsCommentList: PostsComment[];
}

export default () => {
  const posts = {
    tags: [{
      t_id: 1,
      name: "学习交友"
    }, {
      t_id: 2,
      name: "寻找伴侣"
    }, {
      t_id: 3,
      name: "推荐"
    }],
  };
  return posts;
};
