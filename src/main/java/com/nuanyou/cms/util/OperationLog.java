package com.nuanyou.cms.util;

import com.nuanyou.cms.config.SystemContext;
import com.nuanyou.cms.sso.client.util.UserHolder;
import com.nuanyou.cms.sso.client.validation.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by alan on 17/4/5.
 */
public class OperationLog {

    private static final Logger log = LoggerFactory.getLogger("OperationLog");

    public static void log(String action, Object target) {
        HttpServletRequest request = SystemContext.getRequest();
        if (request == null)
            return;

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        User user = UserHolder.getUser();
        if (user != null) {
            sb.append(user.getUserid()).append(",");
            sb.append(user.getName()).append(",");
        } else {
            sb.append("null").append(",");
            sb.append("null").append(",");
        }
        sb.append(DateUtils.format(new Date())).append(",");
        sb.append(request.getRequestURI()).append(",");
        sb.append(action).append(",");
        sb.append(target.getClass().getCanonicalName()).append(",");
        sb.append(ToStringBuilder.reflectionToString(target, ToStringStyle.SHORT_PREFIX_STYLE));
        sb.append("]");
        log.info(sb.toString());
    }
}