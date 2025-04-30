package com.ose.tasks.dto.numeric;

import com.ose.tasks.vo.qc.NDEType;
import com.ose.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 无损探伤抽检百分比值对象。
 */
public class NDERatioDTO {

    private static final Pattern PATTERN = Pattern.compile(
        "(RT|PT|MT|UT)?(\\s+|/)?(100(\\.0+)?|\\d{1,2}(\\.\\d+)?)?\\s*%?",
        Pattern.CASE_INSENSITIVE
    );

    // 无损探伤抽检百分比
    private Integer ratio = null;

    // 无损探伤类型
    private NDEType type = null;

    /**
     * 构造方法。
     */
    public NDERatioDTO(String string) {

        string = StringUtils.trim(string);

        if (StringUtils.isEmpty(string)) {
            return;
        }

        Matcher m = PATTERN.matcher(string);

        if (m.matches()) {

            if (m.group(3) != null) {
                ratio = Integer.valueOf(m.group(3));
            }

            if (m.group(1) != null) {
                type = NDEType.valueOf(m.group(1));
            }

        }

    }

    public Integer getRatio() {
        return ratio;
    }

    public NDEType getType() {
        return type;
    }

}
