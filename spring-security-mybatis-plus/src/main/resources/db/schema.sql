create table user (
  id bigint not null auto_increment,
  username varchar(200) not null default '',
  password varchar(200) not null  default '',
  role varchar(200) not null default '',
  primary key (id)
);