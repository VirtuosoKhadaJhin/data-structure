package com.nuanyou.cms.commons;

import com.nuanyou.cms.util.BeanUtils;
import com.nuanyou.cms.util.OperationLog;

import javax.persistence.*;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by Alan.ye on 2017/4/14.
 */
public class DateEntityListener {

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

    @PostPersist
    public void logForCreate(Object target) {
        OperationLog.log(OperationLog.Action.create, target);
    }

    @PostUpdate
    public void logForUpdate(Object target) {
        OperationLog.log(OperationLog.Action.update, target);
    }

    @PostRemove
    public void logForRemove(Object target) {
        OperationLog.log(OperationLog.Action.remove, target);
    }


}