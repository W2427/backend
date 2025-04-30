package com.ose.tasks.dto.wbs;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * wbs entries plain drawing DTO
 */
@SqlResultSetMapping
    (
        name = "WBSEntryDwgDTOSqlResultMapping",
        entities = {
            @EntityResult(
                entityClass = WBSEntryDwgDTO.class, //就是当前这个类的名字
                fields = {
                    @FieldResult(name = "subDrawingId", column = "sub_drawing_id"),
                    @FieldResult(name = "issueStatus", column = "issue_status"),
                    @FieldResult(name = "subDrawingNo", column = "sub_drawing_no"),
                    @FieldResult(name = "subDrawingVersion", column = "sub_drawing_version"),
                    @FieldResult(name = "markDeleted", column = "mark_deleted"),
                    @FieldResult(name = "pageCount", column = "page_count"),
                    @FieldResult(name = "pageNo", column = "dwg_sht_no"),
                    @FieldResult(name = "isoNo", column = "iso_no"),
                }
            )
        }
    )
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WBSEntryDwgDTO {

    @Schema(description = "SUB DRAWING ID")
    private Long subDrawingId;

    @Schema(description = "ISSUE STATUS")
    private Boolean issueStatus;

    @Schema(description = "SUB DRAWING NO")
    private String subDrawingNo;

    @Schema(description = "SUB DRAWING VERSION")
    private Integer subDrawingVersion;

    @Schema(description = "MARK DELETED")
    private Integer markDeleted;

    @Schema(description = "PAGE COUNT")
    private Integer pageCount;

    @Schema(description = "PAGE NO")
    private Integer pageNo;

    @Schema(description = "ISO NO")
    @Id
    private String isoNo;


    public Long getSubDrawingId() {
        return subDrawingId;
    }

    public void setSubDrawingId(Long subDrawingId) {
        this.subDrawingId = subDrawingId;
    }

    public Boolean getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(Boolean issueStatus) {
        this.issueStatus = issueStatus;
    }

    public String getSubDrawingNo() {
        return subDrawingNo;
    }

    public void setSubDrawingNo(String subDrawingNo) {
        this.subDrawingNo = subDrawingNo;
    }

    public Integer getSubDrawingVersion() {
        return subDrawingVersion;
    }

    public void setSubDrawingVersion(Integer subDrawingVersion) {
        this.subDrawingVersion = subDrawingVersion;
    }

    public Integer getMarkDeleted() {
        return markDeleted;
    }

    public void setMarkDeleted(Integer markDeleted) {
        this.markDeleted = markDeleted;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getIsoNo() {
        return isoNo;
    }

    public void setIsoNo(String isoNo) {
        this.isoNo = isoNo;
    }
}
