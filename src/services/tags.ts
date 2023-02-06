import axios from 'axios'


export function getTagsMap() {
  return axios.get('/api/tag/get/map').then((res: any) => {
    console.log(`getTagsMap succeed`);
    return res;
  }).catch((e: any) => {
    console.error(`getTagsMap error`, e);
    return {};
  });
}
