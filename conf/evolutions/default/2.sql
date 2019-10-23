# --- !Ups

CREATE TABLE user (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    age bigint(10) NOT NULL,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE user;