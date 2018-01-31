package com.bin.action.controller.back;

import com.bin.action.common.Const;
import com.bin.action.common.ResponseCode;
import com.bin.action.common.ServerResponse;
import com.bin.action.pojo.Product;
import com.bin.action.pojo.User;
import com.bin.action.service.IFileService;
import com.bin.action.service.IProductService;
import com.bin.action.service.IUserService;
import com.bin.action.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 18/1/4.
 */
@Controller
@RequestMapping(value = "/manage/product")
public class ProductManageController {
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IFileService iFileService;
    @RequestMapping(value = "save_or_update.do")
    @ResponseBody
    public ServerResponse saveOrUpdateProduct(HttpSession session, Product product){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }else{
            if (iUserService.checkAdminRole(user).isSuccess()){
                return iProductService.saveOrUpdate(product);
            }else {
                return ServerResponse.createByErrorMessage("没有管理员权限");
            }
        }
    }

    @RequestMapping(value = "set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session,Integer productId,Integer status){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }else{
            if (iUserService.checkAdminRole(user).isSuccess()){
                return iProductService.setStatus(productId,status);
            }else {
                return ServerResponse.createByErrorMessage("没有管理员权限");
            }
        }
    }

    @RequestMapping(value = "product_detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession session,Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }else{
            if (iUserService.checkAdminRole(user).isSuccess()){
                return iProductService.getDetail(productId);
            }else {
                return ServerResponse.createByErrorMessage("没有管理员权限");
            }
        }
    }

    @RequestMapping(value = "product_list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session
            , @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum
            , @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }else{
            if (iUserService.checkAdminRole(user).isSuccess()){
                return iProductService.list(pageNum,pageSize);
            }else {
                return ServerResponse.createByErrorMessage("没有管理员权限");
            }
        }
    }

    @RequestMapping(value = "product_search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpSession session
            , @RequestParam(value = "productName",required = false) String productName
            , @RequestParam(value = "productId",required = false) Integer productId
            , @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum
            , @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }else{
            if (iUserService.checkAdminRole(user).isSuccess()){
                return iProductService.productSearch(productName,productId,pageNum,pageSize);
            }else {
                return ServerResponse.createByErrorMessage("没有管理员权限");
            }
        }
    }


    @RequestMapping(value = "upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session
            , @RequestParam(value = "upload_file",required = false) MultipartFile multipartFile
            , HttpServletRequest request){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }else{
            if (iUserService.checkAdminRole(user).isSuccess()){
                String path = request.getSession().getServletContext().getRealPath("upload");
                String fileName = iFileService.upload(multipartFile,path);
                if (StringUtils.isNotBlank(fileName)){
                    String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+fileName;
                    Map result = new HashMap();
                    result.put("uri",fileName);
                    result.put("url",url);
                    return ServerResponse.createBySuccess(result);
                }else {
                    return ServerResponse.createByErrorMessage("图片上传失败");
                }

            }else {
                return ServerResponse.createByErrorMessage("没有管理员权限");
            }
        }
    }

    @RequestMapping(value = "rich_text_upload.do")
    @ResponseBody
    public Map richUpload(HttpSession session
            , @RequestParam(value = "upload_file",required = false) MultipartFile multipartFile
            , HttpServletRequest request
            , HttpServletResponse response){
        Map resultMap = new HashMap<>();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员");
            return resultMap;
        }else{
            if (iUserService.checkAdminRole(user).isSuccess()){
                String path = request.getSession().getServletContext().getRealPath("upload");
                String fileName = iFileService.upload(multipartFile,path);
                if (StringUtils.isNotBlank(fileName)){
                    //simditor
                    String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+fileName;
                    resultMap.put("success",true);
                    resultMap.put("msg","上传成功");
                    resultMap.put("file_path",url);
                    response.setHeader("Access-Control-Allow-Headers","X-File-Name");
                    return resultMap;
                }else {
                    resultMap.put("success",true);
                    resultMap.put("msg","上传成功");
                    return resultMap;
                }

            }else {
                resultMap.put("success",false);
                resultMap.put("msg","无权操作");
                return resultMap;
            }
        }
    }
}
