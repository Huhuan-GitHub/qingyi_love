// pages/community/community.js
const {
  getMessageList
} = require("../../utils/chatMessage")
const {
  dateIsToday,
  isCurrentYear
} = require("../../utils/date")
const openid = wx.getStorageSync('openid');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    messageList: []
  },
  /**
   * 跳转到聊天界面
   * @param {*} e 
   */
  toChat(e) {
    wx.navigateTo({
      url: '/pages/chat/chat'
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    if (!openid) {
      wx.showToast({
        title: '请先登录！',
        icon: "none"
      })
      return;
    }
    getMessageList({
      openid: openid
    }).then(res => {
      const result = res.data.data;
      console.log(result);
      for (let i = 0; i < result.length; i++) {
        let date = new Date(result[i].sendTime);
        console.log(date);
        if (dateIsToday(date)) {
          result[i].sendTime = date.getHours() + ":" + date.getMinutes()
        } else if(isCurrentYear(date)) {
          result[i].sendTime = Number(date.getMonth() + 1) + "月" + date.getDate() + "日"
        }else{
          result[i].sendTime = date.getFullYear() + "年" + Number(date.getMonth() + 1) + "月" + date.getDate() + "日"
        }
      }
      this.setData({
        messageList: result
      })
      console.log(this.data.messageList);
    }).catch(err => {
      console.error(`获取小程序聊天列表失败${err}`);
    })
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