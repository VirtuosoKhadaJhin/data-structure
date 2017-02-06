package com.nuanyou.cms.commons;

import com.nuanyou.cms.util.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by xiejing on 2016/7/25.
 */
public class DateEntityListener {


    private static final Logger log = LoggerFactory.getLogger(DateEntityListener.class);

    @PrePersist
    public void touchForCreate(Object target) throws IllegalAccessException {
        BeanInfo beanInfo = BeanUtils.getBeanInfo(target.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method readMethod = propertyDescriptor.getReadMethod();

            if (readMethod.getAnnotation(CreatedAt.class) != null)
                BeanUtils.setValue(target, propertyDescriptor.getWriteMethod(), new Date());

            if (readMethod.getAnnotation(LastModified.class) != null)
                BeanUtils.setValue(target, propertyDescriptor.getWriteMethod(), new Date());
        }
    }

    @PreUpdate
    public void touchForUpdate(Object target) throws IllegalAccessException {
        BeanInfo beanInfo = BeanUtils.getBeanInfo(target.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod.getAnnotation(LastModified.class) != null)
                BeanUtils.setValue(target, propertyDescriptor.getWriteMethod(), new Date());
        }
    }

//    @PostPersist
//    public void logForCreate(Object target) {
//        log("create", target);
//    }
//
//    @PostUpdate
//    public void logForUpdate(Object target) {
//        log("update", target);
//    }
//
//    @PostRemove
//    public void logForRemove(Object target) {
//        log("remove", target);
//    }
//
//    private static void log(String action, Object target) {
//        HttpServletRequest request = SystemContext.getRequest();
//        if (request == null)
//            return;
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("[");
//        sb.append("null").append(",");
//        sb.append(DateUtils.format(new Date())).append(",");
//        sb.append(request.getRequestURI()).append(",");
//        sb.append(action).append(",");
//        sb.append(target.getClass().getCanonicalName()).append(",");
//        sb.append(JsonUtils.toJson(target));
//        sb.append("]");
//        log.info(sb.toString());
//    }

}