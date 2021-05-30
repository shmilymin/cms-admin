package com.mm.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 校验验证码异常
 *
 * @author lwl
 */
public class ValidateCodeException extends AuthenticationException {

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
