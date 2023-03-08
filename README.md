## 更新日志

### 2022-11-06

#### 	后端

1. "发布墙墙"后端功能实现，后端目前是采用分布上传，也就是先上传帖子内容，帖子内容上传成功后再上传图片。这种方式虽然正向用例能跑通，但是存在缺陷，后续需要优化算法。**（目前大致优化方法为：在选择图片时，先将图片存入Redis缓存或者微信小程序本地缓存，等到提交表单时，将表单数据协同缓存里的图片一同进行上传）**

   2. 给查询帖子时，写入Redis缓存的帖子设置了一个过期时间**（5*60s=3min）**，以免用户无法请求到最新的数据；

#### 	前端

1. 首页回到顶部功能的实现。

#### 	尚未解决（完成）问题

1. 发布帖子后，主页面的数据没有实时更新，因为官方没有刷新页面的API，故暂时还未想到办法解决。
2. “发布墙墙”后端功能优化。**（目前大致优化方法为：在选择图片时，先将图片存入Redis缓存或者微信小程序本地缓存，等到提交表单时，将表单数据协同缓存里的图片一同进行上传）**

### 2023-01-28

1. 点赞功能的实现

### 2023-02-08

1. 管理员端发布帖子功能实现（**完成**）
2. 小程序用户管理界面编写（**待完成**）

## Bug/修复清单

| Bug描述                                                      | 修复思路                                            | 发现时间   | 修复时间   |
| :----------------------------------------------------------- | :-------------------------------------------------- | :--------- | :--------- |
| 用户A向用户B发送消息后，用户B未读该消息，然后A去阅读新消息后，B无法看到新消息提示 | 将消息已读未读的功能迁移到小程序，不再使用Redis即可 | 2023-02-21 | 2023-02-22 |

## 功能迭代

### 2023-02-12

#### 聊天系统

小程序与小程序用户之间可以在添加好友通过后，可以发起聊天

##### 需求分析

1. 用户需要先进行登录，登录成功后，才能获取自己的聊天列表。
2. 在聊天列表可以看到自己的历史聊天列表，以及最新的消息。
3. 用户可以在列表中选择一个自己的好友进行聊天（未添加好友不能聊天），该通信只有聊天的双方可以看见，对其他用户是不可见的。
4. 申请添加好友功能：用户可以向另一个用户发起添加好友申请，申请通过后才能发起聊天。

##### 数据库设计

1. 一个小程序用户可以有多个好友，一个小程序也可以是也可以是多个人的好友，所以小程序用户与小程序用户之间的好友关系是**多对多**。
2. 用户与用户之间可以有多条聊天记录，一条聊天记录属于两个用户。
3. 用户与用户之间有多条聊天记录（可以是图片，（语音暂不支持）），A发送的消息，就是B接收的消息。B发送的消息，就是A接收的消息，所以是**多对多**关系。

##### 功能实现

1. 使用Redis查询消息未读数量。

### 2023-02-22

#### 好友系统

##### 功能描述

​	1. 在小程序系统中，用户可以在**主页、帖子、帖子评论区等等**关注小程序用户，关注成功后，需要提示被关注的用户(xxx关注了你)，然后在自己的个人中心页面可以查看自己的关注列表。

2. 只有互相关注的用户才能显示在“消息页面”中，才能进行聊天，未互相关注的用户不能进行聊天。
3. 用户可以进行取消关注操作。用户被取消关注后，被取消关注的那一方会收到通知（xxx对你取消了关注）。

##### 数据库设计

1. 一个用户可以关注多个用户，而一个用户可以被多个用户关注，所以用户~用户之间是多对多关系。

##### 功能实现

1. 互相关注功能实现
1. 缓存帖子查询分页

##### 进度日志

| 功能描述                                                     | 完成时间         |
| ------------------------------------------------------------ | ---------------- |
| **“我的关注”**列表功能实现                                   | 2023-02-28 20:14 |
| **"取消关注"**功能实现，进入关注列表后，点击“已关注->确认取消关注”后，可以取消关注，"已关注"按钮变为“关注”，用户再次点击关注，即可再次重新关注小程序用户。 | 2023-02-28 22:16 |
| “**粉丝列表**”功能实现：点击粉丝列表后，跳转到粉丝列表页面，列表显示粉丝列表（关注我的，我的关注），可以取消关注，"已关注"按钮变为“关注”，用户再次点击关注，即可再次重新关注小程序用户。 | 2023-03-01 22:50 |
| "**好友列表**"功能功能实现：点击好友列表后，跳转到好友列表页面，列表显示好友（只有互相关注的才为好友），可以取消关注，"已关注"按钮变为“关注”，用户再次点击关注，即可再次重新关注小程序用户。 | 2023-03-01 22:52 |
| "**互相聊天功能实现**"                                       |                  |

### 2023-03-03

#### 帖子系统

##### 进度日志

| 功能描述                     | 完成时间 |
| ---------------------------- | -------- |
| 实现帖子的“最热”、“最新”功能 |          |

### 2023-03-07

#### 匹配系统

##### 功能描述

​	用户点击“校园匹配”后，跳转到“校园匹配”页面，页面为当前所在点的地图，中心点为自己的用户头像，然后在地图上还会显示其他用户的头像（称为附近的人），用户点击头像后，可以跳转到对应的个人主页，查看用户所发的帖子和个人信息以及所有的个人功能逻辑。

##### 功能实现

​	当用户进入微信小程序时候，获取小程序用户的经纬度，根据经纬度计算出对应的省、市、区、县、乡、村等基本信息，然后再Redis根据这些信息进行经纬度的匹配筛选，从而来减少检索的`时间复杂度`。由于详细地址属于敏感信息，故不能获取到过于详细的信息。

​	例如：四川省-成都市-都江堰市。然后在`Redis`中，拿到“四川省-成都市-都江堰市”下的，使用`二分查找`，进行经纬度的检索，从而降低`时间复杂度`。想找到都江堰市，然后多线程进行对半的数据检索，

```json
// 都江堰市
"510181000000":[
    {
        longitude:30.894435, // 精度
        latitude:103.60298 // 纬度
    }，
    {
    	longitude:30.83492, // 精度
        latitude:103.584278 // 纬度
    }
]
```

​	

## 尚未完善的功能

1. 帖子的评论列表分页尚未实现。
2. 点赞功能尚未实现。

## 微信小程序问题

1. 微信小程序刷新tabbar页面问题

   可以使用跳转到tabbar的方式来刷新页面，代码如下：

   ```js
   wx.switchTab({
       url: '/pages/index/index',
       success: res => {
         const page = getCurrentPages().pop();
         if (page == undefined || page == null) return;  
         page.onLoad();  
       }
   })
   ```

2. 回到顶部组件封装

   ```xml
   <view class="back-top" bindtap="backTop">
     <view class="back-top-icon">
       <van-icon name="back-top" class="back-top-icon" size="50rpx" color="white" />
     </view>
   </view>
   ```

   ```css
   .back-top{
     border: 1px #409eff solid;
     border-radius: 50%;
     background-color: #409eff;
     width: 80rpx;
     height: 80rpx;
     position: fixed;
     bottom: 45rpx;
     right: 30rpx;
     z-index: 999999;
     display: flex;
     justify-content: center;
     align-items: center;
   }
   ```

   ```javascript
   /**
   * 回到顶部事件
   * @param {*} e 
   */
   backTop(e) {
       wx.pageScrollTo({
         scrollTop: 0,
         duration: 300,
       })
   },
   ```