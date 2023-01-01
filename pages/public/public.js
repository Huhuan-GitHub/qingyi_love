// pages/public/public.js
import Notify from '@vant/weapp/notify/notify';
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    value: "",
    fileList: [],
    notReveal: false,
    showTagSheet: false,
    currentTag: "",
    tagList: [{
        id: "1",
        name: "校园表白"
      },
      {
        id: "2",
        name: "失物招领"
      },
      {
        id: "3",
        name: "卖室友"
      },
      {
        id: "4",
        name: "求助打听"
      },
      {
        id: "5",
        name: "游戏交友"
      },
      {
        id: "6",
        name: "学习交流"
      },
      {
        id: "7",
        name: "随手摄影"
      },
    ]
  },
  /**
   * 点击发布按钮触发的事件
   * @param {*} event 
   */
  submit(event) {
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
    // 标签
    const tag = this.data.currentTag;
    // 是否匿名
    const notReveal = this.data.notReveal;
    // 文件列表
    const fileList = this.data.fileList;
    // 附带上传参数
    const paramseFormData = {
      content: content,
      tag: tag,
      notReveal: notReveal,
      openid: app.globalData.openid
    }
    wx.request({
      url: 'http://localhost:3033/posts/addPosts',
      method: "POST",
      data: {
        content: content,
        openid: app.globalData.openid,
        notReveal: notReveal,
        tag: {
          text: tag
        }
      },
      success: res => {
        if (fileList.length <= 0) {
          // 没有选择图片
          this.setData({
            value: "",
            fileList: [],
            notReveal: false,
            showTagSheet: false,
            currentTag: "",
            tagList: [{
                id: "1",
                name: "校园表白"
              },
              {
                id: "2",
                name: "失物招领"
              },
              {
                id: "3",
                name: "卖室友"
              },
              {
                id: "4",
                name: "求助打听"
              },
              {
                id: "5",
                name: "游戏交友"
              },
              {
                id: "6",
                name: "学习交流"
              },
              {
                id: "7",
                name: "随手摄影"
              },
            ]
          });
          Notify({
            type: 'success',
            message: "发布成功！",
            onOpened: () => {
              wx.switchTab({
                url: '/pages/index/index',
                success: res => {
                  const page = getCurrentPages().pop();
                  if (page == undefined || page == null) return;  
                  page.onLoad();  
                }
              })
            }
          });
          return;
        }
        // 添加帖子成功后再上传图片
        if (res.data.data > 0) {
          for (let i in fileList) {
            wx.uploadFile({
              filePath: fileList[i].url,
              name: 'file',
              url: 'http://localhost:3033/postsImg/addPostsImg',
              formData: {
                pId: res.data.data,
                openid: app.globalData.openid
              },
              success: res => {
                // 上传成功后，清除数据
                this.setData({
                  value: "",
                  fileList: [],
                  notReveal: false,
                  showTagSheet: false,
                  currentTag: "",
                  tagList: [{
                      id: "1",
                      name: "校园表白"
                    },
                    {
                      id: "2",
                      name: "失物招领"
                    },
                    {
                      id: "3",
                      name: "卖室友"
                    },
                    {
                      id: "4",
                      name: "求助打听"
                    },
                    {
                      id: "5",
                      name: "游戏交友"
                    },
                    {
                      id: "6",
                      name: "学习交流"
                    },
                    {
                      id: "7",
                      name: "随手摄影"
                    },
                  ]
                });
                Notify({
                  type: 'success',
                  message: "发布成功！",
                  onOpened: () => {
                    // 本来想通过跳转tabbar来实现数据刷新，后面发现不可行，故暂时弃用
                    // wx.switchTab({
                    //   url: '/pages/index/index',
                    //   success: res => {
                    //     const page = getCurrentPages().pop();
                    //   }
                    // })
                  }
                });
                console.log("上传成功", res);
              },
              fail: res => {
                console.log("上传失败", res);
              }
            })
          }
        }
      },
      fail: res => {
        console.log(res);
      }
    })
    // 遍历文件上传
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
    const tagName = event.currentTarget.dataset.name;
    this.setData({
      showTagSheet: false,
      currentTag: tagName
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