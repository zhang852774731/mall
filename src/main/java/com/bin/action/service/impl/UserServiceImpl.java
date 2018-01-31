package com.bin.action.service.impl;

import com.bin.action.common.Const;
import com.bin.action.common.ServerResponse;
import com.bin.action.common.TokenCache;
import com.bin.action.dao.UserMapper;
import com.bin.action.pojo.User;
import com.bin.action.service.IUserService;
import com.bin.action.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by zhangbin on 17/12/15.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public ServerResponse login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        User user = userMapper.selectLogin(username,password);
        if (user == null){
            return ServerResponse.createByErrorMessage("密码不存在");
        }
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse register(User user) {
        ServerResponse response = checkValid(user.getUsername(),Const.USERNAME);
        if (!response.isSuccess()){
            return response;
        }
        response = checkValid(user.getEmail(),Const.EMAIL);
        if (!response.isSuccess()){
            return response;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        String passwod = user.getPassword();
        passwod = MD5Util.MD5EncodeUtf8(passwod);
        user.setPassword(passwod);
        int resultCount = userMapper.insert(user);
        if (resultCount == 0 ){
            return ServerResponse.createByErrorMessage("用户注册失败");
        }
        return ServerResponse.createBySuccess();

    }

    @Override
    public ServerResponse checkValid(String str, String type) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(type)){
            if (type.equals(Const.USERNAME)){
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0){
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }else if(type.equals(Const.EMAIL)){
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0){
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        }else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse forgetPassword(String username) {
        ServerResponse response = checkValid(username,Const.USERNAME);
        if (response.isSuccess()){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestion(username);
        if (org.apache.commons.lang3.StringUtils.isNoneBlank(question)){
            return ServerResponse.createBySuccess(question);
        }else {
            return ServerResponse.createByErrorMessage("用户没有设置问题");
        }
    }

    @Override
    public ServerResponse forgetCheckAnswer(String username, String question, String answer) {
        int resultCount = userMapper.forgetCheckAnswer(username,question,answer);
        if (resultCount > 0){
            String token = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,token);
            return ServerResponse.createBySuccess(token);
        }else {
            return ServerResponse.createByErrorMessage("答案错误");
        }
    }

    @Override
    public ServerResponse forgetRestPassword(String username, String newPassword, String token) {
        if (StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token为空");
        }
        ServerResponse response = checkValid(username,Const.USERNAME);
        if (response.isSuccess()){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        if (StringUtils.equals(token,TokenCache.getKey(TokenCache.TOKEN_PREFIX+username))){
            String md5Password = MD5Util.MD5EncodeUtf8(newPassword);
            int resultCount = userMapper.updatePasswordByUsername(username,md5Password);
            if (resultCount > 0){
                return ServerResponse.createBySuccess("重置密码成功");
            }else{
                return ServerResponse.createByErrorMessage("重置密码失败");
            }
        }else{
            return ServerResponse.createByErrorMessage("token失效");
        }
    }

    @Override
    public ServerResponse resetPassword(String oldPassword, String newPassword, User user) {
        int resultCount = userMapper.findByPasswordAndUserId(MD5Util.MD5EncodeUtf8(oldPassword),user.getId());
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        resultCount = userMapper.updateByPrimaryKeySelective(user);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("密码更新失败");
        }
        return ServerResponse.createBySuccess("密码更新成功");
    }

    @Override
    public ServerResponse updateUserInfo(User user) {
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if (resultCount >0 ){
            return ServerResponse.createByErrorMessage("邮箱已存在");
        }
        resultCount = userMapper.updateByPrimaryKeySelective(user);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("用户更新失败");
        }
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse checkAdminRole(User user) {
        if (user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByErrorMessage("用户没有权限");
        }
    }
}
