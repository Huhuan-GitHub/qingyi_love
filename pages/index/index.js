// index.js
// 获取应用实例
const app = getApp()
const {baseUrl} = require("../../utils/request");
Page({
  data: {
    active: 0,
    // 下拉到底部的次数
    reachBottomCount: 0,
    // 当前页码
    currentPage: 1,
    // 每页显示的条数
    pageSize: 10,
    // 帖子列表
    postsList: []
  },
  /**
   * 回到顶部事件
   * @param {*} e 
   */
  backTop(e) {
    wx.pageScrollTo({
      scrollTop: 0,
      duration: 300,
    })
  },
  /**
   * 分页获取帖子
   * @param {*} page 
   * @param {*} size 
   */
  getPostsByPage(page, size) {
    const that = this;
    app.getOpenid().then((openid) => {
      wx.request({
        url: baseUrl + '/posts/getPostsByPage',
        method: "GET",
        data: {
          currentPage: page,
          pageSize: size,
          openid: openid
        },
        success: res => {
          console.log(res);
          const postList = res.data.data;
          // 如果返回为空，就说明没有数据了，清空方法
          if (postList.length <= 0) {
            this.onReachBottom = () => {};
          }
          // 否则就开始追加数据
          const posts = postList;
          const newPosts = this.data.postsList;
          for (let i = 0; i < posts.length; i++) {
            newPosts.push(posts[i]);
          }
          this.setData({
            postsList: newPosts
          });
          return postList;
        },
        fail: res => {
          console.log(res);
        }
      })
    })
  },
  /**
   * 设置帖子列表
   */
  setPosts(posts) {
    this.setData({
      postsList: posts
    })
  },
  /**
   * 上拉到底部触发的事件
   */
  onReachBottom() {
    // 滑动到底部了，页码+1
    this.setData({
      currentPage: this.data.currentPage + 1
    });
    this.getPostsByPage(this.data.currentPage, this.data.pageSize);
    console.log("上拉到底部了");
  },
  /**
   * 预览图片
   * @param {*} event 
   */
  previewImage(event) {
    const imgUrl = event.currentTarget.dataset.src;
    wx.previewImage({
      urls: [imgUrl],
    })
  },
  onChange(event) {
    console.log(`切换到标签 ${event.detail.name}`)
  },
  onLoad() {
    // 初始化帖子列表
    // this.getPostsByPage(this.data.currentPage, this.data.pageSize);
    // 初始化当前用户点赞列表
  },
  getUserProfile(e) {
    // 推荐使用wx.getUserProfile获取用户信息，开发者每次通过该接口获取用户个人信息均需用户确认，开发者妥善保管用户快速填写的头像昵称，避免重复弹窗
    wx.getUserProfile({
      desc: '展示用户信息', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
      success: (res) => {
        console.log(res)
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
    })
  },
  getUserInfo(e) {
    // 不推荐使用getUserInfo获取用户信息，预计自2021年4月13日起，getUserInfo将不再弹出弹窗，并直接返回匿名的用户个人信息
    console.log(e)
    this.setData({
      userInfo: e.detail.userInfo,
      hasUserInfo: true
    })
  },
  onShow() {
    // 初始化帖子列表
    this.getPostsByPage(this.data.currentPage, this.data.pageSize);
    console.log("onShow")
  },
  /**
   * 页面下拉刷新事件
   */
  onPullDownRefresh() {}
})