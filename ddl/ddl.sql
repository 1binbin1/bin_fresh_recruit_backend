create database if not exists `db_bin_fresh_job`;
use db_bin_fresh_job;

DROP TABLE IF EXISTS t_account;
CREATE TABLE t_account
(
    id          INT                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    a_id        VARCHAR(16)  default ''                NOT NULL COMMENT '账号ID',
    a_phone     varchar(16)  default ''                not null comment '手机号',
    a_password  VARCHAR(32)  default ''                NOT NULL COMMENT '密码',
    a_role      INT          default 0                 NOT NULL COMMENT '角色，0-管理员，1-应届生，2-企业',
    a_avatar    VARCHAR(256) default ''                NOT NULL COMMENT '头像',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint      default 0                 not null comment '是否删除，0-否，1-是',
    PRIMARY KEY (id),
    index index_a_id (a_id)
) COMMENT = '账号信息表';


DROP TABLE IF EXISTS t_fresh_resume;
CREATE TABLE t_fresh_resume
(
    id             INT                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    user_id        VARCHAR(16)  default ''                NOT NULL COMMENT '应届生id',
    resume_id      VARCHAR(16)  default ''                NOT NULL COMMENT '简历id',
    user_name_link VARCHAR(256) default ''                NOT NULL COMMENT '简历附件',
    create_time    datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete      tinyint      default 0                 not null comment '是否删除，0-否，1-是',
    PRIMARY KEY (id),
    index index_user_id (user_id)
) COMMENT = '应届生附件简历表';

DROP TABLE IF EXISTS t_fresh_user_info;
CREATE TABLE t_fresh_user_info
(
    id             INT                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    user_id        VARCHAR(16)  default ''                NOT NULL COMMENT '用户ID',
    user_name      VARCHAR(16)  default ''                NOT NULL COMMENT '用户姓名',
    user_sex       INT          default 0                 NOT NULL COMMENT '性别 0-男 1-女',
    user_phone     VARCHAR(16)  default ''                NOT NULL COMMENT '用户手机号',
    user_email     VARCHAR(32)  default ''                NOT NULL COMMENT '用户邮箱',
    user_school    VARCHAR(64)  default ''                NOT NULL COMMENT '毕业学校',
    user_major     VARCHAR(128) default ''                NOT NULL COMMENT '专业',
    user_year      VARCHAR(4)   default ''                NOT NULL COMMENT '毕业年份',
    user_education VARCHAR(64)  default ''                NOT NULL COMMENT '学历',
    create_time    datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete      tinyint      default 0                 not null comment '是否删除，0-否，1-是',
    PRIMARY KEY (id),
    index index_user_id (user_id)
) COMMENT = '应届生个人信息表';

DROP TABLE IF EXISTS t_company_info;
CREATE TABLE t_company_info
(
    id            INT                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    com_id        VARCHAR(16)  default ''                NOT NULL COMMENT '企业ID',
    com_phone     VARCHAR(16)  default ''                NOT NULL COMMENT '企业手机号，用于登录',
    com_name      VARCHAR(128) default ''                NOT NULL COMMENT '企业名称',
    com_intro     VARCHAR(255) default ''                NOT NULL COMMENT '企业介绍',
    com_address   VARCHAR(128) default ''                NOT NULL COMMENT '企业地址',
    com_num       VARCHAR(32)  default ''                NOT NULL COMMENT '企业人数',
    com_type      VARCHAR(32)  default ''                NOT NULL COMMENT '企业类型',
    com_work_time VARCHAR(32)  default ''                NOT NULL COMMENT '企业工作时间',
    com_set_time  VARCHAR(32)  default ''                NOT NULL COMMENT '企业成立时间',
    com_welfare   VARCHAR(255) default ''                NOT NULL COMMENT '企业福利',
    create_time   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint      default 0                 not null comment '是否删除，0-否，1-是',
    PRIMARY KEY (id),
    index index_com_id (com_id)
) COMMENT = '企业信息表';

DROP TABLE IF EXISTS t_fresh_com_send;
CREATE TABLE t_fresh_com_send
(
    id          INT                                   NOT NULL AUTO_INCREMENT COMMENT 'id',
    user_id     VARCHAR(16) default ''                NOT NULL COMMENT '应届生ID',
    com_id      VARCHAR(16) default ''                NOT NULL COMMENT '企业ID',
    job_id      VARCHAR(16) default ''                NOT NULL COMMENT '岗位ID',
    resume_id   VARCHAR(16) default ''                NOT NULL COMMENT '简历ID',
    send_time   DATETIME    default ''                NOT NULL COMMENT '投递时间',
    send_state  INT         default 0                 NOT NULL COMMENT '投递状态 0-已投递 1-被查看 2-邀约面试 3-初筛不通过 4-流程结束',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint     default 0                 not null comment '是否删除，0-否，1-是',
    PRIMARY KEY (id),
    index index_user_id_com_id_job_id_resume_id (user_id, com_id, job_id, resume_id)
) COMMENT = '应届生投递记录表';

