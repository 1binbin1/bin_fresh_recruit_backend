package com.bin.bin_fresh_recruit_backend.utils;

import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * 数组工具类
 *
 * @Author: hongxiaobin
 * @Time: 2024/3/4 23:50
 */
public class ArrayUtils {
    public static String[] removeDuplication(String[] array) {
        LinkedHashSet<String> set = new LinkedHashSet<>(Arrays.asList(array));
        return set.toArray(new String[0]);
    }
}
