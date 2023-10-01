package com.liang.service.support.utils;

import java.util.UUID;

/**
 * @since 2023/10/1 9:05
 * @author by liangzj
 */
public class UUIDUtil {
    public static String get() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
