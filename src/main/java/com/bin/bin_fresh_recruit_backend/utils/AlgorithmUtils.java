package com.bin.bin_fresh_recruit_backend.utils;

import com.bin.bin_fresh_recruit_backend.constant.CommonConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 算法工具类
 *
 * @Author: hongxiaobin
 * @Time: 2023/3/8 16:06
 */
public class AlgorithmUtils {

    /**
     * 编辑距离算法（用于计算最相似的两组标签）
     * 原理：https://blog.csdn.net/DBC_121/article/details/104198838
     *
     * @param word1 字符串1
     * @param word2 字符串2
     * @return 相似度：将 word1 转换成 word2 所使用的最少操作数
     */
    private static int minDistance(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();

        if (n * m == 0) {
            return n + m;
        }

        int[][] d = new int[n + 1][m + 1];
        for (int i = 0; i < n + 1; i++) {
            d[i][0] = i;
        }

        for (int j = 0; j < m + 1; j++) {
            d[0][j] = j;
        }

        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < m + 1; j++) {
                int left = d[i - 1][j] + 1;
                int down = d[i][j - 1] + 1;
                int left_down = d[i - 1][j - 1];
                if (word1.charAt(i - 1) != word2.charAt(j - 1)) {
                    left_down += 1;
                }
                d[i][j] = Math.min(left, Math.min(down, left_down));
            }
        }
        return d[n][m];
    }


    /**
     * 获取匹配列表
     *
     * @param freshList 应届生拥有的标签
     * @param dictList  数据字典的标签
     * @return 匹配列表
     */
    public static List<String> getRecommendTypeList(List<String> freshList, List<String> dictList) {
        Map<String, Integer> stringIntegerHashMap = new HashMap<>();
        for (String dict : dictList) {
            int min = Integer.MAX_VALUE;
            for (String fresh : freshList) {
                min = Math.min(min, minDistance(fresh, dict));
            }
            stringIntegerHashMap.put(dict, min);
        }
        // stringIntegerHashMap的value前几小
        return stringIntegerHashMap.entrySet().stream().
                sorted((o1, o2) -> o2.getValue() - o1.getValue()).
                map(Map.Entry::getKey).
                limit(CommonConstant.MATCH_NUM).
                collect(Collectors.toList());
    }
}
