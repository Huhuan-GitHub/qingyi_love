const {
  baseUrl
} = require("./request");
const Multipart = require("../utils/Multipart.min.js")
/**
 * 根据openid获取小程序用户
 * @param {*} params 
 */
export function getMiniUserInfoByOpenid(params) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + '/miniUser/getMiniUserInfoByOpenid',
      method: "GET",
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
/**
 * 关注/取消关注小程序用户接口
 * @param {*} params 
 */
export function attentionMiniUser(params) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + '/miniUser/attentionMiniUser',
      method: "POST",
      data: params,
      success: res => {
        resolve(res);
      },
      fail: res => {
        reject(res);
      }
    })
  })
}

// export function attentionMiniUser(params) {
//   return new Promise((resolve, reject) => {
//     wx.request({
//       url: baseUrl + '/miniUser/attentionMiniUser',
//       method: "POST",
//       data: params,
//       success: res => {
//         resolve(res)
//       },
//       fail: err => {
//         reject(err)
//       }
//     })
//   })
// }

// /**
//  * 取消关注小程序用户接口
//  * @param {*} params 
//  */
// export function cancelAttentionMiniUser(params) {
//   return new Promise((resolve, reject) => {
//     wx.request({
//       url: baseUrl + '/miniUser/cancelAttentionMiniUser',
//       method: "POST",
//       data: params,
//       success: res => {
//         resolve(res)
//       },
//       fail: err => {
//         reject(err)
//       }
//     })
//   })
// }
/**
 * 获取小程序用户openid接口
 */
export function getOpenid() {
  return new Promise((resolve, reject) => {
    wx.login({
      success: res => {
        if (res.code) {
          const JS_CODE = res.code;
          wx.request({
            url: baseUrl + '/miniUser/getOpenid',
            method: "GET",
            data: {
              jsCode: JS_CODE
            },
            success: res => {
              const openid = res.data.data;
              resolve(openid);
              // wx.setStorageSync('openid', openid);
            },
            fail: err => {
              console.log(err)
              reject(err);
            }
          })
        }
      }
    })
  })
}
/**
 * 小程序用户匿名登录接口
 * @param {*} params 
 */
export function anonymousLogin(params) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + '/miniUser/login',
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
/**
 * 更新小程序用户信息接口
 * @param {*} avatar 
 * @param {*} params 
 */
export function updateMiniUser(avatar, params) {
  let m = new Multipart({
    files: [],
    fields: []
  })
  // 构造FormData字段参数
  for (let key of Object.keys(params)) {
    m.field({
      name: key,
      value: params[key]
    })
  }
  // 如果修改了头像
  if (avatar) {
    // 构建FormDate文件参数
    m.file({
      name: "avatar",
      filePath: avatar
    })
  }
  return m.submit(baseUrl + "/miniUser/updateMiniUserInfo");
}