DROP TABLE IF EXISTS t_dict;
CREATE TABLE t_dict
(
    id           INT                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    dict_content VARCHAR(255) default ''                NOT NULL COMMENT '字典内容',
    dict_type    INT          default 0                 NOT NULL COMMENT '字典类型 0-岗位类别 1-薪资范围 2-主要城市 3-岗位名称',
    create_time  datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete    tinyint      default 0                 not null comment '是否删除，0-否，1-是',
    PRIMARY KEY (id)
) COMMENT = '数据字典';

DROP TABLE IF EXISTS t_chat;
CREATE TABLE t_chat
(
    id           INT                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    user_id      VARCHAR(16)  default ''                NOT NULL COMMENT '应届生ID',
    com_id       VARCHAR(16)  default ''                NOT NULL COMMENT '企业ID',
    chat_id      VARCHAR(16)  default ''                NOT NULL COMMENT '聊天ID',
    chat_content VARCHAR(255) default ''                NOT NULL COMMENT '聊天内容',
    create_time  datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete    tinyint      default 0                 not null comment '是否删除，0-否，1-是',
    PRIMARY KEY (id),
    index index_user_id_com_id (user_id, com_id),
    index index_user_id_com_id_chat_id (user_id, com_id, chat_id)
) COMMENT = '聊天记录表';

DROP TABLE IF EXISTS t_job_purpose;
CREATE TABLE t_job_purpose
(
    id          INT                                   NOT NULL AUTO_INCREMENT COMMENT 'id',
    user_id     VARCHAR(16) default ''                NOT NULL COMMENT '应届生ID',
    city        VARCHAR(32) default ''                NOT NULL COMMENT '意向城市',
    job_type    VARCHAR(32) default ''                NOT NULL COMMENT '意向工作类别',
    job_pay     VARCHAR(32) default ''                NOT NULL COMMENT '意向薪资',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint     default 0                 not null comment '是否删除，0-否，1-是',
    PRIMARY KEY (id),
    index index_user_id (user_id)
) COMMENT = '应届生岗位意向表';

DROP TABLE IF EXISTS t_job_info;
CREATE TABLE t_job_info
(
    id          INT                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    com_id      VARCHAR(16)  default ''                NOT NULL COMMENT '企业ID',
    job_id      VARCHAR(16)  default ''                NOT NULL COMMENT '岗位ID',
    job_name    VARCHAR(32)  default ''                NOT NULL COMMENT '岗位名称',
    job_type    VARCHAR(32)  default ''                NOT NULL COMMENT '岗位类别',
    job_intro   VARCHAR(255) default ''                NOT NULL COMMENT '岗位介绍',
    job_require VARCHAR(255) default ''                NOT NULL COMMENT '岗位职责',
    job_pay     VARCHAR(32)  default ''                NOT NULL COMMENT '岗位薪资',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint      default 0                 not null comment '是否删除，0-否，1-是',
    PRIMARY KEY (id),
    index index_com_id_job_id (com_id, job_id)
) COMMENT = '岗位信息表';

DROP TABLE IF EXISTS t_school_intro;
CREATE TABLE t_school_intro
(
    id            INT                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    title         VARCHAR(32)  default ''                NOT NULL COMMENT '标题',
    intro_content VARCHAR(1024) default ''                NOT NULL COMMENT '资讯内容',
    create_time   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint      default 0                 not null comment '是否删除，0-否，1-是',
    PRIMARY KEY (id)
) COMMENT = '就业资讯信息表';

DROP TABLE IF EXISTS t_fresh_com_send;
CREATE TABLE t_fresh_com_send
(
    id          INT                                       NOT NULL AUTO_INCREMENT COMMENT 'id',
    user_id     VARCHAR(16) default ''                    NOT NULL COMMENT '应届生ID',
    com_id      VARCHAR(16) default ''                    NOT NULL COMMENT '企业ID',
    job_id      VARCHAR(16) default ''                    NOT NULL COMMENT '岗位ID',
    resume_id   VARCHAR(16) default ''                    NOT NULL COMMENT '简历id',
    send_time   timestamp   default '0000:00:00 00:00:00' not null comment '发送时间',
    send_state  tinyint     default 0                     not null comment '投递状态 0-已投递 1-被查看 2-邀约面试 3-初筛不通过 4-流程结束',
    create_time datetime    default CURRENT_TIMESTAMP     not null comment '创建时间',
    update_time datetime    default CURRENT_TIMESTAMP     not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint     default 0                     not null comment '是否删除，0-否，1-是',
    PRIMARY KEY (id),
    index index_ids (user_id, com_id, job_id, resume_id)
) COMMENT = '应届生投递记录表';