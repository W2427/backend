package com.ose.auth.domain.model.repository;


import com.ose.auth.dto.CompanyDTO;
import com.ose.auth.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface TeamRepositoryCustom {
    Page<Team> search(CompanyDTO dto, Pageable pageable);
}
