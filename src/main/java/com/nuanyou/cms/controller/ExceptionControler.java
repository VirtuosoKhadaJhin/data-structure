package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Alan.ye on 2016/9/7.
 */
@ControllerAdvice
public class ExceptionControler {

    private static final Logger log = LoggerFactory.getLogger(ExceptionControler.class.getSimpleName());

    @ExceptionHandler(APIException.class)
    @ResponseBody
    public APIResult onAPIException(APIException e) {
        log.warn("业务异常:", e);
        return new APIResult(e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public APIResult onException(Exception e) {
        String message = ExceptionUtils.getMessage(e);
        log.error("系统异常:", e);
        return new APIResult(ResultCodes.UnkownError, message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public APIResult onMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return new APIResult(ResultCodes.MissingParameter, ResultCodes.MissingParameter + ":" + e.getParameterName());
    }

    @ExceptionHandler(TypeMismatchException.class)
    public APIResult onTypeMismatchException(TypeMismatchException e) {
        return new APIResult(ResultCodes.TypeMismatch, ResultCodes.TypeMismatch + ":" + e.getPropertyName());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public APIResult onHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return new APIResult(ResultCodes.NotImplemented);
    }
}
