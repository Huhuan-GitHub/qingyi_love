import axios from "axios";

/**
 * 根据openid获取小程序用户信息
 * @param openid 小程序用户唯一标识符
 */
export function getMiniUserInfo(openid: string) {
  return axios.get("/server/miniUser/getMiniUserInfoByOpenid", {
    params: {openid: openid}
  }).then(res => {
    console.log("获取小程序用户信息成功！");
    return res;
  }).catch(err => {
    console.error("获取小程序用户信息失败！", err);
  })
}
