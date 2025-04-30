package com.ose.tasks.domain.model.service;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.tasks.domain.model.repository.ProjectHierarchyRepository;
import com.ose.tasks.domain.model.repository.ProjectInfoRepository;
import com.ose.tasks.dto.*;
import com.ose.tasks.entity.*;
import com.ose.util.*;
import com.ose.auth.api.OrganizationFeignAPI;
import com.ose.auth.api.RoleFeignAPI;
import com.ose.auth.dto.OrganizationDTO;
import com.ose.auth.entity.Organization;
import com.ose.auth.entity.Role;
import com.ose.auth.vo.OrgType;
import com.ose.docs.api.FileFeignAPI;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FileMetadataDTO;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.dto.TemporaryFileDTO;
import com.ose.docs.entity.FileES;
import com.ose.docs.entity.FileViewES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.service.StringRedisService;
import com.ose.tasks.controller.StageVersionRepository;
import com.ose.tasks.domain.model.repository.BizCodeRepository;
import com.ose.tasks.domain.model.repository.ProjectRepository;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.categoryrule.EntitySubTypeRuleRepository;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.vo.EntityStatus;
import com.ose.vo.RedisKey;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

/**
 * 项目管理服务。
 */
@Component
public class ProjectService extends StringRedisService implements ProjectInterface {

    private final static Logger logger = LoggerFactory.getLogger(ProjectService.class);


    @Value("${application.files.temporary}")
    private String temporaryDir;

    @Value("${application.files.protected}")
    private String protectedDir;

    @Value("${application.files.templateFilePath}")
    private String templateFilePath;

    private ProjectRepository projectRepository;

    private final StageVersionRepository stageVersionRepository;
    private UploadFeignAPI uploadFeignAPI;


    private FileFeignAPI fileFeignAPI;

    private final OrganizationFeignAPI organizationFeignAPI;

    private final BpmProcessCategoryRepository bpmProcessCategoryRepository;

    private final BpmProcessRepository bpmProcessRepository;

    private final BpmProcessStageRepository bpmProcessStageRepository;

    private final BpmEntitySubTypeRepository bpmEntitySubTypeRepository;

    private final BpmEntityTypeRepository bpmEntityTypeRepository;

    private final EntitySubTypeRuleRepository entityCategoryRuleRepository;

    private final RoleFeignAPI roleFeignAPI;

    private final BizCodeRepository bizCodeRepository;

    private final BpmActTaskConfigRepository bpmActTaskConfigRepository;

    private final EntitySubTypeProcessRelationRepository relationRepository;

    private final ProjectHierarchyRepository projectHierarchyRepository;

    private final ProjectInfoRepository projectInfoRepository;


    /**
     * 构造方法。
     *
     * @param projectRepository            项目数据仓库实例
     * @param stageVersionRepository
     * @param organizationFeignAPI         组织API
     * @param bpmProcessCategoryRepository 工序分类 API
     * @param bpmProcessRepository         工序API
     * @param bpmProcessStageRepository    工序阶段 API
     * @param bpmEntitySubTypeRepository   实体类型 API
     * @param bpmEntityTypeRepository      实体类型分类 API
     * @param entityCategoryRuleRepository 实体类型规则 API
     * @param roleFeignAPI                 角色 API
     * @param bizCodeRepository            业务代码 API
     * @param bpmActTaskConfigRepository   业务代理 API
     * @param relationRepository           工序 实体类型 关系 API
     */
    @Autowired
    public ProjectService(ProjectRepository projectRepository,
                          StringRedisTemplate stringRedisTemplate,
                          StageVersionRepository stageVersionRepository, @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI,
                          @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") FileFeignAPI fileFeignAPI,
                          @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") OrganizationFeignAPI organizationFeignAPI,
                          BpmProcessCategoryRepository bpmProcessCategoryRepository,
                          BpmProcessRepository bpmProcessRepository,
                          BpmProcessStageRepository bpmProcessStageRepository,
                          BpmEntitySubTypeRepository bpmEntitySubTypeRepository,
                          BpmEntityTypeRepository bpmEntityTypeRepository,
                          EntitySubTypeRuleRepository entityCategoryRuleRepository,
                          @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") RoleFeignAPI roleFeignAPI,
                          BizCodeRepository bizCodeRepository,
                          BpmActTaskConfigRepository bpmActTaskConfigRepository,
                          EntitySubTypeProcessRelationRepository relationRepository,
                          ProjectHierarchyRepository projectHierarchyRepository,
                          ProjectInfoRepository projectInfoRepository) {
        super(stringRedisTemplate);
        this.projectRepository = projectRepository;
        this.stageVersionRepository = stageVersionRepository;
        this.uploadFeignAPI = uploadFeignAPI;
        this.fileFeignAPI = fileFeignAPI;
        this.organizationFeignAPI = organizationFeignAPI;
        this.bpmProcessCategoryRepository = bpmProcessCategoryRepository;
        this.bpmProcessRepository = bpmProcessRepository;
        this.bpmProcessStageRepository = bpmProcessStageRepository;
        this.bpmEntitySubTypeRepository = bpmEntitySubTypeRepository;
        this.bpmEntityTypeRepository = bpmEntityTypeRepository;
        this.entityCategoryRuleRepository = entityCategoryRuleRepository;
        this.roleFeignAPI = roleFeignAPI;
        this.bizCodeRepository = bizCodeRepository;
        this.bpmActTaskConfigRepository = bpmActTaskConfigRepository;
        this.relationRepository = relationRepository;
        this.projectHierarchyRepository = projectHierarchyRepository;
        this.projectInfoRepository = projectInfoRepository;
    }

    /**
     * 取得项目信息。
     *
     * @param orgId     所属组织 ID
     * @param projectId 项目 ID
     * @return 项目信息
     */
    @Override
    public Project get(Long orgId, Long projectId) {

        Project project = projectRepository.findByIdAndOrgIdAndDeletedIsFalse(projectId, orgId).orElse(null);

        if (project == null) {
            throw new NotFoundError();
        }

        return project;
    }

    @Override
    public Project get(Long projectId) {
        String projectKey = String.format(RedisKey.PROJECT.getDisplayName(), projectId.toString());
        String projectStr = getRedisKey(projectKey);

        if(StringUtils.isEmpty(projectStr)) {
            Project project = null;
            project = projectRepository.findById(projectId).orElse(null);
            if(project != null){
                projectStr = StringUtils.toJSON(project);
                setRedisKey(projectKey, projectStr);
            }
            return project;

        } else {
            return StringUtils.decode(projectStr, new TypeReference<Project>() {});
        }
    }

    @Override
    public boolean set(Project project) {
        if(project == null) {
            return false;
        }
        String projectKey = String.format(RedisKey.PROJECT.getDisplayName(), project.getId().toString());
        String projectStr = "";

        projectStr = StringUtils.toJSON(project);
        setRedisKey(projectKey, projectStr);
        return true;

    }

