package com.ose.tasks.domain.model.service.wps.simple;
import com.ose.auth.api.OrganizationFeignAPI;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.dto.UserDTO;
import com.ose.auth.entity.UserProfile;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.BaseDTO;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.SubconRepository;
import com.ose.tasks.domain.model.repository.wps.simple.WelderGradeSimpleRepository;
import com.ose.tasks.domain.model.repository.wps.simple.WelderGradeWelderSimpleRelationRepository;
import com.ose.tasks.domain.model.repository.wps.simple.WelderSimpleRepository;
import com.ose.tasks.domain.model.service.SubconInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.HierarchyNodeImportDTO;
import com.ose.tasks.dto.wps.simple.*;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.Subcon;
import com.ose.tasks.entity.wps.simple.WelderGradeSimplified;
import com.ose.tasks.entity.wps.simple.WelderGradeWelderSimplifiedRelation;
import com.ose.tasks.entity.wps.simple.WelderSimplified;
import com.ose.tasks.vo.WelderStatus;
import com.ose.util.*;
import com.ose.vo.EntityStatus;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;


@Component
public class WelderSimpleService implements WelderSimpleInterface {
    private final static Logger logger = LoggerFactory.getLogger(WelderSimpleService.class);
    private final WelderSimpleRepository welderSimpleRepository;

    private final WelderGradeWelderSimpleRelationRepository welderGradeWelderSimpleRelationRepository;

    private final WelderGradeSimpleRepository welderGradeSimpleRepository;

    private final UploadFeignAPI uploadFeignAPI;

    private final SubconInterface subconService;

    private final SubconRepository subconRepository;

    private final UserFeignAPI userFeignAPI;

    private final OrganizationFeignAPI organizationFeignAPI;



    @Value("${application.files.temporary}")
    private String temporaryDir;

    // 导出Excel数据输出开始行
    private static final int DATA_START_ROW = 2;

    // 模版行数
    private static final int TEMPLATE_ROW_COUNT = 20;

    @Autowired
    public WelderSimpleService(
        WelderSimpleRepository welderSimpleRepository,
        WelderGradeWelderSimpleRelationRepository welderGradeWelderSimpleRelationRepository,
        WelderGradeSimpleRepository welderGradeSimpleRepository,
        UploadFeignAPI uploadFeignAPI,
        SubconRepository subconRepository,
        SubconInterface subconService,
        UserFeignAPI userFeignAPI,
        OrganizationFeignAPI organizationFeignAPI) {
        this.welderSimpleRepository = welderSimpleRepository;
        this.welderGradeWelderSimpleRelationRepository = welderGradeWelderSimpleRelationRepository;
        this.welderGradeSimpleRepository = welderGradeSimpleRepository;
        this.uploadFeignAPI = uploadFeignAPI;
        this.subconService = subconService;
        this.subconRepository = subconRepository;
        this.userFeignAPI = userFeignAPI;
        this.organizationFeignAPI = organizationFeignAPI;
    }

