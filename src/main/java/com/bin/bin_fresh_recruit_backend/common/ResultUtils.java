package com.bin.bin_fresh_recruit_backend.common;

/**
 * 统一返回
 *
 * @Author: hongxiaobin
 * @Time: 2023/11/4 16:22
 */
public class ResultUtils {
    /**
     * 成功响应
     *
     * @param data 响应数据
     * @return void
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, "ok", data);
    }

    /**
     * 错误时返回异常
     *
     * @param errorCode 异常码
     * @return void
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }


    public static <T> BaseResponse<T> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode, message);
    }

    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, message, null);
    }

}
