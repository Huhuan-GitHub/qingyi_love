package com.neusoft.qingyi.myenum;

public enum ExceptionEnum {
    SIGN_UP_TIMEOUT(761, "报名时间已过时"),
    MATCH_START_END_TIME_ERROR(762, "赛事开始时间不能小于开始时间"),
    MATCH_START_CURRENT_TIME_ERROR(763, "赛事开始时间不能晚于当前系统时间"),
    MATCH_FILE_NAME_ERROR(764, "上传赛事文件格式错误"),

    REDIS_MATCH_VALUE_NULL(864, "存入缓存赛事为空");

    private Integer code;
    private String msg;

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
