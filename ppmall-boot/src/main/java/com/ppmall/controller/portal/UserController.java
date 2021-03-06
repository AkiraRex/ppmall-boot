package com.ppmall.controller.portal;

import com.ppmall.common.Const;
import com.ppmall.common.ResponseCode;
import com.ppmall.common.ServerResponse;
import com.ppmall.config.shiro.annotation.RequiredLogin;
import com.ppmall.pojo.User;
import com.ppmall.service.IUserService;
import com.ppmall.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/user")
/**
 *
 */
public class UserController {
    /**
     * 用户登陆
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    @Autowired
    private IUserService iUserService;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse response = iUserService.shiroLogin(username, password);
        if (response.isSuccess())
            session.setAttribute(Const.CURRENT_USER, response.getData());
        return response;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createSuccessMessage("登出成功");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(String username, String password, String email, String phone, String question, String answer) {
        ServerResponse<String> response = iUserService.register(username, password, email, phone, question, answer, 0);
        return response;
    }

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    @ResponseBody
    @RequiredLogin
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null)
            return ServerResponse.createSuccess(user);
        return ServerResponse.createErrorStatus(ResponseCode.NOT_LOGIN.getCode(), ResponseCode.NOT_LOGIN.getDesc());
    }

    @RequestMapping(value = "/forgetGetQuestion", method = RequestMethod.POST)
    @ResponseBody
    @RequiredLogin
    public ServerResponse<String> getPassQuestion(String username) {
        return iUserService.getPassQuestion(username);
    }

    @RequestMapping(value = "/forgetCheckAnswer", method = RequestMethod.POST)
    @ResponseBody
    @RequiredLogin
    public ServerResponse<String> checkAnswer(User user, HttpSession session) {

        ServerResponse response = iUserService.checkAnswer(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.FORGET_TOKEN, response.getData());
        }

        return response;
    }


    @RequestMapping(value = "/forgetResetPassword", method = RequestMethod.POST)
    @ResponseBody
    @RequiredLogin
    public ServerResponse<String> forgetPassword(User user, String forgetToken, HttpSession session) {
        String forgetTokenS = session.getAttribute(Const.FORGET_TOKEN).toString();

        if (forgetToken.equals(forgetTokenS))
            return iUserService.resetPasswordByQues(user);

        return ServerResponse.createErrorStatus(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    @RequiredLogin
    public ServerResponse<String> forgetPassword(String passwordOld, String passwordNew, HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);

        if (currentUser == null) {
            return ServerResponse.createErrorStatus(ResponseCode.NOT_LOGIN.getCode(), ResponseCode.NOT_LOGIN.getDesc());
        }
        ServerResponse response = iUserService.resetPasswordByPass(currentUser, passwordOld, passwordNew);
        if (response.isSuccess()) {
            currentUser.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        }
        return response;
    }

    @RequestMapping(value = "/checkValid", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str) {

        return iUserService.checkValid(str);
    }


    @RequestMapping(value = "/getInformation", method = RequestMethod.POST)
    @ResponseBody
    @RequiredLogin
    public ServerResponse<User> getInformation(HttpSession session) {

        return getUserInfo(session);
    }

    @RequestMapping(value = "/updateInformation", method = RequestMethod.POST)
    @ResponseBody
    @RequiredLogin
    public ServerResponse<String> updateInformation(User user, HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        user.setUsername(currentUser.getUsername());
        user.setId(currentUser.getId());
        session.setAttribute(Const.CURRENT_USER, user);

        return iUserService.updateInformation(user);
    }


}
