package com.ose.report.entity.statistics;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ose.constant.JsonFormatPattern;
import com.ose.entity.BaseStrEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * WBS 分组汇总 日期表。
 */
@Entity
@Table(name = "statistic_date")
public class StatisticDate extends BaseStrEntity {


    private static final long serialVersionUID = -2942191501173417667L;


    @Schema(description = "MD5 ID ,32 length")
    @Column(length = 32)
    private String id;

    @Schema(description = "项目 ID")
    private Long projectId;

    @Schema(description = "统计分组类型 WBS_PROGRESS_GROUP")
    private String archieveType;

    @Schema(description = "统计类型，日周月")
    private String scheduleType;

    @Schema(description = "分组日期 20190901")
    private Integer groupDate;

//    @Schema(description = "删除时间")
//    @Column
//    @JsonFormat(
//            shape = JsonFormat.Shape.STRING,
//            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
//    )
//    private Date currentDate;

    @Schema(description = "最后更新时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    private Date lastModifiedAt;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getArchieveType() {
        return archieveType;
    }

    public void setArchieveType(String archieveType) {
        this.archieveType = archieveType;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public Integer getGroupDate() {
        return groupDate;
    }

    public void setGroupDate(Integer groupDate) {
        this.groupDate = groupDate;
    }

//    public Date getCurrentDate() {
//        return currentDate;
//    }

//    public void setCurrentDate(Date currentDate) {
//        this.currentDate = currentDate;
//    }

    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
