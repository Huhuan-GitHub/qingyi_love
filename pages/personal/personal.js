// pages/personal/personal.js
const {
  baseUrl
} = require("../../utils/request")
const {
  getMiniUserHomePageDetails,
  whetherMiniUsersEachOtherAttention
} = require("../../utils/miniUser")
Page({

  /**
   * 页面的初始数据
   */
  data: {
    // 帖子图片列表
    imgList: [{
        imgUrl: "https://img1.baidu.com/it/u=2396030750,606213290&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500"
      }, {
        imgUrl: "https://img2.baidu.com/it/u=2592746298,1028958516&fm=253&fmt=auto&app=120&f=JPEG?w=571&h=500"
      },
      {
        imgUrl: "https://img2.baidu.com/it/u=2592746298,1028958516&fm=253&fmt=auto&app=120&f=JPEG?w=571&h=500"
      }
    ],
    // 小程序用户主页数据
    miniHomePageData: {}
  },
  /**
   * 跳转到聊天页面
   * @param {*} e 
   */
  toChat(e) {
    const receiveMiniUser = e.currentTarget.dataset.receiveminiuser;
    const sendMiniUserOpenid = wx.getStorageSync('openid');
    // 1.判断是否互相关注
    const params = {
      resourceOpenid: sendMiniUserOpenid,
      targetOpenid: receiveMiniUser.openid
    }
    // 判断两个小程序用户之间是否互相关注
    whetherMiniUsersEachOtherAttention(params).then(res => {
      // 1.1 如果未互相关注，提示未互相关注，不能进行聊天
      if (!res.data.data) {
        wx.showToast({
          title: '未互相关注不能聊天',
          icon: "none"
        })
      } else {
        // 1.2 已经相互关注，跳转到聊天界面
        wx.navigateTo({
          url: `/pages/chat/chat?sendMiniUser=${encodeURIComponent(JSON.stringify({openid:sendMiniUserOpenid}))}&receiveMiniUser=${encodeURIComponent(JSON.stringify(receiveMiniUser))}`
        })
      }
    }).catch(err => {
      console.error(err);
    })
  },
  /**
   * 跳转到帖子详情页面
   * @param {*} event 
   */
  toPostsDetails(event) {
    const pid = event.currentTarget.dataset.pid;
    wx.navigateTo({
      url: '/pages/postsDetails/postsDetails?pid=' + pid,
      success: res => {
        console.log("跳转帖子详情页面成功")
      },
      fail: err => {
        console.log("跳转帖子详情页面失败", err)
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    const mini_id = options.mini_id;
    // 请求后端获取主页信息
    getMiniUserHomePageDetails({
      miniId: mini_id
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