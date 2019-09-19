package com.main.model;

import java.io.Serializable;

public class ResponseMessage<T> implements Serializable {
    //错误码
    private int code;

    //信息描述
    private String msg;

//具体的信息内容

    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
