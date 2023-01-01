// pages/card-list/card-list.js
const app = getApp();
const {
  baseUrl
} = require("../../utils/request");
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
    posts: {}
  },

  /**
   * 组件的方法列表
   */
  methods: {
    /**
     * 设置关注状态
     * @param {*} isAttentionPostsMiniUser 
     */
    setAttentionState(isAttentionPostsMiniUser) {
      let new_posts = this.properties.post;
      console.log(isAttentionPostsMiniUser)
      new_posts.isAttentionPostsMiniUser = isAttentionPostsMiniUser;
      this.setData({
        post: new_posts
      })
      console.log(this.properties.post)
    },
    /**
     * 取消关注点击事件
     * @param {*} e 
     */
    cancelAttention(e) {
      if (!app.miniUserIsLogin()) {
        wx.showToast({
          title: '请先登录',
          icon: "none"
        })
        return;
      }
      const attentioned_id = e.currentTarget.dataset.id;
      // 请求取消关注接口
      wx.request({
        url: baseUrl + '/miniUser/cancelAttentionMiniUser',
        method: "POST",
        data: {
          id: attentioned_id
        },
        success: res => {
          if (res.data.code == 200) {
            // 修改关注状态
            this.setAttentionState(null);
            wx.showToast({
              title: '取消成功',
              icon: "none"
            })
          }
          console.log(res)
        },
        fail: err => {
          wx.showToast({
            title: '服务器异常',
            icon: "none"
          })
          console.log(err);
        }
      })
      console.log(e);
    },
    /**
     * 关注点击事件
     * @param {*} e 
     */
    attention(e) {
      if (!app.miniUserIsLogin()) {
        wx.showToast({
          title: '请先登录',
          icon: "none"
        })
        return;
      }
      const attentioned_openid = e.currentTarget.dataset.attentioned_openid;
      const openid = wx.getStorageSync('openid');
      // 请求关注接口
      wx.request({
        url: baseUrl + '/miniUser/attentionMiniUser',
        method: "POST",
        data: {
          attentionOpenid: openid,
          attentionedOpenid: attentioned_openid
        },
        success: res => {
          if (res.data.code == 200) {
            // 修改关注状态
            this.setAttentionState(res.data.data);
            wx.showToast({
              title: '关注成功',
              icon: "none"
            })
          }
          console.log(res)
        },
        fail: err => {
          wx.showToast({
            title: '服务器异常',
            icon: "none"
          })
          console.log(err);
        }
      })
      console.log(e)
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
        fail: err => {
          console.log("跳转帖子详情页面失败", err)
        }
      })
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
    /**
     * 点赞方法
     * @param {*} event 
     */
    doLike(event) {
      const pId = event.currentTarget.dataset.pid;
      const openid = app.globalData.openid;
      const that = this;
      wx.request({
        url: baseUrl + '/postsLike/like',
        method: "POST",
        data: {
          p_id: pId,
          openid: openid
        },
        success: res => {
          // 修改点赞状态
          const changPost = this.data.post;
          if (changPost.postsLike == null || changPost.postsLike.status == 0) {
            changPost.postsLike = {
              "status": 1
            }
          } else {
            changPost.postsLike.status = 0;
          }
          // 修改点赞人数
          changPost.currentPostsLikeCount = res.data.data
          console.log(changPost)
          that.setData({
            post: changPost
          })
          console.log(res);
        },
        fail: res => {
          console.log(res);
        }
      })
    }
  }
})