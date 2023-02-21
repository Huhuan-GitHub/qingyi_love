/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : qingyi

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 21/02/2023 15:28:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_mini_user
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_user`;
CREATE TABLE `t_mini_user`  (
  `mini_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '小程序用户主键',
  `openid` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '小程序用户唯一标识符',
  `is_deleted` int(0) NULL DEFAULT NULL COMMENT '是否删除',
  `username` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '小程序用户名',
  `avatar` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '头像地址（腾讯云）',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '加入时间',
  `background_image` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '主页背景图',
  PRIMARY KEY (`mini_id`) USING BTREE,
  UNIQUE INDEX `openid`(`openid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_mini_user
-- ----------------------------
INSERT INTO `t_mini_user` VALUES (1, 'olG-q5aFDk6wc4tR446WUp3Gct1U', 0, '游标卡尺ᯤ⁶ᴳ', 'http://localhost:3033/qingyi_mini_user_avatar/olG-q5aFDk6wc4tR446WUp3Gct1U.png', '2023-02-02 17:55:56', 'https://img2.baidu.com/it/u=3937107996,814182323&fm=253&fmt=auto&app=138&f=JPG?w=755&h=500');
INSERT INTO `t_mini_user` VALUES (2, 'olG-q5dFevLWx4EEJ2OJ6P6EVnMo', 0, '默默', 'https://img2.baidu.com/it/u=915728877,1428815538&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1676394000&t=6b05080dfec25732134ad62c5aeaa3be', '2023-02-13 20:38:32', 'https://img0.baidu.com/it/u=713990679,2543188425&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1676394000&t=d8d4ef8446e3711759717326e9c439d9');
INSERT INTO `t_mini_user` VALUES (3, 'olG-q5d1OZZjNbuhy6epiYgh0BaY', 0, '狗狗', 'https://img1.baidu.com/it/u=1515279825,3080647009&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1676912400&t=cb3ed086825bcacf43569d7d594ddcf8', '2023-02-19 16:10:28', 'https://img1.baidu.com/it/u=1515279825,3080647009&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1676912400&t=cb3ed086825bcacf43569d7d594ddcf8');

-- ----------------------------
-- Table structure for t_mini_user_attention
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_user_attention`;
CREATE TABLE `t_mini_user_attention`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '关注表主键',
  `attention_openid` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '发出关注的小程序用户',
  `attentioned_openid` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '被关注的小程序用户',
  `attention_time` datetime(0) NULL DEFAULT NULL COMMENT '关注的时间',
  `cancel_attention_time` datetime(0) NULL DEFAULT NULL COMMENT '取消关注的时间',
  `is_cancel_attention` int(0) NULL DEFAULT 0 COMMENT '是否取消关注 0:未取消 1:已取消',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_mini_user_attention
-- ----------------------------

-- ----------------------------
-- Table structure for t_mini_user_chat_message
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_user_chat_message`;
CREATE TABLE `t_mini_user_chat_message`  (
  `m_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '消息表主键',
  `send_openid` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '该条消息的发送者',
  `receive_openid` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '该条消息的接收者',
  `message_type` int(0) NULL DEFAULT NULL COMMENT '该条消息的类型 0:文本类型 1:图片类型',
  `message_content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT '该条消息的内容，如果是图片，则是图片消息外键',
  `send_time` datetime(0) NULL DEFAULT NULL COMMENT '消息的发送时间',
  `is_deleted` int(0) NULL DEFAULT NULL COMMENT '消息是否删除 0：未删除 1：已删除',
  `group_id` int(0) NULL DEFAULT NULL COMMENT '消息发送者分组',
  PRIMARY KEY (`m_id`) USING BTREE,
  INDEX `group_id`(`group_id`) USING BTREE,
  CONSTRAINT `group_id` FOREIGN KEY (`group_id`) REFERENCES `t_mini_user_chat_message_group` (`group_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 67 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_mini_user_chat_message
-- ----------------------------
INSERT INTO `t_mini_user_chat_message` VALUES (1, 'olG-q5aFDk6wc4tR446WUp3Gct1U', 'olG-q5d1OZZjNbuhy6epiYgh0BaY', 0, '在干嘛呢', '2023-02-19 12:00:54', 0, 1);
INSERT INTO `t_mini_user_chat_message` VALUES (64, 'olG-q5aFDk6wc4tR446WUp3Gct1U', 'olG-q5d1OZZjNbuhy6epiYgh0BaY', 0, '我要生气了', '2023-02-19 19:56:00', 0, NULL);
INSERT INTO `t_mini_user_chat_message` VALUES (65, 'olG-q5aFDk6wc4tR446WUp3Gct1U', 'olG-q5d1OZZjNbuhy6epiYgh0BaY', 0, '你到底在干嘛', '2023-02-19 19:56:00', 0, NULL);
INSERT INTO `t_mini_user_chat_message` VALUES (66, 'olG-q5aFDk6wc4tR446WUp3Gct1U', 'olG-q5d1OZZjNbuhy6epiYgh0BaY', 0, '还不回我', '2023-02-19 19:56:00', 0, NULL);

-- ----------------------------
-- Table structure for t_mini_user_chat_message_group
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_user_chat_message_group`;
CREATE TABLE `t_mini_user_chat_message_group`  (
  `g_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `send_openid` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '消息发送者openid',
  `receive_openid` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '消息接收者openid',
  `group_id` int(0) NULL DEFAULT NULL COMMENT '组号',
  PRIMARY KEY (`g_id`) USING BTREE,
  INDEX `group_id`(`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_mini_user_chat_message_group
-- ----------------------------
INSERT INTO `t_mini_user_chat_message_group` VALUES (1, 'olG-q5aFDk6wc4tR446WUp3Gct1U', 'olG-q5dFevLWx4EEJ2OJ6P6EVnMo', 1);
INSERT INTO `t_mini_user_chat_message_group` VALUES (2, 'olG-q5dFevLWx4EEJ2OJ6P6EVnMo', 'olG-q5aFDk6wc4tR446WUp3Gct1U', 1);
INSERT INTO `t_mini_user_chat_message_group` VALUES (3, 'olG-q5aFDk6wc4tR446WUp3Gct1U', 'olG-q5d1OZZjNbuhy6epiYgh0BaY', 2);

-- ----------------------------
-- Table structure for t_mini_user_friend_ship
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_user_friend_ship`;
CREATE TABLE `t_mini_user_friend_ship`  (
  `user_openid` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '发起好友申请的小程序用户',
  `friend_openid` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '接受好友申请的小程序用户',
  `status` int(0) NULL DEFAULT 0 COMMENT '好友申请的结果：0：待通过，1：已通过，2：已拒绝，3：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '好友申请发起的时间',
  `pass_time` datetime(0) NULL DEFAULT NULL COMMENT '通过好友申请的时间',
  `refuse_time` datetime(0) NULL DEFAULT NULL COMMENT '拒绝好友申请的时间',
  `delete_time` datetime(0) NULL DEFAULT NULL COMMENT '删除好友的时间',
  INDEX `user_openid`(`user_openid`) USING BTREE,
  INDEX `friend_openid`(`friend_openid`) USING BTREE,
  CONSTRAINT `friend_openid` FOREIGN KEY (`friend_openid`) REFERENCES `t_mini_user` (`openid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_openid` FOREIGN KEY (`user_openid`) REFERENCES `t_mini_user` (`openid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_mini_user_friend_ship
-- ----------------------------

-- ----------------------------
-- Table structure for t_posts
-- ----------------------------
DROP TABLE IF EXISTS `t_posts`;
CREATE TABLE `t_posts`  (
  `p_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '帖子表主键',
  `send_time` datetime(0) NULL DEFAULT NULL COMMENT '发帖时间',
  `content` mediumtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT '帖子内容',
  `openid` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '发帖人openid',
  `t_id` int(0) NULL DEFAULT NULL COMMENT '标签表主键',
  `not_reveal` int(0) NULL DEFAULT 0 COMMENT '是否匿名发布的 0:否 1:是',
  `is_deleted` int(0) NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`p_id`) USING BTREE,
  INDEX `openid`(`openid`) USING BTREE,
  INDEX `t_id`(`t_id`) USING BTREE,
  CONSTRAINT `openid` FOREIGN KEY (`openid`) REFERENCES `t_mini_user` (`openid`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `t_id` FOREIGN KEY (`t_id`) REFERENCES `t_tag` (`t_id`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE = InnoDB AUTO_INCREMENT = 153 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_posts
-- ----------------------------
INSERT INTO `t_posts` VALUES (152, '2023-02-08 17:10:41', '美东时间2月7日，微软举行新闻发布会，宣布将升级搜索软件Bing与游览器Edge的AI功能，本周二正式开启桌面版试用，手机版也将来临。在为OpenAI投资数十亿美元后，微软看到了其在搜索引擎领域逆风翻盘的希望。\r\n\r\n风靡全球的ChatGPT是由GPT-3.5系列中的模型微调而来，OpenAI的CEO Sam Altman在发布会上证实，微软使用GPT-3.5提升Bing的AI能力，其功能与ChatGPT非常相像。\r\n\r\n谈到ChatGPT，微软CEO Satya Nadella表示，这项技术将重塑几乎所有软件甚至是整个互联网。新版Bing使用了下一代的OpenAI模型，吸取了 ChatGPT和GPT-3.5的经验和进步将更加强大。目前微软并未说明该代模型是否为传闻中的GPT-4.0。\r\n\r\n以往各类搜索引擎搜得的结果难以直接回答问题，Satya Nadella表示，目前的搜索引擎非常适合查询路程等信息获取基于事实的基本信息。但对于更复杂的查询，例如“你能推荐一个墨西哥城的五天行程吗？”，目前的搜索引擎会很低效，而这些查询占整体查询数量的一半。\r\n\r\n新版Bing将带来颠覆性的搜索体验，通过其右侧聊天框功能解决该痛点，允许用户搜索后以类ChatGPT聊天的方式获取匹配度更高、更个性化的解答。\r\n\r\n在新版Bing给的例子中，搜索“我刚完成蒙大拿州比格霍恩钓鱼，若春天去佛罗里达群岛钓鱼需做哪些不同的准备？”除了左侧常规搜索结果，右侧聊天框整合生成了一份直接对应问题的答案。\r\n\r\n相比于目前版本的ChatGPT仅包含2021年及之前的数据，最新版的Bing不仅无数据时间限制，同时答案上还标注了来源链接，而之前试用ChatGPT无法获得数据来源及链接。', 'olG-q5aFDk6wc4tR446WUp3Gct1U', 2, 0, 0);

-- ----------------------------
-- Table structure for t_posts_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_posts_comment`;
CREATE TABLE `t_posts_comment`  (
  `c_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '评论表主键',
  `openid` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '评论人openid',
  `p_id` int(0) NULL DEFAULT NULL COMMENT '帖子表主键',
  `comment` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '评论内容',
  `comment_date` datetime(0) NULL DEFAULT NULL COMMENT '评论日期',
  `c_parent_id` int(0) NULL DEFAULT NULL COMMENT '执行父评论的id，如果不是对评论的回复，那么该值为null',
  `is_deleted` int(0) NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`c_id`) USING BTREE,
  INDEX `comment_openid`(`openid`) USING BTREE,
  INDEX `comment_p_id`(`p_id`) USING BTREE,
  CONSTRAINT `comment_openid` FOREIGN KEY (`openid`) REFERENCES `t_mini_user` (`openid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `comment_p_id` FOREIGN KEY (`p_id`) REFERENCES `t_posts` (`p_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 225 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_posts_comment
-- ----------------------------

-- ----------------------------
-- Table structure for t_posts_comment_count
-- ----------------------------
DROP TABLE IF EXISTS `t_posts_comment_count`;
CREATE TABLE `t_posts_comment_count`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `p_id` int(0) NULL DEFAULT NULL COMMENT '帖子表主键',
  `comment_count` int(0) NULL DEFAULT NULL COMMENT '评论数量',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `p_id`(`p_id`) USING BTREE,
  CONSTRAINT `p_id` FOREIGN KEY (`p_id`) REFERENCES `t_posts` (`p_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_posts_comment_count
-- ----------------------------

-- ----------------------------
-- Table structure for t_posts_img
-- ----------------------------
DROP TABLE IF EXISTS `t_posts_img`;
CREATE TABLE `t_posts_img`  (
  `p_id` int(0) NULL DEFAULT NULL COMMENT '帖子表主键',
  `img_url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '帖子图片的链接',
  INDEX `p_id`(`p_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_posts_img
-- ----------------------------
INSERT INTO `t_posts_img` VALUES (110, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__110/7687bfd5-3a95-40de-b665-05d4f271d0f4.png');
INSERT INTO `t_posts_img` VALUES (111, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__111/05e13484-c10a-4ad6-80c1-4a69f68d8426.png');
INSERT INTO `t_posts_img` VALUES (112, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__112/e8960cf1-c2d8-4c76-854e-0e5ecb57fb52.png');
INSERT INTO `t_posts_img` VALUES (113, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__113/2a16ab76-8731-48bb-9c64-f2b086d96c40.png');
INSERT INTO `t_posts_img` VALUES (113, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__113/6d333f82-876b-4319-b423-bc6f2a5713ad.png');
INSERT INTO `t_posts_img` VALUES (113, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__113/c01fd2e9-eb76-44d6-bc4e-1974b375882a.png');
INSERT INTO `t_posts_img` VALUES (114, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__114/7934f9de-bbd2-4dbe-aba6-0fb9df8c9238.png');
INSERT INTO `t_posts_img` VALUES (115, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__115/c9ce30a8-f6db-4c1a-afe7-6386cd75da89.png');
INSERT INTO `t_posts_img` VALUES (116, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__116/95ab8908-b88c-4178-b6f3-b1bad4031972.png');
INSERT INTO `t_posts_img` VALUES (117, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__117/213824e5-6195-4eb5-9b00-a080bd68ffcc.png');
INSERT INTO `t_posts_img` VALUES (118, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__118/46baadac-2c7d-44f7-bfc1-94c9f47309f5.png');
INSERT INTO `t_posts_img` VALUES (119, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__119/fa9ffd80-e366-48c6-9eb0-058e0b9dd809.png');
INSERT INTO `t_posts_img` VALUES (120, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__120/4e2e2450-ffdd-4b6d-be4d-7d024b173fbf.png');
INSERT INTO `t_posts_img` VALUES (136, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__136/17488807-56a0-4354-aa01-cff746985ed2.png');
INSERT INTO `t_posts_img` VALUES (140, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__140/4c96fcf8-c8a5-4603-9b1d-ead0319887db.png');
INSERT INTO `t_posts_img` VALUES (141, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__141/3ded472b-a4dc-40e0-bb44-8c3341bcb373.png');
INSERT INTO `t_posts_img` VALUES (143, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__143/647999fd-8b7c-4241-aba3-4684664bc0d8.png');
INSERT INTO `t_posts_img` VALUES (144, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__144/3fea1c14-23ee-4bf0-81cc-ceb2dc62352c.png');
INSERT INTO `t_posts_img` VALUES (145, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__145/d3ab9522-51c4-48e7-aa7b-9a38bf16c600.png');
INSERT INTO `t_posts_img` VALUES (146, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__146/48e18e43-cd01-41d7-86cf-42821d156bdd.png');
INSERT INTO `t_posts_img` VALUES (147, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__147/e5a9b804-7e7d-4674-9534-4109beda074e.png');
INSERT INTO `t_posts_img` VALUES (152, 'http://localhost:3033/qingyi_posts_file/olG-q5aFDk6wc4tR446WUp3Gct1U__152/5350783e-c6ba-4784-9835-5bf9785b6530.png');

-- ----------------------------
-- Table structure for t_posts_like
-- ----------------------------
DROP TABLE IF EXISTS `t_posts_like`;
CREATE TABLE `t_posts_like`  (
  `openid` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '点赞人openid',
  `p_id` int(0) NULL DEFAULT NULL COMMENT '帖子表主键',
  `status` int(0) NULL DEFAULT NULL COMMENT '点赞状态（0：取消 1：点赞）',
  INDEX `opneid`(`openid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_posts_like
-- ----------------------------

-- ----------------------------
-- Table structure for t_posts_like_count
-- ----------------------------
DROP TABLE IF EXISTS `t_posts_like_count`;
CREATE TABLE `t_posts_like_count`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `p_id` int(0) NULL DEFAULT NULL COMMENT '帖子表主键',
  `count` int(0) NULL DEFAULT NULL COMMENT '点赞数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_posts_like_count
-- ----------------------------

-- ----------------------------
-- Table structure for t_posts_share
-- ----------------------------
DROP TABLE IF EXISTS `t_posts_share`;
CREATE TABLE `t_posts_share`  (
  `openid` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '分享人openid',
  `p_id` int(0) NULL DEFAULT NULL COMMENT '帖子表主键'
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_posts_share
-- ----------------------------

-- ----------------------------
-- Table structure for t_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_tag`;
CREATE TABLE `t_tag`  (
  `t_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '标签表主键',
  `bg_color` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '标签背景颜色，16进制表示',
  `text` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '标签的文字内容',
  PRIMARY KEY (`t_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_tag
-- ----------------------------
INSERT INTO `t_tag` VALUES (0, '#07c160', '推荐');
INSERT INTO `t_tag` VALUES (1, '#f2826a', '学习交流');
INSERT INTO `t_tag` VALUES (2, '#87d068', '科技与狠活');
INSERT INTO `t_tag` VALUES (3, '#2db7f5', '交友');

SET FOREIGN_KEY_CHECKS = 1;
