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
    ip_info         varchar(255)                        null comment '用户ip信息',
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
