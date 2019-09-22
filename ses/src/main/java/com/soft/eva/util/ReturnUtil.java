package com.soft.eva.util;

import java.util.HashMap;
import java.util.Map;

public class ReturnUtil extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public ReturnUtil() {
        put("code", 0);
        put("msg", "操作成功");
    }

    public static ReturnUtil error() {
        return error(1, "操作失败");
    }

    public static ReturnUtil error(String msg) {
        return error(500, msg);
    }

    public static ReturnUtil error(int code, String msg) {
        ReturnUtil r = new ReturnUtil();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static ReturnUtil ok(String msg) {
        ReturnUtil r = new ReturnUtil();
        r.put("msg", msg);
        return r;
    }

    public static ReturnUtil ok(Map<String, Object> map) {
        ReturnUtil r = new ReturnUtil();
        r.putAll(map);
        return r;
    }

    public static ReturnUtil ok() {
        return new ReturnUtil();
    }

    @Override
    public ReturnUtil put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