    /**
     * 取得项目信息。
     *
     * @param orgId     所属组织 ID
     * @param projectId 项目 ID
     * @param version   项目更新版本号
     * @return 项目信息
     */
    @Override
    public Project get(Long orgId, Long projectId, long version) {

        Project project = get(orgId, projectId);


        return project;
    }

    /**
     * 取得指定组织的所有项目。
     *
     * @param orgId   组织 ID
     * @param pageDTO 分页参数
     * @return 项目分页数据
     */
    @Override
    public Page<Project> search(Long orgId, PageDTO pageDTO) {
        return projectRepository.findByOrgIdAndDeletedIsFalse(orgId, pageDTO.toPageable());
    }

    /**
     * 取得所有尚未结束的项目的 ID。
     *
     * @return 项目 ID 列表
     */
    @Override
    public List<Project> getNotFinishedProjects() {
        return projectRepository.findByFinishedAtIsNullAndDeletedIsFalse();
    }

    /**
     * 生成并设置层级结构导入文件。
     *
     * @param operator   操作者信息
     * @param projectDTO 项目更新信息数据传输对象
     * @param project    项目信息
     */
    private void setHierarchyImportFileTemplate(final OperatorDTO operator, final ProjectModifyDTO projectDTO, Project project) {

        String[] templateInfo;


        if ((templateInfo = setHierarchyImportFileTemplate(operator, project, IMPORT_FILE_TYPE_AREA, new ArrayList<>(AREA_NODE_TYPES), new ArrayList<>(AREA_OPTIONAL_NODE_TYPES), projectDTO.getAreaOptionalNodeTypes(), project.getAreaOptionalNodeTypes())) != null) {
            project.setAreaOptionalNodeTypes(templateInfo[0]);
            project.setAreaImportFileId(LongUtils.parseLong(templateInfo[1]));
            project.setAreaImportFileVersion(templateInfo[2]);
        }


        if ((templateInfo = setHierarchyImportFileTemplate(operator, project, IMPORT_FILE_TYPE_SYSTEM, new ArrayList<>(SYSTEM_NODE_TYPES), new ArrayList<>(SYSTEM_OPTIONAL_NODE_TYPES), projectDTO.getSystemOptionalNodeTypes(), project.getSystemOptionalNodeTypes())) != null) {
            project.setSystemOptionalNodeTypes(templateInfo[0]);
            project.setSystemImportFileId(LongUtils.parseLong(templateInfo[1]));
            project.setSystemImportFileVersion(templateInfo[2]);
        }

        //SET ENGINEERING HIERARCHY TEMPLATE
        if ((templateInfo = setHierarchyImportFileTemplate(operator, project,
                IMPORT_FILE_TYPE_ENGINEERING, new ArrayList<>(ENGINEERING_NODE_TYPES),
                new ArrayList<>(ENGINEERING_OPTIONAL_NODE_TYPES), projectDTO.getEngineeringOptionalNodeTypes(),
                project.getEngineeringOptionalNodeTypes())) != null) {
            project.setEngineeringOptionalNodeTypes(templateInfo[0]);
            project.setEngineeringImportFileId(LongUtils.parseLong(templateInfo[1]));
            project.setEngineeringImportFileVersion(templateInfo[2]);
        }

    }

    /**
     * 生成层级导入模版。
     *
     * @param operator            操作者信息
     * @param project             项目信息
     * @param templateTitle       模板标题
     * @param nodeTypeList        所有节点类型列表
     * @param optionalNodeTypeList 可选节点类型列表
     * @param optionalNodeTypes   本次设置的可选节点类型
     * @param lastOptionalTypes   已设置的可选节点类型
     * @return 可选节点类型及文件 ID
     */
    private String[] setHierarchyImportFileTemplate(final OperatorDTO operator, final Project project,
                                                    final String templateTitle, final List<String> nodeTypeList,
                                                    final List<String> optionalNodeTypeList, final List<String> optionalNodeTypes,
                                                    final String lastOptionalTypes) {
//        final String nodeTypes) {
//
//
//        if (optionalNodeTypes == null) {
//            return null;
//        }
        Set<String> optionalNodeTypeSet = new HashSet<>();
        if(!CollectionUtils.isEmpty(optionalNodeTypes)) {
            optionalNodeTypes.forEach(ont -> optionalNodeTypeSet.add(ont));
        }
        Set<String> lastOptionalNodeTypeSet = new HashSet<>();
        if(!StringUtils.isEmpty(lastOptionalTypes)) {
            Arrays.asList(lastOptionalTypes.split(",")).forEach(nodeType -> lastOptionalNodeTypeSet.add(nodeType));
        }

        if(optionalNodeTypeSet.equals(lastOptionalNodeTypeSet)) {
            return null;
        }

        Set<String> removedNodeTypeSet = new HashSet<>();

        optionalNodeTypeList.forEach(nodeType -> {
//            HierarchyNodeType tmpNodeType = HierarchyNodeType.valueOf(nodeType);
            if(CollectionUtils.isEmpty(optionalNodeTypes) || !optionalNodeTypes.contains(nodeType))
                removedNodeTypeSet.add(nodeType);
        });

        nodeTypeList.removeAll(removedNodeTypeSet);

//        List<String> selectedNodeType = new ArrayList<>();
//        selectedNodeTypeList.forEach(nodeType -> selectedNodeType.add(nodeType.name()));


//        optionalNodeTypList.removeAll(selectedNodeTypeList);
//        nodeTypeList.removeAll(optionalNodeTypList);


//        String temporaryFilename = FileUtils.copy(
//            this.getClass().getClassLoader().getResourceAsStream("templates/import-project-hierarchy-flatten.xlsx"),
//            temporaryDir,
//            operator.getId() + "\r\n" + templateTitle
//        );

        String temporaryFilename = System.currentTimeMillis() + ".xlsx";

        File sourceFile = new File(templateFilePath + "import-project-hierarchy-flatten.xlsx");
        if (templateTitle.equals(IMPORT_FILE_TYPE_ENGINEERING)) {
            sourceFile = new File(templateFilePath + "import-project-drawing-hierarchy-flatten.xlsx");
        }
        File excel = null;
        Workbook workbook;


        try {
            excel = new File(temporaryDir, temporaryFilename);
            Path sourcePath = sourceFile.toPath();
            Path excelPath = excel.toPath();
            Files.copy(sourcePath, excelPath, StandardCopyOption.REPLACE_EXISTING);
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            FileUtils.remove(excel);
            throw new BusinessError();
        }

        Sheet sheet = workbook.getSheet("Hierarchy");

        sheet.getRow(0).getCell(0).setCellValue(String.format("%s %s Hierarchy", project.getName(), templateTitle));

        Row row = sheet.getRow(1);
        short colIndex = 0;


        for (String nodeType : nodeTypeList) {
            row.getCell(colIndex).setCellValue(nodeType);
            colIndex += 2;
        }

        int totalColumnCount = 0;
        if(IMPORT_FILE_TYPE_AREA.equals(templateTitle)) {
            totalColumnCount = 8;
        } else if(IMPORT_FILE_TYPE_SYSTEM.equals(templateTitle)) {
            totalColumnCount = 26;
        } else if(IMPORT_FILE_TYPE_ENGINEERING.equals(templateTitle)) {
            totalColumnCount = 11;
        }


        if (colIndex < totalColumnCount) {
            WorkbookUtils.removeColumns(sheet, colIndex, (short) totalColumnCount);
        }


        String importFileVersion = DateUtils.toISOString(new Date());
        Sheet settings = workbook.getSheet("Settings");
        settings.getRow(1).getCell(1).setCellValue(project.getId().toString());
        settings.getRow(2).getCell(1).setCellValue(templateTitle);
        settings.getRow(3).getCell(1).setCellValue(importFileVersion);


        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
        } catch (IOException ioe) {
            FileUtils.remove(excel);
            throw new BusinessError();
        }

