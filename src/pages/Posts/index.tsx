import React, {useEffect, useState} from "react";
import {PageContainer} from '@ant-design/pro-components';
import SearchCard from "@/pages/Posts/components/SearchCard";
import PostsList from "@/pages/Posts/components/PostsList";
import {PageSearchParams} from "@/services/common";
import {getPostsPage} from "@/services/posts";
import {message} from "antd";

const Posts: React.FC = () => {
  const [postList, setPostList] = useState<[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [spin, setSpin] = useState<boolean>(true);
  const loadPostList = async () => {
    const params: PageSearchParams = {pageSize: 10, pageNo: 1};
    const res = await getPostsPage(params);
    if (res.data) {
      setPostList(res.data.data);
      setSpin(false);
      console.log(postList);
    } else {
      message.error("加载失败，请刷新重试")
    }
    setLoading(false);
  }
  useEffect(() => {
    loadPostList()
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
        <PostsList postList={postList} spin={spin}/>
      </PageContainer>
    </div>
  )
}

export default Posts;
