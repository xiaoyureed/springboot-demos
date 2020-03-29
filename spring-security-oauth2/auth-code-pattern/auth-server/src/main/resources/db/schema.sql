-- -------------
-- oauth 相关
-- -------------

DROP TABLE IF EXISTS `oauth_access_token`;
DROP TABLE IF EXISTS `oauth_approvals`;
DROP TABLE IF EXISTS `oauth_client_details`;
DROP TABLE IF EXISTS `oauth_client_token`;
DROP TABLE IF EXISTS `oauth_refresh_token`;

CREATE TABLE `clientdetails`
(
  `appId`                  varchar(128) NOT NULL,
  `resourceIds`            varchar(256)  DEFAULT NULL,
  `appSecret`              varchar(256)  DEFAULT NULL,
  `scope`                  varchar(256)  DEFAULT NULL,
  `grantTypes`             varchar(256)  DEFAULT NULL,
  `redirectUrl`            varchar(256)  DEFAULT NULL,
  `authorities`            varchar(256)  DEFAULT NULL,
  `access_token_validity`  int(11)       DEFAULT NULL,
  `refresh_token_validity` int(11)       DEFAULT NULL,
  `additionalInformation`  varchar(4096) DEFAULT NULL,
  `autoApproveScopes`      varchar(256)  DEFAULT NULL,
  PRIMARY KEY (`appId`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE `oauth_access_token`
(
  `token_id`          varchar(256) DEFAULT NULL,
  `token`             blob,
  `authentication_id` varchar(128) NOT NULL,
  `user_name`         varchar(256) DEFAULT NULL,
  `client_id`         varchar(256) DEFAULT NULL,
  `authentication`    blob,
  `refresh_token`     varchar(256) DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `oauth_approvals`
(
  `userId`         varchar(256) DEFAULT NULL,
  `clientId`       varchar(256) DEFAULT NULL,
  `scope`          varchar(256) DEFAULT NULL,
  `status`         varchar(10)  DEFAULT NULL,
  `expiresAt`      datetime     DEFAULT NULL,
  `lastModifiedAt` datetime     DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `oauth_client_details`
(
  `client_id`               varchar(128) NOT NULL,
  `resource_ids`            varchar(256)  DEFAULT NULL,
  `client_secret`           varchar(256)  DEFAULT NULL,
  `scope`                   varchar(256)  DEFAULT NULL,
  `authorized_grant_types`  varchar(256)  DEFAULT NULL,
  `web_server_redirect_uri` varchar(256)  DEFAULT NULL,
  `authorities`             varchar(256)  DEFAULT NULL,
  `access_token_validity`   int(11)       DEFAULT NULL,
  `refresh_token_validity`  int(11)       DEFAULT NULL,
  `additional_information`  varchar(4096) DEFAULT NULL,
  `autoapprove`             varchar(256)  DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `oauth_client_token`
(
  `token_id`          varchar(256) DEFAULT NULL,
  `token`             blob,
  `authentication_id` varchar(128) NOT NULL,
  `user_name`         varchar(256) DEFAULT NULL,
  `client_id`         varchar(256) DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code`
(
  `code`           varchar(256) DEFAULT NULL,
  `authentication` blob
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `oauth_refresh_token`
(
  `token_id`       varchar(256) DEFAULT NULL,
  `token`          blob,
  `authentication` blob
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------
-- 用户、权限、角色用到的表如下
-- --------------------

DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `role`;
DROP TABLE IF EXISTS `user_role`;
DROP TABLE IF EXISTS `role_permission`;
DROP TABLE IF EXISTS `permission`;

CREATE TABLE `user`
(
  `id`       bigint(11)   NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);
CREATE TABLE `role`
(
  `id`   bigint(11)   NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);
CREATE TABLE `user_role`
(
  `user_id` bigint(11) NOT NULL,
  `role_id` bigint(11) NOT NULL
);
CREATE TABLE `role_permission`
(
  `role_id`       bigint(11) NOT NULL,
  `permission_id` bigint(11) NOT NULL
);
CREATE TABLE `permission`
(
  `id`          bigint(11)   NOT NULL AUTO_INCREMENT,
  `url`         varchar(255) NOT NULL,
  `name`        varchar(255) NOT NULL,
  `description` varchar(255) NULL,
  `pid`         bigint(11)   NOT NULL,
  PRIMARY KEY (`id`)
);
