use nuanyou20;
DROP TABLE IF EXISTS `ny_param_data_mapping`;
CREATE TABLE `ny_param_data_mapping` (
`id`  int(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
`name`  varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`regex`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '正则' ,
`data_type`  int(11) NULL DEFAULT NULL COMMENT '类型：1、文本，2、整型 ，3、浮点型，4、日期，5、备选数据' ,
`note`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=23

;

-- ----------------------------
-- Records of ny_param_data_mapping
-- ----------------------------
BEGIN;
INSERT INTO `ny_param_data_mapping` VALUES ('1', '1-99的数字', '[1-9][1-9]', '2', '请输入1-99的数字'), ('2', '>0的数字', '', '2', '请输入数字'), ('3', '邮箱', '^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$', '1', '请输入正确的邮箱'), ('4', '联系方式', null, '1', null), ('5', '日期', null, '4', null), ('22', '文本', null, '1', null);
COMMIT;

-- ----------------------------
-- Auto increment value for `ny_param_data_mapping`
-- ----------------------------
ALTER TABLE `ny_param_data_mapping` AUTO_INCREMENT=23;
