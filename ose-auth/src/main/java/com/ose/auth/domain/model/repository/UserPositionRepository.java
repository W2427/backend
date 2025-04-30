package com.ose.auth.domain.model.repository;

import com.ose.auth.entity.UserPosition;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface UserPositionRepository extends ListCrudRepository<UserPosition, Integer> {
    List<UserPosition> findByIdIn(List<Integer> positionIds);
}
