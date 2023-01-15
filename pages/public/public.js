// pages/public/public.js
import Notify from '@vant/weapp/notify/notify';
const app = getApp();
const {
  getAllTag,
  uploadPostsImg,
  publicPosts
} = require("../../utils/public")
Page({

  /**
   * 页面的初始数据
   */
  data: {
    value: "",
    fileList: [],
    notReveal: false,
    showTagSheet: false,
    currentTag: {
      tid: 0,
      text: '',
      bgColor: ''
    },
    tagList: []
  },
  /**
   * 点击发布按钮触发的事件
   * @param {*} event 
   */
  async submit(event) {
    const that = this;
    // 判断分享内容是否为空
    const content = this.data.value;
    if (content === null || content === '') {
      Notify({
        type: 'warning',
        message: "分享内容不能为空哦"
      });
      return;
    }
    // 标签、是否匿名、文件列表
    const {
      currentTag,
      notReveal,
      fileList
    } = this.data;
    // 附带上传参数
    const paramseFormData = {
      content: content,
      tid: currentTag.tid,
      notReveal: notReveal,
      openid: app.globalData.openid
    }
    const Multipart = require("../../utils/Multipart.min.js")
    let m = new Multipart({
      files: [],
      fields: []
    })
    m.field({
      name: "username",
      value: "张三"
    })
    for(let i =0;i<fileList.length;i++){
      m.file({
        name: "img",
        filePath: fileList[i].url
      })
    }
    m.submit("http://localhost:3033/posts/formDataTest");
  },

  /**
   * 添加地点
   * @param {*} event 
   */
  addAddress(event) {
    wx.showToast({
      title: '程序员拼命开发中...',
      icon: "none"
    })
  },
  /**
   * 选择标签
   * @param {*} event 
   */
  chooseTag(event) {
    const tag = event.currentTarget.dataset.tag;
    this.setData({
      showTagSheet: false,
      currentTag: tag
    })
  },
  /**
   * 关闭选择标签遮罩层
   * @param {*} event 
   */
  closeSheet(event) {
    this.setData({
      showTagSheet: false
    })
  },
  /**
   * 弹出添加标签遮罩层
   * @param {*} event 
   */
  addTag(event) {
    this.setData({
      showTagSheet: true
    })
  },
  onChange({
    detail
  }) {
    // 需要手动对 checked 状态进行更新
    this.setData({
      notReveal: detail
    });
  },
  /**
   * 删除文件
   * @param {*} event 
   */
  deleteFile(event) {
    // 要删除的文件的索引
    const index = event.detail.index;
    // 根据索引删除文件
    let tempFileList = [];
    for (let i in this.data.fileList) {
      if (i != index) {
        tempFileList.push(this.data.fileList[i]);
      }
    }
    this.setData({
      fileList: tempFileList
    })
  },
  /**
   * 文件上传事件
   * @param {*} event 
   */
  afterRead(event) {
    const {
      file
    } = event.detail;
    // 图片预览
    const tempFileList = [];
    for (let i in file) {
      let tempFile = {
        url: file[i].url,
        name: "图片" + i
      }
      tempFileList.push(tempFile);
    }
    this.setData({
      fileList: tempFileList
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    // console.log(app.globalData.openid);
    // 获取标签
    getAllTag({}).then(res => {
      if (res.data.code === 200) {
        this.setData({
          tagList: res.data.data
        })
      }
      console.log(res)
    }).catch(err => {
      console.log(err)
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})