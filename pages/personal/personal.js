// pages/personal/personal.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    // 帖子图片列表
    imgList: ["https://img.yzcdn.cn/vant/cat.jpeg", "https://img.yzcdn.cn/vant/cat.jpeg", "https://img.yzcdn.cn/vant/cat.jpeg", "https://img.yzcdn.cn/vant/cat.jpeg"]
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
      fail: res => {
        console.log("跳转帖子详情页面失败")
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    const mini_id = options.mini_id;
    // 动态设置导航栏文字(用户昵称)
    wx.setNavigationBarTitle({
      title: '个人主页',
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