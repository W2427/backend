package com.ose.issues.domain.model.service;

import com.ose.util.*;
import com.ose.auth.api.OrganizationFeignAPI;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.dto.BatchGetDTO;
import com.ose.auth.entity.Organization;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.issues.domain.model.repository.*;
import com.ose.issues.dto.*;
import com.ose.issues.entity.Issue;
import com.ose.issues.entity.IssueEntity;
import com.ose.issues.entity.IssuePropertyDefinition;
import com.ose.issues.entity.IssueRecord;
import com.ose.issues.vo.IssueSource;
import com.ose.issues.vo.IssueType;
import com.ose.vo.EntityStatus;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Component
public class IssueService extends IssueBusiness implements IssueInterface {

    // 上传文件临时路径
    @Value("${application.files.temporary}")
    private String temporaryDir;

    // 数据仓库
    private final IssueRepository issueRepository;

    private final IssueEntityRepository issueEntityRepository;

    // Feign API
    private final UserFeignAPI userFeignAPI;

    private final OrganizationFeignAPI orgFeignAPI;

    //issue property repository
    private final IssuePropertyDefinitionRepository issuePropertyDefinitionRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public IssueService(
        IssueRepository issueRepository,
        IssuePropertyRepository issuePropertyRepository,
        IssueRecordRepository issueRecordRepository,
        PropertyDefinitionRepository propertyDefinitionRepository,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UploadFeignAPI uploadFeignAPI,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UserFeignAPI userFeignAPI,
        IssueEntityRepository issueEntityRepository,
        OrganizationFeignAPI orgFeignAPI, IssuePropertyDefinitionRepository issuePropertyDefinitionRepository) {
        super(
            issueRepository,
            issuePropertyRepository,
            issueRecordRepository,
            propertyDefinitionRepository,
            uploadFeignAPI
        );
        this.issueRepository = issueRepository;
        this.userFeignAPI = userFeignAPI;
        this.issueEntityRepository = issueEntityRepository;
        this.orgFeignAPI = orgFeignAPI;
        this.issuePropertyDefinitionRepository = issuePropertyDefinitionRepository;
    }

    /**
     * 创建遗留问题。
     *
     * @param operatorId     操作人ID
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param issueCreateDTO 问题信息
     */
    @Override
    public Issue create(
        final Long operatorId,
        final Long orgId,
        final Long projectId,
        final IssueCreateDTO issueCreateDTO
    ) {

        Issue issue = BeanUtils
            .copyProperties(issueCreateDTO, new Issue(), "attachment");

        checkNoAvailability(projectId, IssueType.ISSUE, issue.getNo());

        issue.setOrgId(orgId);
        issue.setProjectId(projectId);
        issue.setType(IssueType.ISSUE);
        issue.setOriginatorId(ValueUtils.ifNull(issue.getOriginatorId(), operatorId));
        issue.setPersonInChargeId(issueCreateDTO.getLeader());
        issue.setMemberIDs(issueCreateDTO.getMembers());
        issue.setCreatedAt();
        issue.setCreatedBy(operatorId);
        issue.setLastModifiedAt();
        issue.setLastModifiedBy(operatorId);
        issue.setStatus(ValueUtils.ifNull(issue.getStatus(), EntityStatus.ACTIVE));

        saveAttachmentAndProperties(
            orgId, projectId, issue,
            issueCreateDTO.getAttachment(),
            issueCreateDTO.getProperties()
        );

        issue.setSeriesNo(getNewIssueSeriesNo(orgId, projectId, issue.getPunchSource()));
        issue.setNo(getNewIssueNo(issue, issueCreateDTO.getProjectName()));
        issueRepository.save(issue);

        saveIssueRecord(operatorId, issue.getId(), "创建遗留问题。");

        createOrUpdateIssueEntities(issue);

        return issue;
    }

    /**
     * 生成新的编号
     *
     * @param issue
     * @param projectName
     * @return
     */
    private String getNewIssueNo(Issue issue, String projectName) {
        String newNo = projectName + "-";
        if (issue.getPunchSource() == IssueSource.QC) {
            newNo += "W" + "-";
        } else if (issue.getPunchSource() == IssueSource.OWNER) {
            newNo += "C" + "-";
        } else if (issue.getPunchSource() == IssueSource.THIRD_PARTY) {
            newNo += "T" + "-";
        }

        String indexStr = "" + issue.getSeriesNo();
        if (indexStr.length() < 4) {
            for (int i = 0; i < 4 - indexStr.length(); i++) {
                newNo += "0";
            }
        }
        return newNo + indexStr;
    }

    /**
     * 获取新的流水号
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param source
     * @return
     */
    private Integer getNewIssueSeriesNo(Long orgId, Long projectId, IssueSource source) {
        Integer seriesNo = issueRepository.findMaxSeriesByProjectIdAndPunchSource(projectId, source);
        if (seriesNo == null) {
            return 1;
        }
        return seriesNo + 1;
    }

    private void createOrUpdateIssueEntities(Issue issue) {

        List<IssueEntity> list = issueEntityRepository.findByIssueId(issue.getId());
        if (!list.isEmpty()) {
            for (IssueEntity entity : list) {
                issueEntityRepository.delete(entity);
            }
        }

        if (!StringUtils.isEmpty(issue.getEntities())) {
            String[] entities = issue.getEntities().split(",");
            String[] area = (StringUtils.isEmpty(issue.getModule()) ? "" : issue.getModule()).split(",");
            String[] cleanPackage = (StringUtils.isEmpty(issue.getCleanPackage()) ? "" : issue.getCleanPackage()).split(",");
            String[] layer = (StringUtils.isEmpty(issue.getModule()) ? "" : issue.getModule()).split(",");
            String[] pressureTestPackage = (StringUtils.isEmpty(issue.getPressureTestPackage()) ? "" : issue.getPressureTestPackage()).split(",");
            String[] subSystem = (StringUtils.isEmpty(issue.getSubSystem()) ? "" : issue.getSubSystem()).split(",");
            for (int i = 0; i < entities.length; i++) {
                IssueEntity issueEntity = new IssueEntity();
                if (area.length > i) {
                    issueEntity.setArea(StringUtils.isEmpty(area[i]) ? null : area[i]);
                }
                if (cleanPackage.length > i) {
                    issueEntity.setCleanPackage(StringUtils.isEmpty(cleanPackage[i]) ? null : cleanPackage[i]);
                }
                issueEntity.setCreatedAt();
                issueEntity.setDepartment(issue.getDepartmentId());
                issueEntity.setEntityNo(entities[i]);
                issueEntity.setIssueId(issue.getId());
                if (layer.length > i) {
                    issueEntity.setLayer(StringUtils.isEmpty(layer[i]) ? null : layer[i]);
                }
                if (pressureTestPackage.length > i) {
                    issueEntity.setPressureTestPackage(StringUtils.isEmpty(pressureTestPackage[i]) ? null : pressureTestPackage[i]);
                }
                issueEntity.setProjectId(issue.getProjectId());
                issueEntity.setStatus(issue.getStatus());
                if (subSystem.length > i) {
                    issueEntity.setSubSystem(StringUtils.isEmpty(subSystem[i]) ? null : subSystem[i]);
                }
                issueEntityRepository.save(issueEntity);
            }

        }
    }

    /**
     * 更新。
     *
     * @param operatorId     操作人ID
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param issueId        问题ID
     * @param issueUpdateDTO 更新信息
     */
    @Transactional
    @Override
    public Issue update(
        final Long operatorId,
        final Long orgId,
        final Long projectId,
        final Long issueId,
        final IssueUpdateDTO issueUpdateDTO
    ) {

        Issue issue = issueRepository.findByIdAndDeletedIsFalse(issueId);

        if (issue == null) {
            throw new NotFoundError();
        }

        if (issue.getStatus() == EntityStatus.CLOSED) {
            throw new BusinessError("问题已关闭");
        }

        BeanUtils.copyProperties(issueUpdateDTO, issue, "no", "attachment", "status", "personInCharge", "qc");

//        if (issueUpdateDTO.getNo() == null) {
//            issue.setNo(issueUpdateDTO.getNo());
//            checkNoAvailability(projectId, IssueType.ISSUE, issue.getNo(), issueId);
//        }

        // 状态更新时保存状态更新记录
        if (issueUpdateDTO.getStatus() != null
            && issueUpdateDTO.getStatus() != issue.getStatus()) {

            saveIssueRecord(operatorId, issueId, String.format(
                "更新状态：%s -> %s。",
                issue.getStatus().getDisplayName(),
                issueUpdateDTO.getStatus().getDisplayName()
            ));

            issue.setStatus(issueUpdateDTO.getStatus());

            if (issue.getStatus() == EntityStatus.DELETED) {
                issue.setDeletedAt();
                issue.setDeletedBy(operatorId);
            } else {
                issue.setLastModifiedAt();
                issue.setLastModifiedBy(operatorId);
            }
        }

        issue.setMemberIDs(issueUpdateDTO.getMembers());

        saveAttachmentAndProperties(
            orgId, projectId, issue,
            issueUpdateDTO.getAttachment(),
            issueUpdateDTO.getProperties()
        );

        createOrUpdateIssueEntities(issue);

        return issueRepository.save(issue);
    }

