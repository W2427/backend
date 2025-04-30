package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 生产设计图纸清单导入数据传输对象。
 */
public class DrawingCategoryDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "英文名称")
    private String nameEn;

    @Schema(description = "中文名称")
    private String nameCn;

    @Schema(description = "实体类型分类英文名称")
    private String entitiyCategoryTypeNameEn;

    @Schema(description = "实体类型分类中文名称")
    private String entitiyCategoryTypeNameCn;

    @Schema(description = "特征列")
    private String traitColumn;

    @Schema(description = "特征字符")
    private String traitCharacter;

    @Schema(description = "备注")
    private String memo;

    @Schema(description = "是否是图集类型")
    private boolean isAtlas;

    @Schema(description = "子图纸清单模板")
    private String template;

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getEntitiyCategoryTypeNameEn() {
        return entitiyCategoryTypeNameEn;
    }

    public void setEntitiyCategoryTypeNameEn(String entitiyCategoryTypeNameEn) {
        this.entitiyCategoryTypeNameEn = entitiyCategoryTypeNameEn;
    }

    public String getEntitiyCategoryTypeNameCn() {
        return entitiyCategoryTypeNameCn;
    }

    public void setEntitiyCategoryTypeNameCn(String entitiyCategoryTypeNameCn) {
        this.entitiyCategoryTypeNameCn = entitiyCategoryTypeNameCn;
    }

    public String getTraitColumn() {
        return traitColumn;
    }

    public void setTraitColumn(String traitColumn) {
        this.traitColumn = traitColumn;
    }

    public String getTraitCharacter() {
        return traitCharacter;
    }

    public void setTraitCharacter(String traitCharacter) {
        this.traitCharacter = traitCharacter;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public boolean isAtlas() {
        return isAtlas;
    }

    public void setAtlas(boolean isAtlas) {
        this.isAtlas = isAtlas;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
