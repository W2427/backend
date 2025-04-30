package com.ose.tasks.domain.model.service.delegate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.SubDrawing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.report.api.DocumentTransmittalRecordAPI;
import com.ose.report.dto.DocumentTransmittalItemDTO;
import com.ose.report.dto.DocumentTransmittalRecordDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmEntityDocsMaterialsRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingDetailRepository;
import com.ose.tasks.domain.model.repository.drawing.SubDrawingRepository;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.entity.bpm.BpmEntityDocsMaterials;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.vo.bpm.ActInstDocType;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.QrcodePrefixType;

/**
 * 用户服务。
 */
@Component
public class TransmittalReportDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final DrawingRepository drawingRepository;

    private final DrawingDetailRepository drawingDetailRepository;

    private final SubDrawingRepository subDrawingRepository;

    private final BpmEntityDocsMaterialsRepository docsMaterialsRepository;

    private final ProjectInterface projectService;

    private final DocumentTransmittalRecordAPI documentTransmittalRecordAPI;

    /**
     * 构造方法。
     */
    @Autowired
    public TransmittalReportDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                                     DrawingRepository drawingRepository,
                                     DrawingDetailRepository drawingDetailRepository,
                                     SubDrawingRepository subDrawingRepository,
                                     BpmEntityDocsMaterialsRepository docsMaterialsRepository,
                                     @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") DocumentTransmittalRecordAPI documentTransmittalRecordAPI,
                                     ProjectInterface projectService,
                                     StringRedisTemplate stringRedisTemplate,
                                     BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                                     BpmRuTaskRepository ruTaskRepository
    ) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.bpmActInstRepository = bpmActInstRepository;
        this.drawingRepository = drawingRepository;
        this.drawingDetailRepository = drawingDetailRepository;
        this.subDrawingRepository = subDrawingRepository;
        this.projectService = projectService;
        this.docsMaterialsRepository = docsMaterialsRepository;
        this.documentTransmittalRecordAPI = documentTransmittalRecordAPI;
    }


    @Override
    public ExecResultDTO preExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {

        OperatorDTO operatorDTO = (OperatorDTO) data.get("operator");

        Long orgId = (Long) data.get("orgId");

        Long projectId = (Long) data.get("projectId");





        List<BpmRuTask> bpmRuTasks = (List<BpmRuTask>) data.get("ruTasks");

        final Project project = projectService.get(orgId, projectId);

        for (BpmRuTask actTask : bpmRuTasks) {
            Optional<BpmActivityInstanceBase> opActInst = bpmActInstRepository.findById(actTask.getActInstId());
            if (opActInst.isPresent()) {
                BpmActivityInstanceBase actInst = opActInst.get();
                String dwgNo = actInst.getEntityNo();
                Optional<Drawing> opDwg = drawingRepository.findByOrgIdAndProjectIdAndDwgNoAndStatus(orgId, projectId, dwgNo, EntityStatus.ACTIVE);
                if (opDwg.isPresent()) {
                    Drawing dwg = opDwg.get();

                    DocumentTransmittalRecordDTO reportDTO = new DocumentTransmittalRecordDTO();
                    reportDTO.setProjectName(project.getName());
                    reportDTO.setReportName(dwgNo + "-T-" + dwg.getLatestRev());
                    reportDTO.setReportQrCode(QrcodePrefixType.DRAWING.getCode() + StringUtils.generateShortUuid());

                    Optional<DrawingDetail> opDetail = drawingDetailRepository.findByDrawingIdAndRevAndStatus(
                        dwg.getId(), dwg.getLatestRev(), EntityStatus.ACTIVE);
                    if (opDetail.isPresent()) {
                        DrawingDetail detail = opDetail.get();

                        List<SubDrawing> subList = subDrawingRepository.findByDrawingIdAndStatusAndDrawingDetailId(
                            detail.getDrawingId(), EntityStatus.ACTIVE, detail.getId());

                        List<DocumentTransmittalItemDTO> items = new ArrayList<>();
                        for (SubDrawing sub : subList) {
                            DocumentTransmittalItemDTO item = new DocumentTransmittalItemDTO();
                            item.setDocumentNo(sub.getSubNo());
                            item.setDocumentDesc(sub.getSubDrawingNo());
                            item.setRevision(sub.getSubDrawingVersion());
                            item.setRemark(sub.getComment());
                            items.add(item);
                        }

                        reportDTO.setItems(items);

                        JsonObjectResponseBody<ReportHistory> reponse = documentTransmittalRecordAPI.generateDocumentTransmittalRecord(orgId, projectId, reportDTO);
                        ReportHistory his = reponse.getData();

                        List<ActReportDTO> docs = new ArrayList<>();
                        ActReportDTO doc = new ActReportDTO();
                        doc.setFileId(his.getFileId());
                        doc.setFilePath(his.getFilePath());
                        doc.setReportQrCode(his.getReportQrCode());
                        doc.setReportNo(his.getReportNo());
                        docs.add(doc);

                        BpmEntityDocsMaterials bpmDoc = new BpmEntityDocsMaterials();
                        bpmDoc.setProjectId(actInst.getProjectId());
                        bpmDoc.setCreatedAt();
                        bpmDoc.setProcessId(actInst.getProcessId());
                        bpmDoc.setEntityNo(actInst.getEntityNo());
                        bpmDoc.setEntityId(actInst.getEntityId());
                        bpmDoc.setActInstanceId(actInst.getId());
                        bpmDoc.setStatus(EntityStatus.ACTIVE);
                        bpmDoc.setType(ActInstDocType.TRANSMITTAL_REPORT);
                        bpmDoc.setOperator(operatorDTO.getId());
                        bpmDoc.setJsonDocs(docs);
                        docsMaterialsRepository.save(bpmDoc);
                    }

                }
            }
        }

        return execResult;
    }


}
