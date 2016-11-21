-- 攻略表新增城市ID
ALTER TABLE `ny_guideline` ADD (cityid INT);

-- 3.0商户列表页门头图 增加排序
ALTER TABLE `ny_merchant_headimg_allsize` ADD (sort INT(10));

CREATE TABLE ny_discount (
    id INT(10) NOT NULL AUTO_INCREMENT,
    title VARCHAR(40) COMMENT '优惠标题',
    countryid INT(10) COMMENT '国家ID',
    cityid INT(10) COMMENT '城市ID',
    imgurl varchar(200) COMMENT '图片地址',
    type tinyint(4)  COMMENT '类型',
    itemid INT(10) COMMENT '商品ID',
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
    createTime DATETIME COMMENT '创建时间',
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