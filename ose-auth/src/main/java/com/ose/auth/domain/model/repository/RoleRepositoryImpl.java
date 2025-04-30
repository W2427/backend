package com.ose.auth.domain.model.repository;

import com.ose.auth.dto.RoleCriteriaDTO;
import com.ose.auth.entity.Role;
import com.ose.auth.entity.UserBasic;
import com.ose.repository.BaseRepository;
import com.ose.util.PrivilegeUtils;
import com.ose.util.RegExpUtils;
import com.ose.util.SQLUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.*;

public class RoleRepositoryImpl extends BaseRepository implements RoleRepositoryCustom {

    // 根据权限查询用户的 SQL 语句
    @SuppressWarnings("JpaQlInspection")
    private static final String SQL_FIND_USERS_BY_PRIVILEGE = "SELECT DISTINCT u"
        + " FROM com.ose.auth.entity.Organization o"
        + " INNER JOIN com.ose.auth.entity.Organization t"
        + "    ON (t.id = o.id OR t.path LIKE CONCAT(o.path, o.id, '/%'))"
        + "   AND t.deleted = false"
        + " INNER JOIN com.ose.auth.entity.Role r"
        + "    ON r.organizationId = t.id"
        + "   AND r.deleted = false"
        + " INNER JOIN com.ose.auth.entity.UserRole ur"
        + "    ON ur.roleId = r.id"
        + "   AND ur.deleted = false"
        + " INNER JOIN com.ose.auth.entity.UserBasic u"
        + "    ON u.id = ur.userId"
        + "   AND u.deleted = false"
        + " WHERE o.id = :orgId";

    /**
     * 查询角色。
     *
     * @param organizationId  所属组织 ID
     * @param roleCriteriaDTO 角色查询条件
     * @param pageable        分页参数
     * @return 角色分页数据
     */
    @Override
    public Page<Role> search(Long organizationId, RoleCriteriaDTO roleCriteriaDTO, Pageable pageable) {

        SQLQueryBuilder<Role> sqlQueryBuilder = getSQLQueryBuilder(Role.class)
            .is("organizationId", organizationId)
            .like("name", roleCriteriaDTO.getKeyword())
            .is("code", roleCriteriaDTO.getCode())
            .in("status", roleCriteriaDTO.getStatus());

        if (roleCriteriaDTO.getIsTemplate() != null && roleCriteriaDTO.getIsTemplate().equals("true")) {
            sqlQueryBuilder.is("isTemplate", true);
        } else {
            sqlQueryBuilder.is("isTemplate", false);
        }

        return sqlQueryBuilder
            .paginate(pageable)
            .exec()
            .page();
    }

    /**
     * 根据权限取得用户信息。
     *
     * @param orgId          组织 ID
     * @param teamPrivileges 组织-权限映射表
     * @return 用户信息列表
     */
    @Override
    public List<UserBasic> findUsersByPrivilege(Long orgId, Map<Long, Set<String>> teamPrivileges) {

        /*----------------------------------------------------------------------
           设置工作组权限条件 START
             (
               r.privileges LIKE '%,all,%'
               OR r.privileges LIKE '%,LEVEL1,%'
               OR r.privileges LIKE '%,LEVEL1/LEVEL2,%'
               ...
             )
         ---------------------------------------------------------------------*/

        List<String> teamCriteria = new ArrayList<>();

        for (Map.Entry<Long, Set<String>> entry : teamPrivileges.entrySet()) {

            if (!RegExpUtils.isDecID(entry.getKey().toString())) {
                continue;
            }

            Set<String> privileges = new HashSet<>();

            for (String privilege : teamPrivileges.get(entry.getKey())) {
                if (RegExpUtils.isPrivilege(privilege)) {
                    privileges.addAll(PrivilegeUtils.resolveNamespace(privilege));
                }
            }

            List<String> privilegeCriteria = new ArrayList<>();

            privilegeCriteria.add("r.privileges LIKE '%,all,%'");

            for (String privilege : privileges) {
                privilegeCriteria.add("r.privileges LIKE '%," + SQLUtils.escapeLike(privilege) + ",%'");
            }

//            teamCriteria.add(
//                "(t.id = '" + entry.getKey() + "' OR t.path LIKE '%/" + entry.getKey() + "/%')"
//                    + " AND (" + String.join(" OR ", privilegeCriteria) + ")"
//            );
            teamCriteria.add(
                "(t.id = " + entry.getKey() + " OR t.path LIKE '%/" + entry.getKey() + "/%')"
                    + " AND (" + String.join(" OR ", privilegeCriteria) + ")"
            );


        }

        /*----------------------------------------------------------------------
           设置工作组权限条件 END
         ---------------------------------------------------------------------*/

        EntityManager entityManager = getEntityManager();

        TypedQuery<UserBasic> query = entityManager
            .createQuery(
                SQL_FIND_USERS_BY_PRIVILEGE + " AND (" + String.join(" OR ", teamCriteria) + ")",
                UserBasic.class
            );

        query.setParameter("orgId", orgId);

        return query.getResultList();
    }

    /**
     * 根据组织和权限集合获取角色列表。
     *
     * @param orgId      组织ID
     * @param privileges 权限集合
     * @return 角色列表
     */
    @Override
    @SuppressWarnings("JpaQlInspection")
    public List<Role> findByOrgAndPrivileges(Long orgId, Set<String> privileges) {

        String sql = "SELECT r "
            + "FROM "
            + "Role r LEFT JOIN Organization o ON o.id = r.organizationId "
            + "LEFT JOIN Organization AS po ON (o.path LIKE concat('%', po.id, '%') OR o.id = po.id) "
            + "WHERE "
            + "r.deleted = false AND o.deleted = false AND po.deleted = false AND po.id = :orgId ";

        List<String> privilegeCriteria = new ArrayList<>();

        privileges.add("all");

        for (String privilege : privileges) {
            privilegeCriteria.add(" r.privileges LIKE '%" + privilege + "%' ");
        }

        EntityManager entityManager = getEntityManager();

        System.out.println(sql + " AND (" + String.join(" OR ", privilegeCriteria) + ")");
        TypedQuery<Role> query = entityManager
            .createQuery(
                sql + " AND (" + String.join(" OR ", privilegeCriteria) + ")",
                Role.class
            );

        query.setParameter("orgId", orgId);

        return query.getResultList();
    }

}
