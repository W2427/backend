package com.ose.tasks.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DwgRevUtils {

    public static void main(String[] args) {
        generateDrawingDetailVersion(
            919191919L,
            191919191919L
        );
    }

    private static final Map<String, Integer> revMap = new HashMap<String, Integer>() {{
        put("0", 0);
        put("1", 1);
        put("2", 2);
        put("3", 3);
        put("4", 4);
        put("5", 5);
        put("6", 6);
        put("7", 7);
        put("8", 8);
        put("9", 9);
        put("10", 10);
        put("11", 11);
        put("12", 12);
        put("A", 15);
        put("B", 16);
        put("C", 17);
        put("D", 20);
        put("D1", 21);
        put("D2", 22);
        put("D3", 23);
        put("D4", 24);
        put("D5", 25);
        put("D6", 26);
        put("D7", 27);
        put("D8", 28);

    }};

    public static void getLatestRev(Map<String, String> dwgRevMap, String dwgNo, String revision) {
//        if(dwgNo.startsWith("D-102-DAT-FPU-42025") && revision.equalsIgnoreCase("D2")) {
//            System.out.println("ABC");
//        }
        if (dwgRevMap.get(dwgNo) == null) {
            dwgRevMap.put(dwgNo, revision);
        } else if (revMap.get(dwgRevMap.get(dwgNo)) != null && revMap.get(dwgRevMap.get(dwgNo)) < revMap.get(revision)) {
            dwgRevMap.put(dwgNo, revision);
        }
    }

    /**
     * 返回 18位数字字符串，14位为时间戳，2位项目项目信息，2位用户信息
     *
     * @param projectId
     * @param userId
     * @return
     */
    public static String generateDrawingDetailVersion(Long projectId, Long userId) {
        // 获取当前时间戳（毫秒）
        long timestamp = System.currentTimeMillis();
        Instant instant = Instant.ofEpochMilli(timestamp);

        // 使用DateTimeFormatter格式化时间戳
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedDate = formatter.format(instant.atZone(ZoneId.systemDefault()));

        // 将格式化的日期字符串转换为版本号（可选移除分隔符）
        String version = formattedDate; // 或者使用formattedDate.replace("", ""); 来移除分隔符（如果需要）
        // 项目后两位
        String projectVersionStr = String.valueOf(projectId);
        String lastTwoProjectStr = projectVersionStr.substring(projectVersionStr.length() - 2);
        // 用户信息后两位
        String userVersionStr = String.valueOf(projectId);
        String lastTwoUserStr = projectVersionStr.substring(userVersionStr.length() - 2);

        return version + lastTwoProjectStr + lastTwoUserStr;
    }


}
