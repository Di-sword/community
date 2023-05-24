package com.diswordacg.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{
    QUESTION_NOT_FOUND(2001,"发生了预料之外的错误捏"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何帖子或评论进行回复"),
    NO_LOGIN(2003,"当前操作需要进行登录，请登录后重试"),
    SYS_ERROR(2004,"发生了预料之外的错误，请稍后再试"),
    TARGET_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"啊嘞，评论不见了，怎么绘世呢"),
    COMMENT_NOT_FOUND2(2007,"帖子不见了，发生甚么事了"),
    COMMENT_IS_EMPTY(2008,"输入内容不能为空");

    private String message;
    private Integer code;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
