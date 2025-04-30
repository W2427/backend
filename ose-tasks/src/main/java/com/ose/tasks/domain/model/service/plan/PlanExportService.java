package com.ose.tasks.domain.model.service.plan;

import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.tasks.domain.model.repository.ProjectRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryBlobRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.entry.WBSEntry;
import com.ose.tasks.entity.wbs.entry.WBSEntryBlob;
import com.ose.tasks.entity.wbs.entry.WBSEntryState;
import com.ose.tasks.vo.wbs.WBSEntryType;
import com.ose.util.FileUtils;
import com.ose.util.WorkbookUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * 计划信息导出服务。
 */
@Component
public class PlanExportService implements PlanExportInterface {

    // 上传文件的临时路径
    @Value("${application.files.temporary}")
    private String temporaryDir;

    // 项目数据仓库
    private final ProjectRepository projectRepository;

    // WBS 条目数据仓库
    private final WBSEntryRepository wbsEntryRepository;

    private final WBSEntryBlobRepository wbsEntryBlobRepository;

    private final WBSEntryStateRepository wbsEntryStateRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public PlanExportService(
        ProjectRepository projectRepository,
        WBSEntryRepository wbsEntryRepository,
        WBSEntryBlobRepository wbsEntryBlobRepository,
        WBSEntryStateRepository wbsEntryStateRepository) {
        this.projectRepository = projectRepository;
        this.wbsEntryRepository = wbsEntryRepository;
        this.wbsEntryBlobRepository = wbsEntryBlobRepository;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
    }

    /**
     * 导出三级计划。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param rootId    根节点 ID
     * @return 导出文件
     */
    @Override
    public File exportWBSWorkEntries(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final Long rootId
    ) {

        Project project = projectRepository
            .findByIdAndOrgIdAndDeletedIsFalse(projectId, orgId)
            .orElse(null);

        if (project == null || rootId == null) {
            throw new BusinessError("项目不存在"); // TODO
        }

        WBSEntry rootEntry = null;

        if (rootId != null) {

            rootEntry = wbsEntryRepository
                .findByProjectIdAndIdAndDeletedIsFalse(projectId, rootId)
                .orElse(null);

            if (rootEntry == null) {
                throw new NotFoundError();
            }

        }

        List<WBSEntry> wbsEntries = wbsEntryRepository
            .findByProjectIdAndPathLikeAndTypeInAndDeletedIsFalseOrderByPathAsc(
                projectId,
                rootEntry.getId(),
                new HashSet<>(Arrays.asList(WBSEntryType.UNITS, WBSEntryType.WORK))
            );

        if (wbsEntries.size() == 0) {
            return null;
        }

        // 复制模板文件
        String temporaryFilename = FileUtils.copy(
            this
                .getClass()
                .getClassLoader()
                .getResourceAsStream("templates/wbs-work-entries.xlsx"),
            temporaryDir,
            operator.getId().toString()
        );

        File excel;
        Workbook workbook;

        // 读取模板文件
        try {
            excel = new File(temporaryDir, temporaryFilename);
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            throw new BusinessError(); // TODO
        }

        Sheet sheet = workbook.getSheet("WBS");

        sheet.getRow(0).getCell(1).setCellValue(String.format("项目【%s】三级计划一览表", project.getName()));

        int rowIndex = 3;
        int depth;

        // 设置数据内容
        for (WBSEntry wbsEntry : wbsEntries) {
            WBSEntryBlob wbsEntryBlob = wbsEntryBlobRepository.findByWbsEntryId(wbsEntry.getId());
            WBSEntryState wbsEntryState = wbsEntryStateRepository.findByWbsEntryId(wbsEntry.getId());
            if(wbsEntryBlob == null || wbsEntryState == null) continue;

            depth = wbsEntry.getDepth() + 1;

            WorkbookUtils.setCellValue(sheet, rowIndex, 0, depth - 1);

            for (int i = 1; i <= 10; i++) {
                if (i == depth) {
                    WorkbookUtils.setCellValue(sheet, rowIndex, i, wbsEntryBlob.getWbs());
                } else {
                    WorkbookUtils.setCellAsBlank(sheet, rowIndex, i);
                }
            }

            WorkbookUtils.setCellValue(sheet, rowIndex, 11, wbsEntry.getNo());
            WorkbookUtils.setCellValue(sheet, rowIndex, 12, wbsEntry.getName());
            WorkbookUtils.setCellValue(sheet, rowIndex, 13, wbsEntry.getType().toString());
            WorkbookUtils.setCellValue(sheet, rowIndex, 14, wbsEntry.getStartAt());
            WorkbookUtils.setCellValue(sheet, rowIndex, 15, wbsEntry.getFinishAt());
            WorkbookUtils.setCellValue(sheet, rowIndex, 16, wbsEntry.getDuration());
            WorkbookUtils.setCellValue(sheet, rowIndex, 17, wbsEntryState.getStartedAt());
            WorkbookUtils.setCellValue(sheet, rowIndex, 18, wbsEntryState.getFinishedAt());
            WorkbookUtils.setCellValue(sheet, rowIndex, 19, wbsEntryState.getProgress() + "%");
            WorkbookUtils.setCellValue(sheet, rowIndex, 20, wbsEntry.getActive() ? "Active" : "Inactive");
            WorkbookUtils.setCellValue(sheet, rowIndex, 21, wbsEntryState.getRunningStatus());

            rowIndex++;
        }

        // 保存为临时文件
        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
        } catch (IOException ioe) {
            throw new BusinessError(); // TODO
        }

