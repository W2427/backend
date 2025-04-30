package com.ose.issues.domain.model.repository;

import com.ose.issues.entity.ExperienceMail;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface ExperienceMailRepository extends PagingAndSortingWithCrudRepository<ExperienceMail, Long> {

    /**
     * 获取经验教训邮件详情。
     *
     * @param experienceMailId 经验教训ID
     * @return 邮件详情
     */
    ExperienceMail findByIdAndDeletedIsFalse(Long experienceMailId);


    /**
     * 获取经验教训邮件详情。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return 邮件列表
     */
    Page<ExperienceMail> findByOrgIdAndProjectIdAndDeletedIsFalse(Long orgId, Long projectId, Pageable pageable);
}
