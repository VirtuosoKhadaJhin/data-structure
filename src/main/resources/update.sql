use nuanyou20;

-- 2017-5-11
ALTER TABLE `ny_comment_order` ADD COLUMN `replytime` DATETIME COMMENT '回复时间' AFTER `imgs`;
ALTER TABLE `ny_comment_order` ADD COLUMN `deleted` TINYINT(1) DEFAULT 0 COMMENT '是否删除';

ALTER TABLE `ny_country` ADD COLUMN `url` VARCHAR(255) COMMENT 'URL';
ALTER TABLE `ny_feature` ADD COLUMN `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除';

-- 2017-4-14
ALTER TABLE `ny_feature` ADD COLUMN `cat` tinyint(4) DEFAULT 0 COMMENT '分类: 0.全部 1.美食 2.购物 3.娱乐' AFTER `type`;

ALTER TABLE `ny_feature` ADD COLUMN `sort` INT(10) DEFAULT 1 COMMENT '排序';
ALTER TABLE `ny_quick` ADD COLUMN `sort` INT(10) DEFAULT 1 COMMENT '排序';

-- 2017-3-22
ALTER TABLE `ny_item` MODIFY COLUMN `type` tinyint(4) UNSIGNED COMMENT '商品类型: 1.普通商品 2.团购商品 3.优惠券 4.自定义商品';
ALTER TABLE `ny_order` MODIFY COLUMN `ordertype` TINYINT(3) UNSIGNED COMMENT '订单类型: 1.惠购订单 2.优付订单 3.外卖订单 4.团购订单 5.POS收款 6.联盟订单 7.顺风购订单 8.代金券订单 9.直邮购订单 10.记账';
ALTER TABLE `ny_order` MODIFY COLUMN `orderstatus` INT(10) UNSIGNED COMMENT '订单状态: 0.已下单 1.已支付 2.已消费 3.已评价 5.支付中 4.已关闭 104.自动核销 105.商户核销 203.退款成功';
ALTER TABLE `ny_order` MODIFY COLUMN `newrefundstatus` INT(10) UNSIGNED COMMENT '退款状态: 201.退款中 202.退款失败 203.审核通过 204.退款完成';

-- 2017-3-21
ALTER TABLE `ny_merchant_card` ADD COLUMN `deleted` TINYINT(1) DEFAULT 0 COMMENT '是否删除';

-- 2017-3-13
ALTER TABLE `ny_merchant` ADD COLUMN `directmailremind` VARCHAR(1024) COMMENT '直邮提示';

-- 2017-2-21
ALTER TABLE `ny_item` ADD COLUMN `spec` VARCHAR(255) COMMENT '商品规格';

-- 2017-2-20
CREATE TABLE
ny_item_detail_img
(
    id INT(10) unsigned NOT NULL AUTO_INCREMENT,
    refer_id INT COMMENT '所属商品',
    img_url VARCHAR(200) COMMENT '图片URL',
    sort INT(10),
    create_time DATETIME COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_refer_id (refer_id)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO ny_item_detail_img (refer_id, img_url, sort, create_time)
select a.id, a.imgurl, '0', now()
from ny_item a
left join ny_item_detail_img b on a.id = b.refer_id
where b.id is null order by a.id;

-- 2016-12-01
ALTER TABLE `ny_merchant`
ADD COLUMN `verifytype` INT(4) DEFAULT 1 COMMENT '核销方式: 1用户核销,2商家核销' AFTER `businessday`;

-- 2016-11-28
ALTER TABLE `ny_recommend`
ADD COLUMN `display` INT(1) DEFAULT 1 COMMENT '是否显示' AFTER `sort`;

ALTER TABLE `ny_recommend_cat`
ADD COLUMN `display` INT(1) DEFAULT 1 COMMENT '是否显示' AFTER `sort`;

-- 2016-11-20
-- 攻略表新增城市ID
ALTER TABLE `ny_guideline` ADD (cityid INT);

-- 3.0商户列表页门头图 增加排序
ALTER TABLE `ny_merchant_headimg_allsize`
ADD COLUMN `sort` INT(10) NULL COMMENT '排序值' AFTER `size`;

ALTER TABLE `bd_merchant`
ADD COLUMN `subcatid` INT(10) NULL COMMENT '子分类ID' AFTER `catid`;

-- 优惠信息表
CREATE TABLE ny_discount (
    id INT(10) NOT NULL AUTO_INCREMENT,
    title VARCHAR(40) COMMENT '优惠标题',
    countryid INT(10) COMMENT '国家ID',
    cityid INT(10) COMMENT '城市ID',
    mchid INT(10) COMMENT '商户ID',
    imgurl varchar(200) COMMENT '图片地址',
    type tinyint(4)  COMMENT '类型',
    relatedid INT(10) COMMENT '关联ID',
    intro text COMMENT '说明',
    linkurl varchar(200) COMMENT '链接',
    starttime DATETIME COMMENT '开始时间',
    endtime DATETIME COMMENT '结束时间',
    deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除，1：已删除，0：未删除',
    createtime DATETIME COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 优惠券领取记录
CREATE TABLE ny_coupon_receive_record (
    id INT(10) NOT NULL AUTO_INCREMENT,
    title VARCHAR(40) COMMENT '优惠券标题',
    userid INT(10) COMMENT '用户ID',
    coupontemplateid INT(10) COMMENT '优惠券模板ID',
    couponid INT(10) COMMENT '优惠券ID',
    createtime DATETIME COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY `idx_userid` (`userid`),
    KEY `idx_coupontemplateid` (`coupontemplateid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 3.0优惠券模板增加 优惠券面值(本地)
ALTER TABLE `ny_coupon_template`
ADD COLUMN `localprice` DECIMAL NULL COMMENT '优惠券面值(本地)' AFTER `price`;

-- 3.0优惠券模板增加 使用起始限额(本地)
ALTER TABLE `ny_coupon_template`
ADD COLUMN `startlocalprice` DECIMAL NULL COMMENT '使用起始限额(本地)' AFTER `startPrice`;

-- 删除指定商户字段
ALTER TABLE `ny_coupon_template` DROP COLUMN `appointmerchant`
-- 3.0优惠券模板增加类型
ALTER TABLE `ny_coupon_template`
ADD COLUMN `type` tinyint(4) NULL COMMENT '类型(0:无，1:指定商家，2:通用)';

ALTER table ny_coupon_template add countryid int(10) COMMENT '国家ID';
ALTER table ny_coupon_template add merchants varchar(255) COMMENT '指定的商家ID';
ALTER table ny_coupon_template add code varchar(50) COMMENT '模板code';