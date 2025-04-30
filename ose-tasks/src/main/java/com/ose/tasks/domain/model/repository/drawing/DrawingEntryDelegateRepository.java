package com.ose.tasks.domain.model.repository.drawing;

import com.ose.auth.vo.UserPrivilege;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.DrawingEntryDelegate;
import com.ose.vo.EntityStatus;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 *
 */
@Transactional
public interface DrawingEntryDelegateRepository extends PagingAndSortingWithCrudRepository<DrawingEntryDelegate, Long> {

    DrawingEntryDelegate findByDrawingIdAndPrivilegeAndStatus(Long drawingId, UserPrivilege privilege, EntityStatus status);

    List<DrawingEntryDelegate> findByDrawingIdAndStatus(Long drawingId,EntityStatus status);
    @Query(nativeQuery = true,
        value = "SELECT " +
            "u.name " +
            "FROM " +
            "drawing_entry_delegate ded " +
            "LEFT JOIN saint_whale_auth.users u ON u.id = ded.user_id " +
            "WHERE " +
            "ded.status = :status " +
            "AND ded.drawing_id = :drawingId  " +
            "AND ded.privilege = :privilege ")
    String findNameByDrawingIdAndPrivilegeAndStatus(Long drawingId, String privilege, String status);
}
