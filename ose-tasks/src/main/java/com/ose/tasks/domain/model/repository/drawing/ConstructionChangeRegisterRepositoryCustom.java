package com.ose.tasks.domain.model.repository.drawing;

import com.ose.tasks.dto.drawing.DesignChangeCriteriaDTO;
import com.ose.tasks.entity.ConstructionChangeRegister;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface ConstructionChangeRegisterRepositoryCustom {

    Page<ConstructionChangeRegister> searchConstructionChangeRegisterList(Long orgId, Long projectId,
                                                                          Pageable pageable, DesignChangeCriteriaDTO criteriaDTO);

}
