package com.example.demo4.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * 处理整个web controller的异常
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({ArithmeticException.class,NullPointerException.class}) //处理异常
    public String handlerMathException(Exception e){
        log.error("",e.toString());
        return "login"; //返回视图地址
    }
}
