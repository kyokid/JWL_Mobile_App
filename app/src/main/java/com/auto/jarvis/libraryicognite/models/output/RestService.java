package com.auto.jarvis.libraryicognite.models.output;

/**
 * Created by HaVH on 1/13/17.
 */

public class RestService<T> {
    private boolean succeed;
    private String textMessage;
    private String code;
    private T data;

    public RestService(boolean succeed, String textMessage, String code, T data) {
        this.succeed = succeed;
        this.textMessage = textMessage;
        this.code = code;
        this.data = data;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
