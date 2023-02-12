const {
  baseUrl
} = require("./request");

/**
 * 根据openid获取小程序用户信息
 * @param {*} params 
 */
export function getSelfInfo(params) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + '/miniUser/getMiniUserInfoByOpenid',
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