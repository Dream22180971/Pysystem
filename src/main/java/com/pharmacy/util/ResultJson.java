package com.pharmacy.util;

/**
 * 统一返回结果类
 * 用于API接口返回标准化的JSON格式
 */
public class ResultJson {
    /**
     * 状态码：200 成功；4xx/5xx 表示失败（如登录错误可能为 401，业务错误多为 500）
     */
    private int code;
    /**
     * 返回消息
     */
    private String message;
    /**
     * 返回数据
     */
    private Object data;

    public ResultJson() {
    }

    /**
     * 构造方法
     * @param code 状态码
     * @param message 返回消息
     * @param data 返回数据
     */
    public ResultJson(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 成功返回
     * @param data 返回数据
     * @return ResultJson对象
     */
    public static ResultJson success(Object data) {
        return new ResultJson(200, "success", data);
    }

    /**
     * 失败返回
     * @param message 错误消息
     * @return ResultJson对象
     */
    public static ResultJson error(String message) {
        return new ResultJson(500, message, null);
    }

    /**
     * 失败返回
     * @param code 错误码
     * @param message 错误消息
     * @return ResultJson对象
     */
    public static ResultJson error(int code, String message) {
        return new ResultJson(code, message, null);
    }
}