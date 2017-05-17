DROP VIEW IF EXISTS `view_export_order`;
CREATE ALGORITHM=UNDEFINED DEFINER=`nuanyou20`@`%` SQL SECURITY DEFINER VIEW `view_export_order` AS select `nuanyou20`.`ny_merchant`.`name` AS `merchantname`,`nuanyou20`.`ny_merchant`.`kpname` AS `merchantkpname`,`nuanyou20`.`ny_order_logistics`.`address` AS `address`,`nuanyou20`.`ny_order_logistics`.`postalcode` AS `postalcode`,`nuanyou20`.`ny_order_logistics`.`username` AS `username`,`nuanyou20`.`ny_order_logistics`.`province` AS `province`,`nuanyou20`.`ny_order_logistics`.`district` AS `district`,`nuanyou20`.`ny_order_logistics`.`city` AS `city`,`nuanyou20`.`ny_order_logistics`.`tel` AS `tel`,`pas_user_profile`.`nickname` AS `nickname`,`nuanyou20`.`ny_order_directmail`.`postage` AS `postage`,`nuanyou20`.`ny_order_directmail`.`postagermb` AS `postagermb`,`nuanyou20`.`ny_coupon`.`title` AS `coupontitle`,`nuanyou20`.`ny_order`.`id` AS `id`,`nuanyou20`.`ny_order`.`ordersn` AS `ordersn`,`nuanyou20`.`ny_order`.`ordercode` AS `ordercode`,`nuanyou20`.`ny_order`.`ordertype` AS `ordertype`,`nuanyou20`.`ny_order`.`transactionid` AS `transactionid`,`nuanyou20`.`ny_order`.`paytype` AS `paytype`,`nuanyou20`.`ny_order`.`mchid` AS `mchid`,`nuanyou20`.`ny_order`.`userid` AS `userid`,`nuanyou20`.`ny_order`.`couponid` AS `couponid`,`nuanyou20`.`ny_order`.`total` AS `total`,`nuanyou20`.`ny_order`.`price` AS `price`,`nuanyou20`.`ny_order`.`kpprice` AS `kpprice`,`nuanyou20`.`ny_order`.`status` AS `status`,`nuanyou20`.`ny_order`.`statusname` AS `statusname`,`nuanyou20`.`ny_order`.`statustime` AS `statustime`,`nuanyou20`.`ny_order`.`paytime` AS `paytime`,`nuanyou20`.`ny_order`.`confirmtime` AS `confirmtime`,`nuanyou20`.`ny_order`.`usetime` AS `usetime`,`nuanyou20`.`ny_order`.`commenttime` AS `commenttime`,`nuanyou20`.`ny_order`.`oprice` AS `oprice`,`nuanyou20`.`ny_order`.`okpprice` AS `okpprice`,`nuanyou20`.`ny_order`.`platform` AS `platform`,`nuanyou20`.`ny_order`.`OS` AS `OS`,`nuanyou20`.`ny_order`.`merchantsubsidy` AS `merchantsubsidy`,`nuanyou20`.`ny_order`.`paystatus` AS `paystatus`,`nuanyou20`.`ny_order`.`refundstatus` AS `refundstatus`,`nuanyou20`.`ny_order`.`refundtime` AS `refundtime`,`nuanyou20`.`ny_order`.`refundaudittime` AS `refundaudittime`,`nuanyou20`.`ny_order`.`refundreason` AS `refundreason`,`nuanyou20`.`ny_order`.`refundremark` AS `refundremark`,`nuanyou20`.`ny_order`.`refundsource` AS `refundsource`,`nuanyou20`.`ny_order`.`createtime` AS `createtime`,`nuanyou20`.`ny_order`.`iscode` AS `iscode`,`nuanyou20`.`ny_order`.`youfuReduce` AS `youfuReduce`,`nuanyou20`.`ny_order`.`mchReduce` AS `mchReduce`,`nuanyou20`.`ny_order`.`couponprice` AS `couponprice`,`nuanyou20`.`ny_order`.`poundage` AS `poundage`,`nuanyou20`.`ny_order`.`rate` AS `rate`,`nuanyou20`.`ny_order`.`checkingtime` AS `checkingtime`,`nuanyou20`.`ny_order`.`ischecking` AS `ischecking`,`nuanyou20`.`ny_order`.`passageway` AS `passageway`,`nuanyou20`.`ny_order`.`merchantprice` AS `merchantprice`,`nuanyou20`.`ny_order`.`mchrmbprice` AS `mchrmbprice`,`nuanyou20`.`ny_order`.`payable` AS `payable`,`nuanyou20`.`ny_order`.`message` AS `message`,`nuanyou20`.`ny_order`.`flightid` AS `flightid`,`nuanyou20`.`ny_order`.`templateid` AS `templateid`,`nuanyou20`.`ny_order`.`source` AS `source`,`nuanyou20`.`ny_order`.`orderstatus` AS `orderstatus`,`nuanyou20`.`ny_order`.`newrefundstatus` AS `newrefundstatus`,`nuanyou20`.`ny_order`.`youfulocalereduce` AS `youfulocalereduce`,`nuanyou20`.`ny_order`.`mchlocalereduce` AS `mchlocalereduce`,`nuanyou20`.`ny_order`.`appointstatus` AS `appointstatus`,`nuanyou20`.`ny_order`.`isdelete` AS `isdelete`,`nuanyou20`.`ny_order`.`discoutcardid` AS `discoutcardid`,`nuanyou20`.`ny_order`.`voucherprice` AS `voucherprice`,`nuanyou20`.`ny_order`.`itemprice` AS `itemprice`,`nuanyou20`.`ny_order`.`itemlocalprice` AS `itemlocalprice`,`nuanyou20`.`ny_order`.`youfurmbreduce` AS `youfurmbreduce`,`nuanyou20`.`ny_order`.`mchrmbreduce` AS `mchrmbreduce`,`nuanyou20`.`ny_order`.`sceneid` AS `sceneid`,`nuanyou20`.`ny_order`.`tempsceneid` AS `tempsceneid`,`nuanyou20`.`ny_order`.`scenetime` AS `scenetime`,`nuanyou20`.`ny_order`.`tempscenetime` AS `tempscenetime`,`nuanyou20`.`ny_order`.`orderstatusname` AS `orderstatusname`,`nuanyou20`.`ny_order`.`sourceorderid` AS `sourceorderid`,`nuanyou20`.`ny_order`.`yhrate` AS `yhrate`,`nuanyou20`.`ny_order`.`verifycode` AS `verifycode`,`nuanyou20`.`ny_order`.`couponlocalprice` AS `couponlocalprice`,`nuanyou20`.`ny_order`.`realrate` AS `realrate`,`nuanyou20`.`ny_order`.`countryid` AS `countryid` from (((((`nuanyou20`.`ny_order` left join `nuanyou20`.`ny_order_logistics` on((`nuanyou20`.`ny_order_logistics`.`orderid` = `nuanyou20`.`ny_order`.`id`))) left join `nuanyou20`.`pas_user_profile` on((`pas_user_profile`.`userid` = `nuanyou20`.`ny_order`.`userid`))) left join `nuanyou20`.`ny_order_directmail` on((`nuanyou20`.`ny_order_directmail`.`orderid` = `nuanyou20`.`ny_order`.`id`))) left join `nuanyou20`.`ny_coupon` on((`nuanyou20`.`ny_coupon`.`id` = `nuanyou20`.`ny_order`.`couponid`))) join `nuanyou20`.`ny_merchant` on((`nuanyou20`.`ny_merchant`.`id` = `nuanyou20`.`ny_order`.`mchid`))) ;
