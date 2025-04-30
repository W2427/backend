package com.ose.issues.domain.model.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.util.*;
import com.ose.auth.api.OrganizationFeignAPI;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.dto.BatchGetDTO;
import com.ose.auth.dto.OrganizationBasicDTO;
import com.ose.auth.dto.UserNameCriteriaDTO;
import com.ose.auth.entity.UserBasic;
import com.ose.auth.entity.UserProfile;
import com.ose.baiduNormalTranslate.NormalTranslateResult;
import com.ose.baiduNormalTranslate.TransApi;
import com.ose.baiduNormalTranslate.TranslateDetail;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FileMetadataDTO;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.BaseDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.issues.domain.model.repository.*;
import com.ose.issues.dto.IssueImportDTO;
import com.ose.issues.entity.*;
import com.ose.issues.vo.*;
import com.ose.response.JsonObjectResponseBody;
import com.ose.vo.EntityStatus;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class IssueImportService extends IssueBusiness implements IssueImportInterface {

    @Value("${baidu.translate.appid}")
    private String APP_ID;
    @Value("${baidu.translate.key}")
    private String SECURITY_KEY;

    private static final String STYLES_SHEET_NAME = "styles";
    private static final String ISSUES_SHEET_NAME = "issues";
    private static final String COLUMNS_SHEET_NAME = "COLUMNS";
    private static final int HEADER_ROW_NO = 1;
    private static final int COLUMN_HEADER_ROW_NO = 2;
    private static final int LEADER_USERNAME_COLUMN_NO = 7;
    private static final int MEMBER_USERNAMES_COLUMN_NO = 8;
    private static final int CUSTOM_PROPERTY_COLUMN_START_AT = 9;
    private static final SimpleDateFormat BATCH_NO_FORMAT = new SimpleDateFormat("YYYYMMddHHmmss");

    // 上传文件临时路径
    @Value("${application.files.temporary}")
    private String temporaryDir;

    // 数据仓库
    private final IssueRepository issueRepository;
    private final IssuePropertyRepository issuePropertyRepository;
    private final PropertyDefinitionRepository propertyDefinitionRepository;
    private final IssueImportTemplateRepository issueImportTemplateRepository;
    private final IssueImportRecordRepository issueImportRecordRepository;
    private final OrganizationFeignAPI orgFeignAPI;

    // Feign API
    private final UploadFeignAPI uploadFeignAPI;
    private final UserFeignAPI userFeignAPI;

    /**
     * 构造方法。
     */
    @Autowired
    public IssueImportService(
        IssueRepository issueRepository,
        IssuePropertyRepository issuePropertyRepository,
        IssueRecordRepository issueRecordRepository,
        PropertyDefinitionRepository propertyDefinitionRepository,
        IssueImportTemplateRepository issueImportTemplateRepository,
        IssueImportRecordRepository issueImportRecordRepository,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UploadFeignAPI uploadFeignAPI,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UserFeignAPI userFeignAPI,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            OrganizationFeignAPI orgFeignAPI) {
        super(
            issueRepository,
            issuePropertyRepository,
            issueRecordRepository,
            propertyDefinitionRepository,
            uploadFeignAPI
        );
        this.issueRepository = issueRepository;
        this.issuePropertyRepository = issuePropertyRepository;
        this.propertyDefinitionRepository = propertyDefinitionRepository;
        this.issueImportTemplateRepository = issueImportTemplateRepository;
        this.issueImportRecordRepository = issueImportRecordRepository;
        this.uploadFeignAPI = uploadFeignAPI;
        this.userFeignAPI = userFeignAPI;
        this.orgFeignAPI = orgFeignAPI;
    }

    /**
     * 取得问题导入文件信息。
     *
     * @param projectId 项目 ID
     * @param issueType 问题类型
     * @return 问题导入文件信息
     */
    @Override
    public IssueImportTemplate getImportFile(
        final Long projectId,
        final IssueType issueType
    ) {
        return issueImportTemplateRepository
            .findByProjectIdAndIssueType(projectId, issueType)
            .orElse(null);
    }

    /**
     * 重新生成问题导入文件。
     *
     * @param operatorId 操作者 ID
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param issueType  问题类型
     * @return 问题导入文件
     */
    @Override
    public IssueImportTemplate generateImportTemplate(
        final Long operatorId,
        final Long orgId,
        final Long projectId,
        final IssueType issueType
    ) {

        InputStream stream = this
            .getClass()
            .getClassLoader()
            .getResourceAsStream("templates/" + issueType.getImportFileName());

        if (stream == null) {
            return null;
        }

        String temporaryFilename = FileUtils.copy(stream, temporaryDir, operatorId.toString());

        File excel;
        Workbook workbook;
        Sheet sheet;

        try {
            excel = new File(temporaryDir, temporaryFilename);
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            throw new BusinessError();
        }

        if (workbook == null || (sheet = workbook.getSheet(ISSUES_SHEET_NAME)) == null) {
            return null;
        }

        // 取得已生成的导入文件信息
        IssueImportTemplate template = issueImportTemplateRepository
            .findByProjectIdAndIssueType(projectId, issueType).orElse(null);

        // 取得项目的所有自定义属性
        List<IssuePropertyDefinition> propertyDefinitions = propertyDefinitionRepository
            .findByProjectIdAndIssueTypeAndDeletedIsFalse(projectId, issueType);

        // 属性名称列表
        List<String> propertyNameList = new ArrayList<>();
        String propertyNames = null;

        // 若存在自定义属性，则将自定义属性加入到导入文件的表头
        if (propertyDefinitions.size() > 0) {

            propertyDefinitions.sort(Comparator.comparing(IssuePropertyDefinition::getName));

            long maxVersion = 0L;

            for (IssuePropertyDefinition propertyDefinition : propertyDefinitions) {
                maxVersion = Math.max(propertyDefinition.getVersion(), maxVersion);
            }

            if (template != null && template.getVersion() > maxVersion) {
                return template;
            }

            Row headers = sheet.getRow(HEADER_ROW_NO);
            int colNo = CUSTOM_PROPERTY_COLUMN_START_AT;

            for (IssuePropertyDefinition propertyDefinition : propertyDefinitions) {

                propertyNameList.add(propertyDefinition.getName());

                WorkbookUtils.copyColumn(
                    sheet,
                    colNo - 1,
                    colNo,
                    HEADER_ROW_NO + 1,
                    propertyDefinition.getOptionList()
                );

                WorkbookUtils
                    .setCellValue(sheet, HEADER_ROW_NO, colNo, propertyDefinition.getName())
                    .setCellStyle(headers.getCell(colNo - 1).getCellStyle());

                colNo++;
            }

            propertyNames = String.join("\r\n", propertyNameList);
        }

        // 首次生成时创建导入模版文件信息
        if (template == null) {
            template = new IssueImportTemplate();
            template.setProjectId(projectId);
            template.setIssueType(issueType);
            template.setCreatedAt();
            template.setCreatedBy(operatorId);
            // 更新时，若自定义属性无更新则结束
        } else if (ValueUtils.equals(template.getPropertyNames(), propertyNames)) {
            return template;
        }

        template.setPropertyNames(propertyNames);

        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
        } catch (IOException e) {
            throw new BusinessError(e.getMessage());
        }

        FileMetadataDTO.FileUploadConfig uploadConfig = new FileMetadataDTO.FileUploadConfig();
        uploadConfig.setBizType("project/ProjectDocumentES");
        uploadConfig.setPublic(false);
        uploadConfig.setReserveOriginal(true);

        FileMetadataDTO metadata = new FileMetadataDTO();
        metadata.setFilename(issueType.getImportFileName());
        metadata.setMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        metadata.setOrgId(orgId.toString());
        metadata.setUploadedAt(new Date());
        metadata.setUploadedBy(operatorId.toString());
        metadata.setConfig(uploadConfig);

        FileUtils.saveMetadata(excel, metadata);

        // 将文件上传到文档服务
        Long importFileId = LongUtils.parseLong(
            uploadFeignAPI
                .save(orgId.toString(), projectId.toString(), temporaryFilename, new FilePostDTO())
                .getData().getId()
        );

        template.setFileId(importFileId);
        template.setLastModifiedAt();
        template.setLastModifiedBy(operatorId);
        template.setStatus(EntityStatus.ACTIVE);

        // 保存导入文件信息
        issueImportTemplateRepository.save(template);

        return template;
    }

    /**
     * 取得所有自定义属性信息。
     *
     * @param sheet     工作表信息
     * @param projectId 项目 ID
     * @return 列号-自定义属性映射表
     */
    private Map<Integer, IssuePropertyDefinition> getPropertyDefinitions(
        final Sheet sheet,
        final Long projectId,
        final Long orgId,
        final Long operatorId
    ) {

        Set<String> issueFieldNames = Arrays.stream(Issue.class.getDeclaredFields()).
            map(fld -> fld.getName()).collect(Collectors.toSet());
        Map<Integer, IssuePropertyDefinition> result = new HashMap<>();

        int rowNo = COLUMN_HEADER_ROW_NO;
        Row row;
//        sheet.getLastRowNum();

        do {
            if(rowNo > sheet.getLastRowNum()) break;
            row = sheet.getRow(rowNo);
            String columnName = WorkbookUtils.readAsString(row, 0);
            String isTableColumnStr = WorkbookUtils.readAsString(row, 2);
            Integer columnNo = WorkbookUtils.readAsInteger(row, 1);
            String shortDesc = WorkbookUtils.readAsString(row, 3);
            if(StringUtils.isEmpty(columnName)) break;


            IssuePropertyDefinition issuePropertyDefinition = propertyDefinitionRepository.
                    findByProjectIdAndIssueTypeAndNameAndDeletedIsFalse(
                    projectId, IssueType.ISSUE, columnName
                );

            if(issuePropertyDefinition == null) {
                issuePropertyDefinition = new IssuePropertyDefinition();
                issuePropertyDefinition.setIssueType(IssueType.ISSUE);
                issuePropertyDefinition.setName(columnName);
                issuePropertyDefinition.setOrgId(orgId);
                issuePropertyDefinition.setProjectId(projectId);
                issuePropertyDefinition.setPropertyCategory(IssuePropertyCategory.EXTERNAL);
                issuePropertyDefinition.setPropertyType(CustomPropertyType.TEXT);
                issuePropertyDefinition.setIssueField("Y".equalsIgnoreCase(isTableColumnStr) ? true : false);
                issuePropertyDefinition.setShortDesc(shortDesc);
                issuePropertyDefinition.setCreatedBy(operatorId);
                issuePropertyDefinition.setCreatedAt();
                issuePropertyDefinition.setStatus(EntityStatus.ACTIVE);
                issuePropertyDefinition.setDeleted(false);
                issuePropertyDefinition.setVersion(new Date().getTime());
                issuePropertyDefinition.setLastModifiedAt();
                issuePropertyDefinition.setLastModifiedBy(operatorId);
                propertyDefinitionRepository.save(issuePropertyDefinition);

            }
            result.put(columnNo, issuePropertyDefinition);

            rowNo += 1;

        } while(true);

        return result;
    }

    /**
     * 根据登录用户名取得用户信息，并生成登录用户名与用户 ID 的映射表。
     *
     * @param sheet 工作表
     * @return 登录用户名与用户 ID 的映射表
     */
    private Map<String, Long> getUsersByUsernames(final Sheet sheet) {

        Iterator<Row> rows = sheet.rowIterator();
        Row row;
        String leaderUsername;
        String memberUsernames;
        Set<String> usernames = new HashSet<>();

        while (rows.hasNext()) {

            row = rows.next();

            if (row.getRowNum() <= HEADER_ROW_NO) {
                continue;
            }

            leaderUsername = WorkbookUtils.readAsString(row, LEADER_USERNAME_COLUMN_NO);

            if (leaderUsername != null) {
                Arrays.asList(leaderUsername.split(",")).forEach(username ->
                    usernames.add(StringUtils.trim(username)));
            }

            memberUsernames = WorkbookUtils.readAsString(row, MEMBER_USERNAMES_COLUMN_NO);

            if (memberUsernames != null) {
                Arrays.asList(memberUsernames.split(",")).forEach(username ->
                    usernames.add(StringUtils.trim(username)));
            }

        }

        Map<String, Long> result = new HashMap<>();

        usernames.remove("");

        if (usernames.size() == 0) {
            return result;
        }

        userFeignAPI
            .getUserByUsername(new UserNameCriteriaDTO(String.join(",", usernames)))
            .getData()
            .forEach(user -> result.put(user.getUsername(), user.getId()));

        return result;
    }

    /**
     * 批量导入遗留问题。
     *
     * @param operatorId     操作人 ID
     * @param orgId          组织 ID
     * @param projectId      项目 ID
     * @param issueImportDTO 遗留问题导入信息
     */
    @Override
