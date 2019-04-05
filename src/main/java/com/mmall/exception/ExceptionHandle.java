package com.mmall.exception;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ServerResponse handle(Exception e) {
        if (e instanceof MyException) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录");
        } else {
            return ServerResponse.createByError();
        }
    }
}
