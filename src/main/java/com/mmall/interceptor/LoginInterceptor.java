package com.mmall.interceptor;

import com.mmall.annotation.LoginRequired;
import com.mmall.common.Const;
import com.mmall.data.entity.User;
import com.mmall.exception.MyException;
import com.mmall.holder.UserHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.remove();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        Method method = ((HandlerMethod) handler).getMethod();
        boolean needLogin = method.isAnnotationPresent(LoginRequired.class);
        if (needLogin) {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(Const.CURRENT_USER);
            if (null == user) {
                throw new MyException();
            }
            UserHolder.set(user);
        }
        return true;
    }
}
