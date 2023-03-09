const bmap = require('../../utils/bmap-wx.min.js');
const wxMarkerData = [];
const util = require("../../utils/util");
const app = getApp();
const {
  getNearbyUsers
} = require("../../utils/location");
const {
  baseLocationWebSocket
} = require("../../utils/request");
const {
  getMiniUserInfoByOpenid
} = require("../../utils/miniUser");
let ws = false;
Page({
  data: {
    markers: [],
    latitude: '',
    longitude: ''
  },
  /**
   * 视野发生变化时触发触发的事件
   * @param {*} e 
   */
  regionchange(e) {},
  /**
   * 点击标记点触发的事件
   * @param {*} e 
   */
  makertap(e) {
    const miniId = e.detail.markerId;
    wx.navigateTo({
      url: '/pages/personal/personal?mini_id=' + miniId,
      success: res => {
        console.log("跳转到个人主页成功")
      },
      fail: err => {
        console.log("跳转到个人主页失败", err);
      }
    })
  },
  /**
   * 请求用户授权地理位置
   */
  async askForLocationPermission() {
    const authResult = await wx.authorize({
      scope: 'scope.userLocation',
    });
  },
  onLoad: function () {
    this.askForLocationPermission();
  },
  onShow() {
    const openid = wx.getStorageSync('openid');
    if (!openid) {
      wx.showToast({
        title: '请先登录！',
        icon: "none"
      })
      return;
    }
    let miniUser;
    getMiniUserInfoByOpenid({
      openid: openid
    }).then(res => {
      miniUser = res.data.data;
      wx.getLocation({
        type: ""
      }).then(res => {
        const thisLongitude = res.longitude;
        const thisLatitude = res.latitude;
        this.setData({
          longitude: thisLongitude,
          latitude: thisLatitude
        })
        const miniUserLocationVoJSON = {
          points: [
            thisLongitude,
            thisLatitude
          ],
          openid: openid,
          miniUser: null,
          distance: 0
        };
        ws = wx.connectSocket({
          url: `${baseLocationWebSocket}/${encodeURIComponent(JSON.stringify(miniUserLocationVoJSON))}`,
          success: res => {
            console.log("websocket连接成功，获取附近的人...");
            getNearbyUsers({
              longitude: thisLongitude,
              latitude: thisLatitude,
              radius: 100
            }).then(result => {
              const data = result.data.data;
              for (let i = 0; i < data.length; i++) {
                this.showUserPointInMap(data[i]);
              }
            })
          },
          fail: err => {
            console.error("websocket连接失败", err);
            throw new Error;
          }
        })
        // 断开WebSocket连接
        ws.onClose((res) => {
          console.log(res);
        })
        ws.onMessage(msg => {
          // 添加mark标记点
          const miniUserLocationVo = JSON.parse(msg.data);
          this.showUserPointInMap(miniUserLocationVo);
        })
        return [thisLongitude, thisLatitude];
      }).catch(err => {
        wx.showToast({
          title: '请授权位置信息',
          icon: "none"
        })
      })
    }).catch(err => {
      console.error(err);
    })

  },
  /**
   * 将用户信息展示在地图上
   */
  showUserPointInMap(miniUserLocationVo) {
    console.log(miniUserLocationVo);
    // 大于等于0，表示上线
    if (miniUserLocationVo.distance >= 0) {
      const isMiniUserExist = this.data.markers.some((item) => item.id === miniUserLocationVo.miniUser.miniId);
      if (!isMiniUserExist) {
        const marker = {
          id: miniUserLocationVo.miniUser.miniId,
          longitude: miniUserLocationVo.points[0],
          latitude: miniUserLocationVo.points[1],
          iconPath: miniUserLocationVo.miniUser.avatar,
          label: {},
          width: 30,
          height: 30
        };
        this.setData({
          markers: [...this.data.markers, marker]
        })
        console.log(this.data.markers);
      }
    } else {
      const markers = this.data.markers;
      // 否则就是下线
      for (let i = 0; i < markers.length; i++) {
        if (markers[i].id === miniUserLocationVo.miniUser.miniId) {
          markers.splice(i, 1);
          // 需要将 i 减 1，否则会跳过下一个元素
          i--;
          break;
        }
      }
      this.setData({markers:markers})
    }
  }
})