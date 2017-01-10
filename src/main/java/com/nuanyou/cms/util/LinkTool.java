package com.nuanyou.cms.util;

import com.nuanyou.cms.config.SystemContext;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Felix on 2016/10/28.
 */
@Service
public class LinkTool  extends org.apache.velocity.tools.generic.LinkTool{


    public String getContextPath() {
        HttpServletRequest request=SystemContext.getRequest();
        return request.getContextPath();
    }


}