        FileMetadataDTO.FileUploadConfig uploadConfig = new FileMetadataDTO.FileUploadConfig();
        uploadConfig.setBizType("project/ProjectDocumentES");
        uploadConfig.setPublic(false);
        uploadConfig.setReserveOriginal(true);

        FileMetadataDTO metadata = new FileMetadataDTO();
        metadata.setFilename(String.format("import-%s-%s-hierarchy-flatten.xlsx", project.getId().toString(), templateTitle.toLowerCase().replaceAll("\\s+", "-")));
        metadata.setMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        metadata.setOrgId(project.getOrgId().toString());
        metadata.setUploadedAt(new Date());
        metadata.setUploadedBy(operator.getId().toString());
        metadata.setConfig(uploadConfig);

        FileUtils.saveMetadata(excel, metadata);
        logger.info("项目4 保存docs服务->开始");
        JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(project.getOrgId().toString(), project.getId().toString(), temporaryFilename, new FilePostDTO());
        logger.info("项目4 保存docs服务->结束");
        FileUtils.remove(excel);

        return new String[]{String.join(",", optionalNodeTypes), responseBody.getData().getId(), importFileVersion};
    }

    /**
     * 创建项目。
     *
     * @param operator   操作者信息
     * @param companyId  公司 ID
     * @param orgId      所属组织 ID
     * @param projectDTO 项目信息
     * @return 项目数据实体
     */
    @Override
    public Project create(final OperatorDTO operator, final Long companyId, final Long orgId, final ProjectModifyDTO projectDTO) {

        Project project = new Project();

        BeanUtils.copyProperties(projectDTO, project);

        project.setCompanyId(companyId);
        project.setOrgId(orgId);

        if (projectDTO.getNdtPenaltyRules() != null && projectDTO.getNdtPenaltyRules().size() > 0) {
            project.setJsonNdtPenaltyRules(projectDTO.getNdtPenaltyRules());
        }

        if (projectDTO.getNdtInspectionRules() != null && projectDTO.getNdtInspectionRules().size() > 0) {
            project.setJsonNdtInspectionRules(projectDTO.getNdtInspectionRules());
        }

        if (projectDTO.getNdtRepaireRules() != null) {
            project.setNdtRepaireRules(projectDTO.getNdtRepaireRules());
        }
        if (projectDTO.getNdtPenaltyNumber() != null) {
            project.setNdtPenaltyNumber(projectDTO.getNdtPenaltyNumber());
        }
        if (projectDTO.getNdtPenaltyWeldNo() != null) {
            project.setNdtPenaltyWeldNo(projectDTO.getNdtPenaltyWeldNo());
        }

        if (projectDTO.getNdtWeldCountRules() != null && projectDTO.getNdtWeldCountRules().size() > 0) {
            project.setJsonNdtWeldCountRules(projectDTO.getNdtWeldCountRules());
        }

        project.setCreatedBy(operator.getId());
        project.setLastModifiedBy(operator.getId());
        project.setStatus(EntityStatus.ACTIVE);

        if (!StringUtils.isEmpty(projectDTO.getClientTempName())) {
            project.setClientLogoFileId(saveTempImageFile(orgId, projectDTO.getClientTempName()));
        }
        if (!StringUtils.isEmpty(projectDTO.getContractorTempName())) {
            project.setContractorLogoFileId(saveTempImageFile(orgId, projectDTO.getContractorTempName()));
        }

        setHierarchyImportFileTemplate(operator, projectDTO, project);

        setProtectedFolderProjectLogo(project);
        return projectRepository.save(project);
    }

    private Long saveTempImageFile(Long orgId, String tempName) {

        File diskFile = new File(temporaryDir, tempName);

        FileMetadataDTO metadata = FileUtils.readMetadata(diskFile, FileMetadataDTO.class);
        if (!FileUtils.isImage(metadata.getMimeType())) {
            throw new BusinessError("请上传图片文件");
        }
        logger.error("项目3 保存docs服务->开始");
        JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(orgId.toString(), tempName, new FilePostDTO());
        logger.error("项目3 保存docs服务->结束");
        FileES fileES = responseBody.getData();
        if (!fileES.getOrgId().equals(orgId.toString())) {

            try {
                diskFile = new File(protectedDir + fileES.getPath().substring(1));
                System.out.println(diskFile.getName());
                DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file", MediaType.APPLICATION_PDF_VALUE, true, diskFile.getName());
                IOUtils.copy(new FileInputStream(diskFile), fileItem.getOutputStream());

                logger.error("项目1 上传docs服务->开始");
                MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                    APPLICATION_PDF_VALUE, fileItem.getInputStream());
                JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI.uploadProjectDocumentFile(orgId.toString(), fileItem1);
                logger.error("项目1 上传docs服务->结束");
                logger.error("项目1 保存docs服务->开始");
                JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), tempFileResBody.getData().getName(), new FilePostDTO());
                logger.error("项目1 保存docs服务->结束");
                fileES = fileESResBody.getData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return LongUtils.parseLong(fileES.getId());
    }

    private void setProtectedFolderProjectLogo(Project project) {
        String targetDir = protectedDir + project.getId() + "/" + project.getVersion().toString() + "/images/logos";
        File dir = new File(targetDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (!LongUtils.isEmpty(project.getClientLogoFileId())) {
            File existFile = new File(targetDir, "logo.png");
            if (existFile.exists()) {
                FileUtils.remove(existFile);
            }
            try {
                logger.error("项目接口1 调用docs取得详情服务->开始");
                FileViewES fileViewESClient = fileFeignAPI.getFileInfo(project.getOrgId().toString(), LongUtils.toString(project.getClientLogoFileId())).getData();
                logger.error("项目接口1 调用docs取得详情服务->结束");
                File sourceFile = new File(protectedDir + fileViewESClient.getPath().substring(1));
                InputStream inputStream = new FileInputStream(sourceFile);
                String temporaryFileName = FileUtils.copy(inputStream, targetDir, project.getLastModifiedBy().toString());
                File file = new File(targetDir, temporaryFileName);
                file.renameTo(new File(targetDir, "logo.png"));
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }

        if (!LongUtils.isEmpty(project.getContractorLogoFileId())) {
            File existFile = new File(targetDir, "ose.png");
            if (existFile.exists()) {
                FileUtils.remove(existFile);
            }
            try {
                logger.error("项目接口2 调用docs取得详情服务->开始");
                FileViewES fileViewESContractor = fileFeignAPI.getFileInfo(project.getOrgId().toString(), LongUtils.toString(project.getContractorLogoFileId())).getData();
                logger.error("项目接口2 调用docs取得详情服务->结束");
                File sourceFile = new File(protectedDir + fileViewESContractor.getPath().substring(1));
                InputStream inputStream = new FileInputStream(sourceFile);
                String temporaryFileName = FileUtils.copy(inputStream, targetDir, project.getLastModifiedBy().toString());
                File file = new File(targetDir, temporaryFileName);
                file.renameTo(new File(targetDir, "ose.png"));
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }

    /**
     * 更新项目。
     *
     * @param operator   操作者信息
     * @param orgId      所属组织 ID
     * @param projectId  项目 ID
     * @param projectDTO 项目信息
     * @param version    项目版本号
     * @return 项目数据实体
     */
    @Override
    public Project update(final OperatorDTO operator, final Long orgId, final Long projectId, final ProjectModifyDTO projectDTO, final long version) {

        Project project = get(orgId, projectId, version);

        setHierarchyImportFileTemplate(operator, projectDTO, project);

        if (!StringUtils.isEmpty(projectDTO.getName())) {
            project.setName(projectDTO.getName());
        }

        if (!StringUtils.isEmpty(projectDTO.getRemarks())) {
            project.setRemarks(projectDTO.getRemarks());
        }

        if (!StringUtils.isEmpty(projectDTO.getSpmId())) {
            project.setSpmId(projectDTO.getSpmId());
        }

        if (!StringUtils.isEmpty(projectDTO.getBomLnCodeGenerateMethod())) {
            project.setBomLnCodeGenerateMethod(projectDTO.getBomLnCodeGenerateMethod());
        }

        if (projectDTO.getWbsMode() != null) {
            project.setWbsMode(projectDTO.getWbsMode());
        }

        if (projectDTO.getDrawingType() != null) {
            project.setDrawingType(projectDTO.getDrawingType());
        }

        if (!StringUtils.isEmpty(projectDTO.getClientTempName())) {
            if (project.getClientLogoFileId() != null && !projectDTO.getClientTempName().equals(project.getClientLogoFileId().toString())) {
                project.setClientLogoFileId(saveTempImageFile(orgId, projectDTO.getClientTempName()));
            }
        } else {
            project.setClientLogoFileId(null);
        }

        if (!StringUtils.isEmpty(projectDTO.getContractorTempName())) {
            if (project.getContractorLogoFileId() != null && !projectDTO.getContractorTempName().equals(project.getContractorLogoFileId().toString())) {
                project.setContractorLogoFileId(saveTempImageFile(orgId, projectDTO.getContractorTempName()));
            }
        } else {
            project.setContractorLogoFileId(null);
        }
        if (projectDTO.getNdtPenaltyRules() != null && projectDTO.getNdtPenaltyRules().size() > 0) {
            project.setJsonNdtPenaltyRules(projectDTO.getNdtPenaltyRules());
        }

        if (projectDTO.getNdtInspectionRules() != null && projectDTO.getNdtInspectionRules().size() > 0) {
            project.setJsonNdtInspectionRules(projectDTO.getNdtInspectionRules());
        }

        if (projectDTO.getNdtRepaireRules() != null) {
            project.setNdtRepaireRules(projectDTO.getNdtRepaireRules());
        }

        if (projectDTO.getNdtPenaltyNumber() != null) {
            project.setNdtPenaltyNumber(projectDTO.getNdtPenaltyNumber());
        }

        if (projectDTO.getNdtPenaltyWeldNo() != null) {
            project.setNdtPenaltyWeldNo(projectDTO.getNdtPenaltyWeldNo());
        }

        if (projectDTO.getNdtWeldCountRules() != null && projectDTO.getNdtWeldCountRules().size() > 0) {
            project.setJsonNdtWeldCountRules(projectDTO.getNdtWeldCountRules());
        }

        project.setLastModifiedBy(operator.getId());
        project.setLastModifiedAt(new Date());

        setProtectedFolderProjectLogo(project);

        set(project);
        return projectRepository.save(project);
    }

    /**
     * 删除项目。
     *
     * @param operatorDTO 操作者信息
     * @param orgId       所属组织 ID
     * @param projectId   项目 ID
     * @param version     项目版本号
     */
    @Override
    public void delete(OperatorDTO operatorDTO, Long orgId, Long projectId, long version) {

        Project project = get(orgId, projectId, version);

        project.setDeletedBy(operatorDTO.getId());
        project.setDeletedAt(new Date());

        projectRepository.save(project);
    }

    /**
     * 关闭项目。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param version   项目更新版本号
     */
    @Override
    public void close(OperatorDTO operator, Long orgId, Long projectId, long version) {
        Project project = get(orgId, projectId, version);
        project.setLastModifiedAt();
        project.setLastModifiedBy(operator.getId());
        project.setFinishedAt(new Date());
        project.setStatus(EntityStatus.CLOSED);
        projectRepository.save(project);
    }

    /**
     * 重新开启项目。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param version   项目更新版本号
     */
    @Override
    public void reopen(OperatorDTO operator, Long orgId, Long projectId, long version) {
        Project project = get(orgId, projectId, version);
        project.setLastModifiedAt();
        project.setLastModifiedBy(operator.getId());
        project.setFinishedAt(null);
        project.setStatus(EntityStatus.ACTIVE);
        projectRepository.save(project);
    }

    /**
     * CLONE PROJECT
     */
    @Override

    public boolean cloneProject(Long oldOrgId, Long oldProjectId, String projectName) {


        OrganizationDTO organizationDTO = new OrganizationDTO();
        Organization oldOrg = organizationFeignAPI.get(oldOrgId).getData();
        if (oldOrg == null) return false;

        organizationDTO.setName(projectName);
        organizationDTO.setAfterId(oldOrgId);
        organizationDTO.setParentId(oldOrg.getParentId());
        organizationDTO.setStatus(EntityStatus.ACTIVE);
        organizationDTO.setType(OrgType.PROJECT);


        Organization org = organizationFeignAPI.create(organizationDTO).getData();
        if (org == null) return false;
        System.out.println("org Created orgId is :" + org.getId());


        Project oldProject = get(oldOrgId, oldProjectId);

        Project project = new Project();
        BeanUtils.copyProperties(oldProject, project, "id", "name");
        project.setOrgId(org.getId());
        project.setName(projectName);

        projectRepository.save(project);

        System.out.println("project Created projectId is :" + project.getId());

        Long orgId = project.getOrgId();
        Long projectId = project.getId();
        try {


            PageDTO pageDTO = new PageDTO();
            pageDTO.setFetchAll(true);
            Map<Long, BpmProcessCategory> bpmProcessCategoryMap = new HashMap<>();
            List<BpmProcessCategory> oldProcessCategories = bpmProcessCategoryRepository.findByStatusAndProjectIdAndOrgId(EntityStatus.ACTIVE, oldProject.getId(), oldProject.getOrgId(), pageDTO.toPageable()).getContent();

            oldProcessCategories.forEach(oldProcessCategory -> {
                BpmProcessCategory bpmProcessCategory = new BpmProcessCategory();
                BeanUtils.copyProperties(oldProcessCategory, bpmProcessCategory, "id");
                bpmProcessCategory.setOrgId(orgId);
                bpmProcessCategory.setProjectId(projectId);
                bpmProcessCategoryRepository.save(bpmProcessCategory);
                bpmProcessCategoryMap.put(oldProcessCategory.getId(), bpmProcessCategory);
            });

            System.out.println("bpmProcessCategory Created ");


            Map<Long, BpmProcessStage> bpmProcessStageMap = new HashMap<>();
            bpmProcessStageRepository.findByOrgIdAndProjectIdAndStatus(oldOrgId, oldProjectId, EntityStatus.ACTIVE).forEach(oldBpmProcessStage -> {
                BpmProcessStage bpmProcessStage = new BpmProcessStage();
                BeanUtils.copyProperties(oldBpmProcessStage, bpmProcessStage, "id");
                bpmProcessStage.setOrgId(orgId);
                bpmProcessStage.setProjectId(projectId);
                bpmProcessStageRepository.save(bpmProcessStage);
                bpmProcessStageMap.put(oldBpmProcessStage.getId(), bpmProcessStage);
            });

            System.out.println("bpmProcessStage Created ");


            Map<Long, Long> bpmEntityTypeMap = new HashMap<>();
            bpmEntityTypeRepository.findByOrgIdAndProjectIdAndStatus(oldOrgId, oldProjectId, EntityStatus.ACTIVE).forEach(oldBpmEntityType -> {
                BpmEntityType bpmEntityType = new BpmEntityType();
                BeanUtils.copyProperties(oldBpmEntityType, bpmEntityType, "id");
                bpmEntityType.setOrgId(orgId);
                bpmEntityType.setProjectId(projectId);
                bpmEntityTypeMap.put(oldBpmEntityType.getId(), bpmEntityType.getId());
                bpmEntityTypeRepository.save(bpmEntityType);
            });

            System.out.println("bpmEntityType Created ");


            Map<Long, BpmEntitySubType> bpmEntitySubTypeMap = new HashMap<>();
            bpmEntitySubTypeRepository.findByProjectIdAndStatus(oldProjectId, EntityStatus.ACTIVE).forEach(oldBpmEntitySubType -> {
                BpmEntitySubType bpmEntitySubType = new BpmEntitySubType();
                BeanUtils.copyProperties(oldBpmEntitySubType, bpmEntitySubType, "id");
                bpmEntitySubType.setOrgId(orgId);
                bpmEntitySubType.setProjectId(projectId);
                bpmEntitySubType.setEntityTypeId(bpmEntityTypeMap.get(bpmEntitySubType.getEntityTypeId()));
                bpmEntitySubType.setEntityBusinessTypeId(bpmEntityTypeMap.get(bpmEntitySubType.getEntityBusinessTypeId()));
                bpmEntitySubTypeRepository.save(bpmEntitySubType);
                bpmEntitySubTypeMap.put(oldBpmEntitySubType.getId(), bpmEntitySubType);
            });

            System.out.println("插入 实体类型分类 entity_category Created ");


            entityCategoryRuleRepository.findByOrgIdAndProjectIdAndStatus(oldOrgId, oldProjectId, EntityStatus.ACTIVE).forEach(oldEntitySubTypeRule -> {
                EntitySubTypeRule entityCategoryRule = new EntitySubTypeRule();
                BeanUtils.copyProperties(oldEntitySubTypeRule, entityCategoryRule, "id");
                entityCategoryRule.setOrgId(orgId);
                entityCategoryRule.setProjectId(projectId);
                BpmEntitySubType newBpmEntitySubType = bpmEntitySubTypeMap.get(entityCategoryRule.getEntitySubTypeId());
                if (newBpmEntitySubType != null) {
                    entityCategoryRule.setEntitySubTypeId(newBpmEntitySubType.getId());
                    entityCategoryRuleRepository.save(entityCategoryRule);
                }
            });
            System.out.println("插入 实体类型分类规则  Created ");


            bpmProcessRepository.findByOrgIdAndProjectIdAndStatus(oldOrgId, oldProjectId, EntityStatus.ACTIVE).forEach(oldBpmProcess -> {
                BpmProcess bpmProcess = new BpmProcess();
                BeanUtils.copyProperties(oldBpmProcess, bpmProcess, "id");
                bpmProcess.setOrgId(orgId);
                bpmProcess.setProjectId(projectId);
                if (bpmProcess.getProcessCategory() != null) {
                    bpmProcess.setProcessCategory(bpmProcessCategoryMap.get(bpmProcess.getProcessCategory().getId()));
                }
                if (bpmProcess.getProcessStage() != null) {
                    bpmProcess.setProcessStage(bpmProcessStageMap.get(bpmProcess.getProcessStage().getId()));
                }
                Set<BpmEntityTypeProcessRelation> oldRelations = bpmProcess.getEntitySubTypeProcessList();

                bpmProcessRepository.save(bpmProcess);

                Set<BpmEntityTypeProcessRelation> relations = new HashSet<>();
                oldRelations.forEach(oldRelation -> {
                    BpmEntityTypeProcessRelation relation = new BpmEntityTypeProcessRelation();
                    BeanUtils.copyProperties(oldRelation, relation, "id");

                    if (oldRelation.getEntitySubType() != null) {
                        relation.setEntitySubType(bpmEntitySubTypeMap.get(oldRelation.getEntitySubType().getId()));
                    }
                    relation.setProcess(bpmProcess);
                    relation.setOrgId(orgId);
                    relation.setProjectId(projectId);
                    relationRepository.save(relation);
                    relations.add(relation);

                });


            });

            System.out.println("插入 工序 process  Created ");


            Map<Long, Long> orgIdMap = new HashMap<>();
            List<Organization> orgList = new ArrayList<>();
            organizationFeignAPI.getOrgList(oldOrgId, "PROJECT").getData().forEach(oOrg -> {
                Organization organization = new Organization();
                BeanUtils.copyProperties(oOrg, organization, "id");
                orgList.add(organization);
                orgIdMap.put(oOrg.getId(), organization.getId());

            });
            orgIdMap.put(oldOrgId, orgId);
            orgList.forEach(organization -> {
                Long parentId = orgIdMap.get(organization.getParentId());
                if (parentId != null) {
                    organization.setParentId(parentId);
                }

                if (!StringUtils.isEmpty(organization.getPath())) {

                    String[] pathArr = organization.getPath().replace("null", "/").split("/");
                    List<String> newPathList = new ArrayList<>();
                    Arrays.asList(pathArr).forEach(pathItem -> {
                        if (orgIdMap.get(LongUtils.parseLong(pathItem)) == null) {
                            newPathList.add(pathItem);
                        } else {
                            newPathList.add(orgIdMap.get(LongUtils.parseLong(pathItem)).toString());
                        }
                    });

                    String newPath = ("/" + String.join("/", newPathList) + "/").replace("//", "/").replace("null", "/");
                    organization.setPath(newPath);
                }

                if (!StringUtils.isEmpty(organization.getChildren())) {
                    String[] oldChildren = organization.getChildren().split(",");
                    List<String> newChildren = new ArrayList<>();
                    Arrays.asList(oldChildren).forEach(oldChild -> {
                        if (orgIdMap.get(LongUtils.parseLong(oldChild)) == null) {
                            newChildren.add(oldChild);
                        } else {
                            newChildren.add(orgIdMap.get(LongUtils.parseLong(oldChild)).toString());
                        }
                    });

                    String newChildrenStr = String.join(",", newChildren);
                    organization.setChildren(newChildrenStr);

                }

                organizationFeignAPI.createOrg(organization);

            });
            System.out.println("插入 Organization   Created ");


            Map<Long, Long> roleIdMap = new HashMap<>();
            List<Role> roleList = new ArrayList<>();
            roleFeignAPI.getRoleList(oldOrgId).getData().forEach(oldRole -> {
                Role role = new Role();
                BeanUtils.copyProperties(oldRole, role, "id");
                roleList.add(role);
                roleIdMap.put(oldRole.getId(), role.getId());

            });

            roleList.forEach(role -> {

                if (!StringUtils.isEmpty(role.getOrgPath())) {

                    String[] pathArr = role.getOrgPath().replace("null", "/").split("/");
                    List<String> newPathList = new ArrayList<>();
                    Arrays.asList(pathArr).forEach(pathItem -> {
                        if (StringUtils.isEmpty(pathItem)) return;
                        if (pathItem.substring(0, 1).equalsIgnoreCase("B")) {
                            pathItem = CryptoUtils.th36To10(pathItem.substring(0, 12)).toString();
                        }
                        if (orgIdMap.get(LongUtils.parseLong(pathItem)) == null) {
                            newPathList.add(pathItem);
                        } else {
                            newPathList.add(orgIdMap.get(LongUtils.parseLong(pathItem)).toString());
                        }
                    });

                    String newPath = ("/" + String.join("/", newPathList) + "/").replace("//", "/");
                    role.setOrgPath(newPath);
                }
                role.setOrganizationId(orgId);

                roleFeignAPI.createRole(role);

            });
            System.out.println("插入 Roles   Created ");


            bizCodeRepository.findByOrgIdAndProjectIdAndDeletedIsFalse(oldOrgId, oldProjectId).forEach(oldBizCode -> {
                BizCode bizCode = new BizCode();
                BeanUtils.copyProperties(oldBizCode, bizCode, "id");
                bizCode.setOrgId(orgId);
                bizCode.setProjectId(projectId);
                bizCodeRepository.save(bizCode);
            });

            System.out.println("插入 BIZ_CODE   Created ");


            bpmActTaskConfigRepository.findByOrgIdAndProjectIdAndStatus(oldOrgId, oldProjectId, EntityStatus.ACTIVE).forEach(oldBpmActTaskConfig -> {
                BpmActTaskConfig bpmActTaskConfig = new BpmActTaskConfig();
                BeanUtils.copyProperties(oldBpmActTaskConfig, bpmActTaskConfig, "id");
                bpmActTaskConfig.setOrgId(orgId);
                bpmActTaskConfig.setProjectId(projectId);
                bpmActTaskConfigRepository.save(bpmActTaskConfig);
            });
            System.out.println("插入 Act_task_config   Created ");

        } catch (Exception e) {
            e.printStackTrace();

            bpmActTaskConfigRepository.deleteByOrgId(orgId);
            bizCodeRepository.deleteByOrgId(orgId);
            bizCodeRepository.deleteRoleByOrgId(orgId);
            Organization curOrg = organizationFeignAPI.get(orgId).getData();
            if (curOrg != null) {
                bizCodeRepository.deleteOrgByOrgPath(curOrg.getPath() + curOrg.getId() + "/%");
                organizationFeignAPI.delete(orgId);
                Organization parentOrg = organizationFeignAPI.get(curOrg.getParentId()).getData();
                if (parentOrg != null && parentOrg.getChildren() != null) {
                    parentOrg.setChildren(parentOrg.getChildren().replace("," + curOrg.getId(), ""));

                }
            }
            entityCategoryRuleRepository.deleteByOrgId(orgId);
            relationRepository.deleteByOrgId(orgId);
            bpmProcessRepository.deleteByOrgId(orgId);
            bpmProcessStageRepository.deleteByOrgId(orgId);
            bpmProcessCategoryRepository.deleteByOrgId(orgId);
            bpmEntitySubTypeRepository.deleteByOrgId(orgId);
            bpmEntityTypeRepository.deleteByOrgId(orgId);
            projectRepository.deleteById(projectId);
        }


        return true;
    }


    /**
     * 删除项目。
     *
     * @param orgId
     * @param projectId
     * @return
     */
    @Override

    public boolean deleteProject(Long orgId, Long projectId) {

        bpmActTaskConfigRepository.deleteByOrgId(orgId);
        bizCodeRepository.deleteByOrgId(orgId);
        bizCodeRepository.deleteRoleByOrgId(orgId);
        Organization curOrg = organizationFeignAPI.get(orgId).getData();
        if (curOrg != null) {
            bizCodeRepository.deleteOrgByOrgPath(curOrg.getPath() + curOrg.getId() + "/%");
            organizationFeignAPI.delete(orgId);
            Organization parentOrg = organizationFeignAPI.get(curOrg.getParentId()).getData();
            if (parentOrg != null && parentOrg.getChildren() != null) {
                parentOrg.setChildren(parentOrg.getChildren().replace("," + curOrg.getId(), ""));
            }
        }
        entityCategoryRuleRepository.deleteByOrgId(orgId);
        relationRepository.deleteByOrgId(orgId);
        bpmProcessRepository.deleteByOrgId(orgId);
        bpmProcessStageRepository.deleteByOrgId(orgId);
        bpmProcessCategoryRepository.deleteByOrgId(orgId);
        bpmEntitySubTypeRepository.deleteByOrgId(orgId);
        bpmEntityTypeRepository.deleteByOrgId(orgId);
        projectRepository.deleteById(projectId);
        return true;
    }


    /**
     * 保存实体下载临时文件。
     *
     * @param operatorId  项目ID
     * @return 实体下载临时文件
     */
    @Override
    public File saveDownloadFile(Long operatorId) {
        String templateFilePath = "/var/www/saint-whale/backend/resources/templates/export-projects.xlsx";
//        String templateFilePath = "d:/var/www/saint-whale/backend/resources/templates/export-projects.xlsx";

        String temporaryFileName = System.currentTimeMillis() + ".xlsx";
        String filePath = "/var/www/saint-whale/backend/private/upload/" + temporaryFileName;
//        String filePath = "d:/var/www/saint-whale/backend/private/upload/" + temporaryFileName;
        File excel = new File(filePath);

        PageDTO pageDTO = new PageDTO();
        pageDTO.setFetchAll(true);
        List<Map<String, Object>> dataList = projectRepository.archiveProjectData();
        List<ProjectResultDTO> projectResultDTOs = new ArrayList<>();
        for (Map<String, Object> data : dataList) {
            ProjectResultDTO projectResultDTO = new ProjectResultDTO();
            projectResultDTO.setName(data.get("name").toString());
            projectResultDTO.setDayWorkHour(Integer.valueOf(data.get("dayWorkHour").toString()));
            projectResultDTOs.add(projectResultDTO);
        }
        EasyExcel.write(filePath, ProjectResultDTO.class).withTemplate(templateFilePath).sheet("project").doFill(projectResultDTOs);
        return excel;
    }


    public Page<StageVersion> getStageVersionList(
        Long orgId,
        Long projectId,
        PageDTO pageDTO) {
        return stageVersionRepository.findAllByOrgIdAndProjectIdAndStatus(orgId, projectId, EntityStatus.ACTIVE, pageDTO.toPageable());
    }

    public void creatStageVersionList(Long orgId, Long projectId, StageVersionDTO stageVersionDTO, ContextDTO context) {
        StageVersion version = stageVersionRepository.findByOrgIdAndProjectIdAndStageAndVersionCodeAndStatus(
            orgId,
            projectId,
            stageVersionDTO.getStage(),
            stageVersionDTO.getVersionCode(),
            EntityStatus.ACTIVE
        );
        if (version != null) {
            throw new BusinessError("此阶段版本已经存在！");
        } else {
            StageVersion stageVersion = new StageVersion();
            BeanUtils.copyProperties(stageVersionDTO, stageVersion);
            stageVersion.setOrgId(orgId);
            stageVersion.setProjectId(projectId);
            stageVersion.setCreatedAt();
            stageVersion.setStatus(EntityStatus.ACTIVE);
            stageVersion.setLastModifiedAt();
            stageVersionRepository.save(stageVersion);
        }
    }

    public StageVersion getStageVersion(Long orgId, Long projectId, Long stageVersionId) {
        return stageVersionRepository.findAllByOrgIdAndProjectIdAndId(orgId, projectId, stageVersionId);
    }

    public void deleteStageVersionList(Long orgId, Long projectId, Long id, ContextDTO context) {
        StageVersion stageVersion = stageVersionRepository.findAllByOrgIdAndProjectIdAndId(orgId, projectId, id);

        stageVersion.setStatus(EntityStatus.DELETED);

        stageVersionRepository.save(stageVersion);
    }

    public List<StageVersion> getStageVersionList(Long orgId, Long projectId, String stage) {
        return stageVersionRepository.findAllByOrgIdAndProjectIdAndStage(orgId, projectId, stage);
    }

    @Override
    public void updateProjectHourStatus(OperatorDTO operator, Long orgId, Long projectId, long version, ProjectModifyDTO projectModifyDTO) {
        Project project = get(orgId, projectId, version);
        project.setLastModifiedAt(new Date());
        project.setLastModifiedBy(operator.getId());
        if (projectModifyDTO.getHaveOvertime()!= null){
            project.setHaveOvertime(projectModifyDTO.getHaveOvertime());
        }
        if (projectModifyDTO.getHaveHour()!= null){
            project.setHaveHour(projectModifyDTO.getHaveHour());
        }
        projectRepository.save(project);
    }

    @Override
    public void enableOvertime(OperatorDTO operator, Long orgId, Long projectId, long version) {
        Project project = get(orgId, projectId, version);
        project.setLastModifiedAt(new Date());
        project.setLastModifiedBy(operator.getId());
        project.setHaveOvertime(true);
        projectRepository.save(project);
    }

    @Override
    public List<HierarchyWBSDTO> getHierarchyInfo(Long orgId, Long projectId, String key) {


        if (key.equals("FUNC_WBS")) {
            List<ProjectHierarchy> projectHierarchies = projectHierarchyRepository.findByProjectIdOrderByHierarchyLevel(projectId);
            if (!projectHierarchies.isEmpty()) {
                HierarchyWBSDTO parentWBSDTO;
                List<HierarchyWBSDTO> wbsDTOs = new ArrayList<>();
                List<HierarchyWBSDTO> result = new ArrayList<>();
                for(ProjectHierarchy hierarchy : projectHierarchies) {
                    HierarchyWBSDTO hierarchyWBSDTO = new HierarchyWBSDTO();
                    BeanUtils.copyProperties(hierarchy, hierarchyWBSDTO);
                    if (hierarchyWBSDTO.getContent().equals("PROJECT")) {
                        hierarchyWBSDTO.setDelete(false);
                    } else {
                        hierarchyWBSDTO.setDelete(hierarchy.getEdit());
                    }
                    if (hierarchy.getDetail() != null && !hierarchy.getDetail().isEmpty()) {
                        if (hierarchy.getDetail().contains(",")) {
                            hierarchyWBSDTO.setDetail(Arrays.asList(hierarchy.getDetail().split(",")));
                        } else {
                            List<String> details = new ArrayList<>();
                            details.add(hierarchy.getDetail());
                            hierarchyWBSDTO.setDetail(details);
                        }
                    }
                    hierarchyWBSDTO.setLevel(hierarchy.getHierarchyLevel());
                    wbsDTOs.add(hierarchyWBSDTO);
                }

                Map<Long, HierarchyWBSDTO> hierarchyWBSDTOMap = new HashMap<>();
                for(HierarchyWBSDTO wbsDTO : wbsDTOs) {
                    hierarchyWBSDTOMap.put(wbsDTO.getId(), wbsDTO);

                    parentWBSDTO = hierarchyWBSDTOMap.get(wbsDTO.getParentId());

                    if (parentWBSDTO == null) {
                        result.add(wbsDTO);
                        continue;
                    }
                    parentWBSDTO.addChild(wbsDTO);
                }

                return result;
            }

        } else {
            ProjectInfo pi = projectRepository.findByProjectIdAndKey(projectId, key);
            if(pi == null) {
                return new ArrayList<>();
            }
            String vlv = pi.getInfoValue();
            if(StringUtils.isEmpty(vlv)) {
                return new ArrayList<>();
            }
            String[] wbses = vlv.split(",");
            Map<String, HierarchyWBSDTO> nodeDtoMap = new HashMap<>();
            String SUB_SYSTEM_STR = "SUB_SYSTEM";
            boolean isEditable = true;
            int cnt = 1;
            HierarchyWBSDTO parentWBSDTO;
            List<HierarchyWBSDTO> result = new ArrayList<>();

            for(String wbs:wbses) {
                wbs = StringUtils.trim(wbs);
                if(StringUtils.isEmpty(wbs)) continue;
                HierarchyWBSDTO hierarchyWBSDTO = new HierarchyWBSDTO();
                hierarchyWBSDTO.setHierarchyType("WBS_HIERARCHY");
                hierarchyWBSDTO.setContent(wbs);
                if(SUB_SYSTEM_STR.equalsIgnoreCase(wbs)) {
                    isEditable = false;
                }
                hierarchyWBSDTO.setEdit(isEditable);
                hierarchyWBSDTO.setAdd(isEditable);
                hierarchyWBSDTO.setRegular(!isEditable);
                if (hierarchyWBSDTO.getContent().equals("PROJECT")) {
                    hierarchyWBSDTO.setDelete(false);
                } else {
                    hierarchyWBSDTO.setDelete(isEditable);
                }
                hierarchyWBSDTO.setId(Long.valueOf(cnt++));
                hierarchyWBSDTO.setLevel(String.valueOf(cnt-1));
//            hierarchyWBSDTO.setId(String.valueOf(cnt));
                if(cnt != 2) {
                    hierarchyWBSDTO.setParentId(wbses[cnt -3]);
                }
                nodeDtoMap.put(wbs, hierarchyWBSDTO);
            }
            cnt = 1;
            for(String wbs:wbses) {
                if(cnt++ == 1) {
                    parentWBSDTO = null;
                } else {
                    parentWBSDTO = nodeDtoMap.get(wbses[cnt - 3]);
                }
                if (parentWBSDTO == null) {
                    result.add(nodeDtoMap.get(wbs));
                    continue;
                }

                parentWBSDTO.addChild(nodeDtoMap.get(wbses[cnt-2]));
            }

            return result;
        }
        return null;
    }

    @Override
    public void saveWbsSetting(Long orgId, Long projectId, ProjectInfoDTO projectInfoDTO) {
        String key = projectInfoDTO.getInfoKey();
        String vlv = projectInfoDTO.getInfoValue();
        if(StringUtils.isEmpty(key) || StringUtils.isEmpty(vlv)) {
            throw new BusinessError("THERE IS NO KEY OR Value INFORMATION");
        }

        if (key.equals("FUNC_WBS")) {
            List<HierarchyWBSDTO> dtos = projectInfoDTO.getWbsTreeData();
            if (projectInfoDTO.getWbsTreeData() == null || projectInfoDTO.getWbsTreeData().size() == 0) {
                throw new BusinessError("THERE IS NO KEY OR Value INFORMATION");
            } else {
                List<ProjectHierarchy> projectHierarchies = projectHierarchyRepository.findByProjectIdOrderByHierarchyLevel(projectId);
                if (!projectHierarchies.isEmpty()) {
                    projectHierarchyRepository.deleteAll(projectHierarchies);
                }
                for (HierarchyWBSDTO dto : dtos) {
                    appendContent(orgId, projectId, dto, null);
                }
            }
        } else {
            ProjectInfo pi = projectRepository.findByProjectIdAndKey(projectId, key);
            if(pi != null) {
                pi.setInfoValue(vlv);
                projectInfoRepository.save(pi);
            } else {
                ProjectInfo info = new ProjectInfo();
                info.setDeleted(false);
                info.setInfoKey(key);
                info.setOrgId(orgId);
                info.setProjectId(projectId);
                info.setInfoValue(vlv);
                projectInfoRepository.save(info);
            }
        }


    }

    private void appendContent(Long orgId, Long projectId, HierarchyWBSDTO dto, Long parentId) {

        ProjectHierarchy projectHierarchy = new ProjectHierarchy();
        BeanUtils.copyProperties(dto, projectHierarchy, "id");
        projectHierarchy.setOrgId(orgId);
        projectHierarchy.setProjectId(projectId);
        projectHierarchy.setHierarchyType("FUNC_WBS");
        projectHierarchy.setHierarchyLevel(dto.getLevel());
        projectHierarchy.setParentId(parentId);
        if (dto.getContent().equals("PROJECT")) {
            projectHierarchy.setDelete(false);
        } else {
            projectHierarchy.setDelete(true);
        }
        if (dto.getDetail() != null && !dto.getDetail().isEmpty()) {
            projectHierarchy.setDetail(String.join(",", dto.getDetail()));
        }
        projectHierarchyRepository.save(projectHierarchy);

        // 如果有子元素，递归处理它们
        if (dto.getChildren() != null && !dto.getChildren().isEmpty()) {
            for (HierarchyWBSDTO child : dto.getChildren()) {
                appendContent(orgId, projectId, child, projectHierarchy.getId());
            }
        }
    }

    /**
     * 更新项目层级模板。
     *
     * @param operator   操作者信息
     * @param orgId      所属组织 ID
     * @param projectId  项目 ID
     * @return 项目数据实体
     */
    @Override
    public Project updateHierarchyTemplate(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final ProjectModifyDTO projectDTO
    ) {

        Project project = get(orgId, projectId);

        if (projectDTO.getOptionalNodeTypes() == null) {
            throw new BusinessError("请配置相应层级信息！");
        } else {
            ProjectInfo pi = projectRepository.findByProjectIdAndKey(project.getId(), projectDTO.getHierarchyType());
            if(pi == null) {
                pi = new ProjectInfo();
                pi.setDeleted(false);
                pi.setInfoKey(projectDTO.getHierarchyType());
                pi.setOrgId(project.getOrgId());
                pi.setProjectId(project.getId());
            }
            pi.setInfoValue(projectDTO.getOptionalNodeTypes());
            projectInfoRepository.save(pi);
        }

//        setOptionalHierarchyImportFileTemplate(operator, projectDTO, project);
        setHierarchyImportFileTemplate(operator, projectDTO, project);

        project.setLastModifiedBy(operator.getId());
        project.setLastModifiedAt(new Date());

        set(project);
        return projectRepository.save(project);
    }

    @Override
    public Project approveOvertime(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final ProjectModifyDTO projectDTO
    ) {

        Project project = get(orgId, projectId);

        project.setLastModifiedBy(operator.getId());
        project.setLastModifiedAt(new Date());
        project.setApproveOvertime(projectDTO.getApproveOvertime());

        set(project);
        return projectRepository.save(project);
    }
}
