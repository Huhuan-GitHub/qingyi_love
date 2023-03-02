// components/miniuser-avatar/miniuser-avatar.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    // 小程序用户
    miniUser: {
      type: Object,
      value: {}
    },
    // 头像尺寸
    size: {
      type: String,
      value: ""
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
     * 跳转到个人详情页面
     * @param {*} e 
     */
    toPersonal(e) {
      const miniId = e.currentTarget.dataset.miniid;
      wx.navigateTo({
        url: '/pages/personal/personal?mini_id=' + miniId,
      })
    }
  }
})