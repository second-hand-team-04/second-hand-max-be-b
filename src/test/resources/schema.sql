DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS region;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS user_region;

CREATE TABLE category (
    id	        bigint	        NOT NULL AUTO_INCREMENT,
    title	    varchar(20)	    NOT NULL,
    image_url   varchar(300)    NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE region(
    id      bigint      NOT NULL AUTO_INCREMENT,
    title   varchar(30) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE `user`(
    id         bigint       NOT NULL AUTO_INCREMENT,
    login_id   varchar(20)  NOT NULL,
    nickname   varchar(20)  NOT NULL,
    email      varchar(40)  NOT NULL,
    password   varchar(150) NOT NULL,
    profile    varchar(300) NOT NULL,
    created_at datetime     NOT NULL DEFAULT now(),
    PRIMARY KEY(id)
);

CREATE TABLE user_region(
    id        bigint NOT NULL AUTO_INCREMENT,
    user_id   bigint NOT NULL,
    region_id bigint NOT NULL,
    PRIMARY KEY(id)
);
