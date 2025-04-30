package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.ExportExcel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 项目 CRUD 操作接口。
 */
public interface ExportExcelRepository extends PagingAndSortingWithCrudRepository<ExportExcel, Long>, ExportExcelRepositoryCustom {


    /**
     * 根据所属组织,项目查询 导出excel列表。
     *
     * @param orgId    所属组织 ID
     * @param pageable 分页参数
     * @return 项目分页参数
     */
    Page<ExportExcel> findByOrgIdAndProjectIdAndDeletedIsFalse(Long orgId, Long projectId, Pageable pageable);


    @Query("select e from ExportExcel e where e.orgId=:orgId and e.projectId=:projectId and e.excelViewName =:exportView")
    List<ExportExcel> findByViewName(@Param("orgId") Long orgId, @Param("projectId") Long projectId, @Param("exportView") String exportView);
}
