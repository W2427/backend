package com.ose.auth.domain.model.service;

import com.ose.auth.domain.model.repository.UserPositionRepository;
import com.ose.auth.entity.UserPosition;
import com.ose.auth.entity.UserProfile;
import com.ose.dto.BaseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserPositionService implements UserPositionInterface {

    private final UserPositionRepository userPositionRepository;

    @Autowired
    public UserPositionService(UserPositionRepository userPositionRepository) {
        this.userPositionRepository = userPositionRepository;
    }

    @Override
    public List<UserPosition> getAllPositions() {
        return userPositionRepository.findAll();
    }

    /**
     * 设置返回结果的引用数据。
     *
     * @param included 引用数据映射表
     * @param entities 查询结果列表
     * @param <T>      数据实体的泛型
     * @return 引用数据映射表
     */
    @Override
    public <T extends BaseDTO> Map<Long, Object> setIncluded(
        Map<Long, Object> included,
        List<T> entities
    ) {

        if (entities == null || entities.isEmpty()) {
            return included;
        }

        List<Integer> positionIds = new ArrayList<>();
        for (T entity : entities) {
            if (entity instanceof UserProfile) {
                if (((UserProfile) entity).getPositionIds() == null) {
                    continue;
                }
                positionIds.addAll(((UserProfile) entity).getPositionIds());
            }
        }

        if (positionIds.isEmpty()) {
            return included;
        }

        List<UserPosition> positions = userPositionRepository.findByIdIn(positionIds);

        for (UserPosition position : positions) {
            included.put(position.getId(), position);
        }

        return included;
    }
}
