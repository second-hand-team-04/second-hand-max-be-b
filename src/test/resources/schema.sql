DROP TABLE IF EXISTS `item`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `user_region`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `region`;

CREATE TABLE `user`
(
    `id`         BIGINT AUTO_INCREMENT,
    `nickname`   VARCHAR(20) UNIQUE NOT NULL,
    `email`      VARCHAR(50),
    `password`   VARCHAR(200),
    `profile`    VARCHAR(300),
    `created_at` TIMESTAMP,
    PRIMARY KEY (`id`)
);

CREATE TABLE `category`
(
    `id`        BIGINT AUTO_INCREMENT,
    `title`     VARCHAR(20)  NOT NULL,
    `image_url` VARCHAR(300) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `region`
(
    `id`    BIGINT AUTO_INCREMENT,
    `title` VARCHAR(30) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `user_region`
(
    `id`        BIGINT AUTO_INCREMENT,
    `user_id`   BIGINT,
    `region_id` BIGINT,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    FOREIGN KEY (`region_id`) REFERENCES `region` (`id`)
);

CREATE TABLE `item`
(
    `id`          BIGINT AUTO_INCREMENT,
    `user_id`     BIGINT        NOT NULL,
    `category_id` BIGINT        NOT NULL,
    `region_id`   BIGINT        NOT NULL,
    `title`       VARCHAR(60)   NOT NULL,
    `content`     VARCHAR(4000) NOT NULL,
    `price`       INT,
    `status`      VARCHAR(20)   NOT NULL,
    `views`       INT           NOT NULL,
    `posted_at`   TIMESTAMP     NOT NULL,
    `updated_at`  TIMESTAMP     NOT NULL,
    `is_deleted`  BOOLEAN       NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
    FOREIGN KEY (`region_id`) REFERENCES `region` (`id`)
);
