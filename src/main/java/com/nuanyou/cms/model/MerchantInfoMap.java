package com.nuanyou.cms.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by young on 2017/9/19.
 */
public class MerchantInfoMap {

    public static final Map businessDaysMap = new HashMap();

    public static final Map payTypesMap = new HashMap();

    public static final Map supportTypesMap = new HashMap();

    static {
        businessDaysMap.put("Monday","周一");
        businessDaysMap.put("Tuesday","周二");
        businessDaysMap.put("Wednesday","周三");
        businessDaysMap.put("Thursday","周四");
        businessDaysMap.put("Friday","周五");
        businessDaysMap.put("Saturday","周六");
        businessDaysMap.put("Sunday","周日");

        payTypesMap.put("","");

    }
}
