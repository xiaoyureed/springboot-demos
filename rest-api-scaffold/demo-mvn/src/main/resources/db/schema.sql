drop database if exists `dbtest`;
use `dbtest`;

drop table if exists `account`;
create table `account`
(
    `id`       int auto_increment primary key,
    `name`     varchar(100) not null default '',
    `password` varchar(100) not null default '123456'
);