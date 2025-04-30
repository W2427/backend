package com.ose.vo;

import com.ose.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 专业代码。
 */
public enum DisciplineCode implements ValueObject {

    PIPING("PIPING", "管道"),
    ELECTRICAL("ELECTRICAL", "电气"),
    STRUCTURE("STRUCTURE", "结构"),
    MECHANICAL("MECHANICAL", "设备"),
    HVAC("HVAC", "暖通"),
    MATERIAL("MATERIAL", "材料"),
    GENERAL("GENERAL", "通用");

    public static final Set<String> SUPPORTED_DISCIPLINE_STRS = new HashSet<>(Arrays.asList("PIPING", "STRUCTURE", "ELECTRICAL","MECHANICAL", "HVAC", "GENERAL"));

    // 支持的专业
    public static final Set<DisciplineCode> SUPPORTED_DISCIPLINES = new HashSet<>(Arrays.asList(
        DisciplineCode.PIPING,
        DisciplineCode.ELECTRICAL,
        DisciplineCode.STRUCTURE,
        DisciplineCode.HVAC,
        DisciplineCode.GENERAL
    ));


    private String displayName;

    private String description;

    DisciplineCode(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public static boolean isSupportedDiscipline(String disciplineStr) {
        if (StringUtils.isEmpty(disciplineStr)) {
            return false;
        }
        if (!SUPPORTED_DISCIPLINE_STRS.contains(disciplineStr)) {
            return false;
        }
        return true;
    }

    public static boolean isSupportedDiscipline(DisciplineCode disciplineCode) {
        if (disciplineCode == null) {
            return false;
        }
        if (!SUPPORTED_DISCIPLINES.contains(disciplineCode)) {
            return false;
        }
        return true;
    }

    public static Set<DisciplineCode> getSystemDisciplines() {
        Set<DisciplineCode> set = new HashSet<>();
        set.add(DisciplineCode.PIPING);
        set.add(DisciplineCode.ELECTRICAL);
        return set;
    }


}
