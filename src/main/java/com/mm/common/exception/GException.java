package com.mm.common.exception;

import com.mm.common.util.ICode;
import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常
 *
 * @author lwl
 */
@Getter
@Setter
public class GException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;

    public GException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public GException(ICode iCode) {
        super(iCode.getMsg());
        this.code = iCode.getCode();
        this.msg = iCode.getMsg();
    }

    public GException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public GException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public GException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

}
