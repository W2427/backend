package com.ose.report.dto.statistics;

import com.ose.dto.BaseDTO;
import com.ose.report.vo.ArchiveDataGroupKey;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 统计数据归档时间。
 */
public class ArchiveDataGroupKeyDTO extends BaseDTO {

    private static final long serialVersionUID = 2579366330956934234L;

    @Schema(description = "聚合 KEY 类型")
    private String type;

    @Schema(description = "值列表")
    private List<String> values;

    public ArchiveDataGroupKeyDTO() {
    }

    public ArchiveDataGroupKeyDTO(ArchiveDataGroupKey type, List<String> values) {
        setType(type.getPropertyName());
        setValues(values);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

}
