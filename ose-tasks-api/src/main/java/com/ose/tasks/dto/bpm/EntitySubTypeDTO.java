package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体类型 数据传输对象
 */
public class EntitySubTypeDTO extends BaseDTO {


    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    // 实体名称
    private String nameCn;

    // 实体名称-英文
    private String nameEn;

    // 备注
    private String memo;

    private Long entityTypeId;

    // 实体业务类型ID
    private Long entityBusinessTypeId;

    private boolean subDrawingFlg = false;

    @Schema(description = "子图纸二维码打印位置")
    private int drawingPositionX;

    @Schema(description = "子图纸二维码打印位置")
    private int drawingPositionY;

    @Schema(description = "子图纸简写码打印位置")
    private int drawingShortX;

    @Schema(description = "子图纸简写码打印位置")
    private int drawingShortY;

    @Schema(description = "子图纸二维码打印大小")
    private int drawingScaleToFit;

    @Schema(description = "封面二维码打印位置")
    private int coverPositionX;

    @Schema(description = "封面二维码打印位置")
    private int coverPositionY;

    @Schema(description = "封面二维码打印大小")
    private int coverScaleToFit;

    @Schema(description = "check清单文件临时名")
    private List<String> checkListFileName;

    @Schema(description = "专业")
    private String discipline = "PIPING";


    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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

    public Long getEntityTypeId() {
        return entityTypeId;
    }

    public void setEntityTypeId(Long entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

    public Long getEntityBusinessTypeId() {
        return entityBusinessTypeId;
    }

    public void setEntityBusinessTypeId(Long entityBusinessTypeId) {
        this.entityBusinessTypeId = entityBusinessTypeId;
    }

    public boolean getSubDrawingFlg() {
        return subDrawingFlg;
    }

    public void setSubDrawingFlg(boolean subDrawingFlg) {
        this.subDrawingFlg = subDrawingFlg;
    }

    public List<String> getCheckListFileName() {
        return checkListFileName;
    }

    public void setCheckListFileName(List<String> checkListFileName) {
        this.checkListFileName = checkListFileName;
    }

    public int getDrawingPositionX() {
        return drawingPositionX;
    }

    public void setDrawingPositionX(int drawingPositionX) {
        this.drawingPositionX = drawingPositionX;
    }

    public int getDrawingPositionY() {
        return drawingPositionY;
    }

    public void setDrawingPositionY(int drawingPositionY) {
        this.drawingPositionY = drawingPositionY;
    }

    public int getDrawingScaleToFit() {
        return drawingScaleToFit;
    }

    public void setDrawingScaleToFit(int drawingScaleToFit) {
        this.drawingScaleToFit = drawingScaleToFit;
    }

    public int getCoverPositionX() {
        return coverPositionX;
    }

    public void setCoverPositionX(int coverPositionX) {
        this.coverPositionX = coverPositionX;
    }

    public int getCoverPositionY() {
        return coverPositionY;
    }

    public void setCoverPositionY(int coverPositionY) {
        this.coverPositionY = coverPositionY;
    }

    public int getCoverScaleToFit() {
        return coverScaleToFit;
    }

    public void setCoverScaleToFit(int coverScaleToFit) {
        this.coverScaleToFit = coverScaleToFit;
    }


    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public int getDrawingShortX() {
        return drawingShortX;
    }

    public void setDrawingShortX(int drawingShortX) {
        this.drawingShortX = drawingShortX;
    }

    public int getDrawingShortY() {
        return drawingShortY;
    }

    public void setDrawingShortY(int drawingShortY) {
        this.drawingShortY = drawingShortY;
    }
}
