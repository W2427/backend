package com.ose.tasks.controller.bpm.drawing;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FileMetadataDTO;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.BaseDTO;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.drawing.DrawingListAPI;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessVersionRuleRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmReDeploymentRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingDetailRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.ActivityTaskService;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.drawing.*;
import com.ose.tasks.dto.bpm.DiagramResourceDTO;
import com.ose.tasks.dto.bpm.TaskPrivilegeDTO;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmProcessVersionRule;
import com.ose.tasks.entity.bpm.BpmReDeployment;
import com.ose.tasks.entity.drawing.*;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import com.ose.tasks.vo.drawing.RuleType;
import com.ose.util.FileUtils;
import com.ose.util.LongUtils;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ose.tasks.vo.bpm.BpmCode.SD_DWG_PIPE_FABRICATION;
import static com.ose.tasks.vo.bpm.BpmCode.SD_DWG_PIPE_SUPPORT_FABRICATION;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "管道生产设计图纸清单接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/drawing-list")
public class DrawingController extends BaseController implements DrawingListAPI {
    private final static Logger logger = LoggerFactory.getLogger(DrawingController.class);
    private static String FILE_TYPE_ZIP = "zip";
    private static String FILE_TYPE_PDF = "pdf";

    private final DrawingTaskInterface drawingTaskService;


    private final DrawingInterface drawingService;

    private final UploadFeignAPI uploadFeignAPI;

    private final ProjectInterface projectService;

    private final DrawingRedMarkInterface drawingRedMarkService;

    private final DrawingHistoryInterface drawingHistoryService;

    private final DrawingBaseInterface drawingBaseService;

    private final DrawingRepository drawingRepository;
    private final BpmReDeploymentRepository bpmReDeploymentRepository;

    private final ActivityTaskService activityTaskService;

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final DrawingDetailRepository drawingDetailRepository;

    private final EntitySubTypeInterface entitySubTypeService;

    private final BpmProcessVersionRuleRepository versionRuleRepository;

    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;


    @Value("${application.files.public}")
    private String publicDir;


    /**
     * 构造方法
     */
    @Autowired
    public DrawingController(DrawingInterface drawingService,
                             @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI,
                             ProjectInterface projectService,
                             DrawingTaskInterface drawingTaskService,
                             DrawingRedMarkInterface drawingRedMarkService,
                             DrawingHistoryInterface drawingHistoryService,
                             DrawingBaseInterface drawingBaseService,
                             DrawingRepository drawingRepository,
                             BpmReDeploymentRepository bpmReDeploymentRepository,
                             ActivityTaskService activityTaskService,
                             BpmActivityInstanceRepository bpmActInstRepository,
                             BpmProcessVersionRuleRepository versionRuleRepository,
                             DrawingDetailRepository drawingDetailRepository, EntitySubTypeInterface entitySubTypeService) {
        this.drawingService = drawingService;
        this.uploadFeignAPI = uploadFeignAPI;
        this.projectService = projectService;
        this.drawingTaskService = drawingTaskService;

        this.drawingRedMarkService = drawingRedMarkService;
        this.drawingHistoryService = drawingHistoryService;
        this.drawingBaseService = drawingBaseService;
        this.drawingRepository = drawingRepository;
        this.bpmReDeploymentRepository = bpmReDeploymentRepository;
        this.activityTaskService = activityTaskService;
        this.bpmActInstRepository = bpmActInstRepository;
        this.drawingDetailRepository = drawingDetailRepository;
        this.entitySubTypeService = entitySubTypeService;
        this.versionRuleRepository = versionRuleRepository;
    }

    @Override
    @Operation(summary = "创建piping图纸清单条目", description = "创建piping生产设计图纸清单条目")
    @RequestMapping(method = POST, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<Drawing> create(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                  @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                  @RequestBody DrawingDTO pipingDTO) {

        Long userid = getContext().getOperator().getId();

        Drawing dl = drawingService.findByDwgNo(orgId, projectId, pipingDTO.getDwgNo());

        if (dl != null) {
            throw new ValidationError("There is a same DwgNo");
        }
        Drawing d = drawingService.create(orgId, projectId, userid, pipingDTO);

        if (pipingDTO.getCoverTempName() != null) {
            logger.error("图纸controller 保存docs服务->开始");
            JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                pipingDTO.getCoverTempName(), new FilePostDTO());
            logger.error("图纸controller 保存docs服务->结束");
            FileES f = responseBody.getData();
            d.setCoverId(LongUtils.parseLong(f.getId()));
            d.setCoverName(f.getName());
            d.setCoverPath(f.getPath());
            drawingService.save(d);
        }
        /*DrawingTaskDTO dto = new DrawingTaskDTO();
        dto.setDwgNo(d.getDwgNo());
        dto.setAssignee(pipingDTO.getAssigneeId());
        dto.setAssigneeName(pipingDTO.getAssigneeName());
        dto.setEntityId(d.getId());
        dto.setDrawingCategory(d.getDrawingCategory().getNameCn());
        dto.setDrawingCategoryId(d.getDrawingCategory().getId());
        dto.setDrawingCategoryType(d.getDrawingCategory().getEntityType().getNameCn());
        dto.setDrawingCategoryTypeId(d.getDrawingCategory().getEntityType().getId());
        createDrawingTask(orgId, projectId, dto);*/

        return new JsonObjectResponseBody<>(getContext(), d);
    }


