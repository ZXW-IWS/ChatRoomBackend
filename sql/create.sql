DROP DATABASE IF EXISTS chat;
CREATE DATABASE chat;
USE chat;

DROP TABLE IF EXISTS user;
create table user
(
    id              bigint auto_increment not null comment '用户id' primary key,
    nickname        varchar(255)                        null comment '用户昵称',
    avatar          varchar(255)                        null comment '用户头像',
    sex             int                                 null comment '用户性别 0-未知 1-男 2-女',
    openid          varchar(255)                        not null comment '平台中的用户标识',
    user_type       int                                 null comment '用户类别 1-微信  2-测试 ...',
    active_status   int       default 2                 null comment '用户在线状态 1-在线 2-离线',
    ip_info         json                      null comment '用户ip信息',
    item_id bigint DEFAULT NULL COMMENT '佩戴的徽章id',
    last_login_time datetime  default CURRENT_TIMESTAMP not null comment '用户上次登录时间',
    status          tinyint   default 0                 not null comment '账号状态 0-正常 1-异常',
    create_time     timestamp default CURRENT_TIMESTAMP null comment '记录创建时间',
    update_time     timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '记录最近更新时间',
    is_delete       tinyint   default 0                 not null comment '逻辑删除 0-正常 1-删除'

)comment 'chat-room用户表';

create index idx_nickname
    on user (nickname)
    comment '昵称索引';


create index idx_openid
    on user (openid)
    comment 'openid索引';


DROP TABLE IF EXISTS item;
create table item
(
    id          bigint auto_increment comment '主键id',
    type        int                                 not null comment '物品类型 1改名卡 2徽章',
    img         varchar(255)                        null comment '对应的图片',
    `describe`  varchar(255)                        null comment '物品描述信息',
    create_time TIMESTAMP default CURRENT_TIMESTAMP not null comment '记录创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '记录更新时间',
    is_delete   tinyint   default 0                 not null comment '逻辑删除 0-未删除 1-已删除',
    constraint item_pk
        primary key (id)
)
    comment '物品表';

INSERT INTO `item`(id, type, img, `describe`) VALUES (1, 1, NULL, '用户可以使用改名卡，更改自己的名字。');
INSERT INTO `item`(id, type, img, `describe`) VALUES (2, 2, 'https://cdn-icons-png.flaticon.com/128/1533/1533913.png', '爆赞徽章，单条消息被点赞超过10次，即可获得');
INSERT INTO `item`(id, type, img, `describe`) VALUES (3, 2, 'https://cdn-icons-png.flaticon.com/512/6198/6198527.png ', '前10名注册的用户才能获得的专属徽章');
INSERT INTO `item`(id, type, img, `describe`) VALUES (4, 2, 'https://cdn-icons-png.flaticon.com/512/10232/10232583.png', '前100名注册的用户才能获得的专属徽章');