    /**
     * 创建焊工
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param context
     * @param welderSimpleCreateDTO 创建信息
     */
    @Override
    public WelderSimplified create(
        Long orgId,
        Long projectId,
        ContextDTO context,
        WelderSimpleCreateDTO welderSimpleCreateDTO
    ) {
        WelderSimplified welderSimplifiedByNo = welderSimpleRepository.findByOrgIdAndProjectIdAndNoAndStatus(
            orgId,
            projectId,
            welderSimpleCreateDTO.getNo(),
            EntityStatus.ACTIVE
        );
        if (welderSimplifiedByNo != null) {
            throw new BusinessError("焊工编号已存在");
        }
//        WelderSimplified welderSimplifiedByUserId = welderSimpleRepository.findByOrgIdAndProjectIdAndUserIdAndStatus(
//            orgId,
//            projectId,
//            welderSimpleCreateDTO.getUserId(),
//            EntityStatus.ACTIVE
//        );
//        if (welderSimplifiedByUserId != null) {
//            throw new BusinessError("所选用户已存在焊工编号");
//        }

        WelderSimplified welderSimplified = new WelderSimplified();

        if (welderSimpleCreateDTO.getPhoto() != null) {
            logger.error("焊工简4 保存docs服务->开始");
            JsonObjectResponseBody<FileES> attachment =
                uploadFeignAPI.save(
                    orgId.toString(),
                    projectId.toString(),
                    welderSimpleCreateDTO.getPhoto().toString(),
                    new FilePostDTO());
            logger.error("焊工简4 保存docs服务->结束");
            welderSimplified.setPhoto(LongUtils.parseLong(attachment.getData().getId()));
        }

        welderSimplified.setOrgId(orgId);
        welderSimplified.setProjectId(projectId);
        welderSimplified.setNo(welderSimpleCreateDTO.getNo());
        welderSimplified.setName(welderSimpleCreateDTO.getName());
        welderSimplified.setUserId(welderSimpleCreateDTO.getUserId());
        welderSimplified.setSubConId(welderSimpleCreateDTO.getSubConId());
        welderSimplified.setIdCard(welderSimpleCreateDTO.getIdCard());
        welderSimplified.setWpsWelded(welderSimpleCreateDTO.getWpsWelded());
        welderSimplified.setProcessWelded(welderSimpleCreateDTO.getProcessWelded());
        welderSimplified.setRemark(welderSimpleCreateDTO.getRemark());
        welderSimplified.setStatus(EntityStatus.ACTIVE);
        welderSimplified.setCreatedAt(new Date());
        welderSimplified.setCreatedBy(context.getOperator().getId());
        welderSimplified.setLastModifiedAt(new Date());
        welderSimplified.setLastModifiedBy(context.getOperator().getId());
        welderSimplified.setCerId(welderSimpleCreateDTO.getCerId());
        welderSimplified.setCerExpirationAt(welderSimpleCreateDTO.getCerExpirationAt());
        if (welderSimpleCreateDTO.getCerExpirationAt().after(new Date())) {
            welderSimplified.setWelderStatus(WelderStatus.NORMAL);
        }
        if (welderSimpleCreateDTO.getCerExpirationAt().before(new Date())) {
            welderSimplified.setWelderStatus(WelderStatus.EXPIRED);
        }
        return welderSimpleRepository.save(welderSimplified);

    }

    /**
     * 编辑焊工
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param context
     * @param welderSimpleUpdateDTO 创建信息
     */
    @Override
    public void update(
        Long orgId,
        Long projectId,
        Long welderId,
        ContextDTO context,
        WelderSimpleUpdateDTO welderSimpleUpdateDTO
    ) {

        WelderSimplified welderSimplifiedByNo = welderSimpleRepository.findByOrgIdAndProjectIdAndNoAndStatus(
            orgId,
            projectId,
            welderSimpleUpdateDTO.getNo(),
            EntityStatus.ACTIVE
        );
        if (welderSimplifiedByNo != null && !welderId.equals(welderSimplifiedByNo.getId())) {
            throw new BusinessError("焊工编号已存在");
        }
        WelderSimplified welderSimplifiedByUserId = welderSimpleRepository.findByOrgIdAndProjectIdAndUserIdAndStatus(
            orgId,
            projectId,
            welderSimpleUpdateDTO.getUserId(),
            EntityStatus.ACTIVE
        );
        if (welderSimplifiedByUserId != null && welderSimplifiedByNo != null && !welderId.equals(welderSimplifiedByNo.getId())) {
            throw new BusinessError("所选用户已存在焊工编号");
        }
        WelderSimplified welderSimplified = welderSimpleRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, welderId, EntityStatus.ACTIVE);

        if (welderSimplified == null) {
            throw new BusinessError("焊工信息不存在");
        }
        if (welderSimpleUpdateDTO.getPhoto() != null) {
            logger.error("焊工简5 保存docs服务->开始");
            JsonObjectResponseBody<FileES> attachment =
                uploadFeignAPI.save(
                    orgId.toString(),
                    projectId.toString(),
                    welderSimpleUpdateDTO.getPhoto(),
                    new FilePostDTO());
            logger.error("焊工简5 保存docs服务->结束");
            welderSimplified.setPhoto(LongUtils.parseLong(attachment.getData().getId()));
        }

