import React, {useEffect, useState} from "react";
import customParseFormat from 'dayjs/plugin/customParseFormat'
import dayjs from 'dayjs';
import {DatePicker,Form, Input, message, Select, Space} from "antd";
import {getTagsMap} from "@/services/tags";

export const waitTime = (time: number = 100) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(true);
    }, time);
  });
};

dayjs.extend(customParseFormat)
const SearchCard: React.FC = () => {
  const [tagList, setTagList] = useState<[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  // 标签搜索过滤器
  const tagFilter = (input: string, option: any) => {
    return !(option?.label ?? '').indexOf(input)
  }
  const loadTagList = async () => {
    const res = await getTagsMap();
    if (res) {
      setTagList(res.data)
    } else {
      message.error("加载失败，请刷新重试")
    }
    setLoading(false);
  }

  useEffect(() => {
    loadTagList()
  }, [loading])
  return (
    <Form size="large" layout='inline'>
      <Space wrap={true}>
        <Form.Item label="关键字" name="title">
          <Input placeholder="请输入关键字"/>
        </Form.Item>
        <Form.Item label="日期范围" name="sendTime">
          <DatePicker.RangePicker/>
        </Form.Item>
        <Form.Item label="标签" name="tag">
          <Select
            showSearch
            placeholder="选择帖子标签"
            optionFilterProp="children"
            filterOption={(input, option) => tagFilter(input, option)}
            options={tagList}
          />
        </Form.Item>
        <Form.Item label="排序">
          <Select
            showSearch
            placeholder="选择排序方式"
            optionFilterProp="children"
            filterOption={(input, option) => tagFilter(input, option)}
            options={tagList}
          />
        </Form.Item>
      </Space>
    </Form>
  );
}

export default SearchCard;
