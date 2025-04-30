package com.ose.tasks.domain.model.repository.xlsximport;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.xlsximport.ColumnImportConfig;
import com.ose.vo.EntityStatus;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CommonRepository extends PagingAndSortingWithCrudRepository<ColumnImportConfig, Long>, CommonRepositoryCustom {


    ColumnImportConfig findByProjectIdAndColumnNameAndStatus(Long projectId, String columnName, EntityStatus active);

    ColumnImportConfig findByProjectIdAndClazzNameAndColumnNameAndStatus(Long projectId, String clazzName, String columnName, EntityStatus active);

    List<ColumnImportConfig> findByProjectIdAndClazzNameAndStatusOrderByColumnNoDesc(Long projectId, String clazzName, EntityStatus active);
}
