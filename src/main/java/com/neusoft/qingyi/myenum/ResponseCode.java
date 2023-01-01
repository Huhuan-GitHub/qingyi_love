package com.neusoft.qingyi.myenum;


import com.neusoft.qingyi.util.StatusCode;

public enum ResponseCode implements StatusCode {
    SUCCESS(200, "请求成功"),
    FAILED(400, "请求失败"),
    VALIDATE_ERROR(405, "请求方法错误"),
    SERVER_ERROR(500, "请求方法错误"),
    AUTH_ERROR(403, "无权限"),
    RESPONSE_PACK_ERROR(1003, "response返回包装失败"),

    POSTS_COMMENT_PAGE_PARAMS_ERROR(601, "帖子评论分页参数错误");

    private int code;
    private String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
