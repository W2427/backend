package com.ose.tasks.domain.model.repository;

import com.ose.tasks.entity.WorkCode;
import org.springframework.data.repository.CrudRepository;

public interface WorkCodeRepository extends CrudRepository<WorkCode, Long> {
    WorkCode findAllByWorkCode(String workCode);
}
