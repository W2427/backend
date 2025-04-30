package com.ose.auth.domain.model.service;

import com.ose.util.*;
import com.ose.auth.domain.model.repository.*;
import com.ose.auth.dto.*;
import com.ose.auth.entity.*;
import com.ose.auth.vo.OrgType;
import com.ose.auth.vo.UserPrivilege;
import com.ose.dto.BaseDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import java.util.*;

@Component
public class OrganizationService implements OrganizationInterface {

    // 部门数据仓库
    private final OrganizationRepository organizationRepository;
    private final OrganizationBasicRepository organizationBasicRepository;

    // 用户信息操作仓库
    private final UserProfileRepository userProfileRepository;

    // 用户部门关联数据仓库
    private final UserOrganizationRepository userOrganizationRepository;
    private final RoleRepository roleRepository;
    private final RoleMemberRepository roleMemberRepository;
    private final UserBasicRepository userBasicRepository;
    private final UserRoleRepository userRoleRepository;
    private final CalendarRepository calendarRepository;

    /**
     * 部门构造器。
     */
    @Autowired
    public OrganizationService(
        OrganizationRepository organizationRepository,
        OrganizationBasicRepository organizationBasicRepository,
        UserProfileRepository userProfileRepository,
        UserOrganizationRepository userOrganizationRepository,
        RoleRepository roleRepository,
        RoleMemberRepository roleMemberRepository,
        UserBasicRepository userBasicRepository,
        UserRoleRepository userRoleRepository,
        CalendarRepository calendarRepository
    ) {
        this.organizationRepository = organizationRepository;
        this.organizationBasicRepository = organizationBasicRepository;
        this.userProfileRepository = userProfileRepository;
        this.userOrganizationRepository = userOrganizationRepository;
        this.roleRepository = roleRepository;
        this.roleMemberRepository = roleMemberRepository;
        this.userBasicRepository = userBasicRepository;
        this.userRoleRepository = userRoleRepository;
        this.calendarRepository = calendarRepository;
    }

    /**
     * 创建部门。
     *
     * @param operatorId      操作者ID
     * @param organizationDTO 部门数据
     * @return 已创建的部门信息
     */
    @Override
    @Transactional
    public Organization create(Long operatorId, OrganizationDTO organizationDTO) {

        // 如果是IDC，则需要确认当前组织下是否已分配IDC执行人
        if (OrgType.IDC.equals(organizationDTO.getType())) {
            UserOrganization userOrganization = userOrganizationRepository.findByOrganizationIdAndOrganizationTypeAndDeletedIsFalse(
                organizationDTO.getParentId(),
                organizationDTO.getType()
            );
            if (userOrganization != null) {
                throw new BusinessError("The current organization has assigned IDC personnel and cannot continue to add sub-organizations!");
            }
        }

        Organization organization = BeanUtils
            .copyProperties(organizationDTO, new Organization());

        if (organization.getType() == null) {
            organization.setType(OrgType.DEPARTMENT);
        }

        if (!OrgType.COMPANY.equals(organization.getType())
            && organization.getParentId() == null) {
            throw new ValidationError("必须指定上级组织"); // TODO
        }

        if (organization.getStatus() == null) {
            organization.setStatus(EntityStatus.ACTIVE);
        }

        Organization parent = null;

        // 部门时
        if (organization.getParentId() != null) {

            parent = organizationRepository
                .findByIdAndDeletedIsFalse(organization.getParentId());

            if (parent == null) {
                throw new BusinessError("指定的上级组织不存在"); // TODO
            }

            // 取得上级组织下的最后一个子组织的信息
            Organization lastSibling = organizationRepository
                .findTopByParentIdAndDeletedIsFalseOrderBySortDesc(parent.getId());

            int sortFrom = lastSibling == null
                ? parent.getSort()
                : lastSibling.getSort();

            organizationRepository.increaseSort(parent.getCompanyId(), sortFrom);

            // 将当前组织的 ID 加入到上级组织的子组织 ID 列表中
            parent.setChildren(
                parent.getChildren() != null
                    ? (parent.getChildren() + "," + organization.getId())
                    : organization.getId().toString()
            );

            // 设置层级信息
            organization.setCompanyId(parent.getCompanyId());
            organization.setPath((parent.getPath() == null ? "/" : parent.getPath()) + parent.getId() + "/");
            organization.setDepth(parent.getDepth() + 1);
            organization.setSort(sortFrom + 1);

            // 公司时
        } else {
            organization.setCompanyId(organization.getId());
            organization.setPath("/");
        }

        Date now = new Date();
        organization.setCreatedBy(operatorId);
        organization.setCreatedAt(now);
        organization.setLastModifiedBy(operatorId);
        organization.setLastModifiedAt(now);
        organization = organizationRepository.save(organization);

        // 如果是项目组，则默认创建管理员角色
        if (OrgType.PROJECT.equals(organization.getType())
            && (parent != null && !OrgType.PROJECT.equals(parent.getType()))) {
            Role role = new Role();
            role.setName("管理员");
            role.setNo("administrator");
            role.setCode("administrator");
            role.setPrivileges(UserPrivilege.ALL.toString());
            role.setEditable(false);
            role.setOrganizationId(organization.getId());
            role.setCreatedAt(now);
            role.setCreatedBy(operatorId);
            role.setLastModifiedAt(now);
            role.setLastModifiedBy(operatorId);
            role.setStatus(EntityStatus.ACTIVE);
            roleRepository.save(role);
        }

        return organization;
    }

    /**
     * 更新部门信息。
     *
     * @param operatorId      操作者ID
     * @param organizationId  部门ID
     * @param organizationDTO 部门数据
     */
    @Override
    @Transactional
    public void update(Long operatorId, Long organizationId, OrganizationDTO organizationDTO) {

        Organization organization = null;

        if (RegExpUtils.isDecID(organizationId.toString())) {
            organization = organizationRepository.findByIdAndDeletedIsFalse(organizationId);
        }

        if (organization == null) {
            throw new NotFoundError("organization not found");
        }

        if (organizationDTO.getName() != null) {
            organization.setName(organizationDTO.getName());
        }
        if (organizationDTO.getNo() != null) {
            organization.setNo(organizationDTO.getNo());
        }

        if (organizationDTO.getIdc() != null) {
            if (organizationDTO.getIdc()) {
                organization.setIdc(true);
            } else {
                List<UserOrganization> uos = userOrganizationRepository.findByOrganizationIdAndIdcIsTrueAndDeletedIsFalse(organizationId);
                if (uos.size() > 0) {
                    throw new BusinessError("The current department already has an IDC person in charge!");
                } else {
                    organization.setIdc(false);
                }
            }

        }

        // 组织排序
        if (organizationDTO.getAfterId() != null) {

            // 当前组织最大排序值
            int max;
            // 目标组织最大排序值
            int targetMax;
            // 目标组织
            Organization target;
            // 当前组织的子组织中最大排序值的组织
            Organization maxOrg;
            // 目标子组织中最大排序值的组织
            Organization targetMaxOrg;

            // 置顶操作
            if (organizationDTO.getAfterId().equals("top")) {

                if (organization.getParentId() != null) {
                    target = organizationRepository
                        .findTopByParentIdAndDeletedIsFalseOrderBySortAsc(organization.getParentId());
                } else {
                    target = organizationRepository.findTopByParentIdIsNullAndDeletedIsFalseOrderBySortAsc();
                }

                // 如果当前操作部门已经是顶部 不做处理
                if (target.getId().equals(organizationId)) {
                    throw new BusinessError("organization is already on top!");
                }

                maxOrg = organizationRepository
                    .findTopByPathLikeAndDeletedIsFalseOrderBySortDesc("%" + organization.getId().toString() + "%");

                if (maxOrg != null) {
                    max = maxOrg.getSort();
                } else {
                    max = organization.getSort();
                }

                // 设置大于等于顶部部门小于当前部门的排序，设置大于当前部门中最大排序的子部门顺序的所有部门排序
                organizationRepository
                    .setSortBySortGreaterThanEqualTargetAndMax(target.getSort(), organization.getSort(), max);

                // 向上或向下排序【向目标之下排序】（根据当前组织的排序值与目标组织的排序值比较决定）
            } else {

                target = organizationRepository.findByIdAndDeletedIsFalse(organizationDTO.getAfterId());

                if (target == null) {
                    throw new NotFoundError("target organization is not found");
                }

                // 判断目标是否与当前部门同级别
                if ((target.getParentId() == null && organization.getParentId() != null)
                    || (target.getParentId() != null && organization.getParentId() == null)
                    || (organization.getParentId() != null
                    && target.getParentId() != null
                    && !target.getParentId().equals(organization.getParentId()))) {
                    throw new BusinessError("organization ordering must be at the same level");
                }

                // 获取目标部门的最大排序子部门
                targetMaxOrg = organizationRepository.findTopByParentIdAndDeletedIsFalseOrderBySortDesc(target.getId());

                if (targetMaxOrg != null) {
                    targetMax = targetMaxOrg.getSort();
                } else {
                    targetMax = target.getSort();
                }

                // 获取当前部门的最大排序子部门
                maxOrg = organizationRepository.findTopByParentIdAndDeletedIsFalseOrderBySortDesc(organizationId);

                if (maxOrg != null) {
                    max = maxOrg.getSort();
                } else {
                    max = organization.getSort();
                }

                // 向上排序
                if (organization.getSort() > target.getSort()) {

                    // 更新排序（向上排序时，待排序的组织排序值不变，更新的是目标组织及其子组织和排序值大于待排序组织的组织）
                    // 目标组织及其子组织、排序值大于待排序组织最大值的组织 的排序值 = 原排序值 + 待排序组织的最大排序值
                    organizationRepository.setSortBySortGreaterThanTarget(targetMax, organization.getSort(), max);

                    // 向下排序
                } else if (organization.getSort() < target.getSort()) {

                    // 更新排序（大于目标最大排序值的组织、待排序的组织及其子组织 排序值 = 原排序值 + 目标最大排序值）
                    organizationRepository.setSortBySortGreaterThanEqualOrgSort(targetMax, organization.getSort(), max);

                    // 在上一句更新排序之后，数据库中的排序值已经发生改变，但是在内存中的organization中的排序值没有更新，需要同步。
                    organization.setSort(organization.getSort() + targetMax);

                } else {
                    throw new BusinessError("organization ordering error");
                }
            }
        }

        Date now = new Date();
        organization.setLastModifiedAt(now);
        organization.setLastModifiedBy(operatorId);

        organizationRepository.save(organization);
    }

    /**
     * 获取部门的层级结构。
     *
     * @param organizationId  组织ID
     * @param orgHierarchyDTO 查询参数
     * @return 层级列表
     */
    @Override
    public OrganizationBasic hierarchy(Long organizationId, OrgHierarchyDTO orgHierarchyDTO) {

        OrganizationBasic organizationBasic;

        OrgType type = OrgType.valueOf(orgHierarchyDTO.getType());

        // 查看公司下的组织结构的根节点
        if (organizationId == null) {
            organizationBasic = organizationBasicRepository.findTopByTypeAndDeletedIsFalse(OrgType.COMPANY);
            if (organizationBasic == null) {
                throw new BusinessError("company is required");
            }
            organizationId = organizationBasic.getId();

            // 查看部门或项目组织下的组织信息或用户加入的某个组织的组织结构的根节点
        } else {
            // 有组织名和组织类型参数
            if (!StringUtils.isEmpty(orgHierarchyDTO.getName())) {
                organizationBasic = organizationBasicRepository.findByNameAndTypeAndPathLikeAndDeletedIsFalse(
                    orgHierarchyDTO.getName(),
                    type,
                    "%" + organizationId + "%");
            } else if (orgHierarchyDTO.getUserId() == null) {
                // 目标组织下的组织结构的根节点
                organizationBasic = organizationBasicRepository.findByIdAndDeletedIsFalse(organizationId);
                // 用户加入的组织下的组织结构的根节点
            } else {
                organizationBasic = organizationBasicRepository
                    .findUserOrgs(organizationId, orgHierarchyDTO.getUserId());
            }
        }

        if (organizationBasic == null) {
            throw new NotFoundError("organization is not found");
        }

        List<OrganizationBasic> organizations;
        // TODO 没有按照 传进来的排序参数（sort）进行排序
        // 排序目前只有两种（一. 按照createAt排序，二. 按照排序值sort排序）
        // 获取根据节点下的所有组织列表
        // 指定根节点组织名称
        if (!StringUtils.isEmpty(orgHierarchyDTO.getName())) {
            Long rootId = organizationBasic.getId();
            if (LongUtils.isEmpty(rootId)) {
                throw new NotFoundError("organization ID is not found");
            }
            organizations = organizationBasicRepository
                .findByPathLikeAndTypeAndDeletedIsFalseOrderBySortAsc("%" + rootId.toString() + "%", type);
        } else {
            // 未指定根节点组织名称
            if (orgHierarchyDTO.getSort().keySet().size() != 0 && orgHierarchyDTO.getSort().get("createdAt") != null) {
                organizations = organizationBasicRepository
                    .findByPathLikeAndTypeAndDeletedIsFalseOrderByCreatedAtDesc("%" + organizationId.toString() + "%", type);
            } else {
                if (orgHierarchyDTO.getUserId() == null) {
                    organizations = organizationBasicRepository
                        .findByPathLikeAndTypeAndDeletedIsFalseOrderBySortAsc("%" + organizationId.toString() + "%", type);
                    // 获取用户组织的层级结构
                } else {
                    organizations = organizationBasicRepository
                        .findUserChildrenOrgs("%" + organizationId.toString() + "%", type, orgHierarchyDTO.getUserId());
                }
            }
        }

        // 递归获取每层组织结构
        organizationBasic.setChildOrgs(getChildren(organizationBasic, organizations));

        return organizationBasic;
    }

    /**
     * 获取子组织结构。
     *
     * @param organizationBasic 组织
     * @param organizations     子组织列表
     * @return 子组织结构
     */
    private List<OrganizationBasic> getChildren(
        OrganizationBasic organizationBasic, List<OrganizationBasic> organizations) {

        // 子组织结构（每一层都新建一个子组织列表，并将其下级子组织set进来，形成一棵组织树）
        List<OrganizationBasic> children = new ArrayList<>();

        // 递归break条件，当没有子节点时，返回组织树
        if (organizationBasic.getChildren() != null) {
            // 获取当前组织节点的子组织ID列表
            List<Long> childrenIds = new ArrayList<>();
            childrenIds.addAll(LongUtils.change2Str(organizationBasic.getChildren().split(",")));
            // 遍历所有组织，如果存在组织的ID包含在子组织ID列表中，则将该组织及其子组织设置到当前组织节点的子组织中
            for (OrganizationBasic organization : organizations) {
                if (childrenIds.contains(organization.getId())) {
                    organization.setChildOrgs(getChildren(organization, organizations));
                    children.add(organization);
                }
            }
        }
        return children;
    }

    /**
     * 获取工作组下的组织成员层级。
     *
     * @param orgId         组织ID
     * @param teamId        工作组ID
     * @param privilegesDTO 权限集合
     * @return 组织成员层级
     */
    @Override
    public List<Organization> getOrgsMembers(Long orgId, Long teamId, PrivilegesDTO privilegesDTO) {

        if (privilegesDTO.getPrivileges().size() == 0) {
            return new ArrayList<>();
        }

        // 通过权限和组织确定角色列表
        final List<Role> roles = roleRepository.findByOrgAndPrivileges(teamId, privilegesDTO.getPrivileges());

        if (roles == null || roles.size() == 0) {
            return new ArrayList<>();
        }

        // 组织ID列表
        final Set<Long> orgIds = new HashSet<>();
        // 角色ID列表
        final Set<Long> roleIds = new HashSet<>();
        // 角色与用户的关系
        final Map<Long, List<UserBasic>> roleUsersMap = new HashMap<>();
        // 角色ID与组织ID关系
        final Map<Long, Set<Long>> orgRole = new HashMap<>();

        for (Role role : roles) {
            orgIds.add(role.getOrganizationId());
            roleIds.add(role.getId());
            roleUsersMap.put(role.getId(), new ArrayList<>());
            orgRole.computeIfAbsent(role.getOrganizationId(), k -> new HashSet<>()).add(role.getId());
        }

        // 获取所有组织
        final List<Organization> orgs = organizationRepository.findByIdInAndDeletedIsFalse(orgIds);

        if (orgs.size() == 0) {
            return orgs;
        }

        // 获取角色的成员列表
        final List<RoleMember> roleMembers = roleMemberRepository.findByRoleIdInAndDeletedIsFalse(roleIds);
        // 角色与用户的关系
        final HashMap<Long, ArrayList<Long>> roleMembersMap = new HashMap<>();
        // 用户ID列表
        final Set<Long> userIds = new HashSet<>();

        for (RoleMember roleMember : roleMembers) {
            roleMembersMap.computeIfAbsent(roleMember.getRoleId(), k -> new ArrayList<>());
            roleMembersMap.get(roleMember.getRoleId()).add(roleMember.getUserId());

            userIds.add(roleMember.getUserId());
        }

        // 获取用户信息
        final List<UserBasic> members;
        if (privilegesDTO.getKeyword() == null) {
            members = userBasicRepository.findByIdIn(userIds);
        } else {
            members = userBasicRepository.findByIdInAndNameLike(userIds, "%" + privilegesDTO.getKeyword() + "%");
        }
        // 用户ID与用户的映射
        final Map<Long, UserBasic> memberMap = new HashMap<>();
        for (UserBasic member : members) {
            memberMap.put(member.getId(), member);
        }

        for (Map.Entry<Long, ArrayList<Long>> entry : roleMembersMap.entrySet()) {
            for (Long userId : roleMembersMap.get(entry.getKey())) {
                if (memberMap.get(userId) != null) {
                    roleUsersMap.get(entry.getKey()).add(memberMap.get(userId));
                }
            }
        }

        for (Organization org : orgs) {

            Map<Long, UserBasic> users = new HashMap<>();

            orgRole.get(org.getId()).forEach(roleId ->
                roleUsersMap.get(roleId).forEach(user ->
                    users.put(user.getId(), user)
                )
            );

            org.setMembers(new ArrayList<>(users.values()));
        }

        return orgs;
    }

    /**
     * 设置返回结果的引用数据。
     *
     * @param <T>      数据实体类型
     * @param included 引用数据映射表
     * @param entities 查询结果列表
     * @return 引用数据映射表
     */
    @Override
    public <T extends BaseDTO> Map<Long, Object> setIncluded(
        Map<Long, Object> included,
        List<T> entities
    ) {
        if (entities.size() == 0) {
            return included;
        }

        List<Long> organizationIds = new ArrayList<>();

        for (T entity : entities) {
            if (entity instanceof Role) {
                if (((Role) entity).getOrganizationId() != null) {
                    organizationIds.add(((Role) entity).getOrganizationId());
                }
            } else if (entity instanceof UserOrganization) {
                if (((UserOrganization) entity).getOrganizationId() != null) {
                    organizationIds.add(((UserOrganization) entity).getOrganizationId());
                }
            } else if (entity instanceof RoleMember) {
                if (((RoleMember) entity).getOrganizations() != null) {
                    organizationIds.addAll(((RoleMember) entity).getOrganizations());
                }
            }

        }

        List<OrganizationBasic> organizationBasics = organizationBasicRepository
            .findByIdInAndDeletedIsFalse(organizationIds);

        for (OrganizationBasic organizationBasic : organizationBasics) {
            included.put(organizationBasic.getId(), organizationBasic);
        }
        return included;
    }

    /**
     * 设置返回结果的引用数据。
     *
     * @param <T>      数据实体类型
     * @param included 引用数据映射表
     * @param entity   查询结果
     * @return 引用数据映射表
     */
    @Override
    public <T extends BaseDTO> Map<Long, Object> setIncluded(
        Map<Long, Object> included,
        T entity
    ) {
        if (!(entity instanceof Role)) {
            return included;
        }

        Long organizationId = ((Role) entity).getOrganizationId();

        if (organizationId == null) {
            return included;
        }

        OrganizationBasic organizationBasic = organizationBasicRepository.findByIdAndDeletedIsFalse(organizationId);

        if (organizationBasic != null) {
            included.put(organizationBasic.getId(), organizationBasic);
        }
        return included;
    }

    /**
     * 获取部门详情。
     *
     * @param orgId    组织 ID
     * @param parentId 上级组织 ID
     * @return 组织详情
     */
    @Override
    public Organization details(Long orgId, Long parentId) {
        if (LongUtils.isEmpty(parentId) || parentId.equals(orgId)) {
            return organizationRepository.findByIdAndDeletedIsFalse(orgId);
        } else {
            return organizationRepository.findByPathLikeAndIdAndDeletedIsFalse("%/" + parentId.toString() + "/%", orgId);
        }
    }

    /**
     * 删除部门信息。
     *
     * @param organizationId 部门ID
     */
    @Override
    @Transactional
    public void delete(Long operatorId, Long organizationId) {

        // 获取待删除部门
        Organization organization = organizationRepository.findByIdAndDeletedIsFalse(organizationId);

        if (organization == null) {
            throw new NotFoundError("organization is not found");
        }

        if (organizationRepository.countByParentIdAndDeletedIsFalse(organizationId) > 0) {
            throw new BusinessError("organizations with subdivisions cannot be deleted");
        }

//        if (userOrganizationRepository.existsByOrganizationIdAndDeletedIsFalse(organizationId)) {
//            throw new BusinessError("can't delete organization because of exists user in organization");
//        }

        Date now = new Date();

        organization.setDeleted(true);
        organization.setStatus(EntityStatus.DELETED);

        organization.setDeletedAt(now);
        organization.setDeletedBy(operatorId);

        organizationRepository.save(organization);
    }

    /**
     * 删除部门信息。
     *
     * @param path 部门 path
     */
    @Override
    @Transactional
    public void deleteOrgByOrgPath(Long operatorId, String path) {
        organizationRepository.deleteOrgByOrgPath(path);
    }

    /**
     * 取得组织下的工作组Id
     *
     * @param orgId
     * @param teamId
     * @return
     */
    public OrganizationBasicDTO getTeamByOrg(Long orgId, Long teamId) {
        return organizationRepository.findTeamByOrg(orgId, teamId);
    }

    public List<OrganizationBasicDTO> getDepartments(Long orgId, String name) {
        return organizationRepository.findDeparmentByOrgIdAndName("%" + orgId.toString() + "%", name);

    }


    /**
     * 添加成员。
     *
     * @param operatorId     操作人ID
     * @param organizationId 组织ID
     * @param memberId       成员ID
     */
    @Override
    public void addMember(Long operatorId, Long organizationId, Long memberId) {

        if (memberId == null) {
            throw new BusinessError("memberId is required");
        }

        Organization organization = organizationRepository.findByIdAndDeletedIsFalse(organizationId);

        if (organization == null) {
            throw new NotFoundError("organization is not found");
        }

        // 如果是IDC部门的，则只能添加一位用户
        if (organization.getType().equals(OrgType.IDC)) {
            // 1.查询当前组织是否有子组织，如果有则不能添加IDC人员
            List<Organization> organizations = organizationRepository.findByPathLikeAndTypeAndDeletedIsFalse(
                "%" + organization.getId() + "%",
                OrgType.IDC);
            if (organizations.size() > 0) {
                throw new BusinessError("The current organization already has sub-organizations and IDC personnel cannot be configured!");
            }

            // 2.没有子组织，添加人员也只能添加一位
            UserOrganization userOrganization = userOrganizationRepository.findByOrganizationIdAndOrganizationTypeAndDeletedIsFalse(organizationId, organization.getType());
            if (userOrganization != null) {
                throw new BusinessError("The current organization already has IDC personnel!");
            }
        }

        if (!userProfileRepository.existsByIdAndDeletedIsFalse(memberId)) {
            throw new NotFoundError("user is not found");
        }

        // 成员是否已经在组织中
        if (userOrganizationRepository
            .existsByUserIdAndOrganizationIdAndDeletedIsFalse(memberId, organization.getId())) {
            throw new BusinessError("user is already in organization");
        }

        // 创建一个新的用户与组织的关系
        UserOrganization userOrganization = new UserOrganization();

        // 查询尚未加入的上级组织（加入组织时，同时加入尚未加入的上级组织）
        if (!organization.getType().equals(OrgType.IDC) && organization.getPath() != null) {

            // 全部上级组织ID
            List<Long> organizationIds = LongUtils.change2Str(
                organization.getPath().substring(1, organization.getPath().length() - 1).split("/"));
            // 已加入的上级组织列表
            List<Organization> joinedOrganizations = userOrganizationRepository
                .findJoinedParentOrgs(organizationIds, memberId);
            // 已加入的组织ID
            List<Long> joinedOrgIds = new ArrayList<>();
            // 待加入的组织ID
            List<Long> notJoinedOrgIds = new ArrayList<>();
            // 待加入的组织列表
            List<Organization> notJoinedOrganizations = new ArrayList<>();

            // 获取待加入的组织列表
            // 当没有加入的组织时，即需要加入其所有上级组织
            if ((joinedOrganizations == null || joinedOrganizations.size() == 0) && organizationIds.size() > 0) {
                notJoinedOrganizations = organizationRepository.findByIdInAndDeletedIsFalse(organizationIds);
                // 通过加入的组织列表，过滤获取尚未加入的上级组织列表
            } else {
                // 已加入的组织ID列表
                if (joinedOrganizations != null) {
                    for (Organization o : joinedOrganizations) {
                        joinedOrgIds.add(o.getId());
                    }
                }
                // 获取未加入的组织ID列表
                for (Long orgId : organizationIds) {
                    if (!joinedOrgIds.contains(orgId)) {
                        notJoinedOrgIds.add(orgId);
                    }
                }

                // 获取待加入的组织列表
                if (notJoinedOrgIds.size() != 0) {
                    notJoinedOrganizations = organizationRepository.findByIdInAndDeletedIsFalse(notJoinedOrgIds);
                }
            }

            // 加入未加入的上级组织
            if (notJoinedOrganizations != null && notJoinedOrganizations.size() > 0) {
                for (Organization org : notJoinedOrganizations) {
                    UserOrganization userOrg = new UserOrganization();
                    userOrg.setUserId(memberId);
                    userOrg.setOrganizationId(org.getId());
                    userOrg.setOrganizationType(org.getType());
                    userOrg.setStatus(EntityStatus.ACTIVE);
                    userOrg.setCreatedAt(new Date());
                    userOrg.setCreatedBy(operatorId);
                    userOrg.setLastModifiedAt(new Date());
                    userOrg.setLastModifiedBy(operatorId);
                    userOrganizationRepository.save(userOrg);
                }
            }
        }

        // 设置加入的默认组织（切换项目组）
        if (!userOrganizationRepository.existsByUserIdAndIsDefaultTrueAndDeletedIsFalse(memberId)
            && organization.getType().equals(OrgType.PROJECT)
            && organization.getDepth() == 1) {
            userOrganization.setDefault(true);
        }
        userOrganization.setUserId(memberId);
        userOrganization.setOrganizationId(organizationId);
        userOrganization.setOrganizationType(organization.getType());

        userOrganization.setStatus(EntityStatus.ACTIVE);
        userOrganization.setCreatedAt(new Date());
        userOrganization.setCreatedBy(operatorId);
        userOrganization.setLastModifiedAt(new Date());
        userOrganization.setLastModifiedBy(operatorId);

        userOrganizationRepository.save(userOrganization);
    }

    /**
     * 设置成员为主管。
     *
     * @param organizationId             组织ID
     * @param memberId                   成员ID
     * @param organizationMemberSetVpDTO
     */
    @Override
    public void setVp(Long organizationId, Long memberId, OrganizationMemberSetVpDTO organizationMemberSetVpDTO) {
        UserOrganization userOrganization = userOrganizationRepository.findByOrganizationIdAndUserIdAndDeletedIsFalse(
            organizationId,
            memberId
        );

        if (userOrganization != null) {
            if (organizationMemberSetVpDTO.getApplyRole() == null ||
                (organizationMemberSetVpDTO.getApplyRole() != null && !organizationMemberSetVpDTO.getApplyRole())) {

                //查询该组织是否有vp
                UserOrganization applyRoleUserOrganization = userOrganizationRepository.findByOrganizationIdAndApplyRoleIsTrueAndDeletedIsFalse(organizationId);

                if (applyRoleUserOrganization != null){
                    throw new BusinessError("The organization has already set up a leader");
                }else {
                    userOrganization.setApplyRole(true);
                }

            } else {
                userOrganization.setApplyRole(false);
            }
            userOrganizationRepository.save(userOrganization);
        }
    }

    /**
     * 设置成员为IDC负责人。
     *
     * @param organizationId              组织ID
     * @param memberId                    成员ID
     * @param organizationMemberSetIdcDTO
     */
    @Override
    public void setIdc(Long organizationId, Long memberId, OrganizationMemberSetIdcDTO organizationMemberSetIdcDTO) {
        Organization organization = organizationRepository.findByIdAndDeletedIsFalse(organizationId);
        if (organization.getIdc() == null || (organization.getIdc() != null && !organization.getIdc())) {
            throw new BusinessError("The current department is not set to IDC!");
        }

        List<UserOrganization> uos = userOrganizationRepository.findByOrganizationIdAndIdcIsTrueAndDeletedIsFalse(organizationId);
        if (uos.size() > 0) {
            for (UserOrganization uo : uos) {
                if (uo.getUserId().equals(memberId)) {
                    continue;
                }
                uo.setIdc(false);
                userOrganizationRepository.save(uo);
            }
        }

        UserOrganization userOrganization = userOrganizationRepository.findByOrganizationIdAndUserIdAndDeletedIsFalse(
            organizationId,
            memberId
        );
        if (userOrganization != null) {
            userOrganization.setIdc(userOrganization.getIdc() == null ? true : userOrganization.getIdc() ? false : true);
            userOrganizationRepository.save(userOrganization);
        }
    }

    /**
     * 移除成员。
     *
     * @param operatorId     操作ID
     * @param organizationId 部门ID
     * @param memberId       成员列表
     */
    @Override
    public void removeMember(Long operatorId, Long organizationId, Long memberId) {

        Organization organization = organizationRepository.findByIdAndDeletedIsFalse(organizationId);

        if (organization == null) {
            throw new NotFoundError("organization is not found");
        }

        // 成员加入组织的关系
        UserOrganization userOrganization = userOrganizationRepository
            .findTopByOrganizationIdAndUserIdAndDeletedIsFalse(organizationId, memberId);

        if (userOrganization == null) {
            throw new BusinessError("member is not found");
        }

        // 如果移除的组织为当前登录用户默认项目组织，则重新设置一个默认项目组织
        if (userOrganization.isDefault()) {
            // 获取其他项目组织（顶点）
            List<UserOrganization> otherOrganizations = userOrganizationRepository
                .findRootProjectOrgs(organizationId, memberId);
            // 设置默认项目组织
            if (otherOrganizations != null && otherOrganizations.size() != 0) {
                UserOrganization defaultOrg = otherOrganizations.get(0);
                defaultOrg.setDefault(true);
                userOrganizationRepository.save(defaultOrg);
            }
        }

        // 退出其加入的所有子部门
        // 获取其所有子组织
        List<UserOrganization> userOrganizations = userOrganizationRepository
            .findChildrenOrgMembers("%" + organizationId.toString() + "%", memberId);

        // 退出所有子组织
        if (userOrganizations != null) {
            for (UserOrganization userOrg : userOrganizations) {
                userOrg.setStatus(EntityStatus.DELETED);
                userOrg.setDeleted(false);
                userOrg.setDeletedAt(new Date());
                userOrg.setDeletedBy(operatorId);
            }
            userOrganizationRepository.saveAll(userOrganizations);
        }

        // 退出相关部门的角色
        userOrganization.setStatus(EntityStatus.DELETED);
        userOrganization.setDeleted(false);
        userOrganization.setDeletedAt(new Date());
        userOrganization.setDeletedBy(operatorId);

        userOrganizationRepository.save(userOrganization);

        // 将该成员在所有相关组织下的岗位中删除掉
        List<Long> organizationsList = new ArrayList<>();
        for (UserOrganization usOrganization : userOrganizations) {
            organizationsList.add(usOrganization.getOrganizationId());
        }
        organizationsList.add(userOrganization.getOrganizationId());

        List<UserRole> userRoleList = userRoleRepository.findByUserIdAndDeletedIsFalse(memberId);

        List<UserRole> deleUserRoleList = new ArrayList<>();

        for (Long oId : organizationsList) {
            for (UserRole ur : userRoleList) {
                Role role = roleRepository.findByIdAndOrganizationIdAndDeletedIsFalse(ur.getRoleId(), oId);
                if (role != null) {
                    deleUserRoleList.add(ur);
                }
            }
        }

        if (deleUserRoleList != null) {
            for (UserRole ur : deleUserRoleList) {
                ur.setStatus(EntityStatus.DELETED);
                ur.setDeleted(false);
                ur.setDeletedAt(new Date());
                ur.setDeletedBy(operatorId);
            }
            userRoleRepository.saveAll(deleUserRoleList);
        }


    }

    /**
     * 获取组织列表。
     *
     * @param type 组织类型
     */
    @Override
    public Page<Organization> search(String type, PageDTO pageDTO) {
        return organizationRepository.findByTypeAndDeletedIsFalse(OrgType.valueOf(type), pageDTO.toPageable());
    }

    /**
     * 获取用户的顶层项目组织列表。
     *
     * @param userId 用户ID
     * @return 用户项目组织列表
     */
    @Override
    public List<Organization> getTopProjectOrgs(Long userId) {

        List<Map<String, Object>> dataList = organizationRepository.findUserTopProjectOrgs(userId);
        List<Organization> projectOrgs = new ArrayList<>();
        for (Map<String, Object> data : dataList) {
            Organization organization = new Organization();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLowWithNoChange(data);

            BeanUtils.copyProperties(newMap, organization, "type", "members", "status");
            projectOrgs.add(organization);
        }


        if (projectOrgs == null || projectOrgs.size() == 0) {
            return projectOrgs;
        }

        // 获取默认组织
        UserOrganization userOrganization = userOrganizationRepository
            .findTopByUserIdAndIsDefaultIsTrueAndOrganizationTypeAndDeletedIsFalse(userId, OrgType.PROJECT);

        if (userOrganization == null) {
            return projectOrgs;
        }

        // 设置默认项目组织
        for (Organization projectOrg : projectOrgs) {
            if (userOrganization.getOrganizationId().equals(projectOrg.getId())) {
                projectOrg.setDefault(true);
            }
        }

        return projectOrgs;
    }

    /**
     * 根据组织 ID 集合取得组织信息列表。
     *
     * @param entityIDs 组织 ID 集合
     * @return 组织信息列表
     */
    @Override
    public List<OrganizationBasicDTO> getByEntityIDs(Set<Long> entityIDs) {


        if (CollectionUtils.isEmpty(entityIDs)) {
            return new ArrayList<>();
        }

        return organizationRepository.findByIdIn(entityIDs);
    }

    @Override
    public List<Organization> getOrgList(Long organizationId, String orgType) {
        if (orgType == null) {
            return new ArrayList<>();
        }

        Organization org = organizationRepository.findById(organizationId).orElse(null);
        if (org == null) return new ArrayList<>();

        List<Organization> orgList = new ArrayList<>();
//        orgList.add(org);


        List<Organization> orgRemains = organizationRepository.findByParentIdAndType(org.getPath() + org.getId() + "/%", orgType);

        if (orgRemains != null) orgList.addAll(orgRemains);

        return orgList;

    }

    @Override
    public List<Organization> getRootOrgListByUserId(Long userId, String orgType) {
        return organizationRepository.findByUserIdAndIsApployRole(userId, orgType);
    }

    @Override
    public Organization save(Organization organization) {
        return organizationRepository.save(organization);
    }

    @Override
    public Organization get(Long organizationId) {
        return organizationRepository.findByIdAndDeletedIsFalse(organizationId);
    }

    public UserOrganization get(Long orgId, Long userId) {
        return userOrganizationRepository.findByOrganizationIdAndUserIdAndDeletedIsFalse(orgId, userId);
    }

    @Override
    public DCalendar getProjectHour(Long projectId) {

        return calendarRepository.findByProjectId(projectId);
    }

    @Override
    public void updateProjectHour(Long projectId, UpdateProjectHourDTO updateProjectHourDTO) {
        DCalendar calendar = calendarRepository.findByProjectId(projectId);
        if (calendar != null) {
            calendar.setProjectId(updateProjectHourDTO.getProjectId());
            calendar.setProjectName(updateProjectHourDTO.getProjectName());
            if (updateProjectHourDTO.getDayWorkHour() != null) {
                calendar.setDayWorkHour(updateProjectHourDTO.getDayWorkHour());
            }
            if (updateProjectHourDTO.getProjectTasks() != null) {
                calendar.setProjectTasks(updateProjectHourDTO.getProjectTasks());
            }
            calendar.setLastModifiedAt(new Date());
            calendarRepository.save(calendar);
        } else {
            DCalendar dCalendar = new DCalendar();
            BeanUtils.copyProperties(updateProjectHourDTO, dCalendar);
            dCalendar.setCreatedAt(new Date());
            dCalendar.setLastModifiedAt(new Date());
            dCalendar.setStatus(EntityStatus.ACTIVE);
            calendarRepository.save(dCalendar);
        }

    }

    @Override
    public List<DCalendar> getProjectHourList() {

        return calendarRepository.findAll();
    }

