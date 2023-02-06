import {MiniUser} from "@/models/miniUser";

export interface PostsComment {
  postsCommentList: PostsComment[];
  replyMiniUser: MiniUser;
  commentMiniUser: MiniUser;
  cId: number;
  openid: string;
  pId: number;
  comment: string;
  commentDate: string;
  cParentId: number;
}
