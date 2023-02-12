const {
  baseUrl
} = require("../utils/request");
/**
 * 点赞帖子接口
 * @param {*} params
 */
export function likePosts(params) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + '/postsLike/like',
      method: "POST",
      data: params,
      success: res => {
        resolve(res)
      },
      fail: err => {
        reject(err)
      }
    })
  })
}