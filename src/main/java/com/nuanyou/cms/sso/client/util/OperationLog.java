package com.nuanyou.cms.sso.client.util;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Felix on 2017/4/12.
 */
public class OperationLog {

    private static final Logger log = LoggerFactory.getLogger("OperationLog");

    public static void log(Long userId, String name, String uri, Action action, Object target) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(userId).append(",");
        sb.append(name).append(",");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sb.append(sdf.format(new Date())).append(",");
        sb.append(uri).append(",");
        sb.append(action).append(",");
        if(target!=null){
            sb.append(target.getClass().getCanonicalName()).append(",");
        }
        sb.append(ToStringBuilder.reflectionToString(target, ToStringStyle.SHORT_PREFIX_STYLE));
        sb.append("]");
        System.out.println("log in log:"+sb.toString());
        log.info(sb.toString());
    }

    public enum Action {
        Create("新增"),
        Update("更新"),
        Remove("删除"),
        Login("登陆"),
        Logout("登出");

        public final String name;

        Action(String name) {
            this.name = name;
        }
    }




}

