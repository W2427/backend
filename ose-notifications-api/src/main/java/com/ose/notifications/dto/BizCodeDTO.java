package com.ose.notifications.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ose.dto.BaseDTO;
import com.ose.vo.ValueObject;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务代码表。
 */
public class BizCodeDTO extends BaseDTO {

    @Schema(description = "业务代码")
    private String code;

    @Schema(description = "名称")
    private String name;

    @JsonCreator
    public BizCodeDTO() {
    }

    private BizCodeDTO(ValueObject bizCode) {
        this.code = bizCode.name();
        this.name = bizCode.getDisplayName();
    }

    /**
     * 将业务代码数组转为业务代码列表。
     *
     * @param bizCodes 业务代码数组
     * @return 业务代码列表
     */
    public static List<BizCodeDTO> list(ValueObject[] bizCodes) {

        List<BizCodeDTO> bizCodeList = new ArrayList<>();

        if (bizCodes == null) {
            return bizCodeList;
        }

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

}
