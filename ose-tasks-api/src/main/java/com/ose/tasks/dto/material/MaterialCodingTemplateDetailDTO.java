package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体管理 数据传输对象
 */
public class MaterialCodingTemplateDetailDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    @Schema(description = "材料编码模板id")
    private Long matCodingTemplatesId;

    @Schema(description = "模板描述")
    private String codeTemplateDesc;

    @Schema(description = "序号")
    private int sectionNo;

    @Schema(description = "开始位数")
    private int startFigure;

    @Schema(description = "长度")
    private int codeLength;

    @Schema(description = "描述")
    private String codeDesc;

    @Schema(description = "默认值")
    private String defaultValue;

    @Schema(description = "码表table")
    private String disciplineLib;

    @Schema(description = "备注")
    private String remark;


    public Long getMatCodingTemplatesId() {
        return matCodingTemplatesId;
    }

    public void setMatCodingTemplatesId(Long matCodingTemplatesId) {
        this.matCodingTemplatesId = matCodingTemplatesId;
    }

    public String getCodeTemplateDesc() {
        return codeTemplateDesc;
    }

    public void setCodeTemplateDesc(String codeTemplateDesc) {
        this.codeTemplateDesc = codeTemplateDesc;
    }

    public int getSectionNo() {
        return sectionNo;
    }

    public void setSectionNo(int sectionNo) {
        this.sectionNo = sectionNo;
    }

    public int getStartFigure() {
        return startFigure;
    }

    public void setStartFigure(int startFigure) {
        this.startFigure = startFigure;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(int codeLength) {
        this.codeLength = codeLength;
    }

    public String getCodeDesc() {
        return codeDesc;
    }

    public void setCodeDesc(String codeDesc) {
        this.codeDesc = codeDesc;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDisciplineLib() {
        return disciplineLib;
    }

    public void setDisciplineLib(String disciplineLib) {
        this.disciplineLib = disciplineLib;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
