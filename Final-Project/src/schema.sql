drop database if exists homeworknerd;

create database homeworknerd;

use homeworknerd;

create table user (
    user_id int not null auto_increment,
    username varchar(32) not null,
    email varchar(32) not null,
	password varchar(256) not null,
    enabled boolean,
    created_date date default (CURRENT_DATE()),

    primary key(user_id)
);

create table verification_token (
	token_id int not null auto_increment,
    user_id int not null,
    token varchar(32) not null,
    expiry_date datetime,

    primary key(token_id),
    constraint fk_user_id
    foreign key (user_id)
    references user(user_id)
);

create table refresh_token (
	rid int not null auto_increment,
    -- user_id int not null,
    token varchar(32) not null,
    created_date timestamp default (current_timestamp()),

    primary key(rid)
--     constraint fk_user_id
--     foreign key (user_id)
--     references user(user_id)
);

-- create table topic (
-- 	tid int not null auto_increment,
--     topic_name varchar(64) not null,
--     description varchar(128) not null,
--     user_id int not null,
--     post_id int not null,
--
--     primary key(tid),
--
--     constraint fk_user_id_1
--     foreign key (user_id)
--     references user(user_id)
-- );--

create table post (
	pid int not null auto_increment,
    post_name MEDIUMTEXT not null,
--     url varchar(64),
    description longtext,
--     comment_count int,
    tags varchar(128),
    poster_user_id int not null,
    p_created_date timestamp default (current_timestamp()),
    image_url varchar(128),

    primary key(pid),

    constraint fk_user_id_1
    foreign key (poster_user_id)
    references user(user_id)
);

create table comment (
	cid int not null auto_increment,
    text longtext not null,
    vote_count int,
    user_id int not null,
    post_id int not null,
    c_created_date date default (CURRENT_DATE()),

    primary key(cid),

    constraint fk_user_id_2
    foreign key (user_id)
    references user(user_id),

    constraint fk_post_id
    foreign key (post_id)
    references post(pid)
);

create table vote (
	vid int not null auto_increment,
    vote_type int not null,
    user_id int not null,
    comment_id int not null,

    primary key(vid),

    constraint fk_user_id_3
    foreign key (user_id)
    references user(user_id),

    constraint fk_comment_id
    foreign key (comment_id)
    references comment(cid)
);

create table favourite (
	record_id int not null auto_increment,
    fav_user_id int not null,
    post_id int not null,
    r_created_date datetime default (CURRENT_TIMESTAMP),

    primary key(record_id),

    constraint fk_user_id_4
    foreign key (fav_user_id)
    references user(user_id),

    constraint fk_post_id_1
    foreign key (post_id)
    references post(pid)
);


