-- Users schema

-- !Ups

CREATE TABLE people (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    age bigint(10) NOT NULL,
    PRIMARY KEY (id)
);

-- !Downs

DROP TABLE people;