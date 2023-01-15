package com.neusoft.qingyi.util;

import com.neusoft.qingyi.common.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseResult<T> {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 提示信息，如果有错误时，前端可以获取该字段进行提示
     */
    private String msg;
    /**
     * 查询到的结果数据，
     */
    private T data;

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage(),null);
    }
}
