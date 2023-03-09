const {
  baseUrl
} = require("./request");

/**
 * 获取附近的人
 * @param {*} params 
 */
export async function getNearbyUsers(params) {
  return new Promise((resolve,reject)=>{
    wx.request({
      url: baseUrl + '/location/getNearbyUsers',
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