        welderSimplified.setNo(welderSimpleUpdateDTO.getNo());
        welderSimplified.setName(welderSimpleUpdateDTO.getName());
        welderSimplified.setUserId(welderSimpleUpdateDTO.getUserId());
        welderSimplified.setWpsWelded(welderSimpleUpdateDTO.getWpsWelded());
        welderSimplified.setProcessWelded(welderSimpleUpdateDTO.getProcessWelded());
        welderSimplified.setSubConId(welderSimpleUpdateDTO.getSubConId());
        welderSimplified.setIdCard(welderSimpleUpdateDTO.getIdCard());
        welderSimplified.setRemark(welderSimpleUpdateDTO.getRemark());
        welderSimplified.setLastModifiedAt(new Date());
        welderSimplified.setLastModifiedBy(context.getOperator().getId());
        welderSimplified.setCerExpirationAt(welderSimpleUpdateDTO.getCerExpirationAt());
        welderSimplified.setCerId(welderSimpleUpdateDTO.getCerId());
        if (welderSimpleUpdateDTO.getCerExpirationAt().after(new Date())) {
            welderSimplified.setWelderStatus(WelderStatus.NORMAL);
        }
        if (welderSimpleUpdateDTO.getCerExpirationAt().before(new Date())) {
            welderSimplified.setWelderStatus(WelderStatus.EXPIRED);
        }
        welderSimpleRepository.save(welderSimplified);

    }

    /**
     * 打开焊工账户
     *
     * @param orgId 组织ID
     * @param projectId 项目ID
     * @param welderSimpleId 焊工ID
     * @param contextDTO
     */
    @Override
    public void open(
        Long orgId,
        Long projectId,
        Long welderSimpleId,
        ContextDTO contextDTO
    ) {
        WelderSimplified welderSimplified = welderSimpleRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, welderSimpleId, EntityStatus.ACTIVE);
        if (welderSimplified == null) {
            throw new BusinessError("焊工信息不存在");
        }
        welderSimplified.setWelderStatus(WelderStatus.NORMAL);
        welderSimpleRepository.save(welderSimplified);
    }

    /**
     * 停用焊工账户
     *
     * @param orgId 组织ID
     * @param projectId 项目ID
     * @param welderSimpleId 焊工ID
     * @param contextDTO
     */
    @Override
    public void deactivate(
        Long orgId,
        Long projectId,
        Long welderSimpleId,
        ContextDTO contextDTO
    ) {
        WelderSimplified welderSimplified = welderSimpleRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, welderSimpleId, EntityStatus.ACTIVE);
        if (welderSimplified == null) {
            throw new BusinessError("焊工信息不存在");
        }
        welderSimplified.setWelderStatus(WelderStatus.DEACTIVATE);
        welderSimpleRepository.save(welderSimplified);
    }

    /**
     * 焊工详情
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param welderSimpleId 焊工id
     */
    @Override
    public WelderSimplified detail(
        Long orgId,
        Long projectId,
        Long welderSimpleId
    ) {
        WelderSimplified welderSimplified = welderSimpleRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            welderSimpleId,
            EntityStatus.ACTIVE
        );
        if (welderSimplified == null) {
            throw new BusinessError("焊工不存在");
        } else {
            return welderSimplified;
        }
    }

    /**
     * 焊工列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    @Override
    public Page<WelderSimplified> search(
        Long orgId,
        Long projectId,
        WelderSimpleSearchDTO welderSearchDTO
    ) {

        if (!StringUtils.isEmpty(welderSearchDTO.getUserNo()) && welderSearchDTO.getUserNo().length() > 2) {
            List<UserProfile> users = userFeignAPI.getUserByUsername(orgId, welderSearchDTO.getUserNo()).getData();
            if (users != null && users.size() > 0) {
                Set<Long> userIds = new HashSet<>();
                users.forEach(user -> userIds.add(user.getId()));
                welderSearchDTO.setUserIds(userIds);
            } else {
                return new PageImpl<>(
                    new ArrayList<>()
                );
            }
        }
        if (!StringUtils.isEmpty(welderSearchDTO.getSubConNo()) && welderSearchDTO.getSubConNo().length() > 2) {
            List<Subcon> subcons = subconRepository.findByOrgIdAndProjectIdAndDeletedIsFalseAndNameIsLike(orgId, projectId, welderSearchDTO.getSubConNo());
            if (subcons != null && subcons.size() > 0) {
                Set<Long> subconIds = new HashSet<>();
                subcons.forEach(subcon -> subconIds.add(subcon.getId()));
                welderSearchDTO.setSubConIds(subconIds);
            }
        }

        return welderSimpleRepository.findList(
            orgId,
            projectId,
            EntityStatus.ACTIVE,
            welderSearchDTO
        );
    }

    /**
     * 删除焊工
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param welderSimpleId 焊工id
     */
    @Override
    public void delete(
        Long orgId,
        Long projectId,
        Long welderSimpleId,
        ContextDTO context
    ) {
        WelderSimplified welderSimplifiedByUserId = welderSimpleRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            welderSimpleId,
            EntityStatus.ACTIVE
        );
        if (welderSimplifiedByUserId == null) {
            throw new BusinessError("焊工不存在");
        }

        WpsSimpleRelationSearchDTO wpsSimpleRelationSearchDTO = new WpsSimpleRelationSearchDTO();
        wpsSimpleRelationSearchDTO.setFetchAll(true);

        Page<WelderGradeWelderSimplifiedRelation> pageList = welderGradeWelderSimpleRelationRepository.findByOrgIdAndProjectIdAndWelderIdAndStatus(
            orgId,
            projectId,
            welderSimpleId,
            EntityStatus.ACTIVE,
            wpsSimpleRelationSearchDTO.toPageable()
        );
        List<WelderGradeWelderSimplifiedRelation> list = pageList.getContent();
        if (list.size() > 0) {
            for (WelderGradeWelderSimplifiedRelation welderGradeWelderSimplifiedRelation : list) {
                welderGradeWelderSimplifiedRelation.setStatus(EntityStatus.DELETED);
                welderGradeWelderSimplifiedRelation.setLastModifiedAt(new Date());
                welderGradeWelderSimplifiedRelation.setLastModifiedBy(context.getOperator().getId());
            }
            welderGradeWelderSimpleRelationRepository.saveAll(list);
        }

        welderSimplifiedByUserId.setStatus(EntityStatus.DELETED);
        welderSimplifiedByUserId.setLastModifiedAt(new Date());
        welderSimplifiedByUserId.setLastModifiedBy(context.getOperator().getId());
        welderSimpleRepository.save(welderSimplifiedByUserId);
    }

    /**
     * 添加焊工和焊工证关联信息。
     */
    @Override
    public void addWelderGrade(
        Long orgId,
        Long projectId,
        Long welderSimpleId,
        WpsSimpleRelationDTO wpsSimpleRelationDTO,
        ContextDTO context
    ) {
        List<WelderGradeWelderSimplifiedRelation> relationList = new ArrayList<>();
        WelderSimplified welderSimplifiedByUserId = welderSimpleRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            welderSimpleId,
            EntityStatus.ACTIVE
        );
        if (welderSimplifiedByUserId == null) {
            throw new BusinessError("焊工不存在");
        }

        if (wpsSimpleRelationDTO.getWelderGradeIds() == null || wpsSimpleRelationDTO.getWelderGradeIds().size() == 0) {
            throw new BusinessError("添加的焊工证不能未空");
        }
        for (Long welderGradeId : wpsSimpleRelationDTO.getWelderGradeIds()) {

            WelderGradeWelderSimplifiedRelation welderGradeWelderSimplifiedRelationNew = new WelderGradeWelderSimplifiedRelation();

            WelderGradeSimplified welderGradeSimplified = welderGradeSimpleRepository.findByOrgIdAndProjectIdAndIdAndStatus(
                orgId,
                projectId,
                welderGradeId,
                EntityStatus.ACTIVE
            );
            if (welderGradeSimplified == null) {
                throw new BusinessError("焊工证不存在");
            }
            WelderGradeWelderSimplifiedRelation welderGradeWelderSimplifiedRelation = welderGradeWelderSimpleRelationRepository.findByOrgIdAndProjectIdAndWelderIdAndWelderGradeIdAndStatus(
                orgId,
                projectId,
                welderSimpleId,
                welderGradeId,
                EntityStatus.ACTIVE
            );
            if (welderGradeWelderSimplifiedRelation != null) {
                continue;
            }
            welderGradeWelderSimplifiedRelationNew.setOrgId(orgId);
            welderGradeWelderSimplifiedRelationNew.setProjectId(projectId);
            welderGradeWelderSimplifiedRelationNew.setWelderGradeNo(welderGradeSimplified.getNo());
            welderGradeWelderSimplifiedRelationNew.setWelderGradeId(welderGradeSimplified.getId());
            welderGradeWelderSimplifiedRelationNew.setWelderId(welderSimplifiedByUserId.getId());
            welderGradeWelderSimplifiedRelationNew.setWelderNo(welderSimplifiedByUserId.getNo());
            welderGradeWelderSimplifiedRelationNew.setStatus(EntityStatus.ACTIVE);
            welderGradeWelderSimplifiedRelationNew.setCreatedAt(new Date());
            welderGradeWelderSimplifiedRelationNew.setCreatedBy(context.getOperator().getId());
            welderGradeWelderSimplifiedRelationNew.setLastModifiedAt(new Date());
            welderGradeWelderSimplifiedRelationNew.setLastModifiedBy(context.getOperator().getId());
            relationList.add(welderGradeWelderSimplifiedRelationNew);
        }
        welderGradeWelderSimpleRelationRepository.saveAll(relationList);

    }

    /**
     * 焊工下的焊工证关系表。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    @Override
    public Page<WelderGradeWelderSimplifiedRelation> searchWelderGrade(
        Long orgId,
        Long projectId,
        Long welderSimpleId,
        WpsSimpleRelationSearchDTO wpsSimpleRelationSearchDTO
    ) {
        return welderGradeWelderSimpleRelationRepository.findByOrgIdAndProjectIdAndWelderIdAndStatus(
            orgId,
            projectId,
            welderSimpleId,
            EntityStatus.ACTIVE,
            wpsSimpleRelationSearchDTO.toPageable()
        );
    }

    /**
     * 删除焊工证
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param welderSimpleId 焊工id
     */
    @Override
    public void deleteWelderGrade(
        Long orgId,
        Long projectId,
        Long welderSimpleId,
        Long welderGradeRelationSimpleId,
        ContextDTO context
    ) {

        WelderGradeWelderSimplifiedRelation welderGradeWelderSimplifiedRelation = welderGradeWelderSimpleRelationRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            welderGradeRelationSimpleId,
            EntityStatus.ACTIVE
        );
        if (welderGradeWelderSimplifiedRelation == null) {
            throw new BusinessError("焊工证关系不存在");
        }
        welderGradeWelderSimplifiedRelation.setStatus(EntityStatus.DELETED);
        welderGradeWelderSimplifiedRelation.setLastModifiedAt(new Date());
        welderGradeWelderSimplifiedRelation.setLastModifiedBy(context.getOperator().getId());
        welderGradeWelderSimpleRelationRepository.save(welderGradeWelderSimplifiedRelation);
    }

    /**
     * 保存实体下载临时文件。
     *
     * @param orgId                 组织 ID
     * @param projectId             项目 ID
     * @param welderSimpleSearchDTO 查询条件
     * @param operatorId            项目ID
     * @return 实体下载临时文件
     */
    @Override
    public File saveDownloadFile(Long orgId, Long projectId, WelderSimpleSearchDTO welderSimpleSearchDTO, Long operatorId) {

        // ① 获取模板文件 复制到 临时路径下
        String temporaryFileName = FileUtils.copy(
            this.getClass()
                .getClassLoader()
                .getResourceAsStream("templates/export-project-entities-welder-simplify.xlsx"),
            temporaryDir,
            operatorId.toString()
        );

        // ② 把临时路径下的模板文件读到workbook中，并填充数据
        File excel;
        Workbook workbook;

        try {
            excel = new File(temporaryDir, temporaryFileName);
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new BusinessError();
        }

        Sheet sheet = workbook.getSheet("weld");
        // 上部2行是固定列名，第3行开始是数据
        int rowNum = DATA_START_ROW;
        // 零件实体数据
        welderSimpleSearchDTO.setFetchAll(true);
        List<? extends WelderSimplified> welderSimplifieds = search(
            orgId,
            projectId,
            welderSimpleSearchDTO).getContent();
        for (WelderSimplified entity : welderSimplifieds) {
            Row row = WorkbookUtils.getRow(sheet, rowNum++);
            // 如果超出模版行数，要复制模版样式
            if (rowNum >= TEMPLATE_ROW_COUNT + DATA_START_ROW) {
                WorkbookUtils.copyRow(sheet.getRow(DATA_START_ROW), row);
            }
            // 获取分包商名称
            Subcon subCons = subconRepository.findByIdAndDeletedIsFalse(entity.getSubConId());

            WorkbookUtils.getCell(row, 3).setCellValue(subCons.getName());
            WorkbookUtils.getCell(row, 0).setCellValue(entity.getNo());
            WorkbookUtils.getCell(row, 1).setCellValue(entity.getName());
            WorkbookUtils.getCell(row, 2).setCellValue(entity.getIdCard());
            WorkbookUtils.getCell(row, 4).setCellValue(entity.getWpsWelded());
            WorkbookUtils.getCell(row, 5).setCellValue(entity.getProcessWelded());
        }
        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
            return excel;
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new BusinessError();
        }
    }

    @Override
    public BatchResultDTO importWelder(BatchTask batchTask, OperatorDTO operator, Project project, ContextDTO context, HierarchyNodeImportDTO welderImportDTO) {


        String sheetName = "user";
        int headerRow = 2;
        Workbook workbook;
        File excel;
        BatchResultDTO batchResult = new BatchResultDTO();
        Long orgId = project.getOrgId();
        Long projectId = project.getId();
        boolean isUserExist = false;
        boolean isUserOrgRelationExist = false;
        Long userId = null;

        try {
            excel = new File(temporaryDir, welderImportDTO.getFilename());
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            throw new NotFoundError();
        }



        Sheet sheet = workbook.getSheet(sheetName);
        Iterator<Row> rows = sheet.rowIterator();
        List<WelderSimplified> welders = new ArrayList<>();
        List<Subcon> subcons = subconRepository.findByOrgIdAndProjectIdAndDeletedIsFalse(orgId, projectId);
        List<WelderGradeSimplified> welderGrades = welderGradeSimpleRepository.
            findByOrgIdAndProjectIdAndStatus(orgId, projectId, EntityStatus.ACTIVE);

        if (CollectionUtils.isEmpty(subcons)) {
            batchTask.setErrorLog("There is no SubContractors");
            return batchResult;
        }

        Map<String, Long> subConMap = new HashMap<>();
        Map<String, Long> welderGradeMap = new HashMap<>();
        Set<String> welderGradeNos = new HashSet<>();
        Set<Long> welderGradeIds = new HashSet<>();

        subcons.forEach(subcon -> subConMap.put(subcon.getName(), subcon.getId()));
        welderGrades.forEach(welderGrade -> welderGradeMap.put(welderGrade.getNo(), welderGrade.getId()));

        while (rows.hasNext()) {
            boolean isWelderExist = false;
            Row row = rows.next();

            if (row.getRowNum() < headerRow) {
                continue;
            }
            batchResult.addTotalCount(1);

            try {

                UserDTO userDTO = new UserDTO();
                int columnNo = 1;
                String userName = StringUtils.trim(WorkbookUtils.readAsString(row, columnNo++));
                String name = StringUtils.trim(WorkbookUtils.readAsString(row, columnNo++));
                String mobile = StringUtils.trim(WorkbookUtils.readAsString(row, columnNo++));
                if (StringUtils.isEmpty(mobile)) mobile = null;
                String email = StringUtils.trim(WorkbookUtils.readAsString(row, columnNo++));
                if (StringUtils.isEmpty(email)) email = null;
                String welderNo = StringUtils.trim(WorkbookUtils.readAsString(row, columnNo++));
                String idCard = StringUtils.trim(WorkbookUtils.readAsString(row, columnNo++));
                String subCon = StringUtils.trim(WorkbookUtils.readAsString(row, columnNo++));
                String welderGrade = StringUtils.trim(WorkbookUtils.readAsString(row, columnNo++));
                String wpsWelded = StringUtils.trim(WorkbookUtils.readAsString(row, columnNo++));
                String processWelded = StringUtils.trim(WorkbookUtils.readAsString(row, columnNo++));
                String initPassword = StringUtils.trim(WorkbookUtils.readAsString(row, columnNo++));
                if (StringUtils.isEmpty(initPassword)) initPassword = null;
                String cerId = StringUtils.trim(WorkbookUtils.readAsString(row, columnNo++));
                Date cerExpirationAt = WorkbookUtils.readAsDate(row, columnNo++);

                if (!StringUtils.isEmpty(welderGrade)) {
                    String[] welderGradeArr = welderGrade.split(",");
                    welderGradeNos = new HashSet<>(Arrays.asList(welderGradeArr));
                }

                List<UserProfile> users = userFeignAPI.getUserByName(orgId, userName).getData();
                if (users.size() > 0) {
                    userId = users.get(0).getId();
                }


                //校验 邮件地址
//                if (!StringUtils.isEmpty(email) && !RegExpUtils.isEmailAddress(email)) {
//                    throw new ValidationError("EMAIL WRONG");
//                }

                //校验手机号
//                if (!StringUtils.isEmpty(mobile) && !RegExpUtils.isMobileNumber(mobile)) {
//                    throw new ValidationError("MOBILE WRONG");
//                }
                //校验身份证号
//                if (!StringUtils.isEmpty(idCard) && !StringUtils.isIDNumber(idCard)) {
//                    throw new ValidationError("IDNumber WRONG");
//                }

                //校验 分包商
                if (StringUtils.isEmpty(subCon) || subConMap.get(subCon) == null) {
                    throw new ValidationError("NO subcontractor");
                }

                //校验证书号
                if (cerId == null) {
                    throw new ValidationError("NO Certificate Number");
                }

                //校验证书时间
                if (cerExpirationAt == null) {
                    throw new ValidationError("NO Certificate expiration time");
                }

                //check username
//                if (StringUtils.isEmpty(username) || StringUtils.isEmpty(name)) {
//
//                    throw new ValidationError("NO username/name");
//                }
//                UserNameCriteriaDTO userNameCriteriaDTO = new UserNameCriteriaDTO(username);
//                List<UserProfile> users = userFeignAPI.getUserByUsername(userNameCriteriaDTO).getData();
//
//                if (!CollectionUtils.isEmpty(users)) {
//                    isUserExist = true;
//                    userId = users.get(0).getId();
//                    if (organizationFeignAPI.getOrgListByMember(orgId, userId).getData() != null) {
//                        isUserOrgRelationExist = true;
//                    }
//
//                }


                if (!StringUtils.isEmpty(welderNo) && !StringUtils.isEmpty(welderGrade)) {
                    Boolean isContainWelderGrade = null;
                    welderGradeIds.clear();
                    for (String wg : welderGradeNos) {
                        if (welderGradeMap.get(wg) == null) {
                            isContainWelderGrade = false;
                            break;
                        }
                        welderGradeIds.add(welderGradeMap.get(wg));
                        isContainWelderGrade = true;
                    }
                    if (isContainWelderGrade == null || !isContainWelderGrade)
                        throw new ValidationError(" there is no welder grade");
                } else {
                    throw new ValidationError("Welder No/Grading is Wrong ");
                }

                WelderSimplified welder = null;
                if (!StringUtils.isEmpty(welderNo)) {
                    welder = welderSimpleRepository.findByOrgIdAndProjectIdAndNoAndStatus(orgId, projectId, welderNo, EntityStatus.ACTIVE);
                    if (welder != null) {
                        isWelderExist = true;

                    }
                }

//                if (!isUserExist) {
//                    userDTO.setName(name);
//                    userDTO.setType("user");
//                    userDTO.setEmail(email);
//                    userDTO.setUsername(username);
//                    userDTO.setStatus(EntityStatus.ACTIVE);
//                    userDTO.setMobile(mobile);
//                    String pwd = "Wison" + RandomUtils.color(0, 100);
//                    row.getCell(columnNo).setCellValue(pwd);
//                    userDTO.setPassword(pwd);
//
//                    UserProfile userProfile = userFeignAPI.create(userDTO).getData();
//                    if (userProfile == null) {
//                        throw new ValidationError("create user failure");
//                    }
//                    userId = userProfile.getId();
//                }

//                if (!isUserOrgRelationExist) {
//                    AddOrganizationMemberDTO addOrganizationMemberDTO = new AddOrganizationMemberDTO();
//                    addOrganizationMemberDTO.setMemberId(userId);
//                    JsonResponseBody jsonResponseBody = organizationFeignAPI.addMembers(orgId, addOrganizationMemberDTO);
//
//                    if (jsonResponseBody == null) {
//                        throw new ValidationError("create user org relation failure");
//                    }
//                }
                if (!StringUtils.isEmpty(welderNo)) {


//                    welder = welderSimpleRepository.findByOrgIdAndProjectIdAndNoAndStatus(orgId, projectId, welderNo, EntityStatus.ACTIVE);
                    if (!isWelderExist && welder == null) {

                        WelderSimpleCreateDTO welderCreate = new WelderSimpleCreateDTO();
                        welderCreate.setIdCard(idCard);
                        welderCreate.setName(name);
                        welderCreate.setUserId(userId);
                        welderCreate.setSubConId(subConMap.get(subCon));
                        welderCreate.setNo(welderNo);
                        welderCreate.setWpsWelded(wpsWelded);
                        welderCreate.setProcessWelded(processWelded);
                        welderCreate.setRemark("XLS IMPORTED");
                        welderCreate.setUserId(userId);
                        welderCreate.setCerId(cerId);
                        welderCreate.setCerExpirationAt(cerExpirationAt);
                        welder = create(orgId, projectId, context, welderCreate);
                        batchResult.addProcessedCount(1);
                    } else {
                        welder.setIdCard(idCard);
                        welder.setName(name);
                        welder.setUserId(userId);
                        welder.setSubConId(subConMap.get(subCon));
                        welder.setNo(welderNo);
                        welder.setWpsWelded(wpsWelded);
                        welder.setProcessWelded(processWelded);
                        welder.setRemark("XLS IMPORTED");
                        welder.setUserId(userId);
                        welder.setWpsWelded(wpsWelded);
                        welder.setProcessWelded(processWelded);
                        welder.setCerId(cerId);
                        welder.setCerExpirationAt(cerExpirationAt);
                        if (cerExpirationAt.before(new Date())) {
                            welder.setWelderStatus(WelderStatus.EXPIRED);
                        } else if (cerExpirationAt.after(new Date())) {
                            welder.setWelderStatus(WelderStatus.NORMAL);
                        }
                        welderSimpleRepository.save(welder);
                        batchResult.addProcessedCount(1);
                    }



                    WpsSimpleRelationDTO wpsSimpleRelationDTO = new WpsSimpleRelationDTO();
                    wpsSimpleRelationDTO.setWelderGradeIds(new ArrayList<>(welderGradeIds));
                    addWelderGrade(orgId, projectId, welder.getId(), wpsSimpleRelationDTO, context);
                }

            } catch (Exception e) {
                setImportDataErrorMessage(row, e.toString());
                batchResult.addErrorCount(1);
            }

        }
        welderSimpleRepository.saveAll(welders);


        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return batchResult;
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
        List<Long> subConIds = new ArrayList<>();
        for (T entity : entities) {
            if (entity instanceof WelderSimplified) {
                WelderSimplified welderSimplified = (WelderSimplified) entity;
                subConIds.add(welderSimplified.getSubConId());
            }
        }

        if (subConIds.size() > 0) {
            Iterable<Subcon> subconList = subconService.findAllByIds(subConIds);
            for (Subcon subcon : subconList) {
                included.put(subcon.getId(), subcon);
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

        Long subConId = null;
        if (entity instanceof WelderSimplified) {
            WelderSimplified welderSimplified = (WelderSimplified) entity;
            subConId = welderSimplified.getSubConId();
        }

        if (subConId != null && !subConId.equals(0L)) {
            Subcon subconInclude = subconRepository.findByIdAndDeletedIsFalse(subConId);
            if (subconInclude != null) {
                included.put(subconInclude.getId(), subconInclude);
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

    /**
     * 设置导入数据错误信息。
     *
     * @param row     错误所在行
     * @param message 错误消息
     */
    private void setImportDataErrorMessage(Row row, String message) {
        row.createCell(row.getLastCellNum()).setCellValue(message);
    }
}
