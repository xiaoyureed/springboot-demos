create table `demo` (
    `id` bigint auto_increment primary key comment 'primary key',
    `name` varchar(50) not null default '' comment 'name',
    `salary` decimal not null default 0 comment 'salary'
);