        return new File(excel.getAbsolutePath());
    }

    /**
     * 导出四级计划。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param workId    三级计划节点 ID
     * @return 导出文件
     */
    @Override
    public File exportWBSEntityEntries(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final Long workId
    ) {

        Project project = projectRepository
            .findByIdAndOrgIdAndDeletedIsFalse(projectId, orgId)
            .orElse(null);

        if (project == null) {
            throw new BusinessError("项目不存在"); // TODO
        }

        WBSEntry workEntry = wbsEntryRepository
            .findByProjectIdAndIdAndDeletedIsFalse(projectId, workId)
            .orElse(null);

        if (workEntry == null || workEntry.getType() != WBSEntryType.WORK) {
            throw new NotFoundError();
        }

        WBSEntryBlob workEntryBlob = wbsEntryBlobRepository.findByWbsEntryId(workId);
        WBSEntryState workEntryState = wbsEntryStateRepository.findByWbsEntryId(workId);
        if(workEntryBlob == null || workEntryState == null) return null;

        List<WBSEntry> wbsEntries = wbsEntryRepository
            .findByParentIdAndTypeAndDeletedIsFalseOrderBySortAsc(
                workId,
                WBSEntryType.ENTITY
            );

        if (wbsEntries.size() == 0) {
            return null;
        }

        // 复制模板文件
        String temporaryFilename = FileUtils.copy(
            this
                .getClass()
                .getClassLoader()
                .getResourceAsStream("templates/wbs-entity-processes.xlsx"),
            temporaryDir,
            operator.getId().toString()
        );

        File excel;
        Workbook workbook;

        // 读取模板文件
        try {
            excel = new File(temporaryDir, temporaryFilename);
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            throw new BusinessError(); // TODO
        }

        Sheet sheet = workbook.getSheet("Entity Processes");

        sheet.getRow(0).getCell(0).setCellValue(String.format("项目【%s】三级计划【%s】四级计划一览表", project.getName(), workEntry.getName()));
        sheet.getRow(1).getCell(1).setCellValue(workEntryBlob.getWbs());
        sheet.getRow(2).getCell(1).setCellValue(workEntry.getNo());
        sheet.getRow(3).getCell(1).setCellValue(workEntry.getName());
        sheet.getRow(1).getCell(3).setCellValue(wbsEntries.get(0).getModuleType());
        sheet.getRow(2).getCell(3).setCellValue(workEntry.getStage());
        sheet.getRow(3).getCell(3).setCellValue(workEntry.getProcess());

        int rowIndex = 6;

        // 设置数据内容
        for (WBSEntry wbsEntry : wbsEntries) {
            WBSEntryState wbsEntryState = wbsEntryStateRepository.findByWbsEntryId(wbsEntry.getId());
            if(wbsEntryState == null) continue;
            WorkbookUtils.setCellValue(sheet, rowIndex, 1, wbsEntry.getNo());
            WorkbookUtils.setCellValue(sheet, rowIndex, 2, wbsEntry.getName());
            WorkbookUtils.setCellValue(sheet, rowIndex, 3, wbsEntry.getEntitySubType());
            WorkbookUtils.setCellValue(sheet, rowIndex, 4, wbsEntry.getStartAt());
            WorkbookUtils.setCellValue(sheet, rowIndex, 5, wbsEntry.getFinishAt());
            WorkbookUtils.setCellValue(sheet, rowIndex, 6, wbsEntry.getDuration());
            WorkbookUtils.setCellValue(sheet, rowIndex, 7, wbsEntryState.getStartedAt());
            WorkbookUtils.setCellValue(sheet, rowIndex, 8, wbsEntryState.getFinishedAt());
            WorkbookUtils.setCellValue(sheet, rowIndex, 9,wbsEntryState.getProgress() + "%");
            WorkbookUtils.setCellValue(sheet, rowIndex, 10, wbsEntry.getActive() ? "Active" : "Inactive");
            WorkbookUtils.setCellValue(sheet, rowIndex, 11, wbsEntryState.getRunningStatus());
            rowIndex++;
        }

        // 保存为临时文件
        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
        } catch (IOException ioe) {
            throw new BusinessError(); // TODO
        }

        return new File(excel.getAbsolutePath());
    }

}
