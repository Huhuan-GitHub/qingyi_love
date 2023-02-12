// pages/chat/chat.js
const app = getApp();
let socket = false;
let send_openid = wx.getStorageSync('openid');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    message: "",
    currentOpenid: send_openid,
    messageList: [{
        send_openid: "olG-q5aFDk6wc4tR446WUp3Gct1U",
        receive_openid: "olG-q5Tm54CJoZ8AQPAdGaLSjcwk",
        message: "下午三点开会"
      },
      {
        send_openid: "olG-q5aFDk6wc4tR446WUp3Gct1U",
        receive_openid: "olG-q5Tm54CJoZ8AQPAdGaLSjcwk",
        message: "记得带上电脑"
      }, {
        send_openid: "olG-q5Tm54CJoZ8AQPAdGaLSjcwk",
        receive_openid: "olG-q5aFDk6wc4tR446WUp3Gct1U",
        message: "收到"
      }, {
        send_openid: "olG-q5Tm54CJoZ8AQPAdGaLSjcwk",
        receive_openid: "olG-q5aFDk6wc4tR446WUp3Gct1U",
        message: "需要叫上廖远方吗"
      },
      {
        send_openid: "olG-q5aFDk6wc4tR446WUp3Gct1U",
        receive_openid: "olG-q5Tm54CJoZ8AQPAdGaLSjcwk",
        message: "叫上一起吧"
      },
    ]
  },
  /**
   * 输入框绑定
   * @param {*} e 
   */
  bindMessageInput(e){
    this.setData({
      message: e.detail.value
    })
  },
  sendMessage(e) {
    const params = {
      send_openid: send_openid,
      receive_openid: "olG-q5Tm54CJoZ8AQPAdGaLSjcwk",
      message: this.data.message
    }
    socket.send({
      data: JSON.stringify(params),
      success: res => {
        console.log(res);
        this.setData({
          messageList:[...this.data.messageList,params]
        })
      },
      fail: err => {
        console.error(err);
      }
    });
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {},

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {
    socket = wx.connectSocket({
      url: 'ws://localhost:3033/websocket/' + app.globalData.openid,
      success: res => {
        console.log("websocket连接成功");
      },
      fail: err => {
        console.error("websocket连接失败", err);
      }
    })
    socket.onMessage((res) => {
        const data = JSON.parse(res.data);
        console.log(data)
        this.setData({
          messageList: [...this.data.messageList, data]
        })
      }),
      socket.onClose((res) => {
        console.log("webscoket连接断开")
      })
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
    socket.close()
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