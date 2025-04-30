package com.ose.tasks.domain.model.repository.deliverydoc;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ose.tasks.entity.deliverydoc.DeliveryDocument;


public interface DeliveryDocumentRepository extends PagingAndSortingWithCrudRepository<DeliveryDocument, Long>, DeliveryDocumentRepositoryCustom {

    DeliveryDocument findByProjectIdAndModuleAndProcessAndTypeAndDeleted(
        Long projectId,
        String module,
        String process,
        String type,
        boolean deleted
    );

}
