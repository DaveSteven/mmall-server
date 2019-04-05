package com.mmall.controller;

import com.mmall.annotation.LoginRequired;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.data.entity.User;
import com.mmall.holder.UserHolder;
import com.mmall.service.IUserService;
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
    @PostMapping("logout")
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
    @PostMapping("register")
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
    @PostMapping("checkUsernameOrEmail")
    public ServerResponse<String> checkUsernameAndEmail(String str, String type) {
        return iUserService.checkUsernameOrEmail(str, type);
    }

    /**
     * 获取用户信息
     *
     * @param session
     * @return
     */
    @LoginRequired
    @PostMapping("getUserInfo")
    public ServerResponse<User> getUserInfo() {
        return ServerResponse.createBySuccess(UserHolder.get());
    }

    /**
     * 重置密码获取问题
     *
     * @param username
     * @return
     */
    @PostMapping("getQuestionInForget")
    public ServerResponse<String> getQuestionInForget(String username) {
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
    @PostMapping("checkAnswerInForget")
    public ServerResponse<String> checkAnswerInForget(String username, String question, String answer) {
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
    @PostMapping("resetPasswordInForget")
    public ServerResponse<String> resetPasswordInForget(String username, String passwordNew, String forgetToken) {
        return iUserService.resetPasswordInForget(username, passwordNew, forgetToken);
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
    @PostMapping("resetPassword")
    public ServerResponse<String> resetPassword(HttpSession session, String passwordOld, String passwordNew) {
        return iUserService.resetPassword(passwordOld, passwordNew, UserHolder.get());
    }

    /**
     * 更新用户信息
     *
     * @param session
     * @param user
     * @return
     */
    @LoginRequired
    @PostMapping("updateInformation")
    public ServerResponse<User> updateInformation(HttpSession session, User user) {
        User currentUser = UserHolder.get();
        user.setId(currentUser.getId());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }
}
