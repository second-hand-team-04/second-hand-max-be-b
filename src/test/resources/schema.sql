DROP TABLE IF EXISTS category;

CREATE TABLE category (
    id	        bigint	        NOT NULL AUTO_INCREMENT,
    title	    varchar(20)	    NOT NULL,
    image_url   varchar(300)    NOT NULL,
    PRIMARY KEY(id)
);
