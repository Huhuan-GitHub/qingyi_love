import type {PageSearchParams} from '@/services/common';
import axios from "axios";

/**
 * 获取帖子
 */
export function getPostsPage(params: PageSearchParams) {
  const {pageSize = 10, pageNo = 1} = params;
  return axios.get('/server/posts/getPostsPage', {
    params: {
      pageSize: pageSize, currentPage: pageNo
    }
  }).then((res: any) => {
    console.log(`分页获取帖子成功！`);
    return res;
  }).catch((err: any) => {
    console.error(`分页获取帖子失败！`, err);
    return {};
  });
}

/**
 * 获取帖子详情接口
 * @param pid 帖子主键
 * @param openid 微信小程序用户唯一标识符
 */
export function getPostsDetails(pid: number, openid?: string) {
  return axios.get('/server/posts/getPostsDetails', {
    params: {
      pId: pid,
      openid: openid
    }
  }).then((res: any) => {
    console.log(`获取帖子详情成功！`);
    return res;
  }).catch((err: any) => {
    console.log(`获取帖子详情失败！`, err);
    return {};
  })
}

/**
 * 删除帖子接口
 * @param params
 */
export function deletePostByPid(params: { pId: number }) {
  return axios.post("/server/posts/deletePost", {pId: params.pId})
    .then((res: any) => {
      console.log(`删除帖子成功！`)
      return res;
    }).catch((err: any) => {
      console.error(`删除帖子失败！`, err)
      return {};
    })
}

/**
 * 发布帖子接口
 * @param params
 */
export function publicPosts(params: FormData) {
  return axios.post("/server/posts/publicPosts", params, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }).then((res: any) => {
    console.log(`帖子发布成功！`)
    return res;
  }).catch((err: any) => {
    console.error(`帖子发布失败！`, err)
    return {};
  })
}
