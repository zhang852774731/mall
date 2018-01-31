package com.bin.action.service;

import com.bin.action.common.ServerResponse;
import com.bin.action.pojo.User;
import org.springframework.stereotype.Service;

/**
 * Created by zhangbin on 17/12/15.
 */
public interface IUserService {
    ServerResponse login(String username, String password);
    ServerResponse register(User user);
    ServerResponse checkValid(String str,String type);
    ServerResponse forgetPassword(String username);
    ServerResponse forgetCheckAnswer(String username,String question,String answer);
    ServerResponse forgetRestPassword(String username,String newPassword,String token);
    ServerResponse resetPassword(String oldPassword,String newPassword,User user);
    ServerResponse updateUserInfo(User user);
    ServerResponse checkAdminRole(User user);
}
