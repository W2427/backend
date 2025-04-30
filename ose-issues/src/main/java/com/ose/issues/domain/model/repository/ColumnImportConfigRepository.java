package com.ose.issues.domain.model.repository;

import com.ose.issues.entity.ColumnImportConfig;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ColumnImportConfigRepository extends PagingAndSortingWithCrudRepository<ColumnImportConfig, Long> {


    ColumnImportConfig findByProjectIdAndColumnNameAndStatus(Long projectId, String columnName, EntityStatus active);

    ColumnImportConfig findByProjectIdAndClazzNameAndColumnNameAndStatus(Long projectId, String clazzName, String columnName, EntityStatus active);

    List<ColumnImportConfig> findByProjectIdAndClazzNameAndStatusOrderByColumnNoDesc(Long projectId, String clazzName, EntityStatus active);
}
