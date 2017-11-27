package com.nuanyou.cms.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by young on 2017/6/22.
 */
public class OrderDetailLocalKeys {

    public static final String global_merchant_name = "global_merchant_name";

    public static final String global_order_detail = "global_order_detail";

    public static final String global_ordersn = "global_ordersn";

    public static final String global_product_amount = "global_product_amount";

    public static final String global_product_name = "global_product_name";

    public static final String global_price = "global_price";

    public static final String global_total_price = "global_total_price";

    public static final String global_tips = "global_tips";

    private static List<String> keys ;

    static {
        keys = new ArrayList<>();
        keys.add(global_merchant_name);
        keys.add(global_order_detail);
        keys.add(global_ordersn);
        keys.add(global_product_amount);
        keys.add(global_product_name);
        keys.add(global_price);
        keys.add(global_total_price);
        keys.add(global_tips);
    }

    public static List<String> getKeys() {
        return keys;
    }

    public static List<String> getOneKeys(String oneKey) {
        List<String> onekey = keys = new ArrayList<>();
        onekey.add(oneKey);
        return onekey;
    }
}
