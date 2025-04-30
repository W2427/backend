package com.ose.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.vo.ValueObject;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务代码数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BizCodeDTO extends BaseDTO {

    private static final long serialVersionUID = -1409733479519235452L;

    @Schema(description = "业务代码")
    private String code;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "说明")
    private String description;

    public BizCodeDTO() {
    }

    public BizCodeDTO(ValueObject bizCode) {
        this.code = bizCode.name();
        this.name = bizCode.getDisplayName();
        this.description = bizCode.getDescription();
    }

    /**
     * 将业务代码数组转为业务代码列表。
     *
     * @param bizCodes 业务代码数组
     * @return 业务代码列表
     */
    public static List<BizCodeDTO> list(ValueObject[] bizCodes) {

        List<BizCodeDTO> bizCodeList = new ArrayList<>();

        for (ValueObject bizCode : bizCodes) {
            bizCodeList.add(new BizCodeDTO(bizCode));
        }

        return bizCodeList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
