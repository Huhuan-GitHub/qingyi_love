import React, {useState} from "react";
import {Button} from "antd";

const TagItem: React.FC<{ item: any }> = ({item}) => {
  const [checked, setChecked] = useState(false);
  const name = item.name;
  const handleTagChecked = (checked: boolean) => {
    setChecked(checked)
  }
  return (<Button type={checked ? 'primary' : 'text'} size="small" onClick={() => handleTagChecked(!checked)}
                  style={{margin: '0 10px 0 10px'}}>{name}</Button>)
}
const TagBar: React.FC<{ tagOptions: Array<any> }> = ({tagOptions}) => {
  const tagList = tagOptions.map((tag) => <TagItem key={tag.toString()} item={tag}/>)
  return (<div>
    所属类别：{tagList}
  </div>)
}

export default TagBar;
