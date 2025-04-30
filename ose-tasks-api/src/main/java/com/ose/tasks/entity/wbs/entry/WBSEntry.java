package com.ose.tasks.entity.wbs.entry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.vo.wbs.WBSEntryType;
import com.ose.util.CryptoUtils;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import net.sf.mpxj.Duration;
import net.sf.mpxj.Task;
import net.sf.mpxj.TaskType;

import jakarta.persistence.*;

import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ose.vo.EntityStatus.ACTIVE;

/**
 * WBS 条目数据。
 */
@Entity
@Table(
    name = "wbs_entry",
    indexes = {
        @Index(
            columnList = "projectId,guid",
            unique = true
        ),
        @Index(columnList = "projectId,parentId,deleted"),
        @Index(columnList = "projectId,processId,deleted"),
        @Index(columnList = "projectId,entityId,active,deleted"),
        @Index(columnList = "projectId,entityId,stage,process"),
        @Index(columnList = "projectId,version"),
        @Index(columnList = "projectId,parentHierarchyNodeId,active,deleted"),
        @Index(columnList = "projectId,process,name,deleted"),
        @Index(columnList = "entityId,projectId"),
        @Index(columnList = "process,entityType")
    }
)
public class WBSEntry extends WBSEntryBase {

    private static final long serialVersionUID = 494323102816895764L;

    private static Random random = new Random(System.currentTimeMillis());

    // UT3 项目 WBS 格式
    private static final Pattern TEST_PATTERN = Pattern.compile(
        "^([^.]+\\.)*([^.]+)\\.(construction|(mech(anical)?_)?completion)((\\.[^.]+){2,9})$",
        Pattern.CASE_INSENSITIVE
    );
//    final Pattern TEST_PATTERN = Pattern.compile(
//        "^([^.]+\\.)*([^.]+)\\.(construction|(mech(anical)?_)?completion)((\\.[^.]+){0,9})$",
//        Pattern.CASE_INSENSITIVE
//    );

    private static final Pattern TEST_PATTERN_ENG = Pattern.compile(
        "^([^.]+\\.)*([^.]+)\\.(ENGINEERING)((\\.[^.]+){0,9})$",
        Pattern.CASE_INSENSITIVE
    );

    @Schema(description = "公司 ID")
    @Column
    private Long companyId;

    @Schema(description = "组织 ID")
    @Column
    private Long orgId;

    @Schema(description = "建造视图层级节点 ID")
    @Column
    private Long hierarchyNodeId;

    @Schema(description = "建造视图上级层级节点 ID")
    @Column
    private Long parentHierarchyNodeId;

    @Schema(description = "抽检随机编号")
    @Column
    private Integer randomNo;

    @Schema(description = "工序 ID")
    @Column
    private Long processId;

    @Schema(description = "ISO 图纸编号")
    @Column
    private Integer dwgShtNo;


    /**
     * 构造方法。
     */
    public WBSEntry() {
        super();
        randomNo = Math.abs(random.nextInt());
    }

    /**
     * 构造方法。
     */
    public WBSEntry(OperatorDTO operator,
                    Project project,
                    Task task,
                    Map<String, Set<String>> stageProcesses,
                    Map<String, Long> processFuncPartMap) {
        this();
        setCompanyId(project.getCompanyId());
        setOrgId(project.getOrgId());
        setProjectId(project.getId());
        setCreatedAt();
        setCreatedBy(operator.getId());
        update(operator, task, stageProcesses, project, processFuncPartMap, task.getWBS());
    }

