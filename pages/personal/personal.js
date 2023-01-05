// pages/personal/personal.js
const {
  baseUrl
} = require("../../utils/request")
const {
  getMiniUserHomePageDetails
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
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    const mini_id = options.mini_id;
    // 请求后端获取主页信息
    getMiniUserHomePageDetails({
      miniId: mini_id
    }).then(res => {
      if (res.data.code === 200) {
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