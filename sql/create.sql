DROP DATABASE IF EXISTS chat;
CREATE DATABASE chat;
USE chat;

DROP TABLE IF EXISTS user;
create table user
(
    id              bigint auto_increment comment '用户id',
    uid             bigint                              not null comment '用户账号标识（类似qq号）',
    nickname        varchar(255)                        null comment '用户昵称',
    avatar          varchar(255)                        null comment '用户头像',
    sex             int                                 null comment '用户性别 0-未知 1-男 2-女',
    openid          varchar(255)                        not null comment '平台中的用户标识',
    user_type       int                                 null comment '用户类别 1-微信  2-测试 ...',
    active_status   int       default 2                 null comment '用户在线状态 1-在线 2-离线',
    ip_info         varchar(255)                        null comment '用户ip信息',
    last_login_time datetime  default CURRENT_TIMESTAMP not null comment '用户上次登录时间',
    status          tinyint   default 0                 not null comment '账号状态 0-正常 1-异常',
    create_time     timestamp default CURRENT_TIMESTAMP null comment '记录创建时间',
    update_time     timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '记录最近更新时间',
    is_delete       tinyint   default 0                 not null comment '逻辑删除 0-正常 1-删除',
    constraint user_pk
        primary key (id)
)
    comment 'chat-room用户表';

create index idx_nickname
    on user (nickname)
    comment '昵称索引';

create index idx_uid
    on user (uid)
    comment 'uid索引';

create index idx_openid
    on user (openid)
    comment 'openid索引';

alter table user
    add constraint user_pk2
        unique (uid) comment 'uid索引';