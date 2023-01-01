package com.neusoft.qingyi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.qingyi.pojo.Tag;
import com.neusoft.qingyi.service.TagService;
import com.neusoft.qingyi.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author 29600
* @description 针对表【t_tag】的数据库操作Service实现
* @createDate 2022-11-12 23:10:04
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




