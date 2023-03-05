// components/personal-post-list/personal-post-list.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    // 帖子数组
    postsList: {
      type: Array,
      value: []
    }
  },

  /**
   * 组件的初始数据
   */
  data: {

  },

  /**
   * 组件的方法列表
   */
  methods: {
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
  }
})