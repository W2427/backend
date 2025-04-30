package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 采购包相关的实体子类型DTO
 */
public class MmPurchasePackageEntitySubTypeDTO extends BaseDTO {

    private static final long serialVersionUID = 2843518467923172875L;

    @Schema(description = "中文名")
    private String nameCn;

    @Schema(description = "英文名")
    private String nameEn;

    @Schema(description = "id")
    private Long id;

    public MmPurchasePackageEntitySubTypeDTO() {
    }

    public MmPurchasePackageEntitySubTypeDTO(String nameCn, String nameEn, Long id) {
        this.nameCn = nameCn;
        this.nameEn = nameEn;
        this.id = id;

    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
