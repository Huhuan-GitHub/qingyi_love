// pages/posts-img/posts-img.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    imgList: {
      type: Array,
      value: [{}]
    }
  },

  /**
   * 组件的初始数据
   */
  data: {
    imgList: [{}]
  },

  /**
   * 组件的方法列表
   */
  methods: {
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
  }
})