//    @Transactional
    public void importIssues(
        final Long operatorId,
        final Long orgId,
        final Long projectId,
        final IssueImportDTO issueImportDTO
    ) {

        File excel;
        Workbook workbook;
        Sheet sheet;
        Sheet columnSheet;
        Sheet styles;
        int skipCnt = 0;
        int totalCnt = 0;
        int failedCount = 0;
//        if(skipCnt == 0) { translate(projectId); return;}


        try {
            excel = new File(temporaryDir, issueImportDTO.getFileName());
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessError(e.getMessage());
        }

        if (workbook == null
            || (sheet = workbook.getSheet(ISSUES_SHEET_NAME)) == null
            || (columnSheet = workbook.getSheet(COLUMNS_SHEET_NAME)) == null
            || (styles = workbook.getSheet(STYLES_SHEET_NAME)) == null) {
            return;
        }
        System.out.println("FILE IS OK");

        // 取得所有自定义属性信息
        Map<Integer, IssuePropertyDefinition> propertyDefinitions
            = getPropertyDefinitions(columnSheet, projectId, orgId, operatorId);
        Integer noCol = null;
        int maxColumnNo = 0;
        for(Map.Entry<Integer, IssuePropertyDefinition> map :propertyDefinitions.entrySet()){
            if("no".equalsIgnoreCase(map.getValue().getName())) {
                noCol = map.getKey();
                break;
            }
        }
        maxColumnNo = Collections.max(propertyDefinitions.keySet());

        // 取得用户信息
//        Map<String, Long> users = getUsersByUsernames(sheet);

        final String batchNo = BATCH_NO_FORMAT.format(new Date());
        // 保存导入记录
        IssueImportRecord issueImportRecord = new IssueImportRecord();
        issueImportRecord.setBatchNo(batchNo);
        issueImportRecord.setOrgId(orgId);
        issueImportRecord.setProjectId(projectId);
        issueImportRecord.setTotalCount(sheet.getPhysicalNumberOfRows() - HEADER_ROW_NO );
        issueImportRecord.setErrorCount(failedCount);
        issueImportRecord.setProcessedCount(totalCnt);
        issueImportRecord.setCreatedBy(operatorId);
        issueImportRecord.setCreatedAt();
        issueImportRecord.setLastModifiedBy(operatorId);
        issueImportRecord.setLastModifiedAt();
        issueImportRecord.setSkipCnt(skipCnt);
        issueImportRecord.setStatus(EntityStatus.ACTIVE);

        Row row;
        List<Issue> issues = new ArrayList<>();
//        IssuePropertyDefinition propertyDefinition;
        List<IssueProperty> issueProperties = new ArrayList<>();
        CellStyle errorStyle = styles.getRow(0).getCell(0).getCellStyle();
        int errorColNo = maxColumnNo + 1;

        // 逐行读取工作表中的问题信息
        for (
            int rowNo = HEADER_ROW_NO + 1;
            rowNo < sheet.getPhysicalNumberOfRows();
            rowNo++
            ) {
            totalCnt ++;
            row = sheet.getRow(rowNo);
            String noStr = WorkbookUtils.readAsString(row, noCol);
            List<String> errors = new ArrayList<>();
            Map<String, Long> userMap = new HashMap<>();
            Map<String, Long> departmentMap = new HashMap<>();
            int propertyCount = 0;

            Issue issue = issueRepository.findByProjectIdAndNoAndDeletedIsFalse(projectId, noStr);
            if(issue != null) {
                skipCnt ++;
                continue;
            }
            issue = new Issue();
            String columnName = null;
            try {
                Map<String, Object> issueMap = new HashMap<>();
                for (Map.Entry<Integer, IssuePropertyDefinition> map : propertyDefinitions.entrySet()) {
                    columnName = map.getValue().getName();
                    Object cellVal = null;

                    if (columnName.equalsIgnoreCase("originDate") ||
                        columnName.equalsIgnoreCase("planFinishDate")) {
                        if( WorkbookUtils.readAsString(row, map.getKey()).equals("TBC")){
                            Calendar myCalendar = new GregorianCalendar(2000, 1, 1);
                            cellVal = myCalendar.getTime();
                        }else {
                            cellVal = WorkbookUtils.readAsDate(row, map.getKey());
                        }
                    }

                    else  if(
                        !columnName.equalsIgnoreCase("originDate") &&
                        !columnName.equalsIgnoreCase("planFinishDate") &&
                        !columnName.equalsIgnoreCase("disputed") &&
                        WorkbookUtils.readAsString(row, map.getKey()).equals("TBC")
                    ){
                        issueMap.put(columnName, null);
                        continue;
                    }
                    else if(columnName.equalsIgnoreCase("disputed")){
                        cellVal = String.valueOf(WorkbookUtils.readAsBoolean(row, map.getKey()));
                    }
                    else{
                        cellVal = WorkbookUtils.readAsString(row, map.getKey());
                    }
                    if (map.getValue().getIssueField()) { //为 issue类字段
                        switch (columnName) {
                            case "punchSource":
                                String cVal = (String) cellVal;
                                if(StringUtils.isEmpty(cVal)) break;
                                cVal = cVal.replaceAll("-","_");
                                issueMap.put(columnName, IssueSource.valueOf(cVal));

                                break;
                            case "punchCategory":
                                issueMap.put(columnName, IssueCategory.valueOf((String) cellVal));

                                break;
                            case "type":
                                issueMap.put(columnName, IssueType.valueOf((String) cellVal));
                                break;

                            case "personInCharge":
                            case "qc":
                                String uName = (String) cellVal;
                                Long pId = userMap.get(uName);
                                if (pId == null) {
                                    List<UserProfile> users = userFeignAPI.getUserByName(orgId, uName).getData();
                                    if (!CollectionUtils.isEmpty(users)) {
                                        pId = users.get(0).getId();
                                        userMap.put(uName, pId);
                                    } else {
                                        String errorMessage = WorkbookUtils.readAsString(row, errorColNo);
                                        if (errorMessage == null) errorMessage = "";
                                        errorMessage += errorMessage + " " + columnName + " CAN't find userId";
                                        Cell cell = row.getCell(errorColNo);
                                        if (cell == null) {
                                            cell = row.createCell(errorColNo);
                                        }
                                        cell.setCellValue(errorMessage);
                                    }
                                }
                                if (pId != null) {
                                    issueMap.put(columnName, cellVal);
                                    issueMap.put(columnName + "Id", pId);
                                }
                                break;
                            case "department":
                                String dName = (String) cellVal;
                                Long dId = departmentMap.get(dName);
                                if (dId == null) {
                                    List<OrganizationBasicDTO> departments = orgFeignAPI.getDepartments(orgId, dName).getData();
                                    if (!CollectionUtils.isEmpty(departments)) {
                                        dId = departments.get(0).getId();
                                        departmentMap.put(dName, dId);
                                    } else {
                                        String errorMessage = WorkbookUtils.readAsString(row, errorColNo);
                                        if (errorMessage == null) errorMessage = "";
                                        errorMessage += errorMessage + " " + columnName + " CAN't find department Id";
                                        Cell cell = row.getCell(errorColNo);
                                        if (cell == null) {
                                            cell = row.createCell(errorColNo);
                                        }
                                        cell.setCellValue(errorMessage);
                                    }
                                }
                                if (dId != null) {
                                    issueMap.put(columnName, cellVal);
                                    issueMap.put(columnName + "Id", dId);
                                }
                                break;
                            default:
                                issueMap.put(columnName, cellVal);

                        }

                    }
                    else {
                        Long propertyId = map.getValue().getId();
                        Long issueId = issue.getId();
                        IssueProperty issueProperty = issuePropertyRepository.
                            findByProjectIdAndIssueIdAndPropertyIdAndStatus(projectId, issueId, propertyId, EntityStatus.ACTIVE);
                        if (issueProperty == null) {
                            issueProperty = new IssueProperty();
                            issueProperty.setPropertyId(propertyId);
                            issueProperty.setIssueId(issueId);
                            issueProperty.setStatus(EntityStatus.ACTIVE);
                        }
                        issueProperty.setValues(String.valueOf(cellVal));
                        issueProperty.setProjectId(projectId);
                        issueProperty.setPropertyName(map.getValue().getName());
                        issueProperty.setPropertyType(map.getValue().getPropertyType());
                        issueProperties.add(issueProperty);
                    }
                }
                issueMap.put("projectId", projectId);
                issueMap.put("orgId", orgId);
                issueMap.put("lastModifiedAt", new Date());
                issueMap.put("createdAt", new Date());
                issueMap.put("createdBy", operatorId);
                issueMap.put("lastModifiedBy", operatorId);
                issueMap.put("status", EntityStatus.ACTIVE);
                issueMap.putIfAbsent("type", IssueType.ISSUE);
                issueMap.put("deleted", false);

                BeanUtils.copyProperties(issueMap, issue);

//            issues.add(issue);


                issueRepository.save(issue);

                if (issueProperties.size() > 0) {
                    issuePropertyRepository.saveAll(issueProperties);
                    issueProperties = new ArrayList<>();
                }

                if (totalCnt % 20 == 0) {
                    issueImportRecord.setErrorCount(failedCount);
                    issueImportRecord.setProcessedCount(totalCnt);
                    issueImportRecord.setSkipCnt(skipCnt);

                    issueImportRecordRepository.save(issueImportRecord);
                }
            } catch (Exception e) {
                String err = e.toString() + " " + noStr + " " + columnName;
                System.out.println(err);
                WorkbookUtils
                    .setCellValue(sheet, rowNo, errorColNo, err)
                    .setCellStyle(errorStyle);

                failedCount++;
            }
        }



        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
        } catch (IOException e) {
            // do nothing
        }

        // 保存导入文件
        FileMetadataDTO.FileUploadConfig uploadConfig = new FileMetadataDTO.FileUploadConfig();
        uploadConfig.setBizType("issue/import");
        uploadConfig.setPublic(false);
        uploadConfig.setReserveOriginal(true);

        FileMetadataDTO metadata = new FileMetadataDTO();
        metadata.setFilename("import-issues-" + projectId + ".xlsx");
        metadata.setMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        metadata.setOrgId(orgId.toString());
        metadata.setUploadedAt(new Date());
        metadata.setUploadedBy(operatorId.toString());
        metadata.setConfig(uploadConfig);

        FileUtils.saveMetadata(excel, metadata);

        JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(
            orgId.toString(),
            projectId.toString(),
            issueImportDTO.getFileName(),
            new FilePostDTO()
        );

        issueImportRecord.setFileId(LongUtils.parseLong(
            responseBody.getData().getId()
        ));
        issueImportRecord.setErrorCount(failedCount);
        issueImportRecord.setProcessedCount(totalCnt);
        issueImportRecord.setSkipCnt(skipCnt);

        issueImportRecordRepository.save(issueImportRecord);

