package com.nuanyou.cms.commons;

import com.nuanyou.cms.util.BeanUtils;

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

}