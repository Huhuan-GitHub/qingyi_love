// pages/mine/mine.js
const {
  getSelfInfo,
  getAttentionSize,
  getAttentionedSize,
  getMyFriendSize
} = require("../../utils/mine")
const {
  formatNumber
} = require("../../utils/numberFormat")
const {
  getOpenid,
  anonymousLogin,
  getMiniUserHomePageDetailsByOpenid
} = require("../../utils/miniUser");
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    miniUser: {},
    // 小程序用户是否已经登录
    isLogin: false,
    // 我的关注的数量
    myAttentionSize: 0,
    // 关注我的数量
    myAttentionedSize: 0,
    // 用户的好友数量
    friendSize: 0,
    // 小程序用户主页数据
    miniHomePageData: {}
  },
  /**
   * 点击登录按钮触发的事件
   * @param {*} e 
   */
  login(e) {
    getOpenid().then(openid => {
        wx.setStorageSync('openid', openid)
        return anonymousLogin({
          openid: openid
        });
      }).then(res => {
        this.setData({
          miniUser: res.data.data,
          isLogin: true
        })
        console.log(res)
      })
      .catch(err => {
        console.log(err)
      })
  },
  /**
   * 跳转到粉丝列表
   * @param {*} e 
   */
  toFansList(e) {
    wx.navigateTo({
      url: '/pages/fansList/fansList',
    })
  },
  /**
   * 跳转到好友界面
   * @param {*} e 
   */
  toFriendList(e) {
    wx.navigateTo({
      url: '/pages/friendList/friendList',
    })
  },
  /**
   * 跳转到我的关注页面
   * @param {*} e 
   */
  toMyAttentionPage(e) {
    app.isLogin().then(res => {
      wx.navigateTo({
        url: '/pages/myAttention/myAttention'
      })
    }).catch(err => {
      wx.showToast({
        title: '请先登录',
      })
    })
  },

  /**
   * 跳转到修改个人信息页面
   * @param {*} e 
   */
  toEditUserInfo(e) {
    wx.navigateTo({
      url: '/pages/editMiniUserInfo/editMiniUserInfo?openid=' + wx.getStorageSync('openid')
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    // 判断是否登录，如果未登录，跳转到登录页面
    const openid = wx.getStorageSync('openid');
    if (!openid) {
      this.setData({
        isLogin: false
      })
      return;
    }
    // 获取用户信息
    getSelfInfo({
      openid: openid
    }).then(res => {
      if (res.statusCode === 200) {
        this.setData({
          miniUser: res.data.data,
          isLogin: true
        })
      }
      console.log(res);
    }).catch(err => {
      console.log(err);
    })
    // 获取关注数据
    getAttentionSize({
      openid: openid
    }).then(res => {
      this.setData({
        myAttentionSize: formatNumber(res.data.data)
      })
    }).catch(err => {
      console.error(err);
    })
    // 获取被关注的数量
    getAttentionedSize({
      openid: openid
    }).then(res => {
      this.setData({
        myAttentionedSize: formatNumber(res.data.data)
      })
    }).catch(err => {
      console.error(err);
    })
    // 获取好友的数量
    getMyFriendSize({
      openid: openid
    }).then(res => {
      this.setData({
        friendSize: formatNumber(res.data.data)
      })
    }).catch(err => {
      console.error(err);
    })
    // 请求后端获取主页信息
    getMiniUserHomePageDetailsByOpenid({
      openid: wx.getStorageSync('openid')
    }).then(res => {
      if (res.statusCode === 200) {
        this.setData({
          miniHomePageData: res.data.data
        })
        // 动态设置导航栏文字(用户昵称)
        wx.setNavigationBarTitle({
          title: res.data.data.username,
        })
      }
      console.log(res)
    }).catch(err => {
      console.log("请求主页信息失败！", err);
    })
    console.log("onShow")
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})