    /**
     * 查询问题。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询参数
     * @param pageable    分页参数
     * @return 问题列表
     */
    @Override
    public Page<Issue> search(
        Long orgId,
        Long projectId,
        IssueCriteriaDTO criteriaDTO,
        Pageable pageable
    ) {
        return setProperties(projectId,
            issueRepository
                .search(orgId, projectId, criteriaDTO, pageable, Issue.class)
        );
    }

    /**
     * 查询遗留问题。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询参数
     * @return 遗留问题列表
     */
    @Override
    public List<Issue> search(
        final Long orgId,
        final Long projectId,
        final IssueCriteriaDTO criteriaDTO
    ) {
        return setProperties(projectId,
            issueRepository.search(orgId, projectId, criteriaDTO, Issue.class)
        );
    }

    /**
     * 获取遗留问题详情。
     *
     * @param issueId 问题ID
     * @return 问题信息
     */
    @Override
    public Issue get(Long projectId, Long issueId) {
        return setProperties(projectId, issueRepository.findByIdAndDeletedIsFalse(issueId));
    }

    /**
     * 问题移交。
     *
     * @param operatorId       操作人ID
     * @param orgId            组织ID
     * @param projectId        项目ID
     * @param issueTransferDTO 更新信息
     */
//    @Transactional
    @Override
    public void transfer(
        Long operatorId,
        Long orgId,
        Long projectId,
        IssueTransferDTO issueTransferDTO
    ) {

        List<Issue> issues = issueRepository
            .findByIdInAndDeletedIsFalse(issueTransferDTO.getIssues());

        if (issues.size() < issueTransferDTO.getIssues().size()) {
            throw new BusinessError("部分问题不存在，请重新选择");
        }

        Long toUserId = issueTransferDTO.getPersonInCharge();
        Long newDepartment = issueTransferDTO.getDepartments() == null ? null : issueTransferDTO.getDepartments().get(0);
        Long toQcId = issueTransferDTO.getQc();
        Long fromUserId;
        Long fromQcId;
        Set<Long> userIDs = new HashSet<>();
        Set<Long> departmentIDs = new HashSet<>();
        List<Issue> issuesToBeModified = new ArrayList<>();

        userIDs.add(toUserId);

        // 更新问题信息，当负责人被更新时将问题信息加入到待更新列表中
        for (Issue issue : issues) {

            fromUserId = issue.getPersonInChargeId();
            fromQcId = issue.getQcId();
            Long oldDepartment = issue.getDepartmentId();

            if (ValueUtils.equals(fromUserId, toUserId) && ValueUtils.equals(fromQcId, toQcId) && ValueUtils.equals(oldDepartment, newDepartment)) {
                continue;
            }

            if (!LongUtils.isEmpty(fromUserId)) {
                userIDs.add(fromUserId);
            }
            if (!LongUtils.isEmpty(fromQcId)) {
                userIDs.add(fromQcId);
            }
            if (!LongUtils.isEmpty(newDepartment)) {
                departmentIDs.add(newDepartment);
            }

            issue.setPersonInChargeId(toUserId);
            issue.setDepartmentId(newDepartment);
            issue.setQcId(toQcId);
            issue.setLastModifiedAt();
            issue.setLastModifiedBy(operatorId);
            issuesToBeModified.add(issue);
        }

        // 若不存在负责人被更新的问题信息则结束
        if (issuesToBeModified.size() == 0) {
            return;
        }

        Map<Long, String> userMap = new HashMap<>();
        Map<Long, String> departmentMap = new HashMap<>();

        // 取得所有相关用户信息
        userFeignAPI
            .batchGet(new BatchGetDTO(userIDs))
            .getData()
            .forEach(user -> userMap.put(user.getId(), user.getName()));

        orgFeignAPI.batchGet(new BatchGetDTO(departmentIDs)).getData().forEach(dpm -> {
            departmentMap.put(dpm.getId(), dpm.getName());
        });

        for (Issue issue : issuesToBeModified) {
            if (issue.getPersonInChargeId() != null) {
                String pic = userMap.get(issue.getPersonInChargeId());
                if (!StringUtils.isEmpty(pic)) issue.setPersonInCharge(pic);
            }

            if (issue.getQcId() != null) {
                String qcc = userMap.get(issue.getQcId());
                if (!StringUtils.isEmpty(qcc)) issue.setQc(qcc);
            }

            if (issue.getDepartmentId() != null) {
                String dpmn = departmentMap.get(issue.getDepartmentId());
                if (!StringUtils.isEmpty(dpmn)) issue.setDepartment(dpmn);
            }
        }

        // 取得目标负责人姓名
        String toUserName = userMap.get(issueTransferDTO.getPersonInCharge()) == null
            ? "[无]"
            : userMap.get(issueTransferDTO.getPersonInCharge());

        List<IssueRecord> records = new ArrayList<>();

        // 设置移交记录
        for (Issue issue : issues) {
            records.add(new IssueRecord(
                issue.getId(),
                String.format(
                    "遗留问题移交：%s -> %s；移交原因：%s。",
                    ValueUtils.ifNull(userMap.get(issue.getPersonInChargeId()), "[无]"),
                    toUserName,
                    issueTransferDTO.getComment()
                )
            ));
        }

        // 保存遗留问题信息
        issueRepository.saveAll(issuesToBeModified);

        // 保存移交记录
        saveIssueRecords(operatorId, records);
    }

    @Override
    public List<IssueDepartmentDTO> departments(Long orgId, Long projectId, IssueCriteriaDTO issueCriteriaDTO,
                                                OrganizationFeignAPI orgFeignAPI) {
        Page<Issue> issueList = issueRepository.search(orgId, projectId, issueCriteriaDTO, null, Issue.class);
        List<Long> departments = new ArrayList<>();
        for (Issue issue : issueList.getContent()) {
            if (issue.getDepartment() != null
                && !departments.contains(issue.getDepartment())) {
                departments.add(issue.getDepartmentId());
            }
        }
        List<IssueDepartmentDTO> result = new ArrayList<>();
        for (Long departmentId : departments) {
            Organization department = orgFeignAPI.details(departmentId, null).getData();
            if (department != null)
                result.add(new IssueDepartmentDTO(department.getName(), departmentId));
        }
        return result;
    }

    @Override
    public File saveDownloadFile(Long orgId, Long projectId, String issueType, Long operatorId) {

        File excel;
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        // 在当前Excel创建一个子表
        SXSSFSheet sheet = workbook.createSheet(issueType);
        int rowNum = 0;//DATA_START_ROW;
        Row titleRow = sheet.createRow(rowNum++);
//        WorkbookUtils.getRow(sheet, rowNum++);


        String sql = "select " +
            "i.title as Title, " +
//                     "i.owner as introducer, " +
            "usr.name as introducer, " +
            "i.created_at as creatTime, " +
            "i.description as DESCR, " +
            "i.status as Status ";
        WorkbookUtils.getCell(titleRow, 0).setCellValue("标题");
        WorkbookUtils.getCell(titleRow, 1).setCellValue("提出人");
        WorkbookUtils.getCell(titleRow, 2).setCellValue("创建时间");
        WorkbookUtils.getCell(titleRow, 3).setCellValue("描述");
        WorkbookUtils.getCell(titleRow, 4).setCellValue("状态");

        List<IssuePropertyDefinition> issuePropertyDefinitions = new ArrayList<>();

        if (issueType.equalsIgnoreCase(IssueType.EXPERIENCE.toString())) {
            issuePropertyDefinitions = issuePropertyDefinitionRepository.findByOrgIdAndProjectIdAndIssueTypeAndDeletedIsFalse(orgId, projectId, IssueType.EXPERIENCE);
        } else {
            issuePropertyDefinitions = issuePropertyDefinitionRepository.findByOrgIdAndProjectIdAndDeletedIsFalse(orgId, projectId);
        }


        String caseSql = "";
        int j = 4;
        //遗留意见自定义项导出
        for (IssuePropertyDefinition ipd : issuePropertyDefinitions) {
//            if(caseSql.equals("")) {
//                caseSql = "case when ip.property_id = '" + ipd.getId() + "' then ip.value_options end as " + ipd.getName();
//            } else {
            caseSql = caseSql + ", group_concat(case when ip.property_id = '" + ipd.getId() + "' then ip.value_options end) as `" + ipd.getName() + "`";
//            }
            j++;
            WorkbookUtils.getCell(titleRow, j).setCellValue(ipd.getName());
        }
        j++;
        String from = " from issues i " +
            " left join issue_properties ip on i.id = ip.issue_id " +
            " left join ose_auth.users usr on usr.id = i.created_by ";

        String where = " where i.org_id = :orgId and i.project_id = :projectId and i.deleted = 0 and i.type = '" + issueType + "' and i.current_experience is not false ";

        String searchSelect = "SELECT DISTINCT i";
        String groupBy = " group by i.id ";
        String orderBy = " ORDER BY i.created_at DESC";

        sql = sql + caseSql + from + where + groupBy + orderBy;


        List<List<Object>> issueList = issueRepository.getXlsList(orgId, projectId, sql);//,pageDTO,criteriaDTO).getContent();//.findByOrgIdAndProjectId(orgId,projectId);

        for (int k = 0; k < issueList.size(); k++) {
            // 行
            SXSSFRow row = sheet.createRow(rowNum++);
            for (int i = 0; i < j; i++) {
                String str = String.valueOf(issueList.get(k).get(i));
                if (str.equals("null")) str = "";
                // 单元格
                SXSSFCell cell = row.createCell(i);
                // 写入数据
                cell.setCellValue(str);
            }
        }
        String temporaryFileName = "issueExperinces" + System.currentTimeMillis() + ".xlsx";

        try {
            workbook.write(new FileOutputStream(temporaryDir + "/" + temporaryFileName));
            //WorkbookUtils.save(workbook, temporaryDir +"/" + temporaryFileName);//excel.getAbsolutePath());
            excel = new File(temporaryDir, temporaryFileName);
            workbook.close();
            return excel;
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new BusinessError();
        }
    }

