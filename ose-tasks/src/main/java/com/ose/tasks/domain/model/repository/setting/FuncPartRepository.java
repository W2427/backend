package com.ose.tasks.domain.model.repository.setting;

import com.ose.tasks.entity.setting.FuncPart;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


/**
 * Func Part CRUD 操作接口。
 */
public interface FuncPartRepository extends PagingAndSortingRepository<FuncPart, Long> {


    List<FuncPart> findByProjectId(Long projectId);

    FuncPart findByProjectIdAndNameEn(Long projectId, String name);
}
