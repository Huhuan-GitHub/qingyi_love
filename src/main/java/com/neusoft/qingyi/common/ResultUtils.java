package com.neusoft.qingyi.common;

import com.neusoft.qingyi.util.ResponseResult;

public class ResultUtils {
    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(0, "ok", data);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static <T> ResponseResult<T> error(ErrorCode errorCode) {
        return new ResponseResult<>(errorCode);
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @return
     */
    public static <T> ResponseResult<T> error(int code, String message) {
        return new ResponseResult<>(code, message, null);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static <T> ResponseResult<T> error(ErrorCode errorCode, String message) {
        return new ResponseResult<>(errorCode.getCode(), message, null);
    }
}
