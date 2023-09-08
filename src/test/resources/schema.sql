DROP TABLE IF EXISTS `item_image`;
DROP TABLE IF EXISTS `wishlist`;
DROP TABLE IF EXISTS `item`;
DROP TABLE IF EXISTS `status`;
DROP TABLE IF EXISTS `user_region`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `refresh_token`;
DROP TABLE IF EXISTS `region`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `image`;
DROP TABLE IF EXISTS `provider`;

CREATE TABLE `image`
(
    `id`        BIGINT AUTO_INCREMENT,
    `image_url` VARCHAR(300) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `provider`
(
    `id`        BIGINT AUTO_INCREMENT,
    `type`      VARCHAR(30) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `user`
(
    `id`            BIGINT             NOT NULL AUTO_INCREMENT,
    `provider_id`   BIGINT             NOT NULL,
    `image_id`      BIGINT,
    `email`         VARCHAR(40)        NOT NULL,
    `nickname`      VARCHAR(20) UNIQUE NOT NULL,
    `password`      VARCHAR(150),
    `created_at`    TIMESTAMP          NOT NULL DEFAULT now(),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`provider_id`) REFERENCES `provider` (`id`),
    FOREIGN KEY (`image_id`)    REFERENCES `image` (`id`)
);

CREATE TABLE `category`
(
    `id`        BIGINT       NOT NULL AUTO_INCREMENT,
    `title`     VARCHAR(20)  NOT NULL,
    `image_url` VARCHAR(300) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `region`
(
    `id`    BIGINT      NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(30) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `user_region`
(
    `id`        BIGINT NOT NULL AUTO_INCREMENT,
    `user_id`   BIGINT NOT NULL,
    `region_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    FOREIGN KEY (`region_id`) REFERENCES `region` (`id`)
);

CREATE TABLE `status`
(
    `id`   BIGINT AUTO_INCREMENT,
    `type` VARCHAR(20),
    PRIMARY KEY (`id`)
);

CREATE TABLE `item`
(
    `id`          BIGINT AUTO_INCREMENT,
    `user_id`     BIGINT      NOT NULL,
    `category_id` BIGINT      NOT NULL,
    `region_id`   BIGINT      NOT NULL,
    `status_id`   BIGINT      NOT NULL,
    `title`       VARCHAR(60) NOT NULL,
    `content`     VARCHAR(4000),
    `price`       INT,
    `views`       INT         NOT NULL DEFAULT 0,
    `created_at`  TIMESTAMP   NOT NULL DEFAULT now(),
    `updated_at`  TIMESTAMP   NOT NULL DEFAULT now(),
    `is_deleted`  BOOLEAN              DEFAULT 0,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
    FOREIGN KEY (`region_id`) REFERENCES `region` (`id`)
);

CREATE TABLE `wishlist`
(
    `id`      BIGINT AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `item_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    FOREIGN KEY (`item_id`) REFERENCES `item` (`id`)
);

CREATE TABLE `item_image`
(
    `id`         BIGINT    AUTO_INCREMENT,
    `item_id`    BIGINT    NOT NULL,
    `image_id`   BIGINT    NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`image_id`) REFERENCES `image` (`id`),
    FOREIGN KEY (`item_id`) REFERENCES `item` (`id`)
);

CREATE TABLE `refresh_token`
(
    `id`      BIGINT       AUTO_INCREMENT,
    `user_id` BIGINT       NOT NULL,
    `token`   VARCHAR(300) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);
