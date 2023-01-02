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
    }
  }
})