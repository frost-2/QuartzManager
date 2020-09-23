package com.frost2.quartz.common.config;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.frost2.quartz.common.bean.Code;
import com.frost2.quartz.common.bean.ResultList;
import com.frost2.quartz.common.customException.CustomException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.management.ReflectionException;
import java.sql.SQLSyntaxErrorException;

/**
 * 全局异常处理机制
 *
 * @author 陈伟平
 * @date 2020-9-23 17:58:45
 */
@RestControllerAdvice
public class HandleException {

    /*
        validator当没有通过校验是报org.springframework.validation.BindException
     */
    @ExceptionHandler(BindException.class)
    public ResultList handleException(BindException e) {
        return new ResultList(2004, e.getBindingResult().getFieldError().getDefaultMessage(), null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResultList handleException(MethodArgumentTypeMismatchException e) {
        return new ResultList(2004, "参数`" + e.getName() + "`类型转换错误-->" + e.getCause(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultList handleException(MethodArgumentNotValidException e) {
        return new ResultList(2004, e.getBindingResult().getFieldError().getDefaultMessage(), null);
    }

    /*
        请求方式不正确
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultList handleException(HttpRequestMethodNotSupportedException e) {
        return new ResultList(2004, e.getMessage(), null);
    }

    //code转换异常
    @ExceptionHandler(CustomException.class)
    public ResultList handleException(CustomException e) {
        return new ResultList(e.getCode(), e.getMsg(), null);
    }

    //SQL 语法异常
    @ExceptionHandler(SQLSyntaxErrorException.class)
    public ResultList handleException(SQLSyntaxErrorException e) {
        return new ResultList(Code.EXECUTION_ERROR.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(ReflectionException.class)
    public ResultList handleException(ReflectionException e) {
        return new ResultList(Code.EXECUTION_ERROR.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResultList handleException(InvalidFormatException e) {
        return new ResultList(Code.EXECUTION_ERROR.getCode(), e.getOriginalMessage(), null);
    }

}
