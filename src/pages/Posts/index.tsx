import React, {useEffect, useState} from "react";
import {PageContainer,} from '@ant-design/pro-components';
import PostsList from "@/pages/Posts/components/PostsList";
import {PageSearchParams} from "@/services/common";
import {getPostsPage, publicPosts} from "@/services/posts";
import {Button, Form, message, Modal, Select, Spin, Switch, Upload, UploadFile, UploadProps} from "antd";
import dayjs from "dayjs";
import customParseFormat from "dayjs/plugin/customParseFormat";
import {PlusOutlined} from "@ant-design/icons";
import TextArea from "antd/es/input/TextArea";
import {RcFile} from "antd/es/upload";
import {Values} from "async-validator";
import {PostTag} from "@/models/postTag";
import {getTagsMap} from "@/services/tags";
import axios from "axios";

interface PublicPostForm {
  content: string,
  tid: number,
  notReveal: number
}

export const PostsContext = React.createContext({
  deletePost: (pid: number) => {
  },
  getMore: () => {
  }
})
const getBase64 = (file: RcFile): Promise<string> =>
  new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result as string);
    reader.onerror = (error) => reject(error);
  });
export const waitTime = (time: number = 100) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(true);
    }, time);
  });
};
dayjs.extend(customParseFormat);
const Posts: React.FC = () => {
  const [postList, setPostList] = useState<any[]>([]);
  const [tagList, setTagList] = useState<PostTag[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [spin, setSpin] = useState<boolean>(true);
  const [pageNo, setPageNo] = useState<number>(1);
  const [isOpenPublicPostForm, setIsOpenPublicPostForm] = useState<boolean>(false);
  const loadTagList = async () => {
    const res = await getTagsMap();
    if (res.data) {
      setTagList(res.data.data);
      return true;
    } else {
      message.error("数据加载失败，请刷新重试！");
      return false;
    }
  }
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
  const refreshPostListByDelete = async (pid: number) => {
    let tempPostList = postList;
    let newPostList = [];
    for (let i = 0; i < tempPostList.length; i++) {
      if (tempPostList[i].pid !== pid) {
        newPostList.push(tempPostList[i]);
      }
    }
    setPostList(newPostList);
  }
  useEffect(() => {
    loadPostList(pageNo)
  }, [loading])
  useEffect(() => {
    loadTagList();
  }, [loading])

  const tagOnChange = (value: string) => {
    console.log(`selected ${value}`);
  };

  const isRevealOnChange = (checked: boolean) => {
    console.log(`switch to ${checked}`);
  };
  const ActionMenu = () => {
    const [postForm] = Form.useForm();
    const [previewOpen, setPreviewOpen] = useState(false);
    const [previewImage, setPreviewImage] = useState('');
    const [previewTitle, setPreviewTitle] = useState('');
    const [fileList, setFileList] = useState<UploadFile[]>([]);
    const [showSubmitSpin, setShowSubmitSpin] = useState<boolean>(false);
    const handleCancel = () => setPreviewOpen(false);

    const handlePreview = async (file: UploadFile) => {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj as RcFile);
      }

      setPreviewImage(file.url || (file.preview as string));
      setPreviewOpen(true);
      setPreviewTitle(file.name || file.url!.substring(file.url!.lastIndexOf('/') + 1));
    };

    const handleChange: UploadProps['onChange'] = ({fileList: newFileList}) =>
      setFileList(newFileList);
    const uploadProps: UploadProps = {
      onRemove: (file) => {
        const index = fileList.indexOf(file);
        const newFileList = fileList.slice();
        newFileList.splice(index, 1);
        setFileList(newFileList);
      },
      beforeUpload: (file: RcFile) => {
        const isPNG = file.type === 'image/png' || file.type === "image/jpeg";
        if (!isPNG) {
          message.error(`请上传 .png/.jpg 格式的图片`);
          return Upload.LIST_IGNORE;
        } else {
          setFileList([...fileList, file]);
        }
        return false;
      },
      fileList,
    };
    const uploadButton = (
      <div>
        <PlusOutlined/>
        <div style={{marginTop: 8}}>Upload</div>
      </div>
    );
    const handleUpload = async (postData: PublicPostForm) => {
      const formData = new FormData();
      fileList.forEach((file) => {
        formData.append('img', file.originFileObj as RcFile);
      });
      console.log(fileList)
      Object.keys(postData).forEach((field) => {
        formData.append(field, postData[field as keyof typeof postData].toString());
      })
      // 默认官方openid
      formData.append("openid", "olG-q5aFDk6wc4tR446WUp3Gct1U");
      setShowSubmitSpin(true);
      const res = await publicPosts(formData);
      setShowSubmitSpin(false);
      if (res.data) {
        message.success(`帖子发布成功`);
        postForm.resetFields();
        setIsOpenPublicPostForm(false);
        setPostList([res.data.data, ...postList])
      } else {
        message.error(`帖子发布失败，请重试！`);
      }
    };
    return (
      <>
        <Button type={"primary"} htmlType={"submit"} icon={<PlusOutlined/>} onClick={() => {
          setIsOpenPublicPostForm(true);
        }}>发帖</Button>
        <Modal title={"发布帖子"} centered open={isOpenPublicPostForm} onOk={() => {
          postForm
            .validateFields()
            .then((values: Values) => {
              const formValues: PublicPostForm = {
                content: values.content,
                tid: values.tag,
                notReveal: values.isReveal ? 1 : 0
              }
              handleUpload(formValues);
            })
            .catch((info) => {
              console.log('Validate Failed:', info);
            });
        }} onCancel={() => {
          setIsOpenPublicPostForm(false);
        }}
        >
          <Spin spinning={showSubmitSpin}>
            <Form requiredMark={false} form={postForm}>
              <Form.Item name={"content"} label={"帖子内容"} rules={[
                {required: true},
                {type: "string"}
              ]}>
                <TextArea placeholder={'分享新鲜事……'}/>
              </Form.Item>
              <Form.Item label={"帖子图片"}>
                <Upload
                  listType="picture-card"
                  fileList={fileList}
                  onPreview={handlePreview}
                  onChange={handleChange}
                  {...uploadProps}
                >
                  {fileList.length >= 8 ? null : uploadButton}
                </Upload>
                <Modal open={previewOpen} title={previewTitle} footer={null} onCancel={handleCancel}>
                  <img alt="example" style={{width: '100%'}} src={previewImage}/>
                </Modal>
              </Form.Item>
              <Form.Item label={"帖子标签"} name={"tag"} initialValue={0}>
                <Select
                  showSearch
                  placeholder="选择帖子标签"
                  optionFilterProp="children"
                  onChange={tagOnChange}
                  options={(tagList || []).map((tag) => ({
                    value: tag.tid,
                    label: tag.text
                  }))}
                />
              </Form.Item>
              <Form.Item label={"是否匿名"} name={"isReveal"} initialValue={false} valuePropName="checked">
                <Switch onChange={isRevealOnChange}/>
              </Form.Item>
            </Form>

          </Spin>
        </Modal>
      </>
    )
  }
  return (
    <div
    >
      <PageContainer
        header={{
          title: '帖子列表',
        }}
        content={<ActionMenu/>}
      >
        <PostsContext.Provider value={{
          deletePost: (pid: number) => {
            refreshPostListByDelete(pid)
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
