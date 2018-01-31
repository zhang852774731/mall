package com.bin.action.controller.back;

import com.bin.action.common.Const;
import com.bin.action.common.ServerResponse;
import com.bin.action.pojo.User;
import com.bin.action.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by zhangbin on 17/12/16.
 */
@Controller
@RequestMapping(value = "/manage/user")
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do")
    @ResponseBody
    public ServerResponse login(String username, String password, HttpSession httpSession){
        ServerResponse serverResponse = iUserService.login(username,password);
        if (serverResponse.isSuccess()){
            User user = (User)serverResponse.getData();
            if (user.getRole().equals(Const.Role.ROLE_ADMIN)){
                httpSession.setAttribute(Const.CURRENT_USER,user);
                return serverResponse;
            }else {
                return ServerResponse.createByErrorMessage("用户没有管理员权限");
            }
        }
        return serverResponse;
    }
}
