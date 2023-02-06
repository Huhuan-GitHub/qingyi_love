import axios from "axios";
import {PostsComment} from "@/models/postsComment";

/**
 * 评论帖子接口
 * @param params
 * @return 回复的信息
 */
export function replyPostsComment(params: { openid: string; pId: number; comment: string }) {
  return axios
    .post("/server/postsComment/replyPostsComment", params)
    .then((res: any) => {
      console.log(`评论成功！`);
      return res;
    }).catch((err: any) => {
      console.error(`评论失败`, err);
      return {}
    })
}
