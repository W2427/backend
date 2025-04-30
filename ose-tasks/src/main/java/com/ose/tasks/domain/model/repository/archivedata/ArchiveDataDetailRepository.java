package com.ose.tasks.domain.model.repository.archivedata;

import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import org.springframework.data.repository.PagingAndSortingRepository;



public interface ArchiveDataDetailRepository extends PagingAndSortingRepository<BpmActivityInstanceBase, Long>, ArchiveDataDetailRepositoryCustom {

}
