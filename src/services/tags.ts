import axios from 'axios'

/**
 * 获取所有帖子标签接口
 */
export function getTagsMap() {
  return axios.get('/server/tag/getAllTag').then((res: any) => {
    console.log(`getTagsMap succeed`);
    return res;
  }).catch((e: any) => {
    console.error(`getTagsMap error`, e);
    return {};
  });
}
