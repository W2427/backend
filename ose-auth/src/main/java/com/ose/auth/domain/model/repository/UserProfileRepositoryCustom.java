package com.ose.auth.domain.model.repository;

import com.ose.auth.dto.UserCriteriaDTO;
import com.ose.auth.dto.UserProjectDTO;
import com.ose.auth.dto.UserProjectSearchDTO;
import com.ose.auth.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 用户查询接口。
 */
public interface UserProfileRepositoryCustom {

    /**
     * 查询用户。
     *
     * @param criteriaDTO 查询条件
     * @param pageable    分页参数
     * @return 用户查询结果分页数据
     */
    Page<UserProfile> search(UserCriteriaDTO criteriaDTO, Pageable pageable);

    Page<UserProjectDTO> searchProject(Long userId, UserProjectSearchDTO criteriaDTO);

}