    /**
     * 构造方法。
     * 增加4th计划时 wbs 可以设置为null，wbs只对3级计划 起作用
     */
    public WBSEntry(
        final OperatorDTO operator,
        final WBSEntry parent,
        final ProjectNode entityNode,
        final String wbs,
        final int entityIndex,
        final Map<String, Set<String>> stageProcesses,
        final Map<String, Long> processFuncPartMap,
        final Long processId,
        final int wbsMode
    ) {
        this();
        setCompanyId(parent.getCompanyId());
        setOrgId(parent.getOrgId());
        setProjectId(parent.getProjectId());
        setParentId(parent.getId());
        setDepth(parent.getDepth() + 1);
//        setPath(parent.getPath() + parent.getId() + "/");
        setType(WBSEntryType.ENTITY);
        setStage(parent.getStage());
        setProcess(parent.getProcess());
        setSector(parent.getSector());
        setProjectNodeId(entityNode.getId());
        setEntityId(entityNode.getEntityId());
        setEntityType(entityNode.getEntityType());
        setEntitySubType(entityNode.getEntitySubType());
        setGuid(CryptoUtils.hashUUID(
            String.format(
                "%s/%s/%s/%s/%s:%s",
                parent.getId(),
                parent.getStage(),
                parent.getProcess(),
                entityNode.getEntityType(),
                entityNode.getEntitySubType(),
                entityNode.getNo()
            )
        ));
//        setWbs(parent.getWbs() + "." + entityNode.getNo());
        setNo(String.format("%s-%04d", parent.getNo(), entityIndex));
        setName(entityNode.getNo());
        setName1(entityNode.getNo1());
        setName2(entityNode.getNo2());
        setSort((long) entityIndex + parent.getSort());
        setCreatedAt();
        setCreatedBy(operator.getId());
        setLastModifiedAt();
        setLastModifiedBy(operator.getId());
        setStatus(ACTIVE);
        setProcess(stageProcesses, processFuncPartMap, wbs, wbsMode );
        setProcessId(processId);
    }

    public WBSEntry updateEntry(
        final OperatorDTO operator,
        final WBSEntry parent,
        final ProjectNode entityNode,
        final String wbs,
        final int entityIndex,
        final Map<String, Set<String>> stageProcesses,
        final Map<String, Long> processFuncPartMap,
        final Long processId,
        final int wbsMode,
        final WBSEntry wbsEntry
    ) {
        wbsEntry.setCompanyId(parent.getCompanyId());
        wbsEntry.setOrgId(parent.getOrgId());
        wbsEntry.setProjectId(parent.getProjectId());
        wbsEntry.setParentId(parent.getId());
        wbsEntry.setDepth(parent.getDepth() + 1);
//        setPath(parent.getPath() + parent.getId() + "/");
        wbsEntry.setType(WBSEntryType.ENTITY);
        wbsEntry.setStage(parent.getStage());
        wbsEntry.setProcess(parent.getProcess());
        wbsEntry.setSector(parent.getSector());
        wbsEntry.setProjectNodeId(entityNode.getId());
        wbsEntry.setEntityId(entityNode.getEntityId());
        wbsEntry.setEntityType(entityNode.getEntityType());
        wbsEntry.setEntitySubType(entityNode.getEntitySubType());
        wbsEntry.setGuid(CryptoUtils.hashUUID(
            String.format(
                "%s/%s/%s/%s:%s",
                parent.getStage(),
                parent.getProcess(),
                entityNode.getEntityType(),
                entityNode.getEntitySubType(),
                entityNode.getNo()
            )
        ));
//        setWbs(parent.getWbs() + "." + entityNode.getNo());
//        wbsEntry.setNo(String.format("%s-%04d", parent.getNo(), entityIndex));
        wbsEntry.setName(entityNode.getNo());
        wbsEntry.setName1(entityNode.getNo1());
        wbsEntry.setName2(entityNode.getNo2());
        wbsEntry.setSort((long) entityIndex + parent.getSort());
        wbsEntry.setLastModifiedAt();
        wbsEntry.setLastModifiedBy(operator.getId());
        wbsEntry.setStatus(ACTIVE);
        wbsEntry.setProcess(stageProcesses, processFuncPartMap, wbs, wbsMode );
        wbsEntry.setProcessId(processId);
        return wbsEntry;
    }

