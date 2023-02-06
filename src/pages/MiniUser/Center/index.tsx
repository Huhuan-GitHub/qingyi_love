import React, {useEffect, useState} from 'react';
import {useMatch} from "react-router";
import {MiniUser} from "@/models/miniUser";
import {getMiniUserInfo} from "@/services/miniUser";
import {Avatar, Divider, Image, message, Space, Tag, Typography} from "antd";
import ProCard from "@ant-design/pro-card";
import {FieldTimeOutlined, HomeOutlined} from '@ant-design/icons';
import PostsList from "@/pages/Posts/components/PostsList";
import {getPostsPage} from "@/services/posts";
import {PageSearchParams} from "@/services/common";

const Center: React.FC = () => {
  const miniUser: any = useMatch("/miniUser/center/:openid");
  const {openid} = miniUser.params;
  const [spin, setSpin] = useState<boolean>(true);
  const [miniUserInfo, setMiniUserInfo] = useState<MiniUser>();
  const [postList, setPostList] = useState<[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const loadData = async () => {
    const pageParams: PageSearchParams = {pageSize: 10, pageNo: 1};
    const res = await getMiniUserInfo(openid);
    const res_post = await getPostsPage(pageParams);
    if (res) {
      setMiniUserInfo(res.data.data)
      setPostList(res_post.data.data)
      setSpin(false)
    } else {
      message.error("加载失败，请刷新重试")
    }
    setLoading(false);
  }
  useEffect(() => {
    loadData();
  }, [loading])
  return (
    <ProCard gutter={[8, 16]} ghost wrap>
      <ProCard colSpan={{xs: 24, sm: 24, md: 24, lg: 6, xl: 8}} layout={"center"}>
        <Space direction={"vertical"} align={"center"} size={[0, 12]}>
          {/*头像*/}
          <Avatar size={108}
                  src={<Image src={miniUserInfo?.avatar}
                              alt={"用户头像"}/>}/>
          {/*基本信息*/}
          <Space direction={"vertical"} align={"center"} size={[0, 0]}>
            <Typography.Text strong>
              {miniUserInfo?.username}
            </Typography.Text>
            <Typography.Text style={{fontSize: '12px'}}>
              永远不会动态规划的小白
            </Typography.Text>
          </Space>
          <Space direction={"vertical"} align={"start"}>
            <Space size={8}>
              <FieldTimeOutlined/>
              2023-02-04 22:25
            </Space>
            <Space size={8}>
              <HomeOutlined/>
              成都东软学院
            </Space>
          </Space>
          <Space wrap>
            <Tag>动态规划</Tag>
            <Tag>Java</Tag>
            <Tag>深度优先搜索</Tag>
            <Tag>递归</Tag>
            <Tag>+</Tag>
          </Space>
        </Space>
      </ProCard>
      <ProCard colSpan={{xs: 24, sm: 24, md: 24, lg: 18, xl: 16}}>
        <PostsList postList={postList} spin={spin}/>
      </ProCard>
    </ProCard>
  )
}

export default Center;
