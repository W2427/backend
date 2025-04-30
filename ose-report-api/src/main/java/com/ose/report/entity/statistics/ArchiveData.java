package com.ose.report.entity.statistics;

import com.ose.util.BeanUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.util.Map;

/**
 * 统计数据归档记录数据实体。
 */
@Entity
@Table(
    name = "statistics",
    indexes = {
        @Index(columnList = "projectId,archiveType,scheduleType,archiveYear,archiveMonth,archiveDay"),
        @Index(columnList = "projectId,archiveType,scheduleType,archiveYear,archiveMonth,archiveDay,groupYear,groupMonth,groupDay"),
        @Index(columnList = "projectId,archiveType,scheduleType,archiveYear,archiveWeek"),
        @Index(columnList = "projectId,archiveType,scheduleType,archiveYear,archiveWeek,groupYear,groupWeek"),
        @Index(columnList = "projectId,archiveType,scheduleType,archiveYear,archiveMonth"),
        @Index(columnList = "projectId,archiveType,scheduleType,archiveYear,archiveMonth,groupYear,groupMonth")
    }
)
public class ArchiveData extends ArchiveDataBase {

    private static final long serialVersionUID = -5967182649815431641L;

    public ArchiveData() {

    }

    public ArchiveData(Map<String, Object> record) {
        BeanUtils.copyProperties(record, this);
    }
}
