package com.ose.tasks.domain.model.repository.pdfannotation;

import com.ose.tasks.entity.pdfAnnotation.PdfAnnotationReply;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


/**
 * PDF Annotation Reply CRUD 操作接口。
 */
public interface PdfAnnotationReplyRepository extends PagingAndSortingRepository<PdfAnnotationReply, Long> {


    PdfAnnotationReply findByProjectIdAndReplyUuid(Long projectId, String replyUuid);

    List<PdfAnnotationReply> findByProjectIdAndDocIdAndPageNoAndDeletedIsFalse(Long projectId, Long docId, Integer pageNo);

    List<PdfAnnotationReply> findByProjectIdAndDocIdAndDeletedIsFalse(Long projectId, Long docId);

    List<PdfAnnotationReply> findByProjectIdAndAnnotationUuidAndDeletedIsFalse(Long projectId, String id);
}