DROP TABLE IF EXISTS `black`;
CREATE TABLE `black`  (
                          `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
                          `type` int(11) NOT NULL COMMENT '拉黑目标类型 1.ip 2uid',
                          `target` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '拉黑目标',
                          `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                          `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                          PRIMARY KEY (`id`) USING BTREE,
                          UNIQUE INDEX `idx_type_target`(`type`, `target`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '黑名单' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
                        `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                        `name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
                        `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                        `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                        PRIMARY KEY (`id`) USING BTREE,
                        KEY `idx_create_time` (`create_time`) USING BTREE,
                        KEY `idx_update_time` (`update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';
insert into role(id,`name`) values(1,'超级管理员');
insert into role(id,`name`) values(2,'群聊管理员');

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
                             `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                             `uid` bigint(20) NOT NULL COMMENT 'uid',
                             `role_id` bigint(20) NOT NULL COMMENT '角色id',
                             `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                             `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                             PRIMARY KEY (`id`) USING BTREE,
                             KEY `idx_uid` (`uid`) USING BTREE,
                             KEY `idx_role_id` (`role_id`) USING BTREE,
                             KEY `idx_create_time` (`create_time`) USING BTREE,
                             KEY `idx_update_time` (`update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关系表';

DROP TABLE IF EXISTS `user_apply`;
CREATE TABLE `user_apply` (
                              `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                              `uid` bigint(20) NOT NULL COMMENT '申请人uid',
                              `type` int(11) NOT NULL COMMENT '申请类型 1加好友',
                              `target_id` bigint(20) NOT NULL COMMENT '接收人uid',
                              `msg` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '申请信息',
                              `status` int(11) NOT NULL COMMENT '申请状态 1待审批 2同意',
                              `read_status` int(11) NOT NULL COMMENT '阅读状态 1未读 2已读',
                              `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                              `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                              PRIMARY KEY (`id`) USING BTREE,
                              KEY `idx_uid_target_id` (`uid`,`target_id`) USING BTREE,
                              KEY `idx_target_id_read_status` (`target_id`,`read_status`) USING BTREE,
                              KEY `idx_target_id` (`target_id`) USING BTREE,
                              KEY `idx_create_time` (`create_time`) USING BTREE,
                              KEY `idx_update_time` (`update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户申请表';
DROP TABLE IF EXISTS `user_friend`;
CREATE TABLE `user_friend` (
                               `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                               `uid` bigint(20) NOT NULL COMMENT 'uid',
                               `friend_uid` bigint(20) NOT NULL COMMENT '好友uid',
                               `delete_status` int(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(0-正常,1-删除)',
                               `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                               `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                               PRIMARY KEY (`id`) USING BTREE,
                               KEY `idx_uid_friend_uid` (`uid`,`friend_uid`) USING BTREE,
                               KEY `idx_create_time` (`create_time`) USING BTREE,
                               KEY `idx_update_time` (`update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户联系人表';



DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
                            `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
                            `room_id` bigint(20) NOT NULL COMMENT '会话表id',
                            `from_uid` bigint(20) NOT NULL COMMENT '消息发送者uid',
                            `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息内容',
                            `reply_msg_id` bigint(20) NULL DEFAULT NULL COMMENT '回复的消息内容',
                            `status` int(11) NOT NULL COMMENT '消息状态 0正常 1删除',
                            `gap_count` int(11) NULL DEFAULT NULL COMMENT '与回复的消息间隔多少条',
                            `type` int(11) NULL DEFAULT 1 COMMENT '消息类型 1正常文本 2.撤回消息',
                            `extra` json DEFAULT NULL COMMENT '扩展信息',
                            `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                            `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `idx_room_id`(`room_id`) USING BTREE,
                            INDEX `idx_from_uid`(`from_uid`) USING BTREE,
                            INDEX `idx_create_time`(`create_time`) USING BTREE,
                            INDEX `idx_update_time`(`update_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '消息表' ROW_FORMAT = Dynamic;



DROP TABLE IF EXISTS `message_mark`;
CREATE TABLE `message_mark`  (
                                 `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
                                 `msg_id` bigint(20) NOT NULL COMMENT '消息表id',
                                 `uid` bigint(20) NOT NULL COMMENT '标记人uid',
                                 `type` int(11) NOT NULL COMMENT '标记类型 1点赞 2举报',
                                 `status` int(11) NOT NULL COMMENT '消息状态 0正常 1取消',
                                 `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                                 `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 INDEX `idx_msg_id`(`msg_id`) USING BTREE,
                                 INDEX `idx_uid`(`uid`) USING BTREE,
                                 INDEX `idx_create_time`(`create_time`) USING BTREE,
                                 INDEX `idx_update_time`(`update_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '消息标记表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `room`;
CREATE TABLE `room` (
                        `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                        `type` int(11) NOT NULL COMMENT '房间类型 1群聊 2单聊',
                        `hot_flag` int(11) DEFAULT '0' COMMENT '是否全员展示 0否 1是',
                        `active_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '群最后消息的更新时间（热点群不需要写扩散，只更新这里）',
                        `last_msg_id` bigint(20) DEFAULT NULL COMMENT '会话中的最后一条消息id',
                        `ext_json` json DEFAULT NULL COMMENT '额外信息（根据不同类型房间有不同存储的东西）',
                        `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                        `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                        PRIMARY KEY (`id`) USING BTREE,
                        KEY `idx_create_time` (`create_time`) USING BTREE,
                        KEY `idx_update_time` (`update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房间表';
DROP TABLE IF EXISTS `room_friend`;
CREATE TABLE `room_friend` (
                               `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                               `room_id` bigint(20) NOT NULL COMMENT '房间id',
                               `uid1` bigint(20) NOT NULL COMMENT 'uid1（更小的uid）',
                               `uid2` bigint(20) NOT NULL COMMENT 'uid2（更大的uid）',
                               `room_key` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '房间key由两个uid拼接，先做排序uid1_uid2',
                               `status` int(11) NOT NULL COMMENT '房间状态 0正常 1禁用(删好友了禁用)',
                               `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                               `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                               PRIMARY KEY (`id`) USING BTREE,
                               UNIQUE KEY `room_key` (`room_key`) USING BTREE,
                               KEY `idx_room_id` (`room_id`) USING BTREE,
                               KEY `idx_create_time` (`create_time`) USING BTREE,
                               KEY `idx_update_time` (`update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='单聊房间表';
DROP TABLE IF EXISTS `room_group`;
CREATE TABLE `room_group` (
                              `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                              `room_id` bigint(20) NOT NULL COMMENT '房间id',
                              `name` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '群名称',
                              `avatar` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '群头像',
                              `ext_json` json DEFAULT NULL COMMENT '额外信息（根据不同类型房间有不同存储的东西）',
                              `delete_status` int(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(0-正常,1-删除)',
                              `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                              `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                              PRIMARY KEY (`id`) USING BTREE,
                              KEY `idx_room_id` (`room_id`) USING BTREE,
                              KEY `idx_create_time` (`create_time`) USING BTREE,
                              KEY `idx_update_time` (`update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='群聊房间表';
DROP TABLE IF EXISTS `group_member`;
CREATE TABLE `group_member` (
                                `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                                `group_id` bigint(20) NOT NULL COMMENT '群组id',
                                `uid` bigint(20) NOT NULL COMMENT '成员uid',
                                `role` int(11) NOT NULL COMMENT '成员角色 1群主 2管理员 3普通成员',
                                `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                                `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                                PRIMARY KEY (`id`) USING BTREE,
                                KEY `idx_group_id_role` (`group_id`,`role`) USING BTREE,
                                KEY `idx_create_time` (`create_time`) USING BTREE,
                                KEY `idx_update_time` (`update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='群成员表';
DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact` (
                           `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                           `uid` bigint(20) NOT NULL COMMENT 'uid',
                           `room_id` bigint(20) NOT NULL COMMENT '房间id',
                           `read_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '阅读到的时间',
                           `active_time` datetime(3) DEFAULT NULL COMMENT '会话内消息最后更新的时间(只有普通会话需要维护，全员会话不需要维护)',
                           `last_msg_id` bigint(20) DEFAULT NULL COMMENT '会话最新消息id',
                           `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                           `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
                           PRIMARY KEY (`id`) USING BTREE,
                           UNIQUE KEY `uniq_uid_room_id` (`uid`,`room_id`) USING BTREE,
                           KEY `idx_room_id_read_time` (`room_id`,`read_time`) USING BTREE,
                           KEY `idx_create_time` (`create_time`) USING BTREE,
                           KEY `idx_update_time` (`update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会话列表';

insert INTO `room`(`id`,`type`,`hot_flag`) values (1,1,1);
insert INTO `room_group`(`id`,`room_id`,`name`,`avatar`) values (1,1,'全员群','');