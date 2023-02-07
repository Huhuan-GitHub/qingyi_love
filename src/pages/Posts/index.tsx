import React, {useEffect, useState} from "react";
import {PageContainer} from '@ant-design/pro-components';
import SearchCard from "@/pages/Posts/components/SearchCard";
import PostsList from "@/pages/Posts/components/PostsList";
import {PageSearchParams} from "@/services/common";
import {getPostsPage} from "@/services/posts";
import {message} from "antd";

export const PostsContext = React.createContext({
  update: () => {
  },
  getMore: () => {
  }
})
const Posts: React.FC = () => {
  const [postList, setPostList] = useState<any[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [spin, setSpin] = useState<boolean>(true);
  const [pageNo, setPageNo] = useState<number>(1);
  const loadPostList = async (pageNoValue: number) => {
    const pageSizeValue = 2;
    const params: PageSearchParams = {pageSize: pageSizeValue, pageNo: pageNoValue};
    const res = await getPostsPage(params);
    if (res.data) {
      // 去重
      let arr = [...postList, ...res.data.data];
      const res_arr = new Map();
      setPostList(arr.filter((item) => !res_arr.has(item["pid"]) && res_arr.set(item["pid"], 1)));
      if (res.data.data.length !== 0 && res.data.data.length % pageSizeValue === 0) {
        setPageNo(pageNo + 1);
      }
      if (res.data.data.length === 0) {
        message.warning("没有更多帖子了！");
      }
      setSpin(false);
    } else {
      message.error("加载失败，请刷新重试")
    }
    setLoading(false);
  }
  useEffect(() => {
    loadPostList(pageNo)
  }, [loading])
  return (
    <div
      style={{
        background: '#F5F7FA',
      }}
    >
      <PageContainer
        header={{
          title: '帖子列表',
        }}
        content={<SearchCard/>}
      >
        <PostsContext.Provider value={{
          update: () => {
            loadPostList(pageNo)
          }, getMore: () => {
            loadPostList(pageNo)
          }
        }}>
          <PostsList postList={postList} spin={spin}/>
        </PostsContext.Provider>
      </PageContainer>
    </div>
  )
}

export default Posts;
