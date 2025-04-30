package com.ose.issues.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.util.LongUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Entity
@Table(name = "issue_participants")
public class IssueParticipant extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 4152368344931626185L;

    @Column
    private Long issueId;

    @Column
    private Long userId;

    @Column(length = 20)
    private String roles;

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getRoles() {

        if (this.roles == null) {
            return new ArrayList<>();
        }
        List<Long> tmpRoleIds = new ArrayList<>();

        Arrays.asList(this.roles.split(",")).forEach(
            tmpRoleId -> {
                tmpRoleIds.add(LongUtils.parseLong(tmpRoleId));
            }
        );
        return tmpRoleIds;
    }

    public void setRoles(List<String> roles) {

        if (roles == null || roles.size() == 0) {
            this.roles = null;
            return;
        }

        StringBuilder rolesInfo = new StringBuilder();
        for (String role : roles) {
            rolesInfo = rolesInfo.append(role).append(",");
        }
        this.roles = rolesInfo.delete(rolesInfo.length() - 1, rolesInfo.length()).toString();
    }

    /**
     * 取得参与者引用信息。
     *
     * @return 参与者引用信息
     */
    @JsonProperty(value = "userId", access = READ_ONLY)
    public ReferenceData getUserIdRef() {
        return this.userId == null
            ? null
            : new ReferenceData(this.userId);
    }

    /**
     * 取得相关联的用户 ID 的集合。
     *
     * @return 相关联的用户 ID 的集合
     */
    @Override
    public Set<Long> relatedUserIDs() {

        Set<Long> userIDs = new HashSet<>();

        if (this.userId != null && this.userId != 0L) {
            userIDs.add(this.userId);
        }

        return userIDs;
    }
}
