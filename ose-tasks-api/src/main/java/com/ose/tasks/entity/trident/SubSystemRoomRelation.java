package com.ose.tasks.entity.trident;

import com.ose.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table( name ="sub_system_room_relation" )
public class SubSystemRoomRelation extends BaseEntity {

    private static final long serialVersionUID = -7225579989320212419L;
    @Column
    private Long projectId;

    @Column //EntityId
    private Long subSystemId;

    @Column
    private String subSystemNo;


    @Column
    private String roomNo;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getSubSystemId() {
        return subSystemId;
    }

    public void setSubSystemId(Long subSystemId) {
        this.subSystemId = subSystemId;
    }

    public String getSubSystemNo() {
        return subSystemNo;
    }

    public void setSubSystemNo(String subSystemNo) {
        this.subSystemNo = subSystemNo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }
}
