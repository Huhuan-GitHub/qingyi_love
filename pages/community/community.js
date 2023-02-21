// pages/community/community.js
const {
  getMessageList
} = require("../../utils/chatMessage")
const {
  dateIsToday,
  isCurrentYear
} = require("../../utils/date");
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    messageList: [],
    currentUserOpenid: wx.getStorageSync('openid')
  },
  /**
   * 跳转到聊天界面
   * @param {*} e 
   */
  toChat(e) {
    const {
      sendminiuser,
      receiveminiuser
    } = e.currentTarget.dataset;
    wx.navigateTo({
      url: `/pages/chat/chat?sendMiniUser=${encodeURIComponent(JSON.stringify(sendminiuser))}&receiveMiniUser=${encodeURIComponent(JSON.stringify(receiveminiuser))}`
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
    app.globalData.socket.onMessage((res) => {
      console.log(JSON.parse(res.data));
      this.updateMessage(res);
    })
  },
  /**
   * 
   */
  updateMessage(res){
    let msgList = this.data.messageList;
    let newMessage = JSON.parse(res.data).messageBody;
    for (let i = 0; i < msgList.length; i++) {
      if ((msgList[i].sendOpenid === newMessage.sendOpenid || msgList[i].sendOpenid === newMessage.receiveOpenid) && (msgList[i].receiveOpenid === newMessage.sendOpenid || msgList[i].receiveOpenid === newMessage.receiveOpenid)) {
        msgList[i] = newMessage;
        let date = new Date(msgList[i].sendTime);
        msgList[i].sendTime = date.getHours() + ":" + (date.getMinutes() <= 9 ? '0' + date.getMinutes() : date.getMinutes())
        break;
      }
    }
    this.setData({
      messageList: msgList
    })
    console.log(this.data.messageList)
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    const openid = wx.getStorageSync('openid');
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
      for (let i = 0; i < result.length; i++) {
        let date = new Date(result[i].sendTime);
        if (dateIsToday(date)) {
          result[i].sendTime = date.getHours() + ":" + (date.getMinutes()<=9?'0'+date.getMinutes():date.getMinutes())
        } else if (isCurrentYear(date)) {
          result[i].sendTime = Number(date.getMonth() + 1) + "月" + date.getDate() + "日"
        } else {
          result[i].sendTime = date.getFullYear() + "年" + Number(date.getMonth() + 1) + "月" + date.getDate() + "日"
        }
      }
      this.setData({
        messageList: result
      })
    }).catch(err => {
      console.error(`获取小程序聊天列表失败${err}`);
    })
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {
    app.globalData.socket.onMessage((res) => {
      this.updateMessage(res);
    })
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {
    console.log("onUpload");
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