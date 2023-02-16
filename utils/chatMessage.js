const {
  baseUrl
} = require("./request");
/**
 * 获取小程序用户聊天列表
 * @param {*} params 
 */
export function getMessageList(params) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + '/message/list',
      method: "GET",
      data: params,
      success: res => {
        console.log(`获取小程序用户聊天列表成功`);
        resolve(res);
      },
      fail: err => {
        console.log(`获取小程序用户聊天列表失败${err}`);
        reject(err);
      }
    })
  })
}

/**
 * 小程序用户查看消息详情接口
 * @param {*} params 
 */
export function viewMessage(params) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + '/message/viewMessage',
      method: "POST",
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