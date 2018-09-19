package com.ppmall.service;

import com.ppmall.common.ServerResponse;
import com.ppmall.pojo.User;


public interface IUserService {
	
	@Deprecated
    ServerResponse<User> login(String username, String password);
	
	ServerResponse<User> shiroLogin(String username, String password);
    
    ServerResponse wechatLogin(String code, String encryptedData, String iv);

    ServerResponse<String> register(String username, String password, String mail, String phone, String question, String answer, int role);

    ServerResponse<String> getPassQuestion(String username);

    ServerResponse<String> checkAnswer(User user);

    ServerResponse<String> resetPasswordByQues(User user);

    ServerResponse<String> resetPasswordByPass(User currentUser, String password, String passwordNew);

    ServerResponse<String> checkValid(String str);

    ServerResponse<String> updateInformation(User user);

    ServerResponse checkAdmin(User user);


}
