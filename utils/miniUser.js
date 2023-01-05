const {
  baseUrl
} = require("./request");

/**
 * 获取其他用户的主页信息
 * @param {*} params 
 */
export function getMiniUserHomePageDetails(params) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + "/miniUser/getMiniUserHomePageDetails",
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