    @Override
    public UserOrgChartDTO getOrgChartData() {
        UserOrgChartDTO orgChart = new UserOrgChartDTO();
        orgChart.setName("OceanSTAR Elite group");
        orgChart.setTitle("No manager set");
        List<String> companyList = userBasicRepository.findCompanyList();
        if (companyList.size() == 0) {
            throw new BusinessError("no company Find");
        }
        List<UserOrgChartDTO> companyDatas = new ArrayList<>();
        for (String company : companyList) {
            UserOrgChartDTO companyData = new UserOrgChartDTO();
            companyData.setName(company);
            String companyGM = userBasicRepository.findCompanyGM(company);
            if (companyGM == null || "".equals(companyGM)) {
                companyData.setTitle("No manager set");
            } else {
                companyData.setTitle(companyGM);
            }
            List<String> divisionList = userBasicRepository.findDivisionListByCompany(company);
            List<UserOrgChartDTO> divisionDatas = new ArrayList<>();
            for (String division : divisionList) {
                UserOrgChartDTO divisionData = new UserOrgChartDTO();
                divisionData.setName(division);
                String divisionVP = userBasicRepository.findDivisionVPByCompanyAndDivision(company, division);
                if (divisionVP == null || "".equals(divisionVP)) {
                    divisionData.setTitle("No manager set");
                } else {
                    divisionData.setTitle(divisionVP);
                }

                List<String> teamList = userBasicRepository.findTeamListByCompanyAndDivision(company, division);
                List<UserOrgChartDTO> teamDatas = new ArrayList<>();
                for (String team : teamList) {
                    UserOrgChartDTO teamData = new UserOrgChartDTO();
                    teamData.setName(team);
                    String teamLeader = userBasicRepository.findTeamLeader(company, division, team);
                    if (teamLeader == null || "".equals(teamLeader)) {
                        teamData.setTitle("No manager set");
                    } else {
                        teamData.setTitle(teamLeader);
                    }
                    teamDatas.add(teamData);
                }
                divisionData.setChildren(teamDatas);
                divisionDatas.add(divisionData);
            }
            companyData.setChildren(divisionDatas);
            companyDatas.add(companyData);
        }
        orgChart.setChildren(companyDatas);
        return orgChart;
    }

    @Override
    public UserOrgChartDTO getProfessionalOrgChartData() {
        UserOrgChartDTO orgChart = new UserOrgChartDTO();
        orgChart.setName("OceanSTAR Elite group");
        orgChart.setTitle("No manager set");
        List<String> divisionList = userBasicRepository.findDivisionList();
        if (divisionList.size() == 0) {
            throw new BusinessError("no division Find");
        }
        List<UserOrgChartDTO> divisionDatas = new ArrayList<>();
        for (String division : divisionList) {
            UserOrgChartDTO divisionData = new UserOrgChartDTO();
            divisionData.setName(division);
            String divisionVP = userBasicRepository.findDivisionVP(division);
            if (divisionVP == null || "".equals(divisionVP)) {
                divisionData.setTitle("No manager set");
            } else {
                divisionData.setTitle(divisionVP);
            }
            List<String> teamList = userBasicRepository.findTeamListByDivision(division);
            List<UserOrgChartDTO> teamDatas = new ArrayList<>();
            for (String team : teamList) {
                UserOrgChartDTO teamData = new UserOrgChartDTO();
                teamData.setName(team);
                String teamLeader = userBasicRepository.findTeamLeaderByDivisionAndTeam(division, team);
                if (teamLeader == null || "".equals(teamLeader)) {
                    teamData.setTitle("No manager set");
                } else {
                    teamData.setTitle(teamLeader);
                }
                teamDatas.add(teamData);

                divisionData.setChildren(teamDatas);
            }
            divisionDatas.add(divisionData);
        }
        orgChart.setChildren(divisionDatas);
        return orgChart;
    }

    @Override
    public List<OrganizationIDCDetailDTO> getHierarchyInfo(
        final Long orgId
    ) {

        List<Organization> organizations = organizationRepository.findByPathLikeAndTypeAndDeletedIsFalse("%" + orgId.toString() + "%", OrgType.IDC);

        if (organizations.size() == 0) {
            return null;
        }

        List<OrganizationIDCDetailDTO> nodes = new ArrayList<>();
        for (Organization organization : organizations) {
            OrganizationIDCDetailDTO dto = new OrganizationIDCDetailDTO();
            dto.setDepartmentId(organization.getId());
            dto.setDepartmentName(organization.getName());
            dto.setParentId(organization.getParentId());

            Map<String, Object> userMap = organizationRepository.findUserInfoByOrgId(organization.getId());
            if (userMap != null) {
                dto.setUserId(userMap.get("id") == null ? null : Long.valueOf(userMap.get("id").toString()));
                dto.setUserName(userMap.get("name") == null ? null : userMap.get("name").toString());
            }
            nodes.add(dto);
        }

        Map<String, OrganizationIDCDetailDTO> hierarchyEntryDtoMap = new HashMap<>();
        OrganizationIDCDetailDTO parentDetailDTO;
        List<OrganizationIDCDetailDTO> result = new ArrayList<>();

        // 构造树形层级结构
        for (OrganizationIDCDetailDTO entryDTO : nodes) {
            hierarchyEntryDtoMap.put(entryDTO.getDepartmentId().toString(), entryDTO);
            parentDetailDTO = hierarchyEntryDtoMap.get(entryDTO.getParentId().toString());
            if (parentDetailDTO == null) {
                result.add(entryDTO);
                continue;
            }
            parentDetailDTO.addChild(entryDTO);
            if (parentDetailDTO.getChildren() != null && parentDetailDTO.getChildren().size() != 0) {
                parentDetailDTO.setLeaf(false);
            }
        }

        return result;
    }

}
