package com.example.quickstart.bo;

import java.util.List;

/**
 * <p>
 * 前端对接LayUi的数据承载类
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-07-15 11:58
 */
public class PagingTool<T> {

    /**
     * 解析接口状态
     */
    private String code;

    /**
     * 解析提示文本
     */
    private String msg;

    /**
     * 解析数据列表
     */
    private List<T> data;

    /**
     * 解析数据长度 总记录数
     */
    private long count;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public PagingTool() {
        this.code = "0";
    }

    public PagingTool(String msg, List<T> data, long count) {
        this.code = "0";
        this.msg = msg;
        this.data = data;
        this.count = count;
    }

    public PagingTool(String code, String msg, List<T> data, long count) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.count = count;
    }

    @Override
    public String toString() {
        return "PagingTool{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
