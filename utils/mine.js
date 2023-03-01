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

/**
 * 获取小程序用户的关注数量
 * @param {*} params 
 */
export function getAttentionSize(params) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + '/miniUser/getAttentionSize',
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
 * 获取小程序用户被关注的数量
 * @param {*} params 
 */
export function getAttentionedSize(params) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + '/miniUser/getAttentionedSize',
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
 * 获取我的好友的数量
 * @param {*} params 
 */
export function getMyFriendSize(params) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + '/miniUser/friendIntersectionSize',
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
 * 分页获取小程序用户关注列表
 * @param {*} params 
 */
export function getMyAttentionList(params) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + '/miniUser/getMyAttentionList',
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
 * 获取小程序用户粉丝列表
 * @param {*} params 
 */
export function getMiniUserFansList(params) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + '/miniUser/getMiniUserFansList',
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
 * 小程序用户取消关注接口
 * @param {*} params 
 */
export function cancelAttenion(params) {
  const {
    id
  } = params;
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + '/miniUser/cancelAttention/' + id,
      method: "POST",
      success: res => {
        resolve(res);
      },
      fail: err => {
        reject(err);
      }
    })
  })
}