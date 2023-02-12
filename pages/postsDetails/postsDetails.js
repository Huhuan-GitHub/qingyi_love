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
   * 点赞帖子事件
   * @param {*} e 
   */
  likePosts(e) {
    wx.showToast({
      title: '头发快掉完了，还没写的',
      icon: "none"
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
    const openid = wx.getStorageSync('openid');
    // 如果小程序用户未登录
    if (!openid) {
      wx.showToast({
        title: '请先登录',
        icon: "none"
      })
      return;
    }
    // 如果输入评论为空
    if (replyContent == '') {
      this.setData({
        showReplyInput: false
      })
      return;
    }
    // 发送评论。。。。
    const params = {
      openid: openid,
      pId: this.data.postsDetails.pid,
      comment: replyContent
    };
    this.sendPostsCommentToServer(params).then(res => {
        return this.updatePostsCommentList(res);
      }).then(updateMsg => {
        console.log(updateMsg);
        // 发送评论成功
        wx.showToast({
          title: '评论成功！',
          icon: "none"
        })
        // 清空输入框内容，隐藏遮罩层
        this.setData({
          showReplyInput: false,
          reply: ''
        })
      })
      .catch(err => {
        console.log(err);
      })
  },
  /**
   * 更新帖子评论列表
   */
  updatePostsCommentList(postsComment) {
    return new Promise((resolve, reject) => {
      // 如果是帖子的根评论，则直接添加到帖子的评论列表头部即可
      console.log(postsComment)
      if (postsComment.cParentId === -1) {
        let newPostsDetails = this.data.postsDetails;
        newPostsDetails.postsCommentList = [postsComment, ...newPostsDetails.postsCommentList];
        console.log(newPostsDetails)
        this.setData({
          postsDetails: newPostsDetails
        })
        resolve("更新评论列表成功");
      } else {
        reject("更新评论列表失败");
      }
    })
  },
  /**
   * 发送评论到服务器
   * @param {*} comment 
   */
  sendPostsCommentToServer(params) {
    return new Promise((resolve, reject) => {
      wx.request({
        url: baseUrl + '/postsComment/replyPostsComment',
        method: "POST",
        data: params,
        success: res => {
          if (res.data.code === 200) {
            resolve(res.data.data);
          }
        },
        fail: err => {
          reject(err);
        }
      })
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