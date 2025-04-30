package com.ose.auth.domain.model.service;

import com.ose.auth.domain.model.repository.*;
import com.ose.auth.dto.RoleCriteriaDTO;
import com.ose.auth.dto.RoleDTO;
import com.ose.auth.entity.*;
import com.ose.dto.BaseDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.DuplicatedError;
import com.ose.exception.NotFoundError;
import com.ose.util.BeanUtils;
import com.ose.util.LongUtils;
import com.ose.util.RegExpUtils;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RoleService implements RoleInterface {

    private final RoleRepository roleRepository;
    private final UserProfileRepository userProfileRepository;
    private final OrganizationRepository organizationRepository;
    private final UserOrganizationRepository userOrganizationRepository;
    private final UserRoleRepository userRoleRepository;

    /**
     * 构造器。
     *
     * @param roleRepository         角色数据仓库
     * @param userProfileRepository  用户数据仓库
     * @param organizationRepository 部门数据仓库
     */
    public RoleService(
        RoleRepository roleRepository,
        UserProfileRepository userProfileRepository,
        OrganizationRepository organizationRepository,
        UserOrganizationRepository userOrganizationRepository,
        UserRoleRepository userRoleRepository
    ) {
        this.roleRepository = roleRepository;
        this.userProfileRepository = userProfileRepository;
        this.organizationRepository = organizationRepository;
        this.userOrganizationRepository = userOrganizationRepository;
        this.userRoleRepository = userRoleRepository;
    }

    /**
     * 创建角色信息。
     *
     * @param operatorId     操作者ID
     * @param organizationId 组织ID
     * @param roleDTO        创建信息
     * @return 角色信息
     */
    @Override
    public Role create(Long operatorId, Long organizationId, RoleDTO roleDTO) {

        Role role = BeanUtils.copyProperties(roleDTO, new Role());

        if (role.getStatus() == null) {
            role.setStatus(EntityStatus.ACTIVE);
        } else if (role.getStatus().equals(EntityStatus.DISABLED)) {
            role.setStatus(EntityStatus.DISABLED);
        }

        // 部门检查
        if (organizationId == null) {
            throw new BusinessError("organizationId is required");
        }

        if (!organizationRepository.existsByIdAndDeletedIsFalse(organizationId)) {
            throw new NotFoundError("organization is not found");
        }

        // 名称检查
        if (role.getName() == null) {
            throw new BusinessError("role's name is required");
        }
        // 名称是否重复
        if (roleRepository.existsByNameAndDeletedIsFalseAndOrganizationId(role.getName(), organizationId)) {
            throw new DuplicatedError("error.duplicate.name");
        }

        // 编号重复
        if (role.getNo() != null && roleRepository.existsByNoAndDeletedIsFalse(role.getNo())) {
            throw new DuplicatedError("error.duplicate.no");
        }

        Organization organization = organizationRepository.findByIdAndDeletedIsFalse(organizationId);

        if (organization == null) {
            throw new NotFoundError("organization is not found");
        }

        if (roleDTO.isTemplate()) {
            role.setTemplate(roleDTO.isTemplate());
        }

        role.setOrganizationId(organization.getId());
        role.setOrgPath(organization.getPath() == null ? "/" + organizationId + "/"
            : organization.getPath() + organizationId + "/");

        // 角色排序
        Role maxSortRole = roleRepository.findTopByDeletedIsFalseOrderBySortDesc();
        int sort = 1;
        if (maxSortRole != null) {
            sort = maxSortRole.getSort() + 1;
        }

        role.setSort(sort);

        Date now = new Date();
        role.setPrivileges(roleDTO.getPrivileges());
        role.setOrganizationId(organizationId);
        role.setCreatedBy(operatorId);
        role.setCreatedAt(now);
        role.setLastModifiedBy(operatorId);
        role.setLastModifiedAt(now);

        return BeanUtils.clone(roleRepository.save(role), Role.class);
    }

    /**
     * 创建system角色信息。
     *
     * @param operatorId     操作者ID
     * @param organizationId 组织ID
     * @param roleDTO        创建信息
     * @return 角色信息
     */
    @Override
    public Role createSystem(Long operatorId, Long organizationId, RoleDTO roleDTO) {

        Role role = BeanUtils.copyProperties(roleDTO, new Role());

        if (role.getStatus() == null) {
            role.setStatus(EntityStatus.ACTIVE);
        } else if (role.getStatus().equals(EntityStatus.DISABLED)) {
            role.setStatus(EntityStatus.DISABLED);
        }

        // 名称检查
        if (role.getName() == null) {
            throw new BusinessError("role's name is required");
        }
        // 名称是否重复
        if (roleRepository.existsByNameAndDeletedIsFalseAndOrganizationId(role.getName(), organizationId)) {
            throw new DuplicatedError("error.duplicate.name");
        }

        // 编号重复
        if (role.getNo() != null && roleRepository.existsByNoAndDeletedIsFalse(role.getNo())) {
            throw new DuplicatedError("error.duplicate.no");
        }

        if (roleDTO.isTemplate()) {
            role.setTemplate(roleDTO.isTemplate());
        }

        role.setOrganizationId(organizationId);

        // 角色排序
        Role maxSortRole = roleRepository.findTopByDeletedIsFalseOrderBySortDesc();
        int sort = 1;
        if (maxSortRole != null) {
            sort = maxSortRole.getSort() + 1;
        }

        role.setSort(sort);

        Date now = new Date();
        role.setPrivileges(roleDTO.getPrivileges());
        role.setOrganizationId(organizationId);
        role.setCreatedBy(operatorId);
        role.setCreatedAt(now);
        role.setLastModifiedBy(operatorId);
        role.setLastModifiedAt(now);

        return BeanUtils.clone(roleRepository.save(role), Role.class);
    }

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    /**
     * 获取角色列表。
     *
     * @param organizationId  组织ID
     * @param roleCriteriaDTO 角色查询参数
     * @param pagination      分页参数
     * @return 角色列表
     */
    @Override
    public Page<Role> search(Long organizationId, RoleCriteriaDTO roleCriteriaDTO, PageDTO pagination) {
        return roleRepository.search(organizationId, roleCriteriaDTO, pagination.toPageable());
    }

    /**
     * 获取角色详情。
     *
     * @param roleId 角色ID
     * @return 角色信息
     */
    @Override
    public Role get(Long roleId) {
        return roleRepository.findByIdAndDeletedIsFalse(roleId);
    }

    /**
     * 更新角色信息。
     *
     * @param operatorId 操作人ID
     * @param roleId     角色ID
     * @param roleDTO    角色信息
     */
    @Override
    public void update(Long operatorId, Long organizationId, Long roleId, RoleDTO roleDTO) {

        Role role = null;

        if (RegExpUtils.isDecID(roleId.toString())) {
            role = roleRepository.findByIdAndOrganizationIdAndDeletedIsFalse(roleId, organizationId);
        }

        if (role == null) {
            throw new NotFoundError("role is not found");
        }

        if (roleDTO.getName() != null) {
            role.setName(roleDTO.getName());
        }

        if (roleDTO.getNo() != null) {
            role.setNo(roleDTO.getNo());
        }

        if (roleDTO.getPrivileges() != null) {
            role.setPrivileges(roleDTO.getPrivileges());
        }

        // 设置排序
        Long targetId = roleDTO.getAfterId();
        if (targetId != null && !targetId.equals(roleId)) {
            // 顺序大小
            int sort;
            Role target;
            // 置顶
            if (targetId.equals("top")) {
                target = roleRepository.findTopByDeletedIsFalseOrderBySortAsc();
                sort = target.getSort() - 1;
            } else {
                target = roleRepository.findByIdAndDeletedIsFalse(targetId);
                sort = target.getSort() + 1;
            }
            role.setSort(sort);
        }

        Date now = new Date();
        role.setLastModifiedAt(now);
        role.setLastModifiedBy(operatorId);

        roleRepository.save(role);
    }

    /**
     * 删除角色。
     *
     * @param operatorId 操作人ID
     * @param roleId     角色ID
     */
    @Override
    public void delete(Long operatorId, Long roleId) {

        Role role = roleRepository.findByIdAndDeletedIsFalse(roleId);

        if (role == null) {
            throw new NotFoundError("role is not found");
        }

        role.setDeleted(true);
        role.setStatus(EntityStatus.DELETED);

        role.setDeletedAt(new Date());
        role.setDeletedBy(operatorId);

        roleRepository.save(role);
    }

    /**
     * 删除角色。
     *
     * @param operatorId 操作人ID
     * @param orgId     组织ID
     */
    @Override
    public void deleteByOrgId(Long operatorId, Long orgId){
        roleRepository.deleteRoleByOrgId(orgId);
    }
    /**
     * 添加成员。
     *
     * @param operatorId     操作人ID
     * @param organizationId 组织ID
     * @param roleId         角色ID
     * @param memberId       成员ID
     */
    @Override
    public void addMember(Long operatorId, Long organizationId, Long roleId, Long memberId) {

        if (memberId == null) {
            throw new BusinessError("memberId is required");
        }
        // 查看成员是否存在
        if (!userProfileRepository.existsByIdAndDeletedIsFalse(memberId)) {
            throw new NotFoundError("user is not found");
        }

        // 查看成员是否已拥有该角色
        if (userRoleRepository.existsByRoleIdAndUserIdAndDeletedIsFalse(roleId, memberId)) {
            throw new BusinessError("user already has the role");
        }

        // 查看角色的部门是否存在
        Role role = roleRepository.findByIdAndOrganizationIdAndDeletedIsFalse(roleId, organizationId);
        if (role == null) {
            throw new NotFoundError("role is not found");
        }
        if (role.getOrganizationId() == null) {
            throw new BusinessError("role's organizationId is required");
        }
        Organization organization = organizationRepository.findByIdAndDeletedIsFalse(role.getOrganizationId());
        if (organization == null) {
            throw new NotFoundError("organization is not found");
        }

        // 查看成员是否属于角色部门或其上级部门
        Boolean userInOrg = userOrganizationRepository
            .existsByUserIdAndOrganizationIdAndDeletedIsFalse(memberId, role.getOrganizationId());

        if (userInOrg == null || !userInOrg) {
            throw new BusinessError("user must be in role's organization");
        }

        UserRole userRole = new UserRole();

        userRole.setRoleId(roleId);
        userRole.setUserId(memberId);
        userRole.setStatus(EntityStatus.ACTIVE);
        userRole.setCreatedAt(new Date());
        userRole.setCreatedBy(operatorId);
        userRole.setLastModifiedAt(new Date());
        userRole.setLastModifiedBy(operatorId);

        userRoleRepository.save(userRole);
    }

    /**
     * 移除成员。
     *
     * @param operatorId     操作人ID
     * @param organizationId 组织ID
     * @param roleId         角色ID
     * @param memberId       成员列表
     */
    @Override
    public void removeMember(Long operatorId, Long organizationId, Long roleId, Long memberId) {

        if (memberId == null || roleId == null) {
            return;
        }

        // 应查询当前组织下所有子组织中是否存在该角色
        // 1.获取所有子组织
        List<Long> allOrganizationIds = new ArrayList<>();
        List<Long> organizationIds = new ArrayList<>();
        List<Organization> allOrganization = organizationRepository.findByDeletedIsFalse();
        for (Organization newOrganization: allOrganization) {
            if (newOrganization.getPath() != null) {
                if (newOrganization.getPath().startsWith("/") && newOrganization.getPath().endsWith("/") && newOrganization.getPath().length() > 1) {
                    organizationIds = LongUtils.change2Str(
                        newOrganization.getPath().substring(1, newOrganization.getPath().length() - 1).split("/"));
                } else {
                    continue;
                }
                organizationIds.add(newOrganization.getId());
            } else {
                organizationIds.add(newOrganization.getId());
            }
            for (Long list: organizationIds) {
                if (list.equals(organizationId)) {
                    allOrganizationIds.addAll(organizationIds);
                }
            }
        }
        // 去重 newAllOrganizationIds为所有子组织
        List<Long> newAllOrganizationIds = allOrganizationIds.stream().distinct().collect(Collectors.toList());

        // 2.查看所有组织中是否存在该角色

//        Role role = roleRepository.findByIdAndOrganizationIdAndDeletedIsFalse(roleId, organizationId);
//
//        if (role == null) {
//            throw new NotFoundError("role is not found");
//        }
        Boolean roleFlag = false;
        for (Long subOrganizationId: newAllOrganizationIds) {
            Role role = roleRepository.findByIdAndOrganizationIdAndDeletedIsFalse(roleId, subOrganizationId);
            if (role != null) {
                roleFlag = true;
                break;
            }
        }
        if (!roleFlag) {
            throw new NotFoundError("role is not found");
        }

        UserRole userRole = userRoleRepository.findByRoleIdAndUserIdAndDeletedIsFalse(roleId, memberId);

        if (userRole == null) {
            throw new BusinessError("user is not in department");
        }

        userRole.setStatus(EntityStatus.DELETED);
        userRole.setDeleted(false);
        userRole.setDeletedAt(new Date());
        userRole.setDeletedBy(operatorId);

        userRoleRepository.save(userRole);
    }

    /**
     * 获取组织成员的角色列表。
     *
     * @param organizationId 组织ID
     * @param userId         用户ID
     * @return 角色列表
     */
    @Override
    public List<Role> getOrgMemberRoles(Long organizationId, Long userId) {

        return roleRepository.findOrgMemberRoles(organizationId, userId);
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

        if (entities == null || entities.size() == 0) {
            return included;
        }

        List<Long> roleIds = new ArrayList<>();
        for (T entity : entities) {
            if (entity instanceof UserProfile) {
                roleIds.addAll(((UserProfile) entity).getRoleIds());
            }
        }

        if (roleIds.size() == 0) {
            return included;
        }

        List<Role> roles = roleRepository.findByIdInAndDeletedIsFalse(roleIds);

        for (Role role : roles) {
            included.put(role.getId(), role);
        }

        return included;
    }


    @Override
    public List<Role> getRoleList(Long organizationId) {
        return roleRepository.findByOrganizationIdAndDeletedIsFalse(organizationId);
    }

}
