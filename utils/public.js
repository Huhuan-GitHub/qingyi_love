const {
  baseUrl
} = require("./request");
/**
 * 获取所有帖子标签接口
 * @param {*} params 
 */
export function getAllTag(params) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + '/tag/getAllTag',
      method: 'GET',
      data: params,
      success: res => {
        resolve(res);
      },
      fail: err => {
        reject(err);
      }
    })
  })
}
/**
 * 分页获取帖子标签
 * @param {*} params
 */
export function getPostsTagByPage(params) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + '/tag/getPostsTagByPage',
      method: "GET",
      data: params,
      success: res => {
        resolve(res);
      },
      fail: err => {
        reject(err);
      }
    })
  })
}

/**
 * 发布帖子接基本信息接口
 * @param {*} params 
 */
export function publicPosts(params) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseUrl + '/posts/publicPosts',
      method: "POST",
      dataType: "json",
      data: params,
      success: res => {
        resolve(res);
      },
      fail: err => {
        reject(err);
      }
    })
  })
}

/**
 * 上传文件到后台缓存
 * @param {*} fileUrl 文件本地路径 
 * @param {*} params 其他请求参数
 */
export function uploadPostsImg(fileUrl, params) {
  return new Promise((resolve, reject) => {
    wx.uploadFile({
      filePath: fileUrl,
      name: 'img',
      url: baseUrl + '/posts/uploadPostsImg',
      formData: params,
      success: res => {
        resolve(res);
      },
      fail: err => {
        reject(err);
      }
    })
  })
}