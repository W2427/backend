package com.ose.auth.entity;

import com.ose.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class UserPosition extends BaseEntity {

    private static final long serialVersionUID = 4010872015667754671L;

    private String name;

    private String level;

    private String parent;
}
