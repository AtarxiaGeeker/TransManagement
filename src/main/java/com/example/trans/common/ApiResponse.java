package com.example.trans.common;


public class ApiResponse<T> {
    private int code;       // 状态码（兼容HTTP状态码）
    private String message; // 提示信息
    private T data;         // 响应数据
    private long timestamp;// 时间戳

    // 构造器
    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    // 成功响应（带数据）
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Success", data);
    }

    // 成功响应（无数据）
    public static ApiResponse<?> success() {
        return success(null);
    }

    // 错误响应
    public static ApiResponse<?> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
