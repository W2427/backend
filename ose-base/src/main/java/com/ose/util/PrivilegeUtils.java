package com.ose.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 权限值处理工具。
 */
public class PrivilegeUtils {

    // 权限命名空间分隔符
    public static final String NAMESPACE_DELIMITER = "/";

    /**
     * 解析权限的命名空间，取得所有上级权限值。
     *
     * @param privilege 权限值
     * @return 包含所有上级权限值的集合
     */
    public static Set<String> resolveNamespace(String privilege) {

        Set<String> privileges = new HashSet<>();

        List<String> parents = new ArrayList<>();

        for (String part : privilege.split(NAMESPACE_DELIMITER)) {
            parents.add(part);
            privileges.add(String.join(NAMESPACE_DELIMITER, parents));
        }

        return privileges;
    }


}
