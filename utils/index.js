const {
  baseUrl
} = require("./request");

/**
 * 分页获取帖子数据
 * @param {*} params 
 */
export function getPostsPage(params) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + '/posts/getPostsPage',
      method: "GET",
      data: params,
      success: res => {
        resolve(res);
      },
      fail: err => {
        reject(err);
      }
    })
  })
}

/**
 * 滚动分页获取帖子
 * @param {*} params 
 */
export function postsList(params) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + '/posts',
      method: "GET",
      data: params,
      success: res => {
        resolve(res);
      },
      fail: err => {
        reject(err);
      }
    })
  })
}