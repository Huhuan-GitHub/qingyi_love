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
  regionchange(e) {
    console.log("视野变化");
  },
  /**
   * 点击标记点触发的事件
   * @param {*} e 
   */
  makertap(e) {
    const {
      markerId
    } = e.detail;
    console.log(markerId);
  },
  onLoad: function () {

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
          distance: -1
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
              console.log(result);
              const data = result.data.data;
              for (let i = 0; i < data.length; i++) {
                this.showUserPointInMap(data[i]);
              }
              console.log(this.data.markers);
            })
          },
          fail: err => {
            console.error("websocket连接失败", err);
            throw new Error;
          }
        })
        ws.onClose((res) => {
          console.log(`websocket连接关闭`);
        })
        return [thisLongitude, thisLatitude];
      })
    }).catch(err => {
      console.error(err);
    })
  },
  /**
   * 将用户信息展示在地图上
   */
  showUserPointInMap(miniUserLocationVo) {
    const marker = {
      id: miniUserLocationVo.miniUser.openid,
      longitude: miniUserLocationVo.points[0],
      latitude: miniUserLocationVo.points[1],
      iconPath: miniUserLocationVo.miniUser.avatar,
      label: {},
      width: 30,
      height: 30
    };
    const newMarkers = this.data.markers;
    newMarkers.push(marker);
    this.setData({
      markers: newMarkers
    })
  }
})