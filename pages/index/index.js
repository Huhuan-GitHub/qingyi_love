// index.js
// 获取应用实例
const app = getApp()
const {
  baseUrl
} = require("../../utils/request");
const {
  getPostsPage
} = require("../../utils/index")
Page({
  data: {
    active: 0,
    // 下拉到底部的次数
    reachBottomCount: 0,
    // 当前页码
    currentPage: 1,
    // 每页显示的条数
    pageSize: 5,
    // 帖子列表
    postsList: []
  },
  /**
   * 点击加载更多触发的事件
   * @param {*} e 
   */
  morePosts(e) {
    this.getPostsByPage(this.data.currentPage, this.data.pageSize);
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
    console.log(this.data.currentPage)
    getPostsPage({
      currentPage: page,
      pageSize: size,
      openid: wx.getStorageSync('openid')
    }).then(res => {
      if (res.statusCode === 200) {
        // 因为显示的帖子数量不会很多，最多不可能超过1000条，所以直接使用过滤器算了
        let arr = [...this.data.postsList, ...res.data.data]
        const res_arr = new Map();
        let new_data = arr.filter((item) => !res_arr.has(item["pid"]) && res_arr.set(item["pid"], 1));
        this.setData({
          postsList: new_data
        })
        console.log(res.data.data)
        // 这里判断当前页返回的数据是否为空
        if (res.data.data.length && this.data.postsList.length % this.data.pageSize === 0) {
          // 如果当前页返回的数据不为空，并且页码内的数据显示完了，那么就页码+1
          console.log(123)
          this.setData({
            currentPage: this.data.currentPage + 1
          })
        } else {
          console.log(456)
        }
        // 否则页码就不变
      }
    }).catch(err => {
      wx.showToast({
        title: '系统错误',
        icon: "error"
      })
      console.log(err)
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
    // 这里应该先请求数据，如果返回的数据不为空，那么页码才加一，否则页码不变
    this.getPostsByPage(this.data.currentPage, this.data.pageSize);
    // 滑动到底部了，页码+1
    // this.setData({
    //   currentPage: this.data.currentPage + 1
    // });
    // console.log("上拉到底部了");
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
  },
  /**
   * 页面下拉刷新事件
   */
  onPullDownRefresh() {}
})