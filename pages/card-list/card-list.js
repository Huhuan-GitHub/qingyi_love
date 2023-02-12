// pages/card-list/card-list.js
const app = getApp();
const {
  baseUrl
} = require("../../utils/request");
const {
  attentionMiniUser,
  cancelAttentionMiniUser
} = require("../../utils/miniUser")
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
      const {
        attentioned_openid
      } = e.currentTarget.dataset;
      const openid = wx.getStorageSync('openid');
      // 请求取消关注接口
      cancelAttentionMiniUser({
        attentionOpenid: openid,
        attentionedOpenid: attentioned_openid
      }).then(res => {
        console.log(res)
        if (res.statusCode === 200) {
          // 修改关注状态
          this.setAttentionState(res.data.data);
          wx.showToast({
            title: '取消成功',
            icon: "none"
          })
        }
      }).catch(err => {
        wx.showToast({
          title: '服务器异常',
          icon: "none"
        })
        console.log(err)
      })
      console.log(e)
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
      const {
        attentioned_openid
      } = e.currentTarget.dataset;
      const openid = wx.getStorageSync('openid');
      // 请求关注接口
      attentionMiniUser({
        attentionOpenid: openid,
        attentionedOpenid: attentioned_openid
      }).then(res => {
        if (res.statusCode === 200) {
          // 修改关注状态
          this.setAttentionState(res.data.data);
          wx.showToast({
            title: '关注成功',
            icon: "none"
          })
        }
      }).catch(err => {
        wx.showToast({
          title: '服务器异常',
          icon: "none"
        })
        console.log(err)
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