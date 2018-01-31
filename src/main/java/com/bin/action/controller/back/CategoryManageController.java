package com.bin.action.controller.back;

import com.bin.action.common.Const;
import com.bin.action.common.ResponseCode;
import com.bin.action.common.ServerResponse;
import com.bin.action.pojo.User;
import com.bin.action.service.ICategoryService;
import com.bin.action.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by zhangbin on 18/1/3.
 */
@Controller
@RequestMapping(value = "/manage/category")
public class CategoryManageController {
    @Autowired
    private ICategoryService iCategoryService;
    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "add_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpSession session
            ,String categoryName
            , @RequestParam(value = "parentId",defaultValue = "0") Integer parentId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }else{
            ServerResponse serverResponse = iUserService.checkAdminRole(user);
            if (serverResponse.isSuccess()){
                return iCategoryService.addCategory(categoryName,parentId);
            }else {
                return serverResponse;
            }
        }
    }

    @RequestMapping(value = "update_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateCategory(HttpSession session,String categoryName,Integer id){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }else{
            ServerResponse serverResponse = iUserService.checkAdminRole(user);
            if (serverResponse.isSuccess()){
                return iCategoryService.updateCategory(categoryName,id);
            }else {
                return serverResponse;
            }
        }
    }

    @RequestMapping(value = "get_son_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getSonCategory(HttpSession session,@RequestParam(defaultValue = "0") Integer parentId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }else{
            ServerResponse serverResponse = iUserService.checkAdminRole(user);
            if (serverResponse.isSuccess()){
                return iCategoryService.selectSonCategoryByParentId(parentId);
            }else {
                return serverResponse;
            }
        }
    }

    @RequestMapping(value = "get_children_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getChildrenCategory(HttpSession session,@RequestParam(defaultValue = "0") Integer parentId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }else{
            ServerResponse serverResponse = iUserService.checkAdminRole(user);
            if (serverResponse.isSuccess()){
                return iCategoryService.selectChildrenCategoryByParentId(parentId);
            }else {
                return serverResponse;
            }
        }
    }



}
