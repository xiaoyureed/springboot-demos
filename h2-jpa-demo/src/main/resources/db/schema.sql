create table tbl_book(
  id bigint auto_increment primary key,
  name varchar(20) not null default '',
  price decimal not null default 0,
  user_id bigint not null default 0
);