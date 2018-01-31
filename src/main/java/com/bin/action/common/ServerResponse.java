package com.bin.action.common;

/**
 * Created by zhangbin on 17/12/15.
 */
public class ServerResponse<T> {
    private int code;
    private String msg;
    private T data;

    private ServerResponse(int code){
        this.code = code;
    }

    private ServerResponse(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    private ServerResponse(int code,T data){
        this.code = code;
        this.data = data;
    }

    private ServerResponse(int code,String msg,T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public boolean isSuccess(){
        return this.code == ResponseCode.SUCCESS.getCode();
    }

    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByErrorMessage(String msg){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),msg);
    }
    public static <T> ServerResponse<T> createByErrorCodeMessage(int code,String msg){
        return new ServerResponse<T>(code,msg);
    }

    public T getData(){
        return data;
    }
}
