package com.example.quickstart.bo;


import java.util.List;

/**
 * <p>
 * 返回结果
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-07-14 10:24
 */
public class ResultBody<T> {

    /**
     * 处理状态
     */
    private boolean status;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应结果
     */
    private Object result;

    /**
     * 对象集合
     */
    private List<T> list;

    public ResultBody(boolean status, String message, Object result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    public ResultBody(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResultBody(boolean status, String message, List<T> list) {
        this.status = status;
        this.message = message;
        this.list = list;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ResultBody{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
