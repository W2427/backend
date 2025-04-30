package com.ose.issues.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "issues")
public class Experience extends IssueBase {

    private static final long serialVersionUID = -4036147749284196040L;

    /**
     * 取得相关联的用户 ID 的集合。
     *
     * @return 相关联的用户 ID 的集合
     */
    @Override
    public Set<Long> relatedUserIDs() {

        Set<Long> userIDs = new HashSet<>();

        if (this.getCreatedBy() != null && this.getCreatedBy() != 0L) {
            userIDs.add(this.getCreatedBy());
        }
        if (this.getLastModifiedBy() != null && this.getLastModifiedBy() != 0L) {
            userIDs.add(this.getLastModifiedBy());
        }

        return userIDs;
    }

}
