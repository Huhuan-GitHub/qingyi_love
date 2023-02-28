// components/simple-attention-button/simple-attenion-button.js
const {
  cancelAttenion
} = require("../../utils/mine");
const {
  attentionMiniUser
} = require("../../utils/miniUser")
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    // 关注状态 0：未关注 1：已关注
    state: {
      type: Number,
      value: 0
    },
    // 当前按钮绑定的小程序用户的openid
    attentionOpenid: {
      type: String,
      value: ""
    }
  },

  /**
   * 组件的初始数据
   */
  data: {
    // state: 0
  },

  /**
   * 组件的方法列表
   */
  methods: {
    /**
     * 切换关注状态
     */
    changeAttentionState() {
      this.setData({
        state: this.data.state === 0 ? 1 : 0
      })
    },
    /**
     * 取消关注接口
     * @param {*} e 
     */
    cancelAttention(e) {
      wx.showModal({
        title: '是否取消关注',
        content: '',
        complete: (res) => {
          if (res.cancel) {
            // 点击取消
          }
          if (res.confirm) {
            // 点击确认，发送取消关注请求
            const params = {
              attentionOpenid: wx.getStorageSync('openid'),
              attentionedOpenid: e.currentTarget.dataset.attenionopenid
            };
            cancelAttenion(params).then(res => {
              this.changeAttentionState();
              wx.showToast({
                title: '取消关注成功！',
                icon: "success"
              })
            }).catch(err => {
              wx.showToast({
                title: '服务器错误，请重试',
                icon: "success"
              })
              console.error("取消失败");
            })
            console.log("点击确认");
          }
        }
      })
    },
    /**
     * 关注小程序用户接口
     * @param {*} e 
     */
    attention(e) {
      // 点击确认，发送取消关注请求
      const params = {
        attentionOpenid: wx.getStorageSync('openid'),
        attentionedOpenid: e.currentTarget.dataset.attenionopenid
      };
      attentionMiniUser(params).then(res => {
        this.changeAttentionState();
        wx.showToast({
          title: '关注成功！',
          icon: "success"
        })
      }).catch(err => {
        wx.showToast({
          title: '服务器错误，请重试',
          icon: "success"
        })
        console.error(err);
      })
    }
  }
})