package com.mm.common.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mm.common.exception.GException;

import java.util.Optional;

/**
 * 数据校验
 *
 * @author lwl
 */
public abstract class Assert {

    public static void bool(Boolean bool, String message) {
        if (bool) {
            throw new GException(message);
        }
    }

    public static void bool(Boolean bool, ICode iCode) {
        if (bool) {
            throw new GException(iCode);
        }
    }

    public static void optional(Optional optional, String message) {
        if (!optional.isPresent()) {
            throw new GException(message);
        }
    }

    public static void optional(Optional optional, ICode iCode) {
        if (!optional.isPresent()) {
            throw new GException(iCode);
        }
    }

    public static void isBlank(String str, String message) {
        if (StrUtil.isBlank(str)) {
            throw new GException(message);
        }
    }

    public static void isBlank(String str, ICode iCode) {
        if (StrUtil.isBlank(str)) {
            throw new GException(iCode);
        }
    }

    public static void isNull(Object object, String message) {
        if (ObjectUtil.isEmpty(object)) {
            throw new GException(message);
        }
    }

    public static void isNull(Object object, ICode iCode) {
        if (ObjectUtil.isEmpty(object)) {
            throw new GException(iCode);
        }
    }
}
