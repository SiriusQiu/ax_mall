drop database if exists ax;
drop user if exists 'ax'@'localhost';
-- 支持emoji：需要mysql数据库参数： character_set_server=utf8mb4
create database ax default character set utf8mb4 collate utf8mb4_unicode_ci;
use ax;
create user 'ax'@'localhost' identified by 'ax123456';
grant all privileges on ax.* to 'ax'@'localhost';
flush privileges;