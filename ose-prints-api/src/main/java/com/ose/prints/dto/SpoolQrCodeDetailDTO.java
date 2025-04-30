package com.ose.prints.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 上传文件的附加信息。
 */
public class SpoolQrCodeDetailDTO extends BaseDTO {


    private static final long serialVersionUID = 8194574818310660466L;

    @Schema(description = "材料编码")
    private String material;

    @Schema(description = "项目名")
    private String project;

    @Schema(description = "单位")
    private String nps;

    @Schema(description = "涂装代码")
    private String paintingCode;

    @Schema(description = "面积(包含规格)")
    private String paintingArea;

    @Schema(description = "重量（包含规格）")
    private String weight;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "二维码")
    private String qrCode;

    @Schema(description = "描述")
    private String desc;

    @Schema(description = "高度")
    private String height;

    @Schema(description = "打印次数")
    private Integer printCount;

    /**
     * 构造方法。
     */
    public SpoolQrCodeDetailDTO() {
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getNps() {
        return nps;
    }

    public void setNps(String nps) {
        this.nps = nps;
    }

    public String getPaintingCode() {
        return paintingCode;
    }

    public void setPaintingCode(String paintingCode) {
        this.paintingCode = paintingCode;
    }

    public String getPaintingArea() {
        return paintingArea;
    }

    public void setPaintingArea(String paintingArea) {
        this.paintingArea = paintingArea;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Integer getPrintCount() {
        return printCount;
    }

    public void setPrintCount(Integer printCount) {
        this.printCount = printCount;
    }

}
