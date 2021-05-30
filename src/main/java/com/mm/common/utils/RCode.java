package com.mm.common.utils;

import com.baomidou.mybatisplus.extension.api.IErrorCode;
import lombok.Getter;

/**
 * 错误码
 *
 * @author lwl
 */
@Getter
public enum RCode implements IErrorCode {

    // 1xx 参数校验错误
    PARAM_FAILED(100L, "参数错误");

    // 5xx 服务异常错误

    private long code;

    private String msg;

    RCode(Long code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
