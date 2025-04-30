package com.ose.auth.domain.model.repository;


import com.ose.auth.dto.CompanyDTO;
import com.ose.auth.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface DepartmentRepositoryCustom {
    Page<Department> search(CompanyDTO dto, Pageable pageable);
}
