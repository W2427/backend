package com.ose.tasks.domain.model.repository.setting;

import com.ose.tasks.entity.setting.Discipline;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


/**
 * Discipline CRUD 操作接口。
 */
public interface DisciplineRepository extends PagingAndSortingRepository<Discipline, Long> {


    List<Discipline> findByProjectId(Long projectId);

}
