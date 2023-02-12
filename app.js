// app.js
App({
  onLaunch() {
    wx.cloud.init({
      env: "dr-love-8gqthpv4aee00926",
      traceUser: true
    })
    // 展示本地存储能力
    const logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)
    // 登录
    wx.login({
      success: res => {
        if (res.code) {
          const APPID = "wx6b0b51f1ee350a3f";
          const SECRET = 'f6b08fd6e91330e47e7f642142eee9b6';
          const JS_CODE = res.code;
          wx.request({
            url: 'https://api.weixin.qq.com/sns/jscode2session',
            method: "GET",
            data: {
              appid: APPID,
              secret: SECRET,
              js_code: JS_CODE,
              grant_type: 'authorization_code'
            },
            success: res => {
              const openid = res.data.openid;
              wx.setStorageSync('openid', openid);
              this.globalData.openid = openid;
            },
            fail: res => {
              console.log("登录失败！");
            }
          })
        } else {
          console.log('登录失败！' + res.errMsg);
        }
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
      }
    })
    
  },
  getOpenid() {
    return new Promise((resolve, reject) => {
      wx.login({
        success: res => {
          if (res.code) {
            const APPID = "wx6b0b51f1ee350a3f";
            const SECRET = 'f6b08fd6e91330e47e7f642142eee9b6';
            const JS_CODE = res.code;
            wx.request({
              url: 'https://api.weixin.qq.com/sns/jscode2session',
              method: "GET",
              data: {
                appid: APPID,
                secret: SECRET,
                js_code: JS_CODE,
                grant_type: 'authorization_code'
              },
              success: res => {
                const openid = res.data.openid;
                this.globalData.openid = openid;
                resolve(openid);
              },
              fail: res => {
                console.log("登录失败！");
                reject("登录失败");
              }
            })
          } else {
            console.log('登录失败！' + res.errMsg);
          }
          // 发送 res.code 到后台换取 openId, sessionKey, unionId
        }
      })
    })
  },
  /**
   * 小程序用户是否登录
   */
  miniUserIsLogin() {
    return wx.getStorageSync('openid');
  },
  globalData: {
    userInfo: null,
    openid: "",
    userLikePidList: [],
    baseUrl: "http://localhost:3033"
  }
})