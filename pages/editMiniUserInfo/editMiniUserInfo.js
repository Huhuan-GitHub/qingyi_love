// pages/editMiniUserInfo/editMiniUserInfo.js
const {
  getMiniUserInfoByOpenid,
  updateMiniUser
} = require("../../utils/miniUser")
Page({

  /**
   * 页面的初始数据
   */
  data: {
    miniUser: {},
    avatarUrl: '',
    nickName: ''
  },
  /**
   * 更新小程序用户信息
   * @param {*} e 
   */
  updateInfo(e) {
    let form_data = {
      openid: wx.getStorageSync('openid'),
      username: this.data.nickName
    };
    if (this.data.nickName === '') {
      form_data['username'] = this.data.miniUser.username;
    }
    updateMiniUser(this.data.avatarUrl, form_data).then(res => {
      if (res.statusCode === 200) {
        this.setData({
          miniUser: res.data.data
        })
        wx.showToast({
          title: '修改成功！',
          icon: "success",
          fail: err => {
            console.log(err)
          },
          complete: res => {
            setTimeout(() => {
              wx.navigateBack({
                delta: 1,
              })
            }, 1000)
          }
        })
      }
    }).catch(err => {
      console.log(err);
    })
  },
  /**
   * 昵称改变后触发的事件
   * @param {*} e 
   */
  nickNameChange(e) {
    const tempUsername = e.detail.value;
    let user = this.data.miniUser;
    user.username = tempUsername;
    this.setData({
      nickName: tempUsername,
      miniUser: user
    })
  },
  /**
   * 选择头像后触发的事件
   * @param {*} e 
   */
  onChooseAvatar(e) {
    const tempAvatar = e.detail.avatarUrl;
    let user = this.data.miniUser;
    user.avatar = tempAvatar;
    this.setData({
      avatarUrl: tempAvatar,
      miniUser: user
    })
    // console.log(avatar)
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    const {
      openid
    } = options;
    getMiniUserInfoByOpenid({
      openid: openid
    }).then(res => {
      if (res.statusCode === 200) {
        this.setData({
          miniUser: res.data.data
        })
      }
    }).catch(err => {
      console.log(err)
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