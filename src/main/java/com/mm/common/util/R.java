package com.mm.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回数据
 *
 * @author lwl
 */
@Data
public class R<T> {

    private Integer code;

    private String msg;

    private T data;

    public R(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> R<T> error() {
        return code(RCode.SYSTEM_ERR);
    }

    public static <T> R<T> error(String msg) {
        return error(500, msg);
    }

    public static <T> R<T> error(int code, String msg) {
        return new R<>(code, msg);
    }

    public static <T> R<T> code(ICode iCode) {
        return new R<>(iCode.getCode(), iCode.getMsg());
    }

    public static <T> R<T> code(ICode iCode, String msg) {
        return new R<>(iCode.getCode(), msg);
    }

    public static <T> R<T> ok(T data) {
        R<T> r = code(RCode.SUCCESS);
        r.setData(data);
        return r;
    }

    public static <T> R<Page<T>> ok(T list, int total) {
        R<Page<T>> r = code(RCode.SUCCESS);
        r.setData(new Page<>(total, list));
        return r;
    }

    public static <T> R<T> ok(T data, ICode iCode) {
        R<T> r = code(iCode);
        r.setData(data);
        return r;
    }

    public static <T> R<T> ok() {
        return code(RCode.SUCCESS);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Page<T> {

        /**
         * 总数
         */
        private int total;

        /**
         * 数据
         */
        private T list;
    }
}
