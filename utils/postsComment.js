const {
  baseUrl
} = require("./request");
/**
 * 回复帖子评论接口
 * @param {*} params 
 */
export function replyPostsComment(params) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + '/postsComment/replyPostsComment',
      method: "POST",
      data: params,
      success: res => {
        if (res.data.code === 200) {
          resolve(res.data.data);
        }
      },
      fail: err => {
        console.log(err);
      }
    })
  })
}