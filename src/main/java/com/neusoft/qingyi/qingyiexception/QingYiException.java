package com.neusoft.qingyi.qingyiexception;

import com.neusoft.qingyi.myenum.ResponseCode;

public class QingYiException extends RuntimeException{
    private final int code;
    public QingYiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public QingYiException(ResponseCode responseCode) {
        super(responseCode.getMsg());
        this.code = responseCode.getCode();
    }

    public QingYiException(ResponseCode responseCode, String message) {
        super(message);
        this.code = responseCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