//        translate(projectId);
    }

    private void translate(Long projectId) {
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);

        List<Issue> issues = issueRepository.findByProjectIdAndTypeAndDescriptionCnIsNullAndDeletedIsFalse(projectId, IssueType.ISSUE);

        for(Issue issue : issues) {
            String desc = issue.getDescription();
            if(StringUtils.isEmpty(desc)) continue;
//               String query = "Height : 600m";
            String result = api.getTransResult(desc, "en", "zh");
            if(result.contains("error_code")) {
                System.out.print(result);
                continue;
            }
            result = result.replaceAll("trans_result", "transResult");
            NormalTranslateResult ntr = StringUtils.decode(result, new TypeReference<NormalTranslateResult>(){});
            List<TranslateDetail> tds = ntr.getJsonTransResult();
            issue.setDescriptionCn(tds.get(0).getDst());
            issueRepository.save(issue);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(tds.get(0).getDst());

            }

    }


    /**
     * 取得问题导入文件信息。
     *
     * @param projectId 项目 ID
     * @param pageDTO 分页
     * @return 问题导入文件信息
     */
    public Page<IssueImportRecord> getImportHistory(
        Long projectId,
        PageDTO pageDTO
    ){
        return issueImportRecordRepository.findByProjectId(projectId, pageDTO.toPageable());
    }


    /**
     * 设置返回结果的引用数据
     *
     * @param <T>      数据实体泛型
     * @param included 引用数据映射表
     * @param entities 查询结果列表
     * @return 引用数据映射表
     */
    @Override
    public <T extends BaseDTO> Map<Long, Object> setIncluded(Map<Long, Object> included, List<T> entities) {

        if (entities == null || entities.size() == 0) {
            return included;
        }
        Set<Long> pIds = new HashSet<>();
        for (T entity : entities) {
            if (entity instanceof IssueImportRecord) {
                IssueImportRecord iir = (IssueImportRecord) entity;
                pIds.add(iir.getCreatedBy());
            }
        }

        if (pIds.size() > 0) {
            BatchGetDTO batchGetDTO = new BatchGetDTO();
            batchGetDTO.setEntityIDs(pIds);
            Iterable<UserBasic> userBasics = userFeignAPI.batchGet(batchGetDTO).getData();
            for (UserBasic userBasic : userBasics) {
                included.put(userBasic.getId(), userBasic.getName());
            }
        }
        return included;
    }

    /**
     * 设置返回结果的引用数据。
     *
     * @param included 引用数据映射表
     * @param entity   返回结果
     * @param <T>      数据实体泛型
     * @return 引用数据映射表
     */
    @Override
    public <T extends BaseDTO> Map<Long, Object> setIncluded(
        Map<Long, Object> included,
        T entity
    ) {

        if (entity == null) {
            return included;
        }

        Long pId = null;
        if (entity instanceof IssueImportRecord) {
            IssueImportRecord iir = (IssueImportRecord) entity;
            pId = iir.getCreatedBy();
        }

        if (pId != null && !pId.equals(0L)) {
            UserProfile userBasic = userFeignAPI.get(pId).getData();
            if (userBasic != null) {
                included.put(userBasic.getId(), userBasic.getName());
            }
        }

        return included;
    }

    private Map<String, Integer> getColumnHeaderInfo(Sheet sheet, int columnIndex) {

        int rowNum = 2;
        columnIndex = columnIndex - 1;
        Map<String, Integer> headerColumnMap = new HashMap<>();

        while (true) {
            Row row = WorkbookUtils.getRow(sheet, rowNum++);
            if (StringUtils.isEmpty(WorkbookUtils.readAsString(row, 0))) break;
            headerColumnMap.put(WorkbookUtils.readAsString(row, 0), WorkbookUtils.readAsInteger(row, columnIndex) - 1);

        }
        return headerColumnMap;
    }
}
