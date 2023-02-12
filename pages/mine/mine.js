// pages/mine/mine.js
const {
  getSelfInfo
} = require("../../utils/mine")
const {
  getOpenid,
  anonymousLogin
} = require("../../utils/miniUser")
Page({

  /**
   * 页面的初始数据
   */
  data: {
    miniUser: {},
    // 小程序用户是否已经登录
    isLogin: false
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
  onLoad(options) {},

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
    if (openid) {
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
    } else {
      this.setData({
        isLogin: false
      })
      console.log("未登录")
    }
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