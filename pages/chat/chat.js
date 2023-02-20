// pages/chat/chat.js
const app = getApp();
let send_openid = wx.getStorageSync('openid');
const {
  viewMessage
} = require("../../utils/chatMessage")
const {
  messageDateFormat
} = require("../../utils/date")
Page({

  /**
   * 页面的初始数据
   */
  data: {
    message: "",
    currentOpenid: send_openid,
    messageList: [],
    sendMiniUser: {},
    receiveMiniUser: {}
  },
  /**
   * 输入框绑定
   * @param {*} e 
   */
  bindMessageInput(e) {
    this.setData({
      message: e.detail.value
    })
  },
  sendMessage(e) {
    const params = {
      sendMiniUser: JSON.stringify(this.data.sendMiniUser),
      receiveMiniUser: JSON.stringify(this.data.receiveMiniUser),
      messageContent: this.data.message
    }
    app.globalData.socket.send({
      data: JSON.stringify(params),
      success: res => {
        console.log(res);
        this.scrollBottom();
      },
      fail: err => {
        console.error(err);
      }
    });
    wx.pageScrollTo({
      scrollTop: 999999
    })
  },
  /**
   * 滚动到底部
   */
  scrollBottom() {
    wx.getSystemInfo().then(res => {
      wx.pageScrollTo({
        scrollTop: res.screenHeight
      })
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    const {
      sendMiniUser,
      receiveMiniUser
    } = options;
    this.setData({
      sendMiniUser: JSON.parse(decodeURIComponent(sendMiniUser)),
      receiveMiniUser: JSON.parse(decodeURIComponent(receiveMiniUser))
    })
    // 该页面一显示，就代表消息已读，所以将缓存中的数据写入数据库
    viewMessage({
      sendOpenid: this.data.currentOpenid === this.data.sendMiniUser.openid ? this.data.receiveMiniUser.openid : this.data.sendMiniUser.openid,
      receiveOpenid: this.data.currentOpenid
    }).then(res => {
      let data = res.data.data;
      for (let i = 0; i < data.length; i++) {
        data[i].sendTime = messageDateFormat(new Date(data[i].sendTime));
      }
      console.log(data);
      this.setData({
        messageList: data
      })
      console.log(res);
    }).catch(err => {
      console.error(err);
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {
    wx.pageScrollTo({
      scrollTop: 999999
    })
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    wx.pageScrollTo({
      scrollTop: 999999
    })
    app.globalData.socket.onMessage((res) => {
      let body = JSON.parse(res.data).messageBody;
      let date = new Date(body.sendTime);
      body.sendTime = messageDateFormat(date);
      this.setData({
        messageList: [...this.data.messageList, body]
      })
      console.log(JSON.parse(res.data).messageBody);
    })
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
    // 该页面一显示，就代表消息已读，所以将缓存中的数据写入数据库
    viewMessage({
      sendOpenid: this.data.currentOpenid === this.data.sendMiniUser.openid ? this.data.receiveMiniUser.openid : this.data.sendMiniUser.openid,
      receiveOpenid: this.data.currentOpenid
    }).then(res => {
      let data = res.data.data;
      for (let i = 0; i < data.length; i++) {
        data[i].sendTime = messageDateFormat(new Date(data[i].sendTime));
      }
      console.log(data);
      this.setData({
        messageList: data
      })
      console.log(res);
    }).catch(err => {
      console.error(err);
    })
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