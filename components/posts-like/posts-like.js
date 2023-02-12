// components/posts-like/posts-like.js
const {
  likePosts
} = require("../../api/posts")
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    post: {
      type: Object,
      value: {}
    }
  },

  /**
   * 组件的初始数据
   */
  data: {
    post: {}
  },

  /**
   * 组件的方法列表
   */
  methods: {
    /**
     * 点赞帖子的方法
     * @param {*} e 
     */
    likePosts(e) {
      const {
        post
      } = e.currentTarget.dataset;
      console.log(post)
      const openid = wx.getStorageSync("openid");
      // 如果没有登录
      if (!openid) {
        wx.showToast({
          title: '请先登录',
          icon: "none"
        })
      }
      wx.showToast({
        title: '拼命开发中',
        icon: "none"
      })
      // 点赞帖子接口
      // likePosts({
      //   p_id: post.pid,
      //   openid: openid,
      //   status: post.status
      // }).then(res => {
      //   console.log(res)
      // }).catch(err => {
      //   console.log(err)
      // })
    }
  }
})