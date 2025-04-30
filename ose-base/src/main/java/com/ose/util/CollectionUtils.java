package com.ose.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 集合工具。
 */
public class CollectionUtils {

    /**
     * 判断集合是否为空。
     *
     * @param c 集合
     * @return 是否为空
     */
    public static boolean isEmpty(Collection c) {
        return c == null || c.isEmpty();
    }

    public static Collection getSection(Collection c1, Collection c2) {
        return (Collection) c1.stream().filter(item -> c2.contains(item)).collect(toList());

    }
    /**
     * 使用list自带的sort方法先进性排序，然后转成toString去判断两个集合是否相等
     * 方法6
     */
    public static boolean checkDiffrent5(List<String> list, List<String> list1) {
//        long st = System.nanoTime();
//        System.out.println("消耗时间为： " + (System.nanoTime() - st));
        list.sort(Comparator.comparing(String::hashCode));
        list1.sort(Comparator.comparing(String::hashCode));
        return list.toString().equals(list1.toString());
    }

}
