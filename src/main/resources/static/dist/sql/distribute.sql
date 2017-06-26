/*
Navicat MySQL Data Transfer

Source Server         : dev
Source Server Version : 50616
Source Host           : rdsw9r566jxer973wqi2.mysql.rds.aliyuncs.com:3306
Source Database       : bd

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2017-06-21 10:22:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `bd_contract_param_distribute`
-- ----------------------------
DROP TABLE IF EXISTS `bd_contract_param_distribute`;
CREATE TABLE `bd_contract_param_distribute` (
`id`  int(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
`param_id`  int(11) NULL DEFAULT NULL ,
`name_mapping`  varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL ,
`system_id`  int(11) NULL DEFAULT 1 COMMENT '1对账' ,
`desc`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci
AUTO_INCREMENT=1505

;

-- ----------------------------
-- Records of bd_contract_param_distribute
-- ----------------------------
BEGIN;
INSERT INTO `bd_contract_param_distribute` VALUES ('1', '923', 'youfu_billing_cycle', '1', '优付结算周期'), ('2', '882', 'youfu_poundage', '1', '优付手续费'), ('3', '902', 'youfu_payment_day', '1', '优付日结天数'), ('4', '924', 'youfu_start_time', '1', '优付生效开始日期'), ('5', '922', 'youfu_start_price', '1', '优付起始金额'), ('6', '1182', 'group_buying_commission', '1', '团购佣金'), ('7', '1203', 'group_buying_start_time', '1', '团购佣金生效开始时间');
COMMIT;

-- ----------------------------
-- Auto increment value for `bd_contract_param_distribute`
-- ----------------------------
ALTER TABLE `bd_contract_param_distribute` AUTO_INCREMENT=1505;
