package com.nuanyou.cms.util;

import org.apache.velocity.tools.generic.NumberTool;

import java.math.BigDecimal;

/**
 * Created by Felix on 2017/3/1.
 */
public class PriceUtil {

    private static String decimalPattern = "#0.00";
    public static String getFormatPrice(BigDecimal price) {
        if(price==null){
            return null;
        }
        NumberTool numberFormatter=new NumberTool();
        return numberFormatter.format(decimalPattern, price);
    }
}
