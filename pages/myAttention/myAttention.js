// pages/myAttention/myAttention.js
const {
  getMyAttentionList
} = require("../../utils/mine")
Page({
  /**
   * 页面的初始数据
   */
  data: {
    attentionList: [],
    pageNo: 1,
    pageSize: 10,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    const params = {
      openid: wx.getStorageSync('openid'),
      pageNo: this.data.pageNo,
      pageSize: this.data.pageSize
    };
    getMyAttentionList(params).then(res => {
      if (res.data.data) {
        this.setData({
          attentionList: res.data.data
        })
      }
    }).catch(err => {
      console.log(err);
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