    @Override
    @Operation(summary = "创建图纸详情", description = "创建设计图详情")
    @RequestMapping(method = POST, value = "/drawing-detail", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<DrawingDetail> createDetail(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                              @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                              @RequestBody DrawingDetailCriteriaDTO detailDTO) {

        Long userid = getContext().getOperator().getId();

        Drawing dl = drawingRepository.findById(detailDTO.getDrawingId()).orElse(null);
        if (dl == null) throw new NotFoundError("Drawing not found");
        String rev = detailDTO.getRevNo();
        DrawingDetail detail = new DrawingDetail();
        List<DrawingDetail> details = drawingDetailRepository.findByDrawingIdAndRevNoAndStatus(dl.getId(), rev, EntityStatus.PENDING);
        if (!details.isEmpty() && details.size() > 0) {
            throw new BusinessError("Drawing detail already exists");
        } else if (!details.isEmpty()) {
            detail = details.get(0);
        }
        //版次校验
//        if (!verificationVersion(projectId, detailDTO)) {
//            throw new BusinessError("Version filling error!");
//        }
        //new版本校验
        verificationVersion(orgId, projectId, detailDTO);


        // 查询目前图纸进行中的流程，将流程ID存于DrawingDetail中，方便区分
        List<BpmActivityInstanceBase> bais = bpmActInstRepository.findByProjectIdAndEntityIdAndProcessAndFinishStateAndSuspensionState(
            projectId,
            dl.getId(),
            detailDTO.getProcess(),
            ActInstFinishState.NOT_FINISHED,
            SuspensionState.ACTIVE
        );
        if (bais.size() > 0) {
            detail.setActInsId(bais.get(0).getId());
            bais.get(0).setVersion(rev);
            bpmActInstRepository.save(bais.get(0));
        }
        detail.setStatus(EntityStatus.PENDING);
        detail.setLastModifiedAt();
        detail.setCreatedAt();
        detail.setOrgId(orgId);
        detail.setProjectId(projectId);
        detail.setDrawingId(dl.getId());
        detail.setProgressStage(null);
        detail.setRevNo(rev);
        detail.setUploadDate(null);
        detail.setOutgoingTransmittal(null);
        detail.setIncomingTransmittal(null);
        detail.setReplyDate(null);
        detail.setReplyStatus(null);
        drawingDetailRepository.save(detail);

        dl.setLatestRev(rev);
        drawingService.save(dl);

        return new JsonObjectResponseBody<>(getContext(), detail);
    }

    private void verificationVersion(Long orgId, Long projectId, DrawingDetailCriteriaDTO detailDTO) {
        //1.查询是否有对应的版本规则
        BpmProcessVersionRule versionRule = versionRuleRepository.findByProcessName(orgId, projectId, detailDTO.getProcess());
        if (versionRule != null) {
            RuleType ruleType = versionRule.getRuleType();
            switch (ruleType) {
                case FIXED_CHARACTER:
                    String expectedVersion1 = "";
                    if (!detailDTO.getRevNo().equals(versionRule.getFixedCharacter())) {
                        if (versionRule.getFixedPrefix() != null) {
                            expectedVersion1 = versionRule.getFixedPrefix() + versionRule.getFixedCharacter();
                        } else {
                            expectedVersion1 = versionRule.getFixedCharacter();
                        }
                        throw new BusinessError("Version filling error! Should be: " + expectedVersion1 + "(版本号填写错误!应填:" + expectedVersion1 + ")");
                    }
                    break;
                case INCREMENTING_NUMBERS:
                    Pattern pattern;
                    String expectedVersion = "";
                    if (versionRule.getFixedPrefix() != null) {
                        // 定义正则表达式，匹配以"IFR_"开头并跟随数字的字符串
                        pattern = Pattern.compile("^" + versionRule.getFixedPrefix() + "(\\d+)$");
                        expectedVersion += versionRule.getFixedPrefix();
                    } else {
                        pattern = Pattern.compile("^(\\d+)$");
                    }
                    Matcher matcher = pattern.matcher(detailDTO.getRevNo());

                    DrawingDetail detail = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (detail != null) {
                        //详情存在分两种情况，a.上一个详情和当前规则匹配b.上一个详情和当前规则不匹配
                        Matcher matcherLast = pattern.matcher(detail.getRevNo());
                        if (matcherLast.matches()) {
                            //上一个详情和当前规则匹配
                            //匹配当前填写版本号
                            if (matcher.matches()) {
                                int lastNumberPart = Integer.parseInt(matcherLast.group(1));
                                int numberPart = Integer.parseInt(matcher.group(1));
                                expectedVersion += String.valueOf(lastNumberPart + 1);
                                if (numberPart - lastNumberPart != 1) {
                                    throw new BusinessError("Version filling error! Should be: " + expectedVersion + "(版本号填写错误!应填:" + expectedVersion + ")");
                                }
                            } else {
                                expectedVersion += versionRule.getStartPoint();
                                throw new BusinessError("Version filling error! Should be: " + expectedVersion + "(版本号填写错误!应填:" + expectedVersion + ")");
                            }
                        } else {
                            //上一个详情和当前规则不匹配
                            expectedVersion += versionRule.getStartPoint();
                            //匹配当前填写版本号
                            if (!expectedVersion.equals(detailDTO.getRevNo())) {
                                throw new BusinessError("Version filling error! Should be: " + expectedVersion + "(版本号填写错误!应填:" + expectedVersion + ")");
                            }
//                            if (matcher.matches()) {
//                                int numberPart = Integer.parseInt(matcher.group(1));
//                                if (numberPart != Integer.parseInt(versionRule.getStartPoint())){
//                                    throw new BusinessError("Version filling error! Should be: " + expectedVersion + "(版本号填写错误!应填:" + expectedVersion + ")");
//                                }
//                            }else {
//                                throw new BusinessError("Version filling error! Should be: " + expectedVersion + "(版本号填写错误!应填:" + expectedVersion + ")");
//                            }
                        }

                    } else {
                        //没有版本记录只需要验证是否相等
                        expectedVersion += versionRule.getStartPoint();
                        //匹配失败
                        if (!expectedVersion.equals(detailDTO.getRevNo())) {
                            throw new BusinessError("Version filling error! Should be: " + expectedVersion + "(版本号填写错误!应填:" + expectedVersion + ")");
                        }
                    }
                    break;
                case INCREMENTING_LETTERS:
                    Pattern pattern3;
                    String expectedVersion3 = "";
                    if (versionRule.getFixedPrefix() != null) {
                        // 定义正则表达式，匹配以"IFR_"开头并且带有字母的字符串
                        pattern3 = Pattern.compile("^" + versionRule.getFixedPrefix() + "([a-zA-Z]+)$");
                        expectedVersion3 += versionRule.getFixedPrefix();
                    } else {
                        pattern3 = Pattern.compile("^([a-zA-Z]+)$");
                    }
                    Matcher matcher3 = pattern3.matcher(detailDTO.getRevNo());

                    DrawingDetail detail1 = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (detail1 != null) {
                        //详情存在分两种情况，a.上一个详情和当前规则匹配b.上一个详情和当前规则不匹配
                        Matcher matcherLast = pattern3.matcher(detail1.getRevNo());
                        if (matcherLast.matches()) {
                            //上一个详情和当前规则匹配
                            //匹配当前填写版本号
                            char lastCharPart = matcherLast.group(1).charAt(0);
                            expectedVersion3 += (char) (lastCharPart + 1);
                            if (matcher3.matches()) {
                                char charPart = matcher3.group(1).charAt(0);
                                if (charPart - lastCharPart != 1) {
                                    throw new BusinessError("Version filling error! Should be: " + expectedVersion3 + "(版本号填写错误!应填:" + expectedVersion3 + ")");
                                }
                            } else {
                                throw new BusinessError("Version filling error! Should be: " + expectedVersion3 + "(版本号填写错误!应填:" + expectedVersion3 + ")");
                            }
                        } else {
                            expectedVersion3 += versionRule.getStartPoint();
                            //上一个详情和当前规则不匹配
                            if (!expectedVersion3.equals(detailDTO.getRevNo())) {
                                throw new BusinessError("Version filling error! Should be: " + expectedVersion3 + "(版本号填写错误!应填:" + expectedVersion3 + ")");
                            }
                            //匹配当前填写版本号
//                            if (matcher3.matches()) {
//                                int numberPart = Integer.parseInt(matcher3.group(1));
//                                if (numberPart != Integer.parseInt(versionRule.getStartPoint())){
//                                    throw new BusinessError("Version filling error! Should be: " + expectedVersion3 + "(版本号填写错误!应填:" + expectedVersion3 + ")");
//                                }
//                            }else {
//                                throw new BusinessError("Version filling error! Should be: " + expectedVersion3 + "(版本号填写错误!应填:" + expectedVersion3 + ")");
//                            }
                        }
                    } else {
                        //没有版本记录只需要验证前缀部分
                        expectedVersion3 += versionRule.getStartPoint();
                        //匹配失败
                        if (!expectedVersion3.equals(detailDTO.getRevNo())) {
                            throw new BusinessError("Version filling error! Should be: " + expectedVersion3 + "(版本号填写错误!应填:" + expectedVersion3 + ")");
                        }
                    }
                    break;
                default:
                    break;

            }
        }
    }

    private boolean verificationVersion(Long projectId, DrawingDetailCriteriaDTO detailDTO) {
        //T86项目校验规则：IFR校验A，IFR_U校验BCD...，IFA校验00-1，IFA_U校验00-2,01-1,02-1...
        if (projectId == 1712020232861165496L) {
            switch (detailDTO.getProcess()) {
                case "IFR":
                    if (!detailDTO.getRevNo().equals("A")) return false;
                    break;
                case "IFR_U":
                    //查询数据库
                    DrawingDetail latestDetail = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (latestDetail != null) {
                        String latestRevNo = latestDetail.getRevNo();
                        if (!latestRevNo.equals("A")) {
                            return detailDTO.getRevNo().equals("B");
                        }
                        char nextRevChar = (char) (latestRevNo.charAt(latestRevNo.length() - 1) + 1);
                        char nowRevChar = detailDTO.getRevNo().charAt(0);
                        if (nowRevChar != nextRevChar) return false;
                    } else {
                        return detailDTO.getRevNo().equals("B");
                    }
                    break;
                case "IFA":
                    if (!detailDTO.getRevNo().equals("00-1")) return false;
                    break;
                case "IFA_U":
                    //查询数据库
                    DrawingDetail latestDetail1 = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (latestDetail1 != null) {
                        // 分割字符串以比较数字部分
                        String[] currentParts = detailDTO.getRevNo().split("-");
                        String[] previousParts = latestDetail1.getRevNo().split("-");

                        if (previousParts.length != 2) {
                            return true;
                        }

                        if (currentParts.length != 2) {
                            return false;
                        }

                        int currentSection = Integer.parseInt(currentParts[0]);
                        int previousSection = Integer.parseInt(previousParts[0]);
                        int currentItem = Integer.parseInt(currentParts[1]);
                        int previousItem = Integer.parseInt(previousParts[1]);
                        if (currentSection == previousSection) {
                            return currentItem - previousItem == 1;
                        } else if (currentSection - previousSection == 1) {
                            return currentItem == 1;
                        } else {
                            return false;
                        }
                    } else {
                        return true;
                    }

            }
        } else if (projectId == 1711935049020859001L) {
            //P308项目校验规则：IFA校验A，IFA_U校验A1 A2...，IFF校验00，IFF_U、IFC、IFC_U校验01，02，03...
            switch (detailDTO.getProcess()) {
                case "IFA":
                    if (!detailDTO.getRevNo().equals("FA1")) return false;
                    break;
                case "IFA_U":
                    //查询数据库
                    DrawingDetail latestDetail = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (latestDetail != null) {
                        // 分割字符串以比较数字部分
                        String[] currentParts = detailDTO.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);
                        String[] previousParts = latestDetail.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);

                        if (currentParts.length != 2) {
                            return false;
                        }

                        String currentSection = currentParts[0];
                        if (!currentSection.equals("FA")) return false;
                        int currentItem = Integer.parseInt(currentParts[1]);
                        int previousItem = Integer.parseInt(previousParts[1]);

                        return currentItem - previousItem == 1;
                    } else {
                        return false;
                    }
                case "IFF":
                    if (!detailDTO.getRevNo().equals("FF1")) return false;
                    break;
                case "IFF_U":
                    //查询数据库
                    DrawingDetail latestDetailf = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (latestDetailf != null) {
                        // 分割字符串以比较数字部分
                        String[] currentParts = detailDTO.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);
                        String[] previousParts = latestDetailf.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);

                        if (currentParts.length != 2) {
                            return false;
                        }

                        String currentSection = currentParts[0];
                        if (!currentSection.equals("FF")) return false;
                        int currentItem = Integer.parseInt(currentParts[1]);
                        int previousItem = Integer.parseInt(previousParts[1]);

                        return currentItem - previousItem == 1;
                    } else {
                        return false;
                    }
                case "IFC":
                    if (!detailDTO.getRevNo().equals("FC1")) return false;
                    break;
                case "IFC_U":
                    //查询数据库
                    DrawingDetail latestDetailc = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (latestDetailc != null) {
                        // 分割字符串以比较数字部分
                        String[] currentParts = detailDTO.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);
                        String[] previousParts = latestDetailc.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);

                        if (currentParts.length != 2) {
                            return false;
                        }

                        String currentSection = currentParts[0];
                        if (!currentSection.equals("FC")) return false;
                        int currentItem = Integer.parseInt(currentParts[1]);
                        int previousItem = Integer.parseInt(previousParts[1]);

                        return currentItem - previousItem == 1;
                    } else {
                        return false;
                    }
            }
        } else if (projectId == 1711507478264097392L) {
            //P376项目校验规则 IDC:IDC_A,IDC_B  IFR:IFR_RO0 IFR_RO1 IFR_RO2    IFA:IFA_ROO IFA_RO1 IFA_ROO    AFC:AFC_ROO AFC_RO1 AFC_RO2    IFI:IFI_ROO IFI_RO1 IFI_RO2
            switch (detailDTO.getProcess()) {
                case "IDC":
                    if (!detailDTO.getRevNo().equals("IDC_A"))
                        throw new BusinessError("Invalid revision number: expected 'IDC_A', but got '" + detailDTO.getRevNo() + "'./n" +
                            "无效的版本号：应填'IDC_A'，但填了'" + detailDTO.getRevNo() + "'。");
                    break;
                case "IDC_U":
                    //查询数据库
                    DrawingDetail latestDetailIDC = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (latestDetailIDC != null) {
                        // 分割字符串以比较数字部分
                        String[] currentParts = detailDTO.getRevNo().split("_");
                        String[] previousParts = latestDetailIDC.getRevNo().split("_");

                        if (currentParts.length != 2) {
                            throw new BusinessError("Invalid revision number: expected 'IDC_?', but got '" + detailDTO.getRevNo() + "'./n" +
                                "无效的版本号：应填'IDC_?'，但填了'" + detailDTO.getRevNo() + "'。");
                        }
                        String currentSection = currentParts[0];
                        if (!currentSection.equals("IDC")) {
                            throw new BusinessError("Invalid revision number: expected 'IDC_?', but got '" + detailDTO.getRevNo() + "'./n" +
                                "无效的版本号：应填'IDC_?'，但填了'" + detailDTO.getRevNo() + "'。");
                        }
                        String latestRevNo = previousParts[1];
                        char nextRevChar = (char) (latestRevNo.charAt(latestRevNo.length() - 1) + 1);
                        char nowRevChar = currentParts[1].charAt(0);
                        return nowRevChar == nextRevChar;
                    }
                case "IFR":
                    if (!detailDTO.getRevNo().equals("IFR_RO0"))
                        throw new BusinessError("Invalid revision number: expected 'IFR_RO0', but got '" + detailDTO.getRevNo() + "'./n" +
                            "无效的版本号：应填'IFR_RO0'，但填了'" + detailDTO.getRevNo() + "'。");
                case "IFR_U":
                    //查询数据库
                    DrawingDetail latestDetail = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (latestDetail != null) {
                        // 分割字符串以比较数字部分
                        String[] currentParts = detailDTO.getRevNo().split("", 2);
                        String[] previousParts = latestDetail.getRevNo().split("", 2);
                        if (currentParts.length != 2) {
                            throw new BusinessError("Invalid revision number: expected 'IFR_RO1 IFR_RO2..', but got '" + detailDTO.getRevNo() + "'./n" +
                                "无效的版本号：应填'IFR_RO1 IFR_RO2..'，但填了'" + detailDTO.getRevNo() + "'。");
                        }
                        String currentSection = currentParts[0];
                        if (!currentSection.equals("IFR")) {
                            throw new BusinessError("Invalid revision number: expected 'IFR_RO1 IFR_RO2..', but got '" + detailDTO.getRevNo() + "'./n" +
                                "无效的版本号：应填'IFR_RO1 IFR_RO2..'，但填了'" + detailDTO.getRevNo() + "'。");
                        }
                        int currentItem = Integer.parseInt(currentParts[1]);
                        int previousItem = Integer.parseInt(previousParts[1]);

                        if (currentItem - previousItem == 1) {
                            return true;
                        } else {
                            throw new BusinessError("Invalid revision number: expected 'IFR_RO1 IFR_RO2..', but got '" + detailDTO.getRevNo() + "'./n" +
                                "无效的版本号：应填'IFR_RO1 IFR_RO2..'，但填了'" + detailDTO.getRevNo() + "'。");
                        }
                    }
                case "IFA":
                    if (!detailDTO.getRevNo().equals("IFA_ROO")) {
                        throw new BusinessError("Invalid revision number: expected 'IFA_ROO', but got '" + detailDTO.getRevNo() + "'./n" +
                            "无效的版本号：应填'IFA_ROO'，但填了'" + detailDTO.getRevNo() + "'。");
                    }
                    break;
                case "IFA_U":
                    DrawingDetail latestDetaila = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (latestDetaila != null) {
                        // 分割字符串以比较数字部分
                        String[] currentParts = detailDTO.getRevNo().split("", 2);
                        String[] previousParts = latestDetaila.getRevNo().split("", 2);
                        if (currentParts.length != 2) {
                            throw new BusinessError("Invalid revision number: expected 'IFA_RO1 IFA_RO2..', but got '" + detailDTO.getRevNo() + "'./n" +
                                "无效的版本号：应填'IFA_RO1 IFA_RO2..'，但填了'" + detailDTO.getRevNo() + "'。");
                        }
                        String currentSection = currentParts[0];
                        if (!currentSection.equals("IFA")) {
                            throw new BusinessError("Invalid revision number: expected 'IFA_RO1 IFA_RO2..', but got '" + detailDTO.getRevNo() + "'./n" +
                                "无效的版本号：应填'IFA_RO1 IFA_RO2..'，但填了'" + detailDTO.getRevNo() + "'。");
                        }
                        int currentItem = Integer.parseInt(currentParts[1]);
                        int previousItem = Integer.parseInt(previousParts[1]);

                        if (currentItem - previousItem == 1) {
                            return true;
                        } else {
                            throw new BusinessError("Invalid revision number: expected 'IFA_RO1 IFA_RO2..', but got '" + detailDTO.getRevNo() + "'./n" +
                                "无效的版本号：应填'IFA_RO1 IFA_RO2..'，但填了'" + detailDTO.getRevNo() + "'。");
                        }
                    }
                case "AFC":
                    if (!detailDTO.getRevNo().equals("AFC_ROO")) {
                        throw new BusinessError("Invalid revision number: expected 'AFC_ROO', but got '" + detailDTO.getRevNo() + "'./n" +
                            "无效的版本号：应填'AFC_ROO'，但填了'" + detailDTO.getRevNo() + "'。");
                    }
                    break;
                case "AFC_U":
                    DrawingDetail latestDetailC = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (latestDetailC != null) {
                        // 分割字符串以比较数字部分
                        String[] currentParts = detailDTO.getRevNo().split("_");
                        String[] previousParts = latestDetailC.getRevNo().split("_");

                        if (currentParts.length != 2) {
                            throw new BusinessError("Invalid revision number: expected 'AFC_RO1 AFC_RO2..', but got '" + detailDTO.getRevNo() + "'./n" +
                                "无效的版本号：应填'AFC_RO1 AFC_RO2..'，但填了'" + detailDTO.getRevNo() + "'。");
                        }
                        String currentSection = currentParts[0];
                        if (!currentSection.equals("AFC")) {
                            throw new BusinessError("Invalid revision number: expected 'AFC_RO1 AFC_RO2..', but got '" + detailDTO.getRevNo() + "'./n" +
                                "无效的版本号：应填'AFC_RO1 AFC_RO2..'，但填了'" + detailDTO.getRevNo() + "'。");
                        }

                        int currentItem = Integer.parseInt(currentParts[1]);
                        int previousItem = Integer.parseInt(previousParts[1]);

                        if (currentItem - previousItem == 1) {
                            return true;
                        } else {
                            throw new BusinessError("Invalid revision number: expected 'AFC_RO1 AFC_RO2..', but got '" + detailDTO.getRevNo() + "'./n" +
                                "无效的版本号：应填'AFC_RO1 AFC_RO2..'，但填了'" + detailDTO.getRevNo() + "'。");
                        }
                    }
                case "IFI":
                    if (!detailDTO.getRevNo().equals("IFI_00")) {
                        throw new BusinessError("Invalid revision number: expected 'IFI_00', but got '" + detailDTO.getRevNo() + "'./n" +
                            "无效的版本号：应填'IFI_00'，但填了'" + detailDTO.getRevNo() + "'。");
                    }
                    break;
                case "IFI_U":
                    DrawingDetail latestDetailI = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (latestDetailI != null) {
                        // 分割字符串以比较数字部分
                        String[] currentParts = detailDTO.getRevNo().split("_");
                        String[] previousParts = latestDetailI.getRevNo().split("_");

                        if (currentParts.length != 2) {
                            throw new BusinessError("Invalid revision number: expected 'IFI_RO1 IFI_RO2..', but got '" + detailDTO.getRevNo() + "'./n" +
                                "无效的版本号：应填'IFI_RO1 IFI_RO2..'，但填了'" + detailDTO.getRevNo() + "'。");
                        }
                        String currentSection = currentParts[0];
                        if (!currentSection.equals("IFI")) {
                            throw new BusinessError("Invalid revision number: expected 'IFI_RO1 IFI_RO2..', but got '" + detailDTO.getRevNo() + "'./n" +
                                "无效的版本号：应填'IFI_RO1 IFI_RO2..'，但填了'" + detailDTO.getRevNo() + "'。");
                        }

                        int currentItem = Integer.parseInt(currentParts[1]);
                        int previousItem = Integer.parseInt(previousParts[1]);


                        if (currentItem - previousItem == 1) {
                            return true;
                        } else {
                            throw new BusinessError("Invalid revision number: expected 'IFI_RO1 IFI_RO2..', but got '" + detailDTO.getRevNo() + "'./n" +
                                "无效的版本号：应填'IFI_RO1 IFI_RO2..'，但填了'" + detailDTO.getRevNo() + "'。");
                        }
                    }
            }
        } else if (projectId == 1716858941391813313L) {
            //P320LDV项目校验规则：IFR/IFI校验A1，_U校验A2 A3...，IFA校验B1，_U校验B2 B3...，AFD/AFC校验C1，_U校验C2 C3...，
            switch (detailDTO.getProcess()) {
                case "IFR":
                case "IFI":
                    if (!detailDTO.getRevNo().equals("A1")) return false;
                    break;
                case "IFR_U":
                case "IFI_U":
                    //查询数据库
                    DrawingDetail latestDetail = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (latestDetail != null) {
                        // 分割字符串以比较数字部分
                        String[] currentParts = detailDTO.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);
                        String[] previousParts = latestDetail.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);

                        if (currentParts.length != 2) {
                            return false;
                        }

                        String currentSection = currentParts[0];
                        if (!currentSection.equals("A")) return false;
                        int currentItem = Integer.parseInt(currentParts[1]);
                        int previousItem = Integer.parseInt(previousParts[1]);

                        return currentItem - previousItem == 1;
                    } else {
                        return detailDTO.getRevNo().equals("A2");
                    }
                case "IFA":
                    if (!detailDTO.getRevNo().equals("B1")) return false;
                    break;
                case "IFA_U":
                    //查询数据库
                    DrawingDetail latestDetailf = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (latestDetailf != null) {
                        // 分割字符串以比较数字部分
                        String[] currentParts = detailDTO.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);
                        String[] previousParts = latestDetailf.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);

                        if (currentParts.length != 2) {
                            return false;
                        }

                        String currentSection = currentParts[0];
                        if (!previousParts[0].equals("B")) return detailDTO.getRevNo().equals("B2");
                        if (!currentSection.equals("B")) return false;
                        int currentItem = Integer.parseInt(currentParts[1]);
                        int previousItem = Integer.parseInt(previousParts[1]);

                        return currentItem - previousItem == 1;
                    } else {
                        return detailDTO.getRevNo().equals("B2");
                    }
                case "AFC":
                case "AFD":
                    if (!detailDTO.getRevNo().equals("C1")) return false;
                    break;
                case "AFC_U":
                case "AFD_U":
                    //查询数据库
                    DrawingDetail latestDetailc = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (latestDetailc != null) {
                        // 分割字符串以比较数字部分
                        String[] currentParts = detailDTO.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);
                        String[] previousParts = latestDetailc.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);

                        if (currentParts.length != 2) {
                            return false;
                        }

                        String currentSection = currentParts[0];
                        if (!previousParts[0].equals("C")) return detailDTO.getRevNo().equals("C2");
                        if (!currentSection.equals("C")) return false;
                        int currentItem = Integer.parseInt(currentParts[1]);
                        int previousItem = Integer.parseInt(previousParts[1]);

                        return currentItem - previousItem == 1;
                    } else {
                        return detailDTO.getRevNo().equals("C2");
                    }
            }
        } else if (projectId == 1719795608571617912L) {
            //P366 Dulang FSO项目校验规则：IFR校验IFR_A，_U校验IFR_B IFR_C...，IFA校验IFA_A，_U校验IFA_B IFA_C...，AFC校验AFC_00，_U校验AFC_01 AFC_02...，

            switch (detailDTO.getProcess()) {
                case "IFR":
                    if (!detailDTO.getRevNo().equals("IFR_A")) return false;
                    break;
                case "IFR_U":
                    //查询数据库
                    DrawingDetail latestDetaild = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (latestDetaild != null) {
                        // 分割字符串以比较数字部分
                        String[] currentParts = detailDTO.getRevNo().split("_");
                        String[] previousParts = latestDetaild.getRevNo().split("_");

                        if (currentParts.length != 2) {
                            return false;
                        }
                        String currentSection = currentParts[0];
                        if (!currentSection.equals("IFR")) return false;
                        String latestRevNo = previousParts[1];
                        char nextRevChar = (char) (latestRevNo.charAt(latestRevNo.length() - 1) + 1);
                        char nowRevChar = currentParts[1].charAt(0);
                        return nowRevChar == nextRevChar;
                    } else {
                        return detailDTO.getRevNo().equals("IFR_B");
                    }
                case "IFA":
                    if (!detailDTO.getRevNo().equals("IFA_A") && !detailDTO.getRevNo().equals("IFA_B")) return false;
                    break;
                case "IFA_U":
                    //查询数据库
                    DrawingDetail latestDetaila = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (latestDetaila != null) {
                        // 分割字符串以比较数字部分
                        String[] currentParts = detailDTO.getRevNo().split("_");
                        String[] previousParts = latestDetaila.getRevNo().split("_");

                        if (currentParts.length != 2) {
                            return false;
                        }
                        String currentSection = currentParts[0];
                        if (!previousParts[0].equals("IFA")) return detailDTO.getRevNo().equals("IFA_B");
                        if (!currentSection.equals("IFA")) return false;
                        String latestRevNo = previousParts[1];
                        char nextRevChar = (char) (latestRevNo.charAt(latestRevNo.length() - 1) + 1);
                        char nowRevChar = currentParts[1].charAt(0);
                        return nowRevChar == nextRevChar;
                    } else {
                        return detailDTO.getRevNo().equals("IFA_B");
                    }
                case "AFC":
                    if (!detailDTO.getRevNo().equals("AFC_0")) return false;
                    break;
                case "AFC_U":
                    //查询数据库
                    DrawingDetail latestDetailc = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (latestDetailc != null) {
                        // 分割字符串以比较数字部分
                        String[] currentParts = detailDTO.getRevNo().split("_");
                        String[] previousParts = latestDetailc.getRevNo().split("_");

                        if (currentParts.length != 2) {
                            return false;
                        }
                        String currentSection = currentParts[0];

                        if (!previousParts[0].equals("AFC")) return detailDTO.getRevNo().equals("AFC_1");
                        if (!currentSection.equals("AFC")) return false;

                        int currentItem = Integer.parseInt(currentParts[1]);
                        int previousItem = Integer.parseInt(previousParts[1]);

                        return currentItem - previousItem == 1;
                    } else {
                        return detailDTO.getRevNo().equals("AFC_1");
                    }
            }
        } else if (projectId == 1726450247088780232L) {
            //P371 项目校验规则：IFR校验R00，IFU校验R01，_U校验R02 R03...，
            switch (detailDTO.getProcess()) {
                case "IDC":
                    if (!detailDTO.getRevNo().equals("A")) return false;
                    break;
                case "IFR":
                    if (!detailDTO.getRevNo().equals("R00")) return false;
                    break;
                case "IFR_U":
                case "IFU":
                case "IFU_U":
                    //查询数据库
                    DrawingDetail latestDetaila = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (latestDetaila != null) {
                        // 分割字符串以比较数字部分
                        String[] currentParts = detailDTO.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);
                        String[] previousParts = latestDetaila.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);

                        if (currentParts.length != 2) {
                            return false;
                        }
                        String currentSection = currentParts[0];
                        if (!previousParts[0].equals("R")) return detailDTO.getRevNo().equals("R01");
                        if (!currentSection.equals("R")) return false;
                        int currentItem = Integer.parseInt(currentParts[1]);
                        int previousItem = Integer.parseInt(previousParts[1]);

                        return currentItem - previousItem == 1;
                    } else {
                        String[] currentParts = detailDTO.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);
                        if (!currentParts[0].equals("R")) return false;
                    }
                    break;
            }
        } else if (projectId == 1735805741248414104l) {
            //P385 项目校验规则：IDC校验A，IFB校验0
            switch (detailDTO.getProcess()) {
                case "IDC":
                    if (!detailDTO.getRevNo().equals("A")) {
                        throw new BusinessError(
                            "Invalid revision number: expected 'A', but got '" + detailDTO.getRevNo() + "'." +
                                "无效的版本号：应填'A'，但填了'" + detailDTO.getRevNo() + "'。");
                    }
                    break;
                case "IFB":
                    if (!detailDTO.getRevNo().equals("0")) {
                        throw new BusinessError(
                            "Invalid revision number: expected '0', but got '" + detailDTO.getRevNo() + "'." +
                                "无效的版本号：应填'0'，但填了'" + detailDTO.getRevNo() + "'。");
                    }
                    break;
            }
        } else if (projectId == 1727574392039118817l) {
            //P378 EPC 项目校验规则：IFR校验A1，A2... IFI校验I1,I2....  AFC校验C1,C2...
            switch (detailDTO.getProcess()) {
                case "IFR":
                    if (!detailDTO.getRevNo().equals("A1")) {
                        throw new BusinessError(
                            "Invalid revision number: expected 'A1', but got '" + detailDTO.getRevNo() + "'." +
                                "无效的版本号：应填'A1'，但填了'" + detailDTO.getRevNo() + "'。");
                    }
                    break;
                case "IFR_U":
                    //查询数据库
                    DrawingDetail latestDetailr = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (latestDetailr != null) {
                        // 分割字符串以比较数字部分
                        String[] currentParts = detailDTO.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);
                        String[] previousParts = latestDetailr.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);

                        if (currentParts.length != 2) {
                            return false;
                        }
                        String currentSection = currentParts[0];
                        if (!previousParts[0].equals("A")) return detailDTO.getRevNo().equals("A2");
                        if (!currentSection.equals("A")) return false;
                        int currentItem = Integer.parseInt(currentParts[1]);
                        int previousItem = Integer.parseInt(previousParts[1]);

                        return currentItem - previousItem == 1;
                    } else {
                        String[] currentParts = detailDTO.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);
                        if (!currentParts[0].equals("A")) return false;
                    }
                    break;
                case "IFI":
                    if (!detailDTO.getRevNo().equals("I1")) {
                        throw new BusinessError(
                            "Invalid revision number: expected 'I1', but got '" + detailDTO.getRevNo() + "'." +
                                "无效的版本号：应填'I1'，但填了'" + detailDTO.getRevNo() + "'。");
                    }
                    break;
                case "IFI_U":
                    //查询数据库
                    DrawingDetail latestDetailI = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (latestDetailI != null) {
                        // 分割字符串以比较数字部分
                        String[] currentParts = detailDTO.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);
                        String[] previousParts = latestDetailI.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);

                        if (currentParts.length != 2) {
                            return false;
                        }
                        String currentSection = currentParts[0];
                        if (!previousParts[0].equals("I")) return detailDTO.getRevNo().equals("I2");
                        if (!currentSection.equals("I")) return false;
                        int currentItem = Integer.parseInt(currentParts[1]);
                        int previousItem = Integer.parseInt(previousParts[1]);

                        return currentItem - previousItem == 1;
                    } else {
                        String[] currentParts = detailDTO.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);
                        if (!currentParts[0].equals("I")) return false;
                    }
                    break;
                case "AFC":
                    if (!detailDTO.getRevNo().equals("C1")) {
                        throw new BusinessError(
                            "Invalid revision number: expected 'C1', but got '" + detailDTO.getRevNo() + "'." +
                                "无效的版本号：应填'C1'，但填了'" + detailDTO.getRevNo() + "'。");
                    }
                    break;
                case "AFC_U":
                    //查询数据库
                    DrawingDetail latestDetailC = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (latestDetailC != null) {
                        // 分割字符串以比较数字部分
                        String[] currentParts = detailDTO.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);
                        String[] previousParts = latestDetailC.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);

                        if (currentParts.length != 2) {
                            return false;
                        }
                        String currentSection = currentParts[0];
                        if (!previousParts[0].equals("C")) return detailDTO.getRevNo().equals("C2");
                        if (!currentSection.equals("C")) return false;
                        int currentItem = Integer.parseInt(currentParts[1]);
                        int previousItem = Integer.parseInt(previousParts[1]);

                        return currentItem - previousItem == 1;
                    } else {
                        String[] currentParts = detailDTO.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);
                        if (!currentParts[0].equals("C")) return false;
                    }
                    break;
            }
        } else if (projectId == 1730192442234180143l) {
            //P378CED项目版本校验规则:AFC  C1 C2 C3...
            switch (detailDTO.getProcess()) {
                case "AFC":
                    if (!detailDTO.getRevNo().equals("C1")) {
                        throw new BusinessError(
                            "Invalid revision number: expected 'C1', but got '" + detailDTO.getRevNo() + "'." +
                                "无效的版本号：应填'C1'，但填了'" + detailDTO.getRevNo() + "'。");
                    }
                    break;
                case "AFC_U":
                    //查询数据库
                    DrawingDetail latestDetailC = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(detailDTO.getDrawingId(), EntityStatus.PENDING);
                    if (latestDetailC != null) {
                        // 分割字符串以比较数字部分
                        String[] currentParts = detailDTO.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);
                        String[] previousParts = latestDetailC.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);

                        if (currentParts.length != 2) {
                            return false;
                        }
                        String currentSection = currentParts[0];
                        if (!previousParts[0].equals("C")) return detailDTO.getRevNo().equals("C2");
                        if (!currentSection.equals("C")) return false;
                        int currentItem = Integer.parseInt(currentParts[1]);
                        int previousItem = Integer.parseInt(previousParts[1]);

                        return currentItem - previousItem == 1;
                    } else {
                        String[] currentParts = detailDTO.getRevNo().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);
                        if (!currentParts[0].equals("C")) return false;
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    @Operation(summary = "修改piping图纸清单条目", description = "修改piping生产设计图纸清单条目")
    @RequestMapping(method = POST, value = "/{id}", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<Drawing> edit(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                @PathVariable @Parameter(description = "条目ID") Long id,
                                                @RequestBody DrawingDTO pipingDTO) {

        Long userid = getContext().getOperator().getId();

        Drawing d = drawingService.modify(orgId, projectId, id, userid, pipingDTO);

        if (pipingDTO.getCoverTempName() != null && !pipingDTO.getCoverTempName().equals(d.getCoverId())) {
            logger.error("图纸controller2 保存docs服务->开始");
            JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                pipingDTO.getCoverTempName().toString(), new FilePostDTO());
            logger.error("图纸controller2 保存docs服务->结束");
            FileES f = responseBody.getData();
            d.setCoverId(LongUtils.parseLong(f.getId()));
            d.setCoverName(f.getName());
            d.setCoverPath(f.getPath());
            d.setQrCode(null);
            drawingService.save(d);
        }

        return new JsonObjectResponseBody<>(getContext(), d);
    }

    @Override
    @Operation(summary = "修改piping图纸最新版本号", description = "修改piping图纸最新版本号")
    @RequestMapping(method = POST, value = "/{id}/version", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<Drawing> editVersion(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                       @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                       @PathVariable @Parameter(description = "条目ID") Long id,
                                                       @RequestBody DrawingDTO pipingDTO) {

        Long userid = getContext().getOperator().getId();

        Drawing d = drawingService.modifyVersion(orgId, projectId, id, userid, pipingDTO);

        return new JsonObjectResponseBody<>(getContext(), d);
    }

    @Override
    @Operation(summary = "查询piping图纸清单列表", description = "查询piping生产设计图纸清单列表")
    @RequestMapping(method = GET, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonListResponseBody<DrawingWorkHourDTO> list(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                         @PathVariable @Parameter(description = "项目 ID") Long projectId, PageDTO page, DrawingCriteriaDTO criteriaDTO) {
        return new JsonListResponseBody<>(getContext(),
            drawingService.getList(orgId, projectId, page, criteriaDTO));
    }

    @Operation(summary = "按分配人查询图纸清单", description = "按分配人查询图纸清单")
    @RequestMapping(method = GET, value = "/users/{assigneeId}/drawings", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    @Override
    public JsonListResponseBody<DrawingWorkHourDTO> listByAssignee(@PathVariable @Parameter(description = "分配人 ID") Long assigneeId, PageDTO page, DrawingCriteriaDTO criteriaDTO) {
        criteriaDTO.setAssigneeId(assigneeId);
        return new JsonListResponseBody<>(getContext(),
            drawingService.getList(null, null, page, criteriaDTO));
    }

    @Override
    @Operation(summary = "查询图纸专业筛选信息", description = "查询图纸专业筛选信息")
    @RequestMapping(method = GET, consumes = ALL_VALUE, value = "/drawing-disciplines", produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<DrawingFilterDTO> getDrawingDisciplines(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                         @PathVariable @Parameter(description = "项目 ID") Long projectId) {
        return new JsonObjectResponseBody<>(getContext(),
            drawingService.getDrawingDisciplines(orgId, projectId));
    }

    @Override
    @Operation(summary = "查询图纸专业筛选信息", description = "查询图纸专业筛选信息")
    @RequestMapping(method = GET, consumes = ALL_VALUE, value = "/drawing-doc-types", produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<DrawingFilterDTO> getDrawingDocTypes(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                                          @PathVariable @Parameter(description = "项目 ID") Long projectId) {
        return new JsonObjectResponseBody<>(getContext(),
            drawingService.getDrawingDocTypes(orgId, projectId));
    }

    @Override
    @Operation(summary = "查询piping图纸筛选条件列表", description = "查询piping生产设计图纸筛选条件列表")
    @RequestMapping(method = GET, consumes = ALL_VALUE, value = "/condition", produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonListResponseBody<DrawingCriteriaDTO> getConditionList(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                                     @PathVariable @Parameter(description = "项目 ID") Long projectId) {
        return new JsonListResponseBody<>(getContext(),
            drawingService.getParamList(orgId, projectId));
    }

    @Override
    @Operation(summary = "查询piping图纸清单列表(包含deleted)", description = "查询piping生产设计图纸清单列表(包含deleted)")
    @RequestMapping(method = GET, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE, value = "/all")
    @WithPrivilege
    public JsonListResponseBody<Drawing> getList(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                 @PathVariable @Parameter(description = "项目 ID") Long projectId) {
        return new JsonListResponseBody<>(getContext(),
            drawingService.getAllList(orgId, projectId));
    }

    @Override
    @Operation(summary = "获取图纸对应的全部图纸分类", description = "获取图纸对应的全部图纸分类")
    @RequestMapping(method = GET, value = "/categories", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonListResponseBody<BpmEntitySubType> getDrawingCategoryList(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId) {
        return new JsonListResponseBody<>(getContext(),
            drawingBaseService.getDrawingCategoryList(orgId, projectId));
    }

    @Override
    @Operation(summary = "删除piping图纸清单条目", description = "删除piping生产设计图纸清单条目")
    @RequestMapping(method = POST, value = "/{id}/delete", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonResponseBody delete(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                   @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                   @PathVariable @Parameter(description = "条目ID") Long id,
                                   @RequestBody DrawingDeleteDTO drawingDeleteDTO) {
        drawingService.delete(orgId, projectId, id, drawingDeleteDTO);
        return new JsonResponseBody();
    }

    @Operation(summary = "删除图纸文件，标记删除", description = "删除设计图纸文件条目")
    @RequestMapping(
        method = POST,
        value = "/drawing-file/{id}/delete",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonResponseBody deleteDrawingFile(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long drawingFileId,
        @RequestBody DrawingDeleteDTO drawingDeleteDTO
    ) {
        drawingService.deleteDrawingFile(orgId, projectId, drawingFileId, drawingDeleteDTO, getContext().getOperator().getId());
        return new JsonResponseBody();
    }

    @Override
    @Operation(summary = "删除piping图纸清单条目", description = "删除piping生产设计图纸清单条目")
    @RequestMapping(method = DELETE, value = "/{id}/physical-delete", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonResponseBody physicalDelete(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                           @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                           @PathVariable @Parameter(description = "条目ID") Long id) {
        drawingService.physicalDelete(orgId, projectId, id);
        return new JsonResponseBody();
    }

    @Override
    @Operation(summary = "获取piping图纸清单条目详细信息", description = "获取piping生产设计图纸清单条目详细信息")
    @RequestMapping(method = GET, value = "/{id}", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<Drawing> detail(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                  @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                  @PathVariable @Parameter(description = "条目ID") Long id) {
        Drawing dl = drawingBaseService.getDetailedDrawing(orgId, projectId, id);

        List<BpmActivityInstanceBase> list = drawingTaskService.findByEntityNo(orgId, projectId, dl.getDwgNo());
        if (!list.isEmpty()) {
            int bpmnVersion = list.get(0).getBpmnVersion();
            if (bpmnVersion == 0) {
                BpmReDeployment maxEntity = bpmReDeploymentRepository.findFirstByProjectIdAndProcessIdOrderByVersionDesc(projectId, list.get(0).getProcessId());
                bpmnVersion = maxEntity.getVersion();
            }

            DiagramResourceDTO diagramDTO = activityTaskService
                .getDiagramResource(projectId, list.get(0).getId(), list.get(0).getProcessId(), bpmnVersion);

            dl.setDiagramResource(diagramDTO.getDiagramResource());
//            DrawingDetail dwgDetail = drawingService.getLatestDetail(projectId, id);
            List<BpmActivityInstanceBase> actInstList = bpmActInstRepository.findByProjectIdAndEntityIdAndFinishStateAndSuspensionState(
                projectId, id, ActInstFinishState.NOT_FINISHED, SuspensionState.ACTIVE);

            // 通过查询到的正在运行的流程，来查找相应的detail记录
            List<DrawingDetail> details = drawingDetailRepository.findByDrawingIdAndActInsIdOrderByCreatedAt(
                dl.getId(),
                actInstList.get(0).getId()
            );

            if (!actInstList.isEmpty() && details.size() == 0) {
                dl.setDetailDwgCanBeAdded(true);
            } else {
                dl.setDetailDwgCanBeAdded(false);
            }

            if (!details.isEmpty() && null != details.get(0).getPdfUpdateVersion()) {
                dl.setPdfUpdateVersion(details.get(0).getPdfUpdateVersion());
            }

            BpmEntitySubType best = entitySubTypeService.getEntitySubType(projectId, dl.getEntitySubType());
            if (best != null) {
                dl.setSubDrawingFlg(best.isSubDrawingFlg());
            }
        }
        return new JsonObjectResponseBody<>(getContext(), dl);
    }

    @Override
    @Operation(summary = "获取piping图纸清单条目详细信息", description = "获取piping生产设计图纸清单条目详细信息")
    @RequestMapping(method = GET, value = "/{id}/uncheck", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<Drawing> detailUncheck(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                         @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                         @PathVariable @Parameter(description = "条目ID") Long id) {
        Drawing dl = drawingBaseService.getDetailedDrawingUncheck(orgId, projectId, id);

        return new JsonObjectResponseBody<>(getContext(), dl);
    }

    @Override
    @Operation(summary = "获取piping图纸历史记录", description = "获取piping图纸历史记录")
    @RequestMapping(method = GET, value = "/{id}/history", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<DrawingHistory> history(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                        @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                        @PathVariable @Parameter(description = "条目ID") Long id) {
        return new JsonListResponseBody<>(getContext(),
            drawingHistoryService.getHistory(orgId, projectId, id));
    }

    @Override
    @Operation(summary = "上传图纸文件", description = "上传图纸文件")
    @RequestMapping(method = POST, value = "histories/{id}/upload-file", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonResponseBody upload(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                   @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                   @PathVariable @Parameter(description = "目录历史ID") Long id,
                                   @RequestBody DrawingUploadDTO uploadDTO) {

        /*boolean actInst = drawingService.checkActivity(orgId,projectId,id);
        if(!actInst) {
            throw new ValidationError("Please run the activity.");
        }*/

        if (uploadDTO.getFileName() == null) {
            throw new ValidationError("Please upload the drawing file.");
        }

        OperatorDTO user = getContext().getOperator();
        drawingBaseService.uploadPdf(orgId, projectId, id, user, uploadDTO);

        return new JsonResponseBody();
    }

    @Override
    @Operation(summary = "获取ISO图纸清单条目详细信息", description = "获取ISO图纸清单条目详细信息")
    @RequestMapping(method = GET, value = "/{id}/generate-rerport", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<Drawing> generateRerport(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                           @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                           @PathVariable @Parameter(description = "条目ID") Long id) {

        final Project project = projectService.get(orgId, projectId);
        Long userid = getContext().getOperator().getId();

        Drawing dl = drawingBaseService.getDetailedDrawing(orgId, projectId, id);
        switch (dl.getEntitySubType()) {
            case SD_DWG_PIPE_FABRICATION:
                DrawingFileDTO dto = drawingService.generateReportPipe(orgId, projectId, dl, project, userid, false, false);
                if (dto != null) {
                    drawingBaseService.setDrawingFile(dl, dto);
                }
                break;
            case SD_DWG_PIPE_SUPPORT_FABRICATION:
                DrawingFileDTO dtoS = drawingService.generateReportPipeSupport(orgId, projectId, dl, project, userid, false, false);
                if (dtoS != null) {
                    drawingBaseService.setDrawingFile(dl, dtoS);
                }
                break;
            default:

        }

        return new JsonObjectResponseBody<>(dl);
    }


    @Override
    @Operation(summary = "锁定/解锁图纸", description = "锁定/解锁图纸")
    @RequestMapping(method = POST, value = {"/{id}/lock",
        "/{id}/unlock"}, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonResponseBody modifyLocked(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                         @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                         @PathVariable @Parameter(description = "条目ID") Long id) {
        Drawing dl = drawingBaseService.getDetailedDrawing(orgId, projectId, id);
        if (dl != null) {
            dl.setLocked(!dl.isLocked());
            drawingService.save(dl);
        }
        return new JsonResponseBody();
    }


    @Override
    @Operation(summary = "获取图纸清单条目详细列表", description = "获取图纸清单条目详细列表")
    @RequestMapping(method = GET, value = "/{drawingId}/details", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonListResponseBody<DrawingDetail> detailList(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                          @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                          @PathVariable @Parameter(description = "条目ID") Long drawingId,
                                                          DrawingDetailQueryDTO drawingDetailQueryDTO) {
        return new JsonListResponseBody<>(getContext(),
            drawingService.getList(orgId, projectId, drawingId, drawingDetailQueryDTO));
    }

    @Override
    @Operation(
        summary = "查询图纸类型列表",
        description = "查询图纸类型列表"
    )
    @RequestMapping(
        method = GET,
        value = "/drawing-categories",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonListResponseBody<BpmEntitySubType> list(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId) {
        return new JsonListResponseBody<>(getContext(),
            drawingBaseService.getList(orgId, projectId));
    }

    @Override
    @Operation(summary = "锁定/解锁图纸", description = "锁定/解锁图纸")
    @RequestMapping(method = POST, value = "/{id}/check", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonResponseBody check(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸ID") Long id,
        @RequestBody DrawingProofreadDTO proofreadDTO
    ) {
        ContextDTO contextDTO = getContext();
        OperatorDTO operatorDTO = contextDTO.getOperator();
        drawingService.check(
            contextDTO, orgId, projectId, id, operatorDTO, proofreadDTO);
        return new JsonResponseBody();
    }

    @Override
    @Operation(summary = "redmark 锁定/解锁图纸", description = "redmark 锁定/解锁图纸")
    @RequestMapping(method = POST, value = "/{id}/red-mark/check", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonResponseBody redMarkCheck(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸ID") Long id,
        @RequestBody DrawingProofreadDTO proofreadDTO
    ) {
        ContextDTO contextDTO = getContext();
        OperatorDTO operatorDTO = contextDTO.getOperator();
        drawingService.redMarkCheck(
            contextDTO, orgId, projectId, id, operatorDTO, proofreadDTO);
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "查询上传zip文件历史记录",
        description = "查询上传zip文件历史记录"
    )
    @RequestMapping(
        method = GET,
        value = "/{drawingId}/upload-zip-file-history",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<DrawingUploadZipFileHistory> zipFileHistory(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "条目ID") Long drawingId,
        PageDTO page) {
        return new JsonListResponseBody<>(getContext(),
            drawingBaseService.zipFileHistory(orgId, projectId, drawingId, page));
    }

    @Override
    @Operation(
        summary = "查询上传zip文件历史记录明细",
        description = "查询上传zip文件历史记录明细"
    )
    @RequestMapping(
        method = GET,
        value = "/{drawingId}/upload-zip-file-history/{id}/details",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonListResponseBody<DrawingUploadZipFileHistoryDetail> zipFileHistoryDetail(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "条目ID") Long drawingId,
        @PathVariable @Parameter(description = "历史记录 ID") Long id,
        PageDTO page) {
        return new JsonListResponseBody<>(getContext(),
            drawingBaseService.zipFileHistoryDetail(orgId, projectId, drawingId, id, page));
    }

    @Override
    @Operation(
        summary = "查询上传zip文件历史记录明细",
        description = "查询上传zip文件历史记录明细"
    )
    @RequestMapping(
        method = GET,
        value = "/{qrCode}/check-issue",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonObjectResponseBody checkIssue(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "qrCode") String qrCode) {
        return new JsonObjectResponseBody<>((BaseDTO) drawingBaseService.checkIssue(orgId, projectId, qrCode));
    }


    @Override
    @Operation(summary = "创建图纸工作流实例", description = "创建图纸工作流实例")
    @RequestMapping(method = GET, value = "/{id}/create-task-info", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<DrawingCreateTaskInfoDTO> getCreateTaskInfo(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸条目ID") Long id) {
        return new JsonObjectResponseBody<>(drawingBaseService.getCreateTaskInfo(orgId, projectId, id));
    }

    @Override
    @Operation(summary = "上传子图纸", description = "上传子图纸")
    @RequestMapping(method = POST, value = "/upload/file", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<UploadDrawingFileResultDTO> uploadSubFile(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody DrawingUploadDTO uploadDTO) {

        String temporaryFileName = uploadDTO.getFileName();

        File diskFile = new File(temporaryDir, temporaryFileName);

        FileMetadataDTO metadata = FileUtils.readMetadata(diskFile, FileMetadataDTO.class);
        String uploadFileName = metadata.getFilename();
        String fileType = FileUtils.extname(uploadFileName);
        UploadDrawingFileResultDTO dto = new UploadDrawingFileResultDTO();
        if (fileType.equals("." + FILE_TYPE_ZIP)) {
            dto = drawingBaseService.uploadDrawingSubPipingZip(orgId, projectId, uploadFileName, getContext(), uploadDTO);
        }

        return new JsonObjectResponseBody<>(dto);
    }

    @Operation(
        summary = "获取图纸权限列表",
        description = "获取图纸权限列表"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}/privileges"
    )
    @SetUserInfo
    @ResponseStatus(OK)
    @WithPrivilege
    @Override
    public JsonListResponseBody<TaskPrivilegeDTO> getProcessPrivileges(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "图纸id") Long id) {
        return new JsonListResponseBody<>(drawingBaseService.getProcessPrivileges(orgId, projectId, id));
    }

    @Override
    @Operation(
        summary = "设置图纸权限执行人",
        description = "设置图纸权限执行人"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}/privileges"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody setProcessPrivileges(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "图纸id") Long id,
        @RequestBody TaskPrivilegeDTO DTO) {
        OperatorDTO operatorDTO = getContext().getOperator();
        drawingBaseService.setProcessPrivileges(orgId, projectId, id, DTO, operatorDTO.getId());
        return new JsonResponseBody();
    }

    @Operation(
        summary = "check图纸升版版本号",
        description = "check图纸升版版本号"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}/check-up-version/{version}"
    )
    @ResponseStatus(OK)
    @WithPrivilege
    @Override
    public JsonResponseBody checkUpVersion(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "图纸id") Long id,
        @PathVariable @Parameter(description = "图纸id") String version) {
        drawingBaseService.checkUpVersion(orgId, projectId, id, version);
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "check图纸锁定状态",
        description = "check图纸锁定状态"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}/check-lock"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody checkLock(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "图纸id") Long id) {
        drawingBaseService.checkLock(orgId, projectId, id);
        return new JsonResponseBody();
    }


    @Override
    @Operation(summary = "上传redmark图纸", description = "上传redmark图纸")
    @RequestMapping(method = POST, value = "/redmark-upload", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonResponseBody uploadRedmarkFile(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody DrawingUploadDTO uploadDTO) {

        if (uploadDTO.getTaskId() == null) {
            throw new BusinessError("RedMark ProcInst or TaskId is Empty");
        }
        String temporaryFileName = uploadDTO.getFileName();

        File diskFile = new File(temporaryDir, temporaryFileName);

        FileMetadataDTO metadata = FileUtils.readMetadata(diskFile, FileMetadataDTO.class);
        String uploadFileName = metadata.getFilename();
        String fileType = FileUtils.extname(uploadFileName);

        if (fileType.toLowerCase().equals("." + FILE_TYPE_PDF)) {
            drawingRedMarkService.uploadRedmarkFile(
                orgId, projectId, uploadFileName, getContext(), uploadDTO);
        } else if (fileType.toLowerCase().equals("." + FILE_TYPE_ZIP)) {
            drawingRedMarkService.uploadRedmarkFileZip(
                orgId, projectId, uploadFileName, getContext(), uploadDTO);
        } else {
            throw new ValidationError("请上传pdf或zip文件");
        }

        return new JsonResponseBody();
    }

    /**
     * 导出生产设计图纸目录信息。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    @Operation(
        summary = "导出生产设计图纸目录信息"
    )
    @GetMapping(
        value = "/export-xls",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public void exportPipingDrawingList(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        DrawingCriteriaDTO criteriaDTO
    ) throws IOException {
        final OperatorDTO operator = getContext().getOperator();
        File excel = drawingService.saveDownloadFile(orgId, projectId, criteriaDTO, operator.getId());

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-piping-drawing-list.xlsx\""
            );

            IOUtils.copy(
                new FileInputStream(excel), response.getOutputStream()
            );

        } catch (FileNotFoundException e) {
            throw new NotFoundError();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessError("", "文件编码不支持");
        } catch (IOException e) {
            throw new BusinessError("", "下载文件出错");
        }

        response.flushBuffer();
    }

    @Override
    @Operation(summary = "扫描文件内二维码上传redmark图纸", description = "扫描文件内二维码上传redmark图纸")
    @RequestMapping(method = POST, value = "/redmark-upload/qrcode", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonResponseBody uploadRedmarkFileQrcode(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody DrawingUploadDTO uploadDTO
    ) {
        String temporaryFileName = uploadDTO.getFileName();

        File diskFile = new File(temporaryDir, temporaryFileName);

        FileMetadataDTO metadata = FileUtils.readMetadata(diskFile, FileMetadataDTO.class);
        String uploadFileName = metadata.getFilename();
        String fileType = FileUtils.extname(uploadFileName);

        if (fileType.toLowerCase().equals("." + FILE_TYPE_PDF)) {
            drawingRedMarkService.uploadRedmarkFileQrcode(
                orgId, projectId, uploadFileName, getContext(), uploadDTO);
        } else {
            throw new ValidationError("请上传pdf或zip文件");
        }

        return new JsonResponseBody();
    }

    @Override
    @Operation(summary = "上传图纸文件zip文件包", description = "上传图纸文件zip文件包")
    @RequestMapping(method = POST, value = "/batch-upload-pdf",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<UploadDrawingFileResultDTO> batchUpload(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                                          @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                                          @RequestBody DrawingUploadDTO uploadDTO) throws IOException {

        UploadDrawingFileResultDTO dto = new UploadDrawingFileResultDTO();

        String temporaryFileName = uploadDTO.getFileName();
        // 取得已上传的临时文件
        File diskFile = new File(temporaryDir, temporaryFileName);
        // 读取上传文件的元数据
        FileMetadataDTO metadata = FileUtils.readMetadata(diskFile, FileMetadataDTO.class);
        String uploadFileName = metadata.getFilename();
        String fileType = FileUtils.extname(uploadFileName);
        if (fileType.toLowerCase().equals("." + FILE_TYPE_ZIP)) {
            dto = drawingBaseService.uploadDrawingSubPipingZipExternal(
                orgId,
                projectId,
                uploadFileName,
                0L,
                0L,
                getContext(),
                uploadDTO);
        } else {
            throw new ValidationError("请上传zip文件");
        }

        return new JsonObjectResponseBody<>(dto);
    }

    /**
     * 获取生产设计图纸清单条目-piping
     */
    @RequestMapping(
        method = GET,
        value = "/drawing-detail/{drawingDetailId}"
    )
    @Override
    @WithPrivilege
    public JsonObjectResponseBody<DrawingDetail> drawingDetail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingDetailId") Long drawingDetailId
    ) {
        return new JsonObjectResponseBody<>(drawingDetailRepository.findById(drawingDetailId).orElse(new DrawingDetail()));
    }

    /**
     * 获取设计图纸清单条目，带有各种文件信息
     */
    @RequestMapping(
        method = GET,
        value = "/{drawingId}/detail-files"
    )
    @WithPrivilege
    @Override
    public JsonListResponseBody<DrawingDetail> detailFileList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingId") Long drawingId,
        DrawingDetailQueryDTO drawingDetailQueryDTO
    ) {
        return new JsonListResponseBody<>(getContext(),
            drawingService.getListFiles(orgId, projectId, drawingId, drawingDetailQueryDTO));
    }

    /**
     * 获取图纸所有清单条目，带有各种文件信息
     */
    @RequestMapping(
        method = GET,
        value = "/{drawingId}/details-files/all"
    )
    @WithPrivilege
    @Override
    public JsonListResponseBody<DrawingDetail> allDetailFileList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingId") Long drawingId,
        DrawingDetailQueryDTO drawingDetailQueryDTO
    ) {
        return new JsonListResponseBody<>(getContext(),
            drawingService.getAllListFiles(orgId, projectId, drawingId, drawingDetailQueryDTO));
    }

    /**
     * 获取生产设计图纸设校审人员
     */
    @RequestMapping(
        method = GET,
        value = "/drawing-delegate/{drawingId}"
    )
    @WithPrivilege
    @Override
    public JsonObjectResponseBody<DrawingDelegateDTO> drawingDelegate(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingId") Long drawingId
    ) {
        return new JsonObjectResponseBody<>(getContext(),
            drawingService.getDrawingDelegate(orgId, projectId, drawingId));
    }

    /**
     * 获取图纸专业树
     *
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "/treeData",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public List<Map<String, Object>> getDisciplineTreeData(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    ) {
        return drawingService.getDisciplineTreeData(orgId, projectId);
    }

    @RequestMapping(
        method = GET,
        value = "/treeData/x-axis",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public List<Map<String, Object>> getTreeXData(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    ) {
        return drawingService.getTreeXData(orgId, projectId);
    }

    @RequestMapping(
        method = GET,
        value = "/treeData/y-axis/{discipline}/{level}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public List<Map<String, Object>> getTreeYData(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("discipline") String discipline,
        @PathVariable("level") String level
    ) {
        return drawingService.getTreeYData(orgId, projectId, discipline, level);
    }


    @Override
    @Operation(summary = "上传图纸文件zip文件包,补录图纸", description = "上传图纸文件zip文件包,补录图纸")
    @RequestMapping(method = POST,
        value = "/repair-drawing",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonObjectResponseBody<UploadDrawingFileResultDTO> repairDrawing(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                                            @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                                            @RequestBody DrawingUploadDTO uploadDTO) throws IOException {
        UploadDrawingFileResultDTO dto = null;
        String temporaryFileName = uploadDTO.getFileName();
        // 取得已上传的临时文件
        File diskFile = new File(temporaryDir, temporaryFileName);
        // 读取上传文件的元数据
        FileMetadataDTO metadata = FileUtils.readMetadata(diskFile, FileMetadataDTO.class);
        String uploadFileName = metadata.getFilename();
        String fileType = FileUtils.extname(uploadFileName);
        if (fileType.toLowerCase().equals("." + FILE_TYPE_ZIP)) {
            dto = drawingBaseService.repairDrawing(
                orgId,
                projectId,
                uploadFileName,
                getContext(),
                uploadDTO);
        } else {
            throw new ValidationError("请上传zip文件");
        }
        return new JsonObjectResponseBody<>(dto);
    }
}
