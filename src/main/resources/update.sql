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
