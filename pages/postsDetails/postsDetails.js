// pages/postsDetails/postsDetails.js
const app = getApp();
const {
  baseUrl
} = require("../../utils/request");
Page({

  /**
   * 页面的初始数据
   */
  data: {
    // 是否显示评论回复框
    showReplyInput: false,
    // 用户输入的评论内容
    reply: "",
    // 帖子图片列表
    imgList: ["https://img.yzcdn.cn/vant/cat.jpeg", "https://img.yzcdn.cn/vant/cat.jpeg", "https://img.yzcdn.cn/vant/cat.jpeg", "https://img.yzcdn.cn/vant/cat.jpeg"],
    postsDetails: {}
  },
  /**
   * 跳转到个人主页的事件
   * @param {*} e 
   */
  toPersonal(e) {
    const mini_id = e.currentTarget.dataset.mini_id;
    wx.navigateTo({
      url: '/pages/personal/personal?mini_id=' + mini_id,
      success: res => {
        console.log("跳转到个人主页成功")
      },
      fail: err => {
        console.log("跳转到个人主页失败", err);
      }
    })
  },
  /**
   * 回复主评论触发的事件
   * @param {*} e 
   */
  commentUser(e) {
    this.setData({
      showReplyInput: true
    })
  },
  /**
   * 关闭回复评论弹出层
   * @param {*} e 
   */
  closeReplyInput(e) {
    this.setData({
      showReplyInput: false
    })
  },
  /**
   * 发送评论触发的事件
   * @param {*} e 
   */
  sumbitReply(e) {
    const replyContent = this.data.reply;
    // 如果输入评论为空
    if (replyContent == '') {
      this.setData({
        showReplyInput: false
      })
      return;
    }
    // 发送评论。。。。
    if (200 == 200) {
      // 发送评论成功
      wx.showToast({
        title: '评论成功！',
        icon: "none"
      })
    }
    // 清空输入框内容，隐藏遮罩层
    this.setData({
      showReplyInput: false,
      reply: ''
    })

  },
  /**
   * 点击回复评论触发的事件
   * @param {*} e 
   */
  replyComment(e) {
    const replyId = e.currentTarget.dataset.replyid;
    // 如果用户未登录，则提示去登录
    if (wx.getStorageSync('openid') == '') {
      wx.showToast({
        title: '请先登录哦',
        icon: "none"
      })
      return;
    }
    // 弹出回复输入框
    this.setData({
      showReplyInput: true
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    const pid = options.pid;
    const openid = wx.getStorageSync('openid');
    this.getPostsDetails(pid, openid);
  },
  /**
   * 根据帖子主键获取帖子详情
   * @param {*} pId 帖子表主键
   * @param {*} openid 小程序用唯一标识符
   */
  getPostsDetails(pId, openid) {
    wx.request({
      url: baseUrl + '/posts/getPostsDetails',
      data: {
        pId: pId,
        openid: openid
      },
      success: res => {
        if (res.data.code === 200) {
          this.setData({
            postsDetails: res.data.data
          })
        }
        console.log(res);
      },
      fail: err => {
        console.log(err);
      }
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