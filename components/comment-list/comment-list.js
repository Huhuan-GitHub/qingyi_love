const {
  baseUrl
} = require("../../utils/request");
const {
  replyPostsComment
} = require("../../utils/postsComment");
// components/comment-list/comment-list.js
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
    // 是否显示评论回复框
    showReplyInput: false,
    // 用户输入的评论内容
    reply: "",
    // 回复帖子评论的评论表主键
    cParentId: -1,
    // 评论的帖子
    pId: -1
  },

  /**
   * 组件的方法列表
   */
  methods: {
    /**
     * 发送评论触发的事件
     * @param {*} e 
     */
    sumbitReply(e) {
      const openid = wx.getStorageSync('openid');
      const {
        cParentId,
        pId,
        reply
      } = this.data;
      // 如果小程序用户未登录
      if (!openid) {
        wx.showToast({
          title: '请先登录',
          icon: "none"
        })
        return;
      }
      // 如果输入评论为空
      if (reply == '') {
        this.setData({
          showReplyInput: false
        })
        return;
      }
      // 发送评论。。。。
      const params = {
        openid: openid,
        cParentId: cParentId,
        comment: reply,
        pId: pId
      };
      replyPostsComment(params).then(res => {
        console.log(res);
        let newPostsCommentList = this.data.postsCommentList;
        this.updateCommentList(res, newPostsCommentList);
        this.setData({
          postsCommentList: this.data.postsCommentList
        })
        // 发送评论成功
        wx.showToast({
          title: '评论成功！',
          icon: "none"
        })
        // 清空输入框内容，隐藏遮罩层
        this.setData({
          showReplyInput: false,
          reply: ''
        })
      }).catch(err => {
        console.log(err);
      })
    },
    /**
     * 更新帖子评论列表
     * @param {*} postsComment 
     */
    updateCommentList(postsComment, oldPostsCommentList) {
      if (!oldPostsCommentList) {
        return;
      }
      const {
        cParentId
      } = postsComment;
      // let oldPostsCommentList = this.properties.postsCommentList;
      for (let i = 0; i < oldPostsCommentList.length; i++) {
        const {
          cId
        } = oldPostsCommentList[i];
        if (cParentId === cId) {
          if (oldPostsCommentList[i].postsCommentList === null) {
            oldPostsCommentList[i].postsCommentList = [postsComment]
          } else {
            oldPostsCommentList[i].postsCommentList = [postsComment, ...oldPostsCommentList[i].postsCommentList]
          }
          return
        } else {
          // 递归查找
          this.updateCommentList(postsComment, oldPostsCommentList[i].postsCommentList)
        }
      }
      // this.setData({
      //   postsCommentList: oldPostsCommentList
      // })
      return oldPostsCommentList
    },
    /**
     * 显示输入评论框
     * @param {*} e 
     */
    showCommentInput(e) {
      console.log(e)
      const cParentId = e.currentTarget.dataset.commentinfo.cId;
      const pId = e.currentTarget.dataset.commentinfo.pId;
      this.setData({
        showReplyInput: true,
        cParentId: cParentId,
        pId: pId
      })
    },
    /**
     * 供子组件调用的显示输入框
     * @param {*} e 
     */
    showCommentInputChild(e) {
      console.log("父组件调用组件的方法")
      console.log(e)
      const {
        cId,
        pId
      } = e.detail;
      this.setData({
        showReplyInput: true,
        cParentId: cId,
        pId: pId
      })
      console.log(this.data)
    },
    /**
     * 关闭回复评论弹出层
     * @param {*} e 
     */
    closeReplyInput(e) {
      this.setData({
        showReplyInput: false,
        cParentId: -1,
        pId: -1
      })
    },
    /**
     * 用户点赞评论方法
     * @param {*} e 
     */
    likeComment(e) {
      wx.showToast({
        title: '别点了,这个功能还没写呢',
        icon: "none"
      })
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