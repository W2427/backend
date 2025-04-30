package com.ose.auth.domain.model.repository;


import com.ose.auth.entity.Team;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
public interface TeamRepository extends PagingAndSortingWithCrudRepository<Team, Long>,TeamRepositoryCustom {
    List<Team> findByDeletedIsFalse();
    Team findByNameAndDeletedIsFalse(String name);

    List<Team> findByParentTeamNameAndDeletedIsFalse(String parentName);
}