    @Override
    public List<Map<String, Object>> getDepartments(Long projectId, IssueCriteriaDTO issueCriteriaDTO) {

        return issueRepository.getColumnItems(projectId, issueCriteriaDTO, "department", true);

    }

    @Override
    public List<Map<String, Object>> getSources(Long projectId, IssueCriteriaDTO issueCriteriaDTO) {

        return issueRepository.getColumnItems(projectId, issueCriteriaDTO, "punch_source", false);

    }

    @Override
    public List<Map<String, Object>> getModules(Long projectId, IssueCriteriaDTO issueCriteriaDTO) {
        return issueRepository.getColumnItems(projectId, issueCriteriaDTO, "module", false);

    }

    @Override
    public Map<String, String> getColumnHeaderMap(Long projectId) {
        Map<String, String> columnHeaderMap = new HashMap<>();
        List<Tuple> columnHeaders = issuePropertyDefinitionRepository.getColumnHeaderMap(projectId, IssueType.ISSUE);
        columnHeaders.forEach(col -> {
            String columnName = (String) col.get("nm");
            String shortDesc = (String) col.get("shortDesc");
            columnHeaderMap.put(columnName, shortDesc);

        });

        return columnHeaderMap;
    }

    @Override
    public List<Map<String, Object>> getSystems(Long projectId, IssueCriteriaDTO issueCriteriaDTO) {
        return issueRepository.getColumnItems(projectId, issueCriteriaDTO, "sub_system", false);
    }

    @Override
    public List<Map<String, Object>> getDisciplines(Long projectId, IssueCriteriaDTO issueCriteriaDTO) {
        return issueRepository.getColumnItems(projectId, issueCriteriaDTO, "discipline", false);
    }

    @Override
    public void open(
        final Long orgId,
        final Long projectId,
        final Long issueId,
        ContextDTO contextDTO
    ) {
        Issue issue = issueRepository.findByIdAndDeletedIsFalse(
            issueId
        );
        if(issue==null){
            throw new BusinessError("遗留问题不存在");
        }

        issue.setLastModifiedBy(contextDTO.getOperator().getId());
        issue.setLastModifiedAt(new Date());
        issue.setStatus(EntityStatus.ACTIVE);
        issueRepository.save(issue);
    }

    @Override
    public void close(
        final Long orgId,
        final Long projectId,
        final Long issueId,
        ContextDTO contextDTO
    ) {
        Issue issue = issueRepository.findByIdAndDeletedIsFalse(
            issueId
        );
        if(issue==null){
            throw new BusinessError("遗留问题不存在");
        }
        issue.setLastModifiedBy(contextDTO.getOperator().getId());
        issue.setLastModifiedAt(new Date());
        issue.setStatus(EntityStatus.CLOSED);
        issueRepository.save(issue);

    }
}
