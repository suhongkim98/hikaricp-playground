create database demohikaridb default character set utf8 collate utf8_general_ci;

CREATE TABLE demohikaridb.board (
    board_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(128) NOT NULL
) DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
