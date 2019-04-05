package com.mmall.controller;

import com.mmall.annotation.LoginRequired;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.data.entity.User;
import com.mmall.holder.UserHolder;
import com.mmall.service.IUserService;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user/")
public class UserController {

    @Resource
    private IUserService iUserService;

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    @PostMapping("login")
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
     * 用户退出
     *
     * @param session
     * @return
     */
    @LoginRequired
    @PostMapping(value = "logout")
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    @PostMapping(value = "register")
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    /**
     * 校验用户名和邮箱是否已存在
     *
     * @param str
     * @param type
     * @return
     */
    @PostMapping(value = "check_valid")
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    /**
     * 获取用户信息
     *
     * @param session
     * @return
     */
    @LoginRequired
    @PostMapping(value = "get_user_info")
    public ServerResponse<User> getUserInfo() {
        return ServerResponse.createBySuccess(UserHolder.get());
    }

    /**
     * 重置密码获取问题
     *
     * @param username
     * @return
     */
    @PostMapping(value = "forget_get_question")
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    /**
     * 校验安全问题的答案
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @PostMapping(value = "forget_check_answer")
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    /**
     * 忘记密码的情况下，重置密码
     *
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @PostMapping(value = "forget_reset_password")
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    /**
     * 修改密码
     *
     * @param session
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @LoginRequired
    @PostMapping(value = "reset_password")
    public ServerResponse<String> resetPassword(HttpSession session, String passwordOld, String passwordNew) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(passwordOld, passwordNew, user);
    }

    /**
     * 更新用户信息
     *
     * @param session
     * @param user
     * @return
     */
    @LoginRequired
    @PostMapping(value = "update_information")
    public ServerResponse<User> updateInformation(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
     * 获取用户信息
     *
     * @param session
     * @return
     */
    @LoginRequired
    @PostMapping(value = "get_information")
    public ServerResponse<User> getInformation(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return iUserService.getInformation(currentUser.getId());
    }
}
