package com.ose.auth.entity;

import com.ose.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class OrganizationUserPosition extends BaseEntity {

    private static final long serialVersionUID = 4010872015667754671L;

    private Integer positionId;

    private String positionName;

    private Long organizationId;

    private Long userId;

    private String userName;
}