    /**
     * 构造方法。
     *
     * @param startAt  计划开始时间
     * @param finishAt 计划截止时间
     */
    public WBSEntry(Date startAt, Date finishAt) {
        setStartAt(startAt);
        setFinishAt(finishAt);
    }

    public WBSEntry(Long id,
                    String entityType,
                    String entitySubType,
                    String stageName,
                    String processName) {
        setId(id);
        setEntityType(entityType);
        setEntitySubType(entitySubType);
        setStage(stageName);
        setProcess(processName);
    }

    /**
     * 更新 WBS 条目。
     */
    public void update(OperatorDTO operator,
                       Task task, Map<String, Set<String>> stageProcesses,
                       Project project,
                       Map<String, Long> processFuncPartMap,
                       String wbs) {
        setGuid(task.getGUID().toString());
//        setWbs(task.getWBS());
        if(task.getWBS().equalsIgnoreCase(task.getActivityID())) {
            setType(task.getType());
            setNo(task);
        } else {
            setType(WBSEntryType.WORK);
            setNo(task.getActivityID());
        }
        setName(task.getName());

        if (task.getStart() != null){
            setStartAt(Date.from(task.getStart().atZone(ZoneId.systemDefault()).toInstant()));//(task.getStart());
        }
        if (task.getFinish() != null){
            setFinishAt(Date.from(task.getFinish().atZone(ZoneId.systemDefault()).toInstant()));//(task.getFinish());
        }
        setDuration(task.getDuration());
        setActive(task.getActive());
        setLastModifiedAt();
        setLastModifiedBy(operator.getId());
        setStatus(ACTIVE);
        setProcess(stageProcesses, processFuncPartMap, wbs, project.getWbsMode());
        setSector(getSector(task.getWBS(), project.getWbsMode() == null ? 1 : project.getWbsMode(), processFuncPartMap));
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getHierarchyNodeId() {
        return hierarchyNodeId;
    }

    public void setHierarchyNodeId(Long hierarchyNodeId) {
        this.hierarchyNodeId = hierarchyNodeId;
    }

    public Long getParentHierarchyNodeId() {
        return parentHierarchyNodeId;
    }

    public void setParentHierarchyNodeId(Long parentHierarchyNodeId) {
        this.parentHierarchyNodeId = parentHierarchyNodeId;
    }

    public void setType(TaskType type) {
        setType(WBSEntryType.getInstance(type.name()));
    }

    /**
     * 设置 WBS 起止时间。
     */
    public void setDuration(Duration duration) {

        if (duration == null) {
            return;
        }

        setDuration(duration.getDuration());
        setDurationUnit(duration.getUnits());
    }

    /**
     * 设置工序信息。
     * 根据任务条目的 WBS 和名称信息解析任务相关实体的区域/部分、工序阶段及工序。
     * 实体所属区域/部分及工序阶段从 WBS 解析获得，工序从名称获得。
     *
     * @param stageProcesses 工序阶段及其工序映射表
     */
    private void setProcess(Map<String, Set<String>> stageProcesses, Map<String, Long> processFuncPartMap, String wbs, int wbsMode) {

//        final String wbs = this.getWbs();

        // 设 WBS 编号为 A.B.C.D，根据句点将 WBS 编号分割
        final List<String> parts = Arrays.asList(wbs.split("\\."));

        if (parts.size() < 3) {
            return;
        }

        Collections.reverse(parts);
        String sectorNo;
        String layerPackage = null;
        String funcPart;
        String stageName;
        String entryName = this.getName().toUpperCase() + " ";
        Matcher matcher = TEST_PATTERN.matcher(wbs);
        Matcher matcherEng = TEST_PATTERN_ENG.matcher(wbs);

        // 格式无效时不解析
        if (!matcher.matches() && !matcherEng.matches()) {
            return;
        }

        int constrPos = parts.indexOf("CONSTRUCTION");
        int engPos = parts.indexOf("ENGINEERING");

        //"^([^.]+\\.)*([^.]+)\\.(construction|(mech(anical)?_)?completion)((\\.[^.]+){2,9})$",
        int tailingWBSCount = 0;
        if(matcher.matches()) {
            tailingWBSCount = matcher.group(6).split("\\.").length - 1;
        } else {
            tailingWBSCount = matcherEng.group(4).split("\\.").length - 1;
        }

        //mode =5 ENG 格式：WBS_LV1 . ENGINEERING . (...) . 工序阶段 . FUNCTION . TYPE . // ENGINEERING_PROJECT.ENGINEERING.DED.EN.DB
        if(wbsMode == 5 && tailingWBSCount >1 && getType() != WBSEntryType.ENTITY) {
            if(tailingWBSCount == 2) {
                sectorNo = parts.get(0);
                stageName = parts.get(1);

            } else if(tailingWBSCount == 3){
                sectorNo = parts.get(1)+"_"+parts.get(0);
                stageName = parts.get(2);
            } else {
                sectorNo = null;
                stageName = null;
            }
            funcPart = "ENGINEERING";

            // 格式：WBS_Lv1 . ... . 建造 . ... . 模块/子系统 . 专业 . 工序阶段
        } else if (tailingWBSCount >= 3 && stageProcesses.get(parts.get(0)) != null) {
            sectorNo = parts.get(2);
            funcPart = parts.get(1);
            stageName = parts.get(0);
            // 格式：WBS_Lv1 . ... . 模块 . 建造 . ... . 工序阶段 . 专业
        } else if (tailingWBSCount >= 2 && stageProcesses.get(parts.get(1)) != null) {
            sectorNo = matcher.group(2);
            funcPart = parts.get(0);
            stageName = parts.get(1);
            // 格式：WBS_Lv1 . ... . 模块 . 建造 . ... . 工序阶段 . 专业 . 层/包
        } else if (tailingWBSCount >= 3 && stageProcesses.get(parts.get(2)) != null) {
            sectorNo = matcher.group(2);
            funcPart = parts.get(1);
            layerPackage = parts.get(0);
            stageName = parts.get(2);
            // mode =4 格式:WBS_LV1 . 建造 . 工序阶段 . (...) . 模块/系统 . (层/包). 专业
        } else if (tailingWBSCount >= 3 &&
            constrPos > 0 && stageProcesses.get(parts.get(constrPos - 1)) != null) {
            sectorNo = parts.get(2);
//            if (processFuncParts.contains(sectorNo)) {
//                funcPart = parts.get(0);
//                layerPackage = parts.get(1);
//                layerPackage = layerPackage == null ? null : sectorNo + "-" + layerPackage;
//                stageName = parts.get(constrPos - 1);
//            } else {
                sectorNo = parts.get(1);
                funcPart = parts.get(0);
                layerPackage = null;
                stageName = parts.get(constrPos - 1);
//            }
            // 否则视为无效的 WBS 格式
        } else {
            return;
        }

        this.setSector(sectorNo);
        this.setLayerPackage(layerPackage);
        this.setFuncPart(funcPart);
        this.setStage(stageName);
        if (this.getType() != WBSEntryType.WORK) {
            return;
        }
        Set<String> processNames = stageProcesses.get(stageName);

        // 若工序阶段不存在则结束
        if (processNames == null) {
            return;
        }

        matcher = Pattern
            .compile("^((" + sectorNo + ")\\s+)?([^\\s]+)(\\s.*)?$", Pattern.CASE_INSENSITIVE)
            .matcher(entryName);

        String processName = matcher.matches()
            ? StringUtils.trim(matcher.group(3)):entryName.split(" ")[0];
//            : StringUtils.trim(entryName);

        // 若工序不存在则结束
        if (!processNames.contains(processName)) {
            return;
        }

        this.setProcess(processName);
        if(!StringUtils.isEmpty(processName) && !StringUtils.isEmpty(funcPart)) {
            setProcessId(processFuncPartMap.get(processName + "-" + funcPart));
        }
    }

    public Integer getRandomNo() {
        return randomNo;
    }

    public void setRandomNo(Integer randomNo) {
        this.randomNo = randomNo;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Integer getDwgShtNo() {
        return dwgShtNo;
    }

    public void setDwgShtNo(Integer dwgShtNo) {
        this.dwgShtNo = dwgShtNo;
    }


    /**
     * 取得编号中序号部分的整数值。
     *
     * @return 序号整数值
     */
    @JsonIgnore
    public int getSortNo() {

        if (this.getNo() == null) {
            return 0;
        }

        String[] parts = this.getNo().split("-");

        String sortNoString = parts[parts.length - 1];

        try {
            return Integer.parseInt(sortNoString);
        } catch (NumberFormatException e) {
            return 0;
        }

    }

    private String getSector(String wbs, int mode, Map<String, Long> processFuncPartMap) {
        /**
         * mode =1 格式：WBS_Lv1 . ... . 建造 . ... . 模块/子系统 . 专业 . 工序阶段
         * mode =2 // 格式：WBS_Lv1 . ... . 模块 . 建造 . ... . 工序阶段 . 专业
         * mode =3 格式：WBS_Lv1 . ... . 模块 . 建造 . ... . 工序阶段 . 专业 . 层/包
         * mode =4 格式:WBS_LV1 . 建造 . 工序阶段 . (...) . 模块/系统 . (层/包). 专业
         * mode =5 格式：WBS_LV1 . ENGINEERING . (...) . 工序阶段 . FUNCTION . TYPE . // ENGINEERING_PROJECT.ENGINEERING.DED.EN.DB
         */
        // 设 WBS 编号为 A.B.C.D，根据句点将 WBS 编号分割
        final List<String> parts = Arrays.asList(wbs.split("\\."));

        if (parts.size() < 3) {
            return null;
        }

        Collections.reverse(parts);
//        if(parts.get(2).equals("DED")) {
//            System.out.println("ftj abc");
//        }
        String sectorNo;

//        final Pattern TEST_PATTERN = Pattern.compile(
//            "^([^.]+\\.)*([^.]+)\\.(construction|(mech(anical)?_)?completion)((\\.[^.]+){0,9})$",
//            Pattern.CASE_INSENSITIVE
//        );
        Matcher matcher = TEST_PATTERN.matcher(wbs);
//
//        final Pattern TEST_PATTERN_ENG = Pattern.compile(
//            "^([^.]+\\.)*([^.]+)\\.(ENGINEERING)((\\.[^.]+){0,9})$",
//            Pattern.CASE_INSENSITIVE
//        );
        Matcher matcherEng = TEST_PATTERN_ENG.matcher(wbs);


        // 格式无效时不解析
        if (!(matcher.matches() || matcherEng.matches())) {
            return null;
        }

        //"^([^.]+\\.)*([^.]+)\\.(construction|(mech(anical)?_)?completion)((\\.[^.]+){2,9})$",

        // 格式：WBS_Lv1 . ... . 建造 . ... . 模块/子系统 . 专业 . 工序阶段
        if (mode == 1) {
            sectorNo = parts.get(2);
            // 格式：WBS_Lv1 . ... . 模块 . 建造 . ... . 工序阶段 . 专业
        } else if (mode == 2 || mode == 0) {
            sectorNo = matcher.group(2);
            // 格式：WBS_Lv1 . ... . 模块 . 建造 . ... . 工序阶段 . 专业 . 层/包
        } else if (mode == 3) {
            sectorNo = matcher.group(2);
            // mode =4 格式:WBS_LV1 . 建造 . 工序阶段 . (...) . 模块/系统 . (层/包). 专业
        } else if (mode == 4 && parts.size() > 3) {
//            Collections.reverse(parts);
            int constPosi = parts.indexOf("CONSTRUCTION");
            if (constPosi < 0) return null;

            sectorNo = parts.get(constPosi - 2);
//            if (!moduleNames.contains(sectorNo) && constPosi > 2) {
                sectorNo = parts.get(constPosi - 3);
//                if (!moduleNames.contains(sectorNo))
//                    return null;
//            }
            // 无效
        } else if(mode == 5) {
            if(parts.size() == 4) {
                sectorNo = parts.get(0);
            } else if(parts.size() == 5) {
                sectorNo = parts.get(1)+"_"+parts.get(0);
            } else {
                sectorNo = null;
            }
        } else {
            return null;
        }

        return sectorNo;
    }

    public static void main(String[] args) {

//        final String wbs = "F246-lv4.FIRST_BATCH.PR300M01.CONSTRUCTION.COATING.PIPING.UT3PS-300-N-60503-UD-PR300M01(/01)-SPL5";
        final String wbs = "LC_CATERING.CONSTRUCTION.INSTALLATION.DECK3.AFT_RESTAURANT.PIPING";
        /*-------------
        LC_CATERING.CONSTRUCTION.INSTALLATION.DECK3.AFT_RESTAURANT.PIPING
        格式：项目.建造.工序阶段.模块.层/包.专业
        LC_CATERING.CONSTRUCTION.MC.UTILITY.WS.PIPING
        格式：项目.建造.工序阶段.。。。.模块/系统.专业

         */
        /**
         * mode =1 格式：WBS_Lv1 . ... . 建造 . ... . 模块/子系统 . 专业 . 工序阶段
         * mode =2 // 格式：WBS_Lv1 . ... . 模块 . 建造 . ... . 工序阶段 . 专业
         * mode =3 格式：WBS_Lv1 . ... . 模块 . 建造 . ... . 工序阶段 . 专业 . 层/包
         * mode =4 格式:WBS_LV1 . 建造 . 工序阶段 . (...) . 模块/系统 . (层/包). 专业
         *
         */
        short mode = 4;
        // 设 WBS 编号为 A.B.C.D，根据句点将 WBS 编号分割
        final List<String> parts = Arrays.asList(wbs.split("\\."));

        if (parts.size() < 3) {
            return;
        }

        Collections.reverse(parts);
        String sectorNo;
        String layerPackage = null;
        String discipline;
        String stageName;
        String entryName;// = this.getName().toUpperCase() + " ";
        final Pattern TEST_PATTERN = Pattern.compile(
            "^([^.]+\\.)*([^.]+)\\.(construction|(mech(anical)?_)?completion)((\\.[^.]+){0,9})$",
            Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = TEST_PATTERN.matcher(wbs);


        // 格式无效时不解析
        if (!matcher.matches()) {
            return;
        }

        //"^([^.]+\\.)*([^.]+)\\.(construction|(mech(anical)?_)?completion)((\\.[^.]+){2,9})$",
        int tailingWBSCount = matcher.group(6).split("\\.").length - 1;

        // 格式：WBS_Lv1 . ... . 建造 . ... . 模块/子系统 . 专业 . 工序阶段
        if (mode == 1) {
            sectorNo = parts.get(2);
            // 格式：WBS_Lv1 . ... . 模块 . 建造 . ... . 工序阶段 . 专业
        } else if (mode == 2) {
            sectorNo = matcher.group(2);
            // 格式：WBS_Lv1 . ... . 模块 . 建造 . ... . 工序阶段 . 专业 . 层/包
        } else if (mode == 3) {
            sectorNo = matcher.group(2);
            // 否则视为无效的 WBS 格式
        } else if (mode == 4) {
            sectorNo = parts.get(2);
            // 否则视为无效的 WBS 格式
        } else {
            return;
        }
    }

    public static Random getRandom() {
        return random;
    }

    public static void setRandom(Random random) {
        WBSEntry.random = random;
    }
}
