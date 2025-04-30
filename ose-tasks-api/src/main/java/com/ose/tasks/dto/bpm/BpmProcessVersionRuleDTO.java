package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

import java.io.Serial;

/**
 * 版本规则 数据传输对象
 */
public class BpmProcessVersionRuleDTO extends BaseDTO {

    @Serial
    private static final long serialVersionUID = 6765646397654716348L;

    private String ruleType;

    private String fixedPrefix;

    private String fixedCharacter;

    private String startPoint;

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getFixedPrefix() {
        return fixedPrefix;
    }

    public void setFixedPrefix(String fixedPrefix) {
        this.fixedPrefix = fixedPrefix;
    }

    public String getFixedCharacter() {
        return fixedCharacter;
    }

    public void setFixedCharacter(String fixedCharacter) {
        this.fixedCharacter = fixedCharacter;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }
}
