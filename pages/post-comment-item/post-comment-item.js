// pages/post-comment-item/post-comment-item.js
Component({
  options: {
    addGlobalClass: true
  },
  /**
   * 组件的属性列表
   */
  properties: {
    // 帖子评论列表
    postsCommentList: {
      type: Array,
      value: []
    }
  },

  /**
   * 组件的初始数据
   */
  data: {
    postsCommentList: [],
  },

  /**
   * 组件的方法列表
   */
  methods: {
    /**
     * 显示评论回复输入框
     * @param {*} e 
     */
    showReplyInput(e) {
      const {
        commentinfo
      } = e.currentTarget.dataset;
      this.triggerEvent('showCommentInputChild', commentinfo, {
        bubbles: false,
        composed: false
      });
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
  }
})