package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.UserSignature;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 电子签名 CRUD 操作接口。
 */
public interface UserSignatureRepository extends PagingAndSortingWithCrudRepository<UserSignature, Long> {

    UserSignature findByUserId(Long userId);
}
