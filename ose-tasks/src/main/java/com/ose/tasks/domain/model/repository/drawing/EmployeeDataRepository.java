package com.ose.tasks.domain.model.repository.drawing;

import com.ose.tasks.dto.EmployeeDataDTO;
import com.ose.tasks.entity.drawing.DingTalkEmployeeData;
import com.ose.tasks.entity.drawing.EmployeeData;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface EmployeeDataRepository extends PagingAndSortingRepository<EmployeeData, Long>, EmployeeDataRepositoryCustom {

//    EmployeeDataDTO findById(Long id);


}
