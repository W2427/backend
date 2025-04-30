package com.ose.tasks.entity.material;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.ose.entity.BaseBizEntity;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "mat_coding_template_detail")
public class MaterialCodingTemplateDetail extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long matCodingTemplatesId;

    //材料编码详情模板id 4P/12:13:14:15:16:17:18-4
    @Column(name = "template_detail_id")
    private String templateDetailCode;

    //材料编码模板id
    @Column(name = "template_id")
    private String templateCode;

/*    //路径
    private String codeTemplatePath;*/

    //描述
    private String codeTemplateDesc;

    //序号
    private int sectionNo;

    //起始位数
    private int startFigure;

    //长度
    private int codeLength;

    //代码描述
    private String codeDesc;

    //默认值
    private String defaultValue;

    //biz code 表
    private String disciplineLib;

    //备注
    private String remark;

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

    public int getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(int codeLength) {
        this.codeLength = codeLength;
    }

    public String getTemplateDetailCode() {
        return templateDetailCode;
    }

    public void setTemplateDetailCode(String templateDetailCode) {
        this.templateDetailCode = templateDetailCode;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public Long getMatCodingTemplatesId() {
        return matCodingTemplatesId;
    }

    public void setMatCodingTemplatesId(Long matCodingTemplatesId) {
        this.matCodingTemplatesId = matCodingTemplatesId;
    }
}
