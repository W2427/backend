package com.ose.auth.domain.model.service;

import com.ose.auth.entity.UserPosition;
import com.ose.service.EntityInterface;

import java.util.List;

public interface UserPositionInterface  extends EntityInterface {

    List<UserPosition> getAllPositions();
}
