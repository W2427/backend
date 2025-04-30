package com.ose.tasks.domain.model.repository.pdfannotation;

import com.ose.tasks.entity.pdfAnnotation.PdfAnnotation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * Pdf Annotation CRUD 操作接口。
 */
public interface PdfAnnotationRepository extends PagingAndSortingRepository<PdfAnnotation, Long> {

    List<PdfAnnotation> findByProjectIdAndDocIdAndPageNoAndDeletedIsFalse(Long projectId, Long docId, Integer pageNo);
    List<PdfAnnotation> findByProjectIdAndDocIdAndDeletedIsFalse(Long projectId, Long docId);
    List<PdfAnnotation> findByProjectId(Long projectId);

    PdfAnnotation findByProjectIdAndAnnotationUuid(Long projectId, String annotationUuid);

    @Query("SELECT pa.id FROM PdfAnnotation pa WHERE pa.projectId = :projectId AND " +
        "pa.annotationUuid = :annotationUuid AND pa.deleted IS FALSE")
    Long findAnnotationIdByProjectIdAndAnnotationUuid(@Param("projectId") Long projectId,
                                                      @Param("annotationUuid") String annotationUuid);
}
