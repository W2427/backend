package com.ose.tasks.domain.model.repository.wbs;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.WBSModuleTaskPackageDTO;
import com.ose.tasks.dto.WBSScreenResultDTO;
import com.ose.tasks.dto.wbs.WBSEntryDwgDTO;
import com.ose.tasks.dto.wbs.WBSEntryIsoDTO;
import com.ose.tasks.dto.wbs.WBSEntryMaterialDTO;
import com.ose.tasks.dto.wbs.WBSEntryPlainQueryDTO;
import com.ose.tasks.entity.wbs.entity.ISOEntity;
import com.ose.tasks.entity.wbs.entry.WBSEntryPlain;
import com.ose.tasks.vo.wbs.WBSEntryRunningStatus;
import com.ose.tasks.vo.wbs.WBSEntryType;
import com.ose.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * WBS 扁平 条目 CRUD 操作接口。
 */
public class WBSEntryPlainCustomRepositoryImpl extends BaseRepository implements WBSEntryPlainCustomRepository {

    /**
     * 查询四级计划。
     *
     * @param projectId             项目 ID
     * @param orgId                 查询条件
     * @param wbsEntryPlainQueryDTO 分页参数
     * @return 扁平计划分页数据
     */
    @Override
    public Page<WBSEntryPlain> search(
        final Long orgId,
        final Long projectId,
        final WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO
    ) {
        /**
         * 查询条件
         * 实体类型 WELD，ISO等，entityTypes            List<WBSEntityType>
         *     其中实体为 COMPONENT, ISO, SPOOL,PIPE_PIECE,WELD_JOINT,PRESSURE_TEST_PACKAGE,CLEAN_PACKAGE,SUB_SYSTEM
         * 实体子类型 FBW，SBW等，            List<String>
         *     FBW,FSFW,FSPW,SBW,SSFW,SSPW,PIPE_SUPPORT,PIPE_PIECE,SPOOL,ISO,PRESSURE_TEST_PACKAGE
         * WBS 类型 type，3级计划和4级计划    List, WORK UNITS ENTITY,DURATION
         * 开始时间                          Date pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
         * 结束时间                          Date pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
         * runningstatus，运行状态           List<String>
         *     PENDING,
         *     RUNNING,
         *     SUSPENDED,
         *     APPROVED,
         *     REJECTED,
         *     IGNORED,
         * 前置任务是否完成                   Boolean
         * 所属模块、子系统 Hierarchy Id      List <String> todo
         * 工序阶段                          List<String>
         * 工序                              List<String>
         * 材料匹配率                        Double
         * 发图情况                          Boolean
         * 班组ID                              List<String> teamIds
         * 模块类型                         List<String>
         * 场地ID                             List
         * 任务包ID                           List
         *
         */

        EntityManager entityManager = getEntityManager();
        CriteriaBuilder countCriteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = countCriteriaBuilder.createQuery(Long.class);

        Root root = countQuery.from(WBSEntryPlain.class);

        Predicate predicate = setCondition(root, countCriteriaBuilder, projectId, orgId, wbsEntryPlainQueryDTO);


        Long count = entityManager
            .createQuery(
                countQuery
                    .select(countCriteriaBuilder.count(root))
                    .where(predicate)
            )
            .getSingleResult();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<WBSEntryPlain> criteriaQuery = criteriaBuilder.createQuery(WBSEntryPlain.class);
//        criteriaQuery.from(WBSEntryPlain.class);
        Root queryRoot = criteriaQuery.from(WBSEntryPlain.class);
        Predicate queryPredicate = setCondition(queryRoot, criteriaBuilder, projectId, orgId, wbsEntryPlainQueryDTO);
        criteriaQuery.where(queryPredicate);

        PageRequest pageRequest = PageRequest.of(
            wbsEntryPlainQueryDTO.toPageable().getPageNumber(),
            wbsEntryPlainQueryDTO.toPageable().getPageSize(),
            Sort.by(Sort.Order.asc("name"))
        );

        if (count == 0) {
            return new PageImpl<>(new ArrayList<>(), pageRequest, 0);
        }


        return new PageImpl<>(
            entityManager
                .createQuery(criteriaQuery)
                .setFirstResult(wbsEntryPlainQueryDTO.toPageable().getPageNumber() * wbsEntryPlainQueryDTO.toPageable().getPageSize())
                .setMaxResults(wbsEntryPlainQueryDTO.toPageable().getPageSize())
                .getResultList(),
            pageRequest,
            count
        );


    }

    @Override
    public Double getOverallMatchPercent(Long orgId, Long projectId, WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO) {
        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);


























        return 0.0;
    }

    @Override
    public List<ISOEntity> getModules(Long orgId, Long projectId) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT DISTINCT(sector) as module_no ");
        sql.append("FROM wbs_entry as we ");
        sql.append("WHERE ");

        sql.append("we.status = 'ACTIVE' ");
        sql.append("AND we.org_id = :orgId AND we.project_id = :projectId ");
        sql.append("ORDER BY sector ");

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("projectId", projectId);
        query.setParameter("orgId", orgId);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List<Map<String, Object>> queryResultList = query.getResultList();

        List<ISOEntity> list = new ArrayList<ISOEntity>();

        for (Map<String, Object> map :queryResultList) {
            ISOEntity isoEntity = new ISOEntity();
            if(map.get("module_no") != null && !"".equals(map.get("module_no"))) {
//                isoEntity.setModuleNo(map.get("module_no").toString());
                list.add(isoEntity);
            }
        }

        return list;
    }

    @Override
    public WBSScreenResultDTO getModulesTaskPackages(Long orgId, Long projectId, WBSModuleTaskPackageDTO wbsModuleTaskPackageDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT wes.task_package_id, we.sector, we.process, wes.work_site_name, wes.running_status, we.stage ");

        sql.append("FROM wbs_entry as we ");
        sql.append("INNER JOIN wbs_entry_state as wes ON we.id = wes.wbs_entry_id ");
        sql.append("WHERE ");
        sql.append("we.status = 'ACTIVE' ");

        sql.append("AND we.org_id = :orgId AND we.project_id = :projectId ");

        if (wbsModuleTaskPackageDTO.getModuleNo() != null && !"".equals(wbsModuleTaskPackageDTO.getModuleNo()) && !"ALL".equals(wbsModuleTaskPackageDTO.getModuleNo())) {
            sql.append("AND we.sector = :moduleNo ");
        }
        if ("1".equals(wbsModuleTaskPackageDTO.getTaskPackageId())) {
            sql.append("AND wes.task_package_id is null ");
        } else if (wbsModuleTaskPackageDTO.getTaskPackageId() != null && !"".equals(wbsModuleTaskPackageDTO.getTaskPackageId()) && !"all".equals(wbsModuleTaskPackageDTO.getTaskPackageId())) {
            sql.append("AND wes.task_package_id = :taskPackageId ");
        }
        if (wbsModuleTaskPackageDTO.getProcessId() != null && !"".equals(wbsModuleTaskPackageDTO.getProcessId()) && !"all".equals(wbsModuleTaskPackageDTO.getProcessId())) {
            sql.append("AND we.process_id = :processId ");
        }
        if (wbsModuleTaskPackageDTO.getStage() != null && !"".equals(wbsModuleTaskPackageDTO.getStage()) && !"ALL".equals(wbsModuleTaskPackageDTO.getStage())) {
            sql.append("AND we.stage = :stage ");
        }
        if (wbsModuleTaskPackageDTO.getAddress() != null && !"".equals(wbsModuleTaskPackageDTO.getAddress()) && !"ALL".equals(wbsModuleTaskPackageDTO.getAddress())) {
            sql.append("AND wes.work_site_name = :address ");
        }
        if (wbsModuleTaskPackageDTO.getStatus() != null && !"".equals(wbsModuleTaskPackageDTO.getStatus()) && !"ALL".equals(wbsModuleTaskPackageDTO.getStatus())) {
            sql.append("AND wes.running_status = :status ");
        }

        Query query = entityManager.createNativeQuery(sql.toString());

        if (wbsModuleTaskPackageDTO.getModuleNo() != null && !"".equals(wbsModuleTaskPackageDTO.getModuleNo()) && !"ALL".equals(wbsModuleTaskPackageDTO.getModuleNo())) {
            query.setParameter("moduleNo", wbsModuleTaskPackageDTO.getModuleNo());
        }

        if (wbsModuleTaskPackageDTO.getTaskPackageId() != null && !"".equals(wbsModuleTaskPackageDTO.getTaskPackageId()) && !"all".equals(wbsModuleTaskPackageDTO.getTaskPackageId()) && !"1".equals(wbsModuleTaskPackageDTO.getTaskPackageId())) {
            query.setParameter("taskPackageId", wbsModuleTaskPackageDTO.getTaskPackageId());
        }

        if (wbsModuleTaskPackageDTO.getProcessId() != null && !"".equals(wbsModuleTaskPackageDTO.getProcessId()) && !"all".equals(wbsModuleTaskPackageDTO.getProcessId())) {
            query.setParameter("processId", wbsModuleTaskPackageDTO.getProcessId());
        }
        if (wbsModuleTaskPackageDTO.getStage() != null && !"".equals(wbsModuleTaskPackageDTO.getStage()) && !"ALL".equals(wbsModuleTaskPackageDTO.getStage())) {
            query.setParameter("stage", wbsModuleTaskPackageDTO.getStage());
        }
        if (wbsModuleTaskPackageDTO.getAddress() != null && !"".equals(wbsModuleTaskPackageDTO.getAddress()) && !"ALL".equals(wbsModuleTaskPackageDTO.getAddress())) {
            query.setParameter("address", wbsModuleTaskPackageDTO.getAddress());
        }
        if (wbsModuleTaskPackageDTO.getStatus() != null && !"".equals(wbsModuleTaskPackageDTO.getStatus()) && !"ALL".equals(wbsModuleTaskPackageDTO.getStatus())) {
            query.setParameter("status", wbsModuleTaskPackageDTO.getStatus());
        }
        query.setParameter("projectId", projectId);
        query.setParameter("orgId", orgId);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List<Map<String, Object>> queryResultList = query.getResultList();

        TreeSet<String> sectorSet= new TreeSet<>();
        TreeSet<String> taskPackageSet = new TreeSet<>();
        TreeSet<String> processSet = new TreeSet<>();
        TreeSet<String> addressSet = new TreeSet<>();
        TreeSet<String> statusSet = new TreeSet<>();
        TreeSet<String> stageSet = new TreeSet<>();
        for (Map<String, Object> map :queryResultList) {
            if (map.get("sector") != null && !"".equals(map.get("sector"))) {
                sectorSet.add(map.get("sector").toString());
            }
            if (map.get("task_package_id") == null) {
                taskPackageSet.add("1");
            } else if (!"".equals(map.get("task_package_id"))) {
                taskPackageSet.add(map.get("task_package_id").toString());
            }
            if (map.get("process") != null && !"".equals(map.get("process"))) {
                processSet.add(map.get("process").toString());
            }
            if (map.get("work_site_name") != null && !"".equals(map.get("work_site_name"))) {
                addressSet.add(map.get("work_site_name").toString());
            }
            if (map.get("running_status") != null && !"".equals(map.get("running_status"))) {
                statusSet.add(map.get("running_status").toString());
            }
            if (map.get("stage") != null && !"".equals(map.get("stage"))) {
                stageSet.add(map.get("stage").toString());
            }
        }
        WBSScreenResultDTO wbsScreenResultDTO = new WBSScreenResultDTO();
        if (sectorSet.size() > 0) {
            wbsScreenResultDTO.setModuleNoList(new ArrayList<>(sectorSet));
        }
        if (taskPackageSet.size() > 0) {
            wbsScreenResultDTO.setTaskPackageIdList(new ArrayList<>(taskPackageSet));
        }
        if (processSet.size() > 0) {
            wbsScreenResultDTO.setProcessList(new ArrayList<>(processSet));
        }
        if (addressSet.size() > 0) {
            wbsScreenResultDTO.setAddressList(new ArrayList<>(addressSet));
        }
        if (statusSet.size() > 0) {
            wbsScreenResultDTO.setStatusList(new ArrayList<>(statusSet));
        }
        if (stageSet.size() > 0) {
            wbsScreenResultDTO.setStageList(new ArrayList<>(stageSet));
        }
        return wbsScreenResultDTO;
    }


    /**
     * 取得 没有匹配的ISO的清单
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    @Override
    public Page<WBSEntryIsoDTO> getMatchedIsos(Long orgId, Long projectId, WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO) {


        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);



        StringBuffer preSqlCount = new StringBuffer()
            .append("select count(0) from")
            .append(" ( select ")
            .append(" distinct iso_no from wbs_entries_plain ")
            .append(" where iso_no is not null ");

        StringBuffer preSql = new StringBuffer()
            .append("select distinct iso_no, bom_match_percent,spm_ln_id from wbs_entries_plain ")
            .append(" where iso_no is not null ");

        StringBuffer sufixSql = new StringBuffer()
            .append(" group by iso_no, bom_match_percent, spm_ln_id")
            .append(" order by bom_match_percent desc");

        StringBuffer sufixSqlCount = new StringBuffer().append(" group by iso_no ) a ");

        Query queryCnt = setCondition(projectId,
            orgId,
            wbsEntryPlainQueryDTO,
            preSqlCount,
            sufixSqlCount,
            entityManager,
            null);

        BigInteger count = (BigInteger) queryCnt.getSingleResult();

        PageRequest pageRequest = PageRequest.of(
            wbsEntryPlainQueryDTO.toPageable().getPageNumber(),
            wbsEntryPlainQueryDTO.toPageable().getPageSize(),
            Sort.by(Sort.Order.desc("bom_match_percent"))
        );

        if (count.longValue() == 0) {
            return new PageImpl<>(new ArrayList<>(), pageRequest, 0);
        }

        Integer pageNumber = wbsEntryPlainQueryDTO.getPage().getNo();
        Integer pageSize = wbsEntryPlainQueryDTO.getPage().getSize();

        sufixSql.append(" limit " + (pageNumber - 1) * pageSize + "," + pageSize);
        Query query = setCondition(projectId,
            orgId,
            wbsEntryPlainQueryDTO,
            preSql,
            sufixSql,
            entityManager,
            "WBSEntryIsoDTOSqlResultMapping");

        List<WBSEntryIsoDTO> wbsEntryIsoDTOs = query.getResultList();
        return new PageImpl<>(wbsEntryIsoDTOs, wbsEntryPlainQueryDTO.toPageable(), count.longValue());
    }


    /**
     * 获取ISO匹配的材料信息
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    @Override
    public Page<WBSEntryMaterialDTO> getMatchedMaterials(Long orgId, Long projectId, WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO) {
        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();


        StringBuffer preSqlCount = new StringBuffer()
            .append("select count(0) from")
            .append(" ( select ")
            .append(" tag_number,")
            .append(" short_desc,")
            .append(" sum(quantity) quantity,")
            .append(" sum(resv_qty) resv_qty,")
            .append(" sum(issue_qty) issue_qty,")
            .append(" sum(lack_qty) lack_qty, ")
            .append(" ident ")
            .append(" from ")
            .append(" wbs_entries_plain_material ")
            .append(" where tag_number is not null ");

        StringBuffer preSql = new StringBuffer()
            .append(" select ")
            .append(" tag_number,")
            .append(" short_desc,")
            .append(" sum(quantity) quantity,")
            .append(" sum(resv_qty) resv_qty,")
            .append(" sum(issue_qty) issue_qty,")
            .append(" sum(lack_qty) lack_qty, ")
            .append(" ident ")
            .append(" from ")
            .append(" wbs_entries_plain_material ")
            .append(" where tag_number is not null ");


        StringBuffer sufixSql = new StringBuffer()
            .append(" group by tag_number, short_desc, ident ");

        StringBuffer sufixSqlCount = new StringBuffer()
            .append(" group by tag_number, short_desc, ident ) a ");

        Query queryCnt = setCondition(projectId,
            orgId,
            wbsEntryPlainQueryDTO,
            preSqlCount,
            sufixSqlCount,
            entityManager,
            null);

        BigInteger count = (BigInteger) queryCnt.getSingleResult();

        PageRequest pageRequest = PageRequest.of(
            wbsEntryPlainQueryDTO.toPageable().getPageNumber(),
            wbsEntryPlainQueryDTO.toPageable().getPageSize(),
            Sort.by(Sort.Order.desc("tag_number"))
        );

        if (count.longValue() == 0) {
            return new PageImpl<>(new ArrayList<>(), pageRequest, 0);
        }

        Integer pageNumber = wbsEntryPlainQueryDTO.getPage().getNo();
        Integer pageSize = wbsEntryPlainQueryDTO.getPage().getSize();

        sufixSql.append(" limit " + (pageNumber - 1) * pageSize + "," + pageSize);
        Query query = setCondition(projectId,
            orgId,
            wbsEntryPlainQueryDTO,
            preSql,
            sufixSql,
            entityManager,
            "WBSEntryMaterialDTOSqlResultMapping");

        List<WBSEntryMaterialDTO> wbsEntryMaterials = query.getResultList();
        return new PageImpl<>(wbsEntryMaterials, wbsEntryPlainQueryDTO.toPageable(), count.longValue());
    }

    /**
     * 扁平计划对应的发图率
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    @Override
    public Double getOverallIssuePercent(Long orgId, Long projectId, WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO) {
        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();


        StringBuffer preSql = new StringBuffer()
            .append("select ")
            .append(" case count(0) when 0 then 0 ")
            .append(" else sum(issue_status)/count(0) end ")
            .append(" from ")
            .append(" ( ")
            .append(" select issue_status ")
            .append(" from wbs_entries_plain_dwg ")
            .append(" where iso_no is not null and dwg_sht_no is not null ");


        StringBuffer sufixSql = new StringBuffer()
            .append(" group by iso_no,issue_status,dwg_sht_no ")
            .append(" ) a");

        Query query = setCondition(projectId,
            orgId,
            wbsEntryPlainQueryDTO,
            preSql,
            sufixSql,
            entityManager,
            null);

        BigDecimal matchPercent = (BigDecimal) query.getSingleResult();

        return matchPercent.doubleValue();
    }

    /**
     * 发图详情
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    @Override
    public Page<WBSEntryDwgDTO> getSubDwgIssedList(Long orgId, Long projectId, WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO) {
        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();


        StringBuffer preSqlCount = new StringBuffer()
            .append("select count(0) from")
            .append(" ( select ")
            .append(" iso_no ")
            .append(" from wbs_entries_plain_dwg ")
            .append(" where iso_no is not null and dwg_sht_no is not null ");

        StringBuffer preSql = new StringBuffer()
            .append(" select ")
            .append(" sub_drawing_id,")
            .append(" issue_status,")
            .append(" sub_drawing_no,")
            .append(" sub_drawing_version,")
            .append(" mark_deleted,")
            .append(" page_count,")
            .append(" dwg_sht_no,")
            .append(" iso_no ")
            .append(" from wbs_entries_plain_dwg ")
            .append(" where iso_no is not null and dwg_sht_no is not null ");

        StringBuffer sufixSql = new StringBuffer()
            .append(" group by iso_no, sub_drawing_id, issue_status, sub_drawing_no,")
            .append(" sub_drawing_version, mark_deleted, page_count, dwg_sht_no ")
            .append(" order by issue_status, iso_no");

        StringBuffer sufixSqlCount = new StringBuffer().append(" group by iso_no,dwg_sht_no ) a ");

        Query queryCnt = setCondition(projectId,
            orgId,
            wbsEntryPlainQueryDTO,
            preSqlCount,
            sufixSqlCount,
            entityManager,
            null);

        BigInteger count = (BigInteger) queryCnt.getSingleResult();

        PageRequest pageRequest = PageRequest.of(
            wbsEntryPlainQueryDTO.toPageable().getPageNumber(),
            wbsEntryPlainQueryDTO.toPageable().getPageSize()
        );

        if (count.longValue() == 0) {
            return new PageImpl<>(new ArrayList<>(), pageRequest, 0);
        }

        Integer pageNumber = wbsEntryPlainQueryDTO.getPage().getNo();
        Integer pageSize = wbsEntryPlainQueryDTO.getPage().getSize();

        sufixSql.append(" limit " + (pageNumber - 1) * pageSize + "," + pageSize);
        Query query = setCondition(projectId,
            orgId,
            wbsEntryPlainQueryDTO,
            preSql,
            sufixSql,
            entityManager,
            "WBSEntryDwgDTOSqlResultMapping");

        List<WBSEntryDwgDTO> wbsEntryIsoDTOs = query.getResultList();
        return new PageImpl<>(wbsEntryIsoDTOs, wbsEntryPlainQueryDTO.toPageable(), count.longValue());
    }

/*
    @Override
    public List<HierarchyBaseDTO> getProcessStages(Long orgId, Long projectId) {
        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        StringBuffer sql = new StringBuffer()
                .append("select ")
                .append("bps.id as stage_id,")
                .append("bps.name_en as name_en,")
                .append("bps.name_cn as name_cn ")
                .append(" from ")
                .append("(")
                .append("select stage from wbs_entry ")
                .append("where project_id = :projectId and org_id = :orgId")
                .append("group by stage ")
                .append(") wes left join bpm_process_stage bps on wes.stage = bps.name_en ")
                .append("where wes.stage is not null and project_id = :projectId");


        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("projectId", projectId);
        query.setParameter("orgId", orgId);

        List<WBSEntryDwgDTO> wbsEntryIsoDTOs = query.getResultList();
        return new PageImpl<>(wbsEntryIsoDTOs, wbsEntryPlainQueryDTO.toPageable(),count.longValue());

        return null;
    }*/


    private Predicate setCondition(Root root,
                                   CriteriaBuilder criteriaBuilder,
                                   Long projectId, Long orgId,
                                   WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO) {

        Predicate predicate;
        predicate = criteriaBuilder.equal(root.get("projectId"), projectId);


        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getEntityTypes())) {
            List<Predicate> predOr = new ArrayList<>();
            for (String entityType : wbsEntryPlainQueryDTO.getEntityTypes()) {
                predOr.add(criteriaBuilder.equal(root.get("entityType"), entityType));
            }

            Predicate[] predOrArr = new Predicate[predOr.size()];

            predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(predOr.toArray(predOrArr)));
        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getEntitySubTypes())) {
            List<Predicate> predOr = new ArrayList<>();
            for (String entitySubType : wbsEntryPlainQueryDTO.getEntitySubTypes()) {
                predOr.add(criteriaBuilder.equal(root.get("entitySubType"), entitySubType));
            }

            Predicate[] predOrArr = new Predicate[predOr.size()];

            predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(predOr.toArray(predOrArr)));
        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getEntryTypes())) {
            List<Predicate> predOr = new ArrayList<>();
            for (WBSEntryType entryType : wbsEntryPlainQueryDTO.getEntryTypes()) {
                predOr.add(criteriaBuilder.equal(root.get("type"), entryType));
            }

            Predicate[] predOrArr = new Predicate[predOr.size()];

            predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(predOr.toArray(predOrArr)));
        }

        Date startAt = wbsEntryPlainQueryDTO.getStartAt();
        Date finishAt = wbsEntryPlainQueryDTO.getFinishAt();


        if (startAt != null && finishAt == null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("finishAt"), startAt));
        } else if (startAt == null && finishAt != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("startAt"), finishAt));
        } else if (startAt != null && finishAt != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("finishAt"), startAt));
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("startAt"), finishAt));
        }


        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getRunningStatus())) {
            List<Predicate> predOr = new ArrayList<>();
            for (WBSEntryRunningStatus runningStatus : wbsEntryPlainQueryDTO.getRunningStatus()) {
                if (runningStatus == WBSEntryRunningStatus.ISNULL) {
                    predOr.add(criteriaBuilder.isNull(root.get("runningStatus")));
                } else {
                    predOr.add(criteriaBuilder.equal(root.get("runningStatus"), runningStatus));
                }
            }

            Predicate[] predOrArr = new Predicate[predOr.size()];

            predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(predOr.toArray(predOrArr)));
        }

/*        if(wbsEntryPlainQueryDTO.getPredecessorDone()==true){
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("predecessorCnt"),root.get("predecessorFinishCnt")));
        } else if(wbsEntryPlainQueryDTO.getPredecessorDone()==false){
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.notEqual(root.get("predecessorCnt"),root.get("predecessorFinishCnt")));
        }*/

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getStages())) {
            List<Predicate> predOr = new ArrayList<>();
            for (String stage : wbsEntryPlainQueryDTO.getStages()) {
                predOr.add(criteriaBuilder.equal(root.get("stage"), stage));
            }

            Predicate[] predOrArr = new Predicate[predOr.size()];

            predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(predOr.toArray(predOrArr)));
        }


        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getProcesses())) {
            List<Predicate> predOr = new ArrayList<>();
            for (String process : wbsEntryPlainQueryDTO.getProcesses()) {
                predOr.add(criteriaBuilder.equal(root.get("process"), process));
            }

            Predicate[] predOrArr = new Predicate[predOr.size()];

            predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(predOr.toArray(predOrArr)));
        }

        if (wbsEntryPlainQueryDTO.getBomMatchPercent() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.ge(root.get("bomMatchPercent"), wbsEntryPlainQueryDTO.getBomMatchPercent()));
        }

        if (wbsEntryPlainQueryDTO.getIssueStatus() != null) {
            if (wbsEntryPlainQueryDTO.getIssueStatus() == true) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isTrue(root.get("issueStatus")));
            } else if (wbsEntryPlainQueryDTO.getIssueStatus() == false) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isFalse(root.get("issueStatus")));
            }
        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getTeamIds())) {
            List<Predicate> predOr = new ArrayList<>();
            for (Long teamId : wbsEntryPlainQueryDTO.getTeamIds()) {
                predOr.add(criteriaBuilder.equal(root.get("teamId"), teamId));
            }

            Predicate[] predOrArr = new Predicate[predOr.size()];

            predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(predOr.toArray(predOrArr)));
        }


        if (!StringUtils.isEmpty(wbsEntryPlainQueryDTO.getWorkSiteAddress())) {

            List<Predicate> predOr = new ArrayList<>();
            predOr.add(criteriaBuilder.equal(root.get("workSiteAddress"), wbsEntryPlainQueryDTO.getWorkSiteAddress()));

            Predicate[] predOrArr = new Predicate[predOr.size()];

            predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(predOr.toArray(predOrArr)));
        }



        if (!StringUtils.isEmpty(wbsEntryPlainQueryDTO.getModuleNo())) {

            List<Predicate> predOr = new ArrayList<>();
            predOr.add(criteriaBuilder.equal(root.get("moduleNo"), wbsEntryPlainQueryDTO.getModuleNo()));

            Predicate[] predOrArr = new Predicate[predOr.size()];

            predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(predOr.toArray(predOrArr)));
        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getModuleTypes())) {
            List<Predicate> predOr = new ArrayList<>();
            for (String moduleType : wbsEntryPlainQueryDTO.getModuleTypes()) {
                predOr.add(criteriaBuilder.equal(root.get("moduleType"), moduleType));
            }

            Predicate[] predOrArr = new Predicate[predOr.size()];

            predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(predOr.toArray(predOrArr)));
        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getTaskPackageIds())) {
            List<Predicate> predOr = new ArrayList<>();
            boolean isNullFlag = false;
            for (Long taskPackageId : wbsEntryPlainQueryDTO.getTaskPackageIds()) {
                if (taskPackageId == 1) {
                    predOr.add(criteriaBuilder.and(predicate, criteriaBuilder.isNull(root.get("taskPackageId"))));
                } else {
                    predOr.add(criteriaBuilder.equal(root.get("taskPackageId"), taskPackageId));
                }
            }

            Predicate[] predOrArr = new Predicate[predOr.size()];

            predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(predOr.toArray(predOrArr)));
        }


        if (wbsEntryPlainQueryDTO.getName() != null) {
            String name = wbsEntryPlainQueryDTO.getName() + '%';
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), name));
        }

        return predicate;
    }


    private Query setCondition(Long projectId, Long orgId,
                               WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO,
                               StringBuffer preSql,
                               StringBuffer sufixSql,
                               EntityManager entityManager,
                               String mappingString) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        StringBuffer sql = new StringBuffer();
        if (projectId != null) sql.append(" and project_id=:projectId");
        if (orgId != null) sql.append(" and org_id=:orgId");

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getEntityTypes())) {




            sql.append(" and entity_type in (:entityTypes) ");

        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getEntitySubTypes())) {
            sql.append(" and entity_sub_type in (:entitySubTypes) ");
        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getEntryTypes())) {
            sql.append(" and type in (:entryTypes) ");
        }

        Date startAt = wbsEntryPlainQueryDTO.getStartAt();
        Date finishAt = wbsEntryPlainQueryDTO.getFinishAt();


        if (startAt != null && finishAt == null) {
            sql.append(" and finish_at>=:startAt ");
        } else if (startAt == null && finishAt != null) {
            sql.append(" and start_at<=:finishAt ");
        } else if (startAt != null && finishAt != null) {
            sql.append(" and finish_at>=:startAt ");
            sql.append(" and start_at<=:finishAt ");
        }

        List<WBSEntryRunningStatus> wbsStatus = wbsEntryPlainQueryDTO.getRunningStatus();
        if (!CollectionUtils.isEmpty(wbsStatus)) {
            if (wbsStatus.contains(WBSEntryRunningStatus.ISNULL)) {
                if (wbsStatus.size() == 1) {
                    sql.append(" and running_status is null");
                } else {
                    sql.append(" and (running_status is null or running_status in (:runningStatus)) ");
                }
            } else {
                sql.append(" and running_status in (:runningStatus) ");
            }
        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getStages())) {
            sql.append(" and stage in (:stages) ");
        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getProcesses())) {
            sql.append(" and process in (:processes) ");
        }

        if (wbsEntryPlainQueryDTO.getBomMatchPercent() != null) {
            sql.append(" and bom_match_percent=:bomMatchPercent");
        }

        if (wbsEntryPlainQueryDTO.getIssueStatus() != null) {
            if (wbsEntryPlainQueryDTO.getIssueStatus() == true) {
                sql.append(" and issue_status=1");
            } else if (wbsEntryPlainQueryDTO.getIssueStatus() == false) {
                sql.append(" and issue_status=0");
            }
        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getTeamIds())) {
            sql.append(" and team_id in (:teamIds) ");
        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getWorkSiteIds())) {
            sql.append(" and work_site_id in (:workSiteIds) ");
        }


        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getModuleIds())) {
            List<String> cnds = new ArrayList<>();
            Integer i = 0;
            for (String module : wbsEntryPlainQueryDTO.getModuleIds()) {
                Long moduleId = Long.parseLong(module);
                cnds.add("hn_path like concat('%/',:moduleId" + i.toString() + ",'%')");
                i++;
            }
            sql.append(" and (" + StringUtils.join(cnds.toArray(), " or ") + " )");
        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getModuleTypes())) {
            sql.append(" and module_type in (:moduleTypes) ");
        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getTaskPackageIds())) {
            sql.append(" and task_package_id in (:taskPackageIds) ");
        }


        Query query;
        if (mappingString != null) {
            query = entityManager.createNativeQuery(preSql.toString() + sql.toString() + " " + sufixSql, mappingString);
        } else {
            query = entityManager.createNativeQuery(preSql.toString() + sql.toString() + " " + sufixSql);
        }
        query.setParameter("projectId", projectId);
        query.setParameter("orgId", orgId);

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getEntityTypes())) {
            Set<String> entityTypes = new HashSet<>();
            for (String entityType : wbsEntryPlainQueryDTO.getEntityTypes()) {
                entityTypes.add(entityType);
            }
            query.setParameter("entityTypes", entityTypes);

        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getEntitySubTypes())) {
            query.setParameter("entitySubTypes", wbsEntryPlainQueryDTO.getEntitySubTypes());
        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getEntryTypes())) {
            Set<String> entryTypes = new HashSet<>();
            for (WBSEntryType entryType : wbsEntryPlainQueryDTO.getEntryTypes()) {
                entryTypes.add(entryType.name());
            }
            query.setParameter("entryTypes", entryTypes);
        }



        if (startAt != null) {
            query.setParameter("startAt", sdf.format(startAt));
        }

        if (finishAt != null) {
            query.setParameter("finishAt", sdf.format(finishAt));
        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getRunningStatus())) {
            Set<String> runningStatuses = new HashSet<>();
            for (WBSEntryRunningStatus runningStatus : wbsEntryPlainQueryDTO.getRunningStatus()) {
                runningStatuses.add(runningStatus.name());
            }
            if ((runningStatuses.contains("ISNULL") && runningStatuses.size() > 1)
                || !runningStatuses.contains("ISNULL")) {
                query.setParameter("runningStatus", runningStatuses);
            }
        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getStages())) {
            query.setParameter("stages", wbsEntryPlainQueryDTO.getStages());
        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getProcesses())) {
            query.setParameter("processes", wbsEntryPlainQueryDTO.getProcesses());
        }

        if (wbsEntryPlainQueryDTO.getBomMatchPercent() != null) {
            query.setParameter("bomMatchPercent", wbsEntryPlainQueryDTO.getBomMatchPercent());
        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getTeamIds())) {
            query.setParameter("teamIds", wbsEntryPlainQueryDTO.getTeamIds());
        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getWorkSiteIds())) {
            query.setParameter("workSiteIds", wbsEntryPlainQueryDTO.getWorkSiteIds());
        }


        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getModuleIds())) {

            Integer i = 0;
            for (String module : wbsEntryPlainQueryDTO.getModuleIds()) {
                Long moduleId = Long.parseLong(module);
                query.setParameter("moduleId" + i, moduleId);
                i++;
            }
        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getModuleTypes())) {
            query.setParameter("moduleTypes", wbsEntryPlainQueryDTO.getModuleTypes());
        }

        if (!CollectionUtils.isEmpty(wbsEntryPlainQueryDTO.getTaskPackageIds())) {
            query.setParameter("taskPackageIds", wbsEntryPlainQueryDTO.getTaskPackageIds());
        }

        return query;
    }

}
