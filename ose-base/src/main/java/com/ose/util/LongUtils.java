package com.ose.util;

import com.ose.vo.InspectParty;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.ose.util.StringUtils.trim;

/**
 * 字符串工具。
 */
public class LongUtils {


    /**
     * 检查Long是否为空。
     *
     * @param lng 检查对象字符串
     * @return 字符串是否为空
     */
    public static boolean isEmpty(Long lng) {
        return lng == null || lng == 0L;

    }


    /**
     * Long 数组转换为 ,分割的字符串
     */
    public static String join(Collection<Long> longSet) {

        Set<String> tmpSet = new HashSet<>();

        if (CollectionUtils.isEmpty(longSet)) {
            return null;
        }

        longSet.forEach(lng ->
        {
            tmpSet.add(lng.toString());
        });

        return String.join(",", tmpSet);
    }

    /**
     * Long 数组转换为 ,分割的字符串
     */
    public static String join(List<Long> longList, String delimit) {

        List<String> tmpList = new ArrayList<>();

        if (CollectionUtils.isEmpty(longList)) {
            return null;
        }

        longList.forEach(lng ->
        {
            tmpList.add(lng.toString());
        });
        String returnStr = String.join(delimit, tmpList);
        if(returnStr.length() > 0) return delimit + returnStr + delimit;
        else return delimit;
//        return String.join(delimit, tmpSet);
    }


    public static Set<Long> change2Str(Set<String> tmpEntityIDs) {

        Set<Long> entityIDs = new HashSet<>();


        tmpEntityIDs.forEach(entityId ->
        {
            entityIDs.add(LongUtils.parseLong(entityId));
        });

        return entityIDs;
    }


    public static List<Long> change2Str(List<String> tmpEntityIDs) {

        List<Long> entityIDs = new ArrayList<>();


        tmpEntityIDs.forEach(entityId ->
        {
            entityIDs.add(LongUtils.parseLong(entityId));
        });

        return entityIDs;
    }

    public static List<Long> change2Str(String[] tmpEntityIDs) {

        List<Long> entityIDs = new ArrayList<>();

        Arrays.asList(tmpEntityIDs)
            .forEach(entityId ->
            {
                entityIDs.add(LongUtils.parseLong(entityId));
            });

        return entityIDs;
    }

    public static Long[] change2LongArr(String[] strArr) {
        List<String> strList = Arrays.asList(strArr);
        strList.remove("");
        List<Long> longList = new ArrayList<>();
        strList.forEach(arr -> {
            longList.add(LongUtils.parseLong(arr));
        });

        return longList.toArray(new Long[longList.size()]);
    }

    public static List<Long> change2LongArr(String str, String delimit) {
        if(StringUtils.isEmpty(str)) return new ArrayList<>();

        String[] strList = str.split(delimit);

        List<Long> longList = new ArrayList<>();
        for(String item : strList){
            Long m = parseLong(item);
            if(m != null)
            longList.add(m);
        }

        return longList;
    }

    public static Long parseLong(String lng) {
        if (lng == null || lng.equals("")) {
            return null;
        }

        return Long.parseLong(trim(lng));
    }

    public static void main(String[] args) {
        String reportStr = "F217-GBS1-PWHT-00001,F217-GBS1-PWHT-00002,F217-GBS1-PWHT-00003,F217-GBS1-PWHT-00004,F217-GBS1-PWHT-00005,F217-GBS1-PWHT-00006,F217-GBS1-PWHT-00007,F217-GBS1-PWHT-00008,F217-GBS1-PWHT-00009,F217-GBS1-PWHT-00010,F217-GBS1-PWHT-00011,F217-GBS1-PWHT-00012,F217-GBS1-PWHT-00013,F217-GBS1-PWHT-00014,F217-GBS1-PWHT-00015,F217-GBS1-PWHT-00016,F217-GBS1-PWHT-00017,F217-GBS1-PWHT-00018,F217-GBS1-PWHT-00019,F217-GBS1-PWHT-00022,F217-GBS1-PWHT-00023,F217-GBS1-PWHT-00024,F217-GBS1-PWHT-00025,F217-GBS1-PWHT-00026,F217-GBS1-PWHT-00027,F217-GBS1-PWHT-00028,F217-GBS1-PWHT-00029,F217-GBS1-PWHT-00030,F217-GBS1-PWHT-00031,F217-GBS1-PWHT-00032,F217-GBS1-PWHT-00033,F217-GBS1-PWHT-00034,F217-GBS1-PWHT-00035,F217-GBS1-PWHT-00036,F217-GBS1-PWHT-00037,F217-GBS1-PWHT-00038,F217-GBS1-PWHT-00039,F217-GBS1-PWHT-00040,F217-GBS1-PWHT-00041,F217-GBS1-PWHT-00042,F217-GBS1-PWHT-00043,F217-GBS1-PWHT-00044,F217-GBS1-PWHT-00045,F217-GBS1-PWHT-00046,F217-GBS1-PWHT-00047,F217-GBS1-PWHT-00048,F217-GBS1-PWHT-00049,F217-GBS1-PWHT-00050,F217-GBS1-PWHT-00051,F217-GBS1-PWHT-00052,F217-GBS1-PWHT-00053,F217-GBS1-PWHT-00054,F217-GBS1-PWHT-00055,F217-GBS1-PWHT-00056,F217-GBS1-PWHT-00057,F217-GBS1-PWHT-00058,F217-GBS1-PWHT-00059,F217-GBS1-PWHT-00060,F217-GBS1-PWHT-00061,F217-GBS1-PWHT-00062,F217-GBS1-PWHT-00063,F217-GBS1-PWHT-00064,F217-GBS1-PWHT-00065,F217-GBS1-PWHT-00066,F217-GBS1-PWHT-00067,F217-GBS1-PWHT-00068,F217-GBS1-PWHT-00069,F217-GBS1-PWHT-00070,F217-GBS1-PWHT-00071,F217-GBS1-PWHT-00072,F217-GBS1-PWHT-00073,F217-GBS1-PWHT-00074";

        String[] reportArr = reportStr.split(",");

        for(String report:reportArr) {
            try {
                Process process = Runtime.getRuntime().exec("cp /var/www/reports/pwht/aa.pdf /var/www/reports/pwht/" + report + ".pdf");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        LongUtils.parseLong("12345L");
//        LongUtils.parseLong(null);
        System.out.println("abc");
        List<Long> tl =new ArrayList<>();
        tl.add(null);
        List<String> t1;
        List<InspectParty> t2 = null;// new ArrayList<>();
//        t2.add(InspectParty.OTHER);
        t1 = t2.stream().map(inspectParty->inspectParty.name()).collect(Collectors.toList());

        System.out.println(1);


    }

    public static String toString(Long longValue) {
        return longValue == null ? null:longValue.toString();
    }
}
