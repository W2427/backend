package com.ose.tasks.domain.model.service.deliverydoc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.ose.tasks.vo.bpm.BpmCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;

import com.ose.docs.api.FileFeignAPI;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.report.api.InspectionReportContentAPI;
import com.ose.tasks.domain.model.repository.bpm.QCReportRepository;
import com.ose.tasks.domain.model.repository.deliverydoc.DeliveryDocumentRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRepository;
import com.ose.tasks.domain.model.service.ProjectService;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.deliverydoc.DeliveryDocModulesDTO;
import com.ose.tasks.dto.deliverydoc.GenerateDocDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.deliverydoc.DeliveryDocument;
import com.ose.tasks.entity.wbs.entry.WBSEntry;
import com.ose.tasks.vo.wbs.WBSEntryType;
import com.ose.util.FileUtils;
import com.ose.vo.EntityStatus;

/**
 * 文档包服务。
 */
@Component
public class DeliveryDocService implements DeliveryDocInterface {
    private final static Logger logger = LoggerFactory.getLogger(DeliveryDocService.class);


    @Value("${application.files.temporary}")
    private String temporaryDir;

    @Value("${application.files.protected}")
    private String protectedDir;


    private final DeliveryDocumentRepository deliveryDocumentRepository;
    private final QCReportRepository qcReportRepository;
    private final WBSEntryRepository wbsEntryRepository;

    private final ProjectService projectService;

    private final UploadFeignAPI uploadFeignAPI;
    private final FileFeignAPI fileFeignAPI;
    private final InspectionReportContentAPI inspectionReportContentAPI;

    /**
     * 构造方法。
     */
    @Autowired
    public DeliveryDocService(
        DeliveryDocumentRepository deliveryDocumentRepository,
        QCReportRepository qcReportRepository,
        WBSEntryRepository wbsEntryRepository,
        ProjectService projectService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") FileFeignAPI fileFeignAPI,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") InspectionReportContentAPI inspectionReportContentAPI
    ) {
        this.deliveryDocumentRepository = deliveryDocumentRepository;
        this.qcReportRepository = qcReportRepository;
        this.wbsEntryRepository = wbsEntryRepository;
        this.projectService = projectService;
        this.uploadFeignAPI = uploadFeignAPI;
        this.fileFeignAPI = fileFeignAPI;
        this.inspectionReportContentAPI = inspectionReportContentAPI;
    }

    /**
     * 生成指定模块工序文档包
     */
    @Override
    public void generateDoc(OperatorDTO operatorDTO, Long orgId, Long projectId, GenerateDocDTO dto, RequestAttributes attributes) {
        final DeliveryDocument deliverydoc = getDeliveryDocument(operatorDTO, orgId, projectId, dto);

        Double rate = getWbsEntryProgress(orgId, projectId, dto);
        if (rate == null) {
            throw new BusinessError("not found work wbs_entry");
        } else {
            if (rate != 1)
                throw new BusinessError("wbs_entry not finished");
        }
        generateDocument(operatorDTO, orgId, projectId, deliverydoc);

    }

    /**
     * 获取三级计划进度
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param dto
     */
    private Double getWbsEntryProgress(Long orgId, Long projectId, GenerateDocDTO dto) {
        List<WBSEntry> wbsEntries = wbsEntryRepository
            .findByOrgIdAndProjectIdAndProcessAndSectorAndTypeAndDeletedIsFalse(
                orgId,
                projectId,
                dto.getProcess(),
                dto.getModule(),
                WBSEntryType.WORK
            );
        if (!wbsEntries.isEmpty()) {
            Double totalCount = (double) 0;
            Double approvedCount = (double) 0;

            for (WBSEntry wbsEntry : wbsEntries) {
                Long total = wbsEntryRepository
                    .findCountByOrgIdAndProjectIdAndParentIdAndSectorAndProcessAndDeletedIsFalse(
                        projectId,
                        wbsEntry.getId(),
                        dto.getModule(),
                        dto.getProcess()
                    );
                totalCount += total;

                Long approved = wbsEntryRepository
                    .findApprovedCountByOrgIdAndProjectIdAndParentIdAndSectorAndProcessAndDeletedIsFalse(
                        projectId,
                        wbsEntry.getId(),
                        dto.getModule(),
                        dto.getProcess()
                    );
                approvedCount += approved;
            }

            if (totalCount == 0)
                return null;

            return approvedCount / totalCount;
        } else {
            if (BpmCode.MATERIAL.equals(dto.getModule())
                || BpmCode.PUNCHLIST.equals(dto.getModule())) {
                return (double) 1;
            }
        }
        return null;
    }

    /**
     * 获取文档包实体
     *
     * @param operatorDTO
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param dto
     * @return
     */
    private DeliveryDocument getDeliveryDocument(
        OperatorDTO operatorDTO,
        Long orgId,
        Long projectId,
        GenerateDocDTO dto
    ) {
        DeliveryDocument deliveryDoc = deliveryDocumentRepository
            .findByProjectIdAndModuleAndProcessAndTypeAndDeleted(
                projectId,
                dto.getModule(),
                dto.getProcess(),
                dto.getType(),
                false
            );
        if (deliveryDoc == null) {
            deliveryDoc = new DeliveryDocument();
            deliveryDoc.setCreatedAt();
            deliveryDoc.setCreatedBy(operatorDTO.getId());
            deliveryDoc.setDeleted(false);
            deliveryDoc.setGenerateFlag(false);
            deliveryDoc.setLastModifiedAt();
            deliveryDoc.setLastModifiedBy(operatorDTO.getId());
            deliveryDoc.setModule(dto.getModule());
            deliveryDoc.setProcess(dto.getProcess());
            deliveryDoc.setProjectId(projectId);
            deliveryDoc.setStatus(EntityStatus.ACTIVE);
            deliveryDoc.setType(dto.getType());
            deliveryDoc.setVersion(deliveryDoc.getLastModifiedAt().getTime());
            deliveryDocumentRepository.save(deliveryDoc);
        }
        return deliveryDoc;
    }

    /**
     * 生成文档包
     *
     * @param operatorDTO
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param deliveryDoc
     */

    private void generateDocument(
        OperatorDTO operatorDTO,
        Long orgId,
        Long projectId,
        DeliveryDocument deliveryDoc
    ) {

        return;
    }

    /**
     * 生成后缀序号
     *
     * @param
     * @return
     */
    private String generateSeriesNo(int index) {
        String noStr = "";
        String indexStr = "" + index;
        if (indexStr.length() < 3) {
            for (int i = 0; i < 3 - indexStr.length(); i++) {
                noStr += "0";
            }
        }
        return noStr + indexStr;
    }

    @Override
    public void generateDoc(OperatorDTO operatorDTO, Long orgId, Long projectId, String module, RequestAttributes attributes) {
        final Project project = projectService.get(orgId, projectId);

    }

    @Override
    public void generateDoc(OperatorDTO operatorDTO, Long orgId, Long projectId, RequestAttributes attributes) {
        List<DeliveryDocModulesDTO> modules = getModules(orgId, projectId, null);
        for (DeliveryDocModulesDTO moduleDTO : modules) {
            generateDoc(
                operatorDTO,
                orgId,
                projectId,
                moduleDTO.getModule(),
                attributes
            );
        }
    }

    /**
     * 查询模块列表
     */
    @Override
    public List<DeliveryDocModulesDTO> getModules(Long orgId, Long projectId, String keyword) {
        if (keyword != null
            && !"".equals(keyword)) {
            List<DeliveryDocModulesDTO> result = wbsEntryRepository.getModulesByOrgIdAndProjectIdAndSectorAndDeletedIsFalse(orgId, projectId, "%" + keyword + "%");
            if (BpmCode.PUNCHLIST.contains(keyword)) {
                result.add(0, new DeliveryDocModulesDTO(BpmCode.PUNCHLIST));
            }
            if (BpmCode.MATERIAL.contains(keyword)) {
                result.add(0, new DeliveryDocModulesDTO(BpmCode.MATERIAL));
            }
            return result;
        }
        List<DeliveryDocModulesDTO> result = wbsEntryRepository.getModulesByOrgIdAndProjectIdAndDeletedIsFalse(orgId, projectId);
        result.add(0, new DeliveryDocModulesDTO(BpmCode.PUNCHLIST));
        result.add(0, new DeliveryDocModulesDTO(BpmCode.MATERIAL));
        return result;
    }

    /**
     * 查询模块文档包
     */
    @Override
    public List<DeliveryDocument> getDeliveryDocs(OperatorDTO operatorDTO, Long orgId, Long projectId, String module) {

        final Project project = projectService.get(orgId, projectId);


        return null;
    }

    @Override
    public File getModuleZipFile(OperatorDTO operatorDTO, Long orgId, Long projectId, String module) throws IOException {

        final Project project = projectService.get(orgId, projectId);

        List<DeliveryDocument> list = this.getDeliveryDocs(operatorDTO, orgId, projectId, module);

        List<Map<String, String>> filePathList = new ArrayList<>();

        for (DeliveryDocument dd : list) {
            if (dd.getJsonDocumentsReadOnly() != null
                && !dd.getJsonDocumentsReadOnly().isEmpty()) {
                String fileName = new StringBuffer("").append(project.getName())
                    .append(BpmCode.SEPARATOR_MID_BAR_LINE)
                    .append("MC")
                    .append(BpmCode.SEPARATOR_MID_BAR_LINE)
                    .append(dd.getModule())
                    .append(BpmCode.SEPARATOR_MID_BAR_LINE)
                    .append(dd.getProcess())
                    .append(BpmCode.SEPARATOR_MID_BAR_LINE)
                    .toString();

                if (dd.getType() != null
                    && !"".equals(dd.getType())) {
                    fileName += dd.getType() + BpmCode.SEPARATOR_MID_BAR_LINE;
                }

                for (ActReportDTO file : dd.getJsonDocumentsReadOnly()) {
                    String seriesNo = file.getSeriesNo();
                    Map<String, String> map = new HashMap<>();
                    map.put(fileName + seriesNo, protectedDir + file.getFilePath().substring(1));
                    filePathList.add(map);
                }
            }
        }

        if (filePathList.isEmpty()) {
            throw new BusinessError("Module:" + module + " have no delivery file yet.");
        }

        String fileName = new StringBuffer("").append(project.getName())
            .append(BpmCode.SEPARATOR_MID_BAR_LINE)
            .append("MC")
            .append(BpmCode.SEPARATOR_MID_BAR_LINE)
            .append(module)
            .toString();

        String zipFilePath = temporaryDir + fileName + ".zip";

        File zipFile = new File(zipFilePath);
        FileOutputStream fos = new FileOutputStream(zipFile);
        CheckedOutputStream cos = new CheckedOutputStream(fos, new CRC32());
        ZipOutputStream zos = new ZipOutputStream(cos);
        for (Map<String, String> map : filePathList) {
            String[] srcName = map.keySet().toArray(new String[0]);
            File src = new File(map.get(srcName[0]));
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
            ZipEntry entry = new ZipEntry(srcName[0] + FileUtils.extname(src));
            zos.putNextEntry(entry);
            int count;
            byte[] buf = new byte[1024];
            while ((count = bis.read(buf)) != -1) {
                zos.write(buf, 0, count);
            }
            bis.close();
        }
        zos.close();

        return zipFile;
    }

}
