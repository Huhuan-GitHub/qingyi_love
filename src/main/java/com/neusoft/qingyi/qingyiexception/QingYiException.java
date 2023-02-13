package com.neusoft.qingyi.qingyiexception;

import com.neusoft.qingyi.common.ErrorCode;

public class QingYiException extends RuntimeException {
    private final int code;

    public QingYiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public QingYiException(ErrorCode responseCode) {
        super(responseCode.getMessage());
        this.code = responseCode.getCode();
    }

    public QingYiException(ErrorCode responseCode, String message) {
        super(message);
        this.code = responseCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
