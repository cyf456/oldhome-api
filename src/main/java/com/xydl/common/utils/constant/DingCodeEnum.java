package com.xydl.common.utils.constant;

public enum DingCodeEnum {

    success(0);

    private int code;

    DingCodeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
