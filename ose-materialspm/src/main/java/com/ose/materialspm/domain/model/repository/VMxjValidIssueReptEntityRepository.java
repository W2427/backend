package com.ose.materialspm.domain.model.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ose.materialspm.entity.VMxjValidIssueReptEntity;


/**
 * 请购单查询接口。
 */
@Transactional
public interface VMxjValidIssueReptEntityRepository extends PagingAndSortingRepository<VMxjValidIssueReptEntity, String>, VMxjValidIssueReptEntityRepositoryCustom {

}
