package com.ose.tasks.domain.model.service.plan;

import com.ose.exception.BusinessError;
import com.ose.tasks.domain.model.repository.HierarchyNodeRelationRepository;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmEntitySubTypeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessRepository;
import com.ose.tasks.domain.model.repository.p6.P6TaskPredRepository;
import com.ose.tasks.domain.model.repository.p6.P6TaskRepository;
import com.ose.tasks.domain.model.repository.p6.ProjwbsRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.SubSystemEntityRepository;
import com.ose.tasks.entity.HierarchyNodeRelation;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.p6.P6Task;
import com.ose.tasks.entity.p6.P6Taskpred;
import com.ose.tasks.entity.p6.Projwbs;
import com.ose.tasks.entity.wbs.entity.SubSystemEntityBase;
import com.ose.tasks.entity.wbs.entry.WBSEntryBase;
import com.ose.util.BeanUtils;
import com.ose.util.CollectionUtils;
import com.ose.util.CryptoUtils;
import com.ose.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class P6GenerateService implements P6GenerateInterface {

    private static List<String> stages = new ArrayList<>();

    {{
        stages.add("MC");
        stages.add("PC_ONSHORE");
        stages.add("COMM_ONSHORE");
    }
    }

    private final ProjwbsRepository projwbsRepository;

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;

    private final BpmProcessRepository bpmProcessRepository;

    private final P6TaskRepository p6TaskRepository;

    private final WBSEntryRepository wbsEntryRepository;

    private final ProjectNodeRepository projectNodeRepository;

    private final BpmEntitySubTypeRepository entityCategoryRepository;

    private final SubSystemEntityRepository subSystemEntityRepository;

    private final P6TaskPredRepository p6TaskPredRepository;

    @Autowired
    public P6GenerateService(
        ProjwbsRepository projwbsRepository,
        HierarchyNodeRelationRepository hierarchyNodeRelationRepository,
        BpmProcessRepository bpmProcessRepository,
        P6TaskRepository p6TaskRepository,
        WBSEntryRepository wbsEntryRepository,
        ProjectNodeRepository projectNodeRepository,
        BpmEntitySubTypeRepository entityCategoryRepository,
        SubSystemEntityRepository subSystemEntityRepository,
        P6TaskPredRepository p6TaskPredRepository) {
        this.projwbsRepository = projwbsRepository;
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;
        this.bpmProcessRepository = bpmProcessRepository;
        this.p6TaskRepository = p6TaskRepository;
        this.wbsEntryRepository = wbsEntryRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.entityCategoryRepository = entityCategoryRepository;
        this.subSystemEntityRepository = subSystemEntityRepository;
        this.p6TaskPredRepository = p6TaskPredRepository;
    }


    @Override
    public void createP6Wbs(Integer p6WbsId, Long projectId){

        Integer rootId = 26060;
        Projwbs root = projwbsRepository.findById(rootId).orElse(null);

        if(root == null) throw new BusinessError("ERROR");

        Integer projId = root.getProjId();

        Integer currentId = projwbsRepository.getMaxId();
        currentId++;

        Integer currentTaskId = p6TaskRepository.getMaxId();
        P6Task currentTask = p6TaskRepository.findById(currentTaskId).orElse(null);
        if(currentTask == null) throw new BusinessError();
        currentTaskId++;


        Set<ProjectNode> sspns = projectNodeRepository.findByProjectIdAndEntityTypeAndDeletedIsFalse(
            projectId, "SUB_SECTOR"
        );

        for (ProjectNode sspn : sspns) {
            Projwbs sn = projwbsRepository.findByProjIdAndWbsShortName(projId, sspn.getNo());
            if (sn != null) continue;
            String psnStr = "";
            if (sspn.getNo().substring(0, 2).equalsIgnoreCase("HS")) {
                psnStr = "HULL";
            } else {
                psnStr = "TOPSIDE";
            }
            Projwbs psn = projwbsRepository.findByProjIdAndWbsShortName(
                projId, psnStr
            );
            if (psn == null) throw new BusinessError("ERROR 1");

            sn = new Projwbs();
            sn = BeanUtils.copyProperties(psn, sn,
                "wbsId", "wbsName", "wbsShortName", "parentWbsId", "guid");
            sn.setWbsShortName(sspn.getNo());
            sn.setWbsId(currentId++);
            sn.setWbsName(sspn.getDisplayName());
            sn.setParentWbsId(psn.getWbsId());
            sn.setGuid(CryptoUtils.hashUUID(
                sn.getProjId() + sn.getWbsId() + sn.getWbsShortName()
            ));
            projwbsRepository.save(sn);
        }


        Set<ProjectNode> sypns = projectNodeRepository.findByProjectIdAndEntityTypeAndDeletedIsFalse(
            projectId, "SYSTEM"
        );

        Integer taskCodeBase = 1000;
        Integer baseTaskId = 102176;
        P6Task baseTask = p6TaskRepository.findById(baseTaskId).orElse(null);
        if(baseTask == null) throw new BusinessError();
        for (ProjectNode sypn : sypns) {
            Projwbs sn = projwbsRepository.findByProjIdAndWbsShortName(projId, sypn.getNo());
            if (sn != null) continue;
            ProjectNode psypn = projectNodeRepository.findParentByProjectIdAndId(projectId, sypn.getId());
            String psnStr = psypn.getNo();

            Projwbs psn = projwbsRepository.findByProjIdAndWbsShortName(
                projId, psnStr
            );
            if (psn == null) throw new BusinessError("ERROR 1");

            sn = new Projwbs();
            sn = BeanUtils.copyProperties(psn, sn,
                "wbsId", "wbsName", "wbsShortName", "parentWbsId", "guid");
            sn.setWbsShortName(sypn.getNo());
            sn.setWbsId(currentId++);
            sn.setWbsName(sypn.getDisplayName());
            sn.setParentWbsId(psn.getWbsId());
            sn.setGuid(CryptoUtils.hashUUID(
                sn.getProjId() + sn.getWbsId() + sn.getWbsShortName()
            ));
            projwbsRepository.save(sn);

            Set<ProjectNode> ssypns = projectNodeRepository.findByProjectIdAndParentIdAndDeletedIsFalse(
                projectId, sypn.getId()
            );

            for (ProjectNode ssypn : ssypns) {
                List<WBSEntryBase> wes = wbsEntryRepository.findSsypnByProjectIdAndNo(
                    projectId, ssypn.getNo()
                );

                for(WBSEntryBase we : wes) {
                    P6Task p6Task = null;
                    taskCodeBase = taskCodeBase + 10;
                    String taskCode = "A" + taskCodeBase;
                    p6Task = p6TaskRepository.findByProjIdAndTaskCode(projId, taskCode);
                    if(p6Task == null) {
                        p6Task = BeanUtils.copyProperties(currentTask, new P6Task());
                        p6Task.setProjId(projId);
                        p6Task.setTaskId(currentTaskId++);
                    }

                    p6Task.setWbsId(sn.getWbsId());
//                    taskCodeBase = taskCodeBase + 10;
                    p6Task.setTaskCode(taskCode);
                    p6Task.setTaskName(we.getName());
                    p6Task.setTargetStartDate(we.getStartAt());
                    p6Task.setTargetEndDate(we.getFinishAt());
                    p6Task.setGuid(CryptoUtils.hashUUID(
                        sn.getProjId() + sn.getWbsId() + sn.getWbsShortName()
                    ));
                    p6TaskRepository.save(p6Task);
                }
            }
        }

    }


    @Override
    public void createFullP6Wbs(String p6WbsCode, Long projectId) {

        Projwbs root = projwbsRepository.findByWbsShortName(p6WbsCode);
//        Projwbs root = projwbsRepository.findByWbsShortNameAndParentWbsIdIsNull(p6WbsCode);

//        Projwbs rootF = projwbsRepository.findByWbsShortNameAndParentWbsIdIsNull("MADURA_FPU");
        Projwbs rootF = projwbsRepository.findByWbsShortName("MADURA_FPU");
        Integer origProjId = rootF.getProjId();

        if (root == null) throw new BusinessError("ERROR");

        Integer projId = root.getProjId();

        Integer currentId = projwbsRepository.getMaxId();
        currentId++;

        Integer currentTaskId = p6TaskRepository.getMaxId();
        P6Task currentTask = p6TaskRepository.findById(currentTaskId).orElse(null);
        if (currentTask == null) throw new BusinessError();
        currentTaskId++;

        Integer taskCodeBase = 1000;
        Integer baseTaskId = 102176;
        P6Task baseTask = p6TaskRepository.findById(baseTaskId).orElse(null);

        Projwbs mcRoot = projwbsRepository.findByProjIdAndWbsShortName(projId, "COMPLETION");
//        Projwbs pcRoot = projwbsRepository.findByProjIdAndWbsShortName(projId, "PC_ONSHORE");
//        Projwbs commRoot = projwbsRepository.findByProjIdAndWbsShortName(projId, "COMM_ONSHORE");

        //MC PART
        Set<Projwbs> mcWbses = getSonIds(projId, mcRoot.getWbsId());

        for (Projwbs mcWbs : mcWbses) {//            Projwbs sn = projwbsRepository.findByProjIdAndWbsShortName(projId, sypn.getNo());
            if (mcWbs == null) continue;
            ProjectNode sysPn = projectNodeRepository.findByProjectIdAndNoAndDeletedIsFalse(projectId, mcWbs.getWbsShortName()).orElse(null);
            if (sysPn == null) throw new BusinessError("1");

            Set<ProjectNode> subSysPns = projectNodeRepository.findByProjectIdAndParentIdAndDeletedIsFalse(projectId, sysPn.getId());

            for (ProjectNode subSysPn : subSysPns) {
                SubSystemEntityBase ss = subSystemEntityRepository.findById(subSysPn.getEntityId()).orElse(null);
//                String psnStr = subSysPn.getNo();
//
//                Projwbs psn = projwbsRepository.findByProjIdAndWbsShortName(
//                    projId, psnStr
//                );
//                if (psn == null) throw new BusinessError("ERROR 1");

                Projwbs sn = new Projwbs();
                sn = BeanUtils.copyProperties(mcWbs, sn,
                    "wbsId", "wbsName", "wbsShortName", "parentWbsId", "guid");
                sn.setWbsShortName(subSysPn.getNo());
                sn.setWbsId(currentId++);
                sn.setWbsName(ss.getNo() + " " + ss.getDescription());
                sn.setParentWbsId(mcWbs.getWbsId());
                sn.setGuid(CryptoUtils.hashUUID(
                    sn.getProjId() + sn.getWbsId() + sn.getWbsShortName()
                ));
                projwbsRepository.save(sn);

                List<P6Task> origSubSysTasks = p6TaskRepository.findByProjIdAndSubSysNo(origProjId, subSysPn.getNo());
                P6Task origSubSysTask = null;
                if (!CollectionUtils.isEmpty(origSubSysTasks)) {
                    origSubSysTask = origSubSysTasks.get(0);
                }

                for (String stage : stages) {
                    boolean requireComm = false;
                    Projwbs stageWbs = new Projwbs();
                    stageWbs = BeanUtils.copyProperties(mcWbs, stageWbs,
                        "wbsId", "wbsName", "wbsShortName", "parentWbsId", "guid");
                    stageWbs.setWbsShortName(stage);
                    stageWbs.setWbsId(currentId++);
                    stageWbs.setWbsName(subSysPn.getDisplayName() + " " + stage + " STAGE");
                    stageWbs.setParentWbsId(sn.getWbsId());
                    stageWbs.setGuid(CryptoUtils.hashUUID(
                        stageWbs.getProjId() + stageWbs.getWbsId() + stageWbs.getWbsShortName()
                    ));
                    projwbsRepository.save(stageWbs);


                    /////// task add here
                    List<BpmProcess> bps = bpmProcessRepository.findByProjectIdAndProcessStage(projectId, stage);

//                Set<Long> mcBpIds = bps.stream().map(BpmProcess::getId).collect(Collectors.toSet());
                    for (BpmProcess bp : bps) {
                        Set<String> subEntityTypes = entityCategoryRepository.findEntitySubTypesByProjectIdAndProcessIn(projectId, bp.getId());
                        subEntityTypes.remove("SUB_SYSTEM");
                        List<HierarchyNodeRelation> hnrs = hierarchyNodeRelationRepository.findByProjectIdAndAncestorEntityIdAndEntitySubTypeIn(
                            projectId, subSysPn.getEntityId(), subEntityTypes
                        );
                        if (CollectionUtils.isEmpty(hnrs)) continue;
                        // 增加工序 TASK
                        P6Task p6Task = null;
                        taskCodeBase = taskCodeBase + 10;
                        String taskCode = "A" + taskCodeBase;
                        p6Task = p6TaskRepository.findByProjIdAndTaskCode(projId, taskCode);
                        if (p6Task == null) {
                            p6Task = BeanUtils.copyProperties(currentTask, new P6Task());
                            p6Task.setProjId(projId);
                            p6Task.setTaskId(currentTaskId++);
                        }

                        p6Task.setWbsId(stageWbs.getWbsId());
//                    taskCodeBase = taskCodeBase + 10;
                        p6Task.setTaskCode(taskCode);
                        p6Task.setTaskName(bp.getNameEn() + " " + bp.getNameCn());
                        double dura = 16d;
                        int fig = 0;

                        if (origSubSysTask != null) {
                            if (stage.equalsIgnoreCase("MC")) {
                                fig = -10;

                            } else if (stage.equalsIgnoreCase("PC_ONSHORE")) {
                                fig = -5;
                            } else if (origSubSysTask != null) {
                                dura = origSubSysTask.getTargetDrtnHrCnt();
                            }
                            p6Task.setTargetStartDate(DateUtils.addDate(origSubSysTask.getTargetStartDate(), fig));
                            p6Task.setTargetEndDate(DateUtils.addDate(origSubSysTask.getTargetEndDate(), fig));
                        }
                        p6Task.setTargetDrtnHrCnt(dura);
                        p6Task.setRemainDrtnHrCnt(dura);
                        p6Task.setTaskType("TT_Task");
                        p6Task.setDurationType("DT_FixedDUR2");
                        p6Task.setGuid(CryptoUtils.hashUUID(
                            p6Task.getProjId() + p6Task.getWbsId() + p6Task.getTaskCode()
                        ));
                        p6TaskRepository.save(p6Task);

                        if (stage.equalsIgnoreCase("MC")) {
                            // 增加工序 TASK
                            P6Task p6TaskIrn = null;
                            taskCodeBase = taskCodeBase + 10;
                            taskCode = "A" + taskCodeBase;
                            p6TaskIrn = p6TaskRepository.findByProjIdAndTaskCode(projId, taskCode);
                            if (p6TaskIrn == null) {
                                p6TaskIrn = BeanUtils.copyProperties(currentTask, new P6Task());
                                p6TaskIrn.setProjId(projId);
                                p6TaskIrn.setTaskId(currentTaskId++);
                            }

                            p6TaskIrn.setWbsId(stageWbs.getWbsId());
                            p6TaskIrn.setTaskCode(taskCode);
                            p6TaskIrn.setTaskName(bp.getNameEn().replaceAll("ITR", "IRN" ));
                            if (origSubSysTask != null) {

                                p6TaskIrn.setTargetStartDate(DateUtils.addDate(origSubSysTask.getTargetStartDate(), fig));
                                p6TaskIrn.setTargetEndDate(DateUtils.addDate(origSubSysTask.getTargetEndDate(), fig));
                            }
                            p6TaskIrn.setTargetDrtnHrCnt(8d);
                            p6TaskIrn.setRemainDrtnHrCnt(8d);
                            p6TaskIrn.setTaskType("TT_Task");
                            p6TaskIrn.setDurationType("DT_FixedDUR2");
                            p6TaskIrn.setGuid(CryptoUtils.hashUUID(
                                p6TaskIrn.getProjId() + p6TaskIrn.getWbsId() + p6TaskIrn.getTaskCode()
                            ));
                            p6TaskRepository.save(p6TaskIrn);

                        } else if (stage.equalsIgnoreCase("COMM_ONSHORE")) {
                            requireComm = true;
                        }

                    }

                    if(stage.equalsIgnoreCase("PC_ONSHORE")) {
                        // 增加工序 TASK
                        P6Task p6TaskRfcc = null;
                        taskCodeBase = taskCodeBase + 10;
                        String taskCode = "A" + taskCodeBase;
                        taskCode = "A" + taskCodeBase;
                        p6TaskRfcc = p6TaskRepository.findByProjIdAndTaskCode(projId, taskCode);
                        if (p6TaskRfcc == null) {
                            p6TaskRfcc = BeanUtils.copyProperties(currentTask, new P6Task());
                            p6TaskRfcc.setProjId(projId);
                            p6TaskRfcc.setTaskId(currentTaskId++);
                        }

                        p6TaskRfcc.setWbsId(stageWbs.getWbsId());
                        p6TaskRfcc.setTaskCode(taskCode);
                        p6TaskRfcc.setTaskName(" PC_ONSHORE_RFCC " + subSysPn.getNo());
                        if (origSubSysTask != null) {

                            p6TaskRfcc.setTargetStartDate(DateUtils.addDate(origSubSysTask.getTargetStartDate(), -5));
                            p6TaskRfcc.setTargetEndDate(DateUtils.addDate(origSubSysTask.getTargetEndDate(), -5));
                        }
                        p6TaskRfcc.setTargetDrtnHrCnt(8d);
                        p6TaskRfcc.setRemainDrtnHrCnt(8d);
                        p6TaskRfcc.setTaskType("TT_Task");
                        p6TaskRfcc.setDurationType("DT_FixedDUR2");
                        p6TaskRfcc.setGuid(CryptoUtils.hashUUID(
                            p6TaskRfcc.getProjId() + p6TaskRfcc.getWbsId() + p6TaskRfcc.getTaskCode()
                        ));
                        p6TaskRepository.save(p6TaskRfcc);
                    }

                    if(stage.equalsIgnoreCase("COMM_ONSHORE")) {
                        if (requireComm) {
                            // 增加工序 TASK
                            P6Task p6TaskRfsu = null;
                            taskCodeBase = taskCodeBase + 10;
                            String taskCode = "A" + taskCodeBase;
                            taskCode = "A" + taskCodeBase;
                            p6TaskRfsu = p6TaskRepository.findByProjIdAndTaskCode(projId, taskCode);
                            if (p6TaskRfsu == null) {
                                p6TaskRfsu = BeanUtils.copyProperties(currentTask, new P6Task());
                                p6TaskRfsu.setProjId(projId);
                                p6TaskRfsu.setTaskId(currentTaskId++);
                            }

                            p6TaskRfsu.setWbsId(stageWbs.getWbsId());
                            p6TaskRfsu.setTaskCode(taskCode);
                            p6TaskRfsu.setTaskName("COMM_ONSHORE_RFSU " + subSysPn.getNo());
                            if (origSubSysTask != null) {

                                p6TaskRfsu.setTargetStartDate(DateUtils.addDate(origSubSysTask.getTargetStartDate(), 0));
                                p6TaskRfsu.setTargetEndDate(DateUtils.addDate(origSubSysTask.getTargetEndDate(), 0));
                            }
                            p6TaskRfsu.setTargetDrtnHrCnt(8d);
                            p6TaskRfsu.setRemainDrtnHrCnt(8d);
                            p6TaskRfsu.setTaskType("TT_Task");
                            p6TaskRfsu.setDurationType("DT_FixedDUR2");
                            p6TaskRfsu.setGuid(CryptoUtils.hashUUID(
                                p6TaskRfsu.getProjId() + p6TaskRfsu.getWbsId() + p6TaskRfsu.getTaskCode()
                            ));
                            p6TaskRepository.save(p6TaskRfsu);

                        }


                        P6Task p6TaskTcp = null;
                        taskCodeBase = taskCodeBase + 10;
                        String taskCode = "A" + taskCodeBase;
                        taskCode = "A" + taskCodeBase;
                        p6TaskTcp = p6TaskRepository.findByProjIdAndTaskCode(projId, taskCode);
                        if (p6TaskTcp == null) {
                            p6TaskTcp = BeanUtils.copyProperties(currentTask, new P6Task());
                            p6TaskTcp.setProjId(projId);
                            p6TaskTcp.setTaskId(currentTaskId++);
                        }

                        p6TaskTcp.setWbsId(stageWbs.getWbsId());
                        p6TaskTcp.setTaskCode(taskCode);
                        p6TaskTcp.setTaskName("COMM_ONSHORE_OTP " +subSysPn.getNo());
                        if (origSubSysTask != null) {

                            p6TaskTcp.setTargetStartDate(DateUtils.addDate(origSubSysTask.getTargetStartDate(), 0));
                            p6TaskTcp.setTargetEndDate(DateUtils.addDate(origSubSysTask.getTargetEndDate(), 0));
                        }
                        p6TaskTcp.setTargetDrtnHrCnt(8d);
                        p6TaskTcp.setRemainDrtnHrCnt(8d);
                        p6TaskTcp.setTaskType("TT_Task");
                        p6TaskTcp.setDurationType("DT_FixedDUR2");
                        p6TaskTcp.setGuid(CryptoUtils.hashUUID(
                            p6TaskTcp.getProjId() + p6TaskTcp.getWbsId() + p6TaskTcp.getTaskCode()
                        ));
                        p6TaskRepository.save(p6TaskTcp);

                    }


                }



                /////////}
            }
        }

    }

    private Set<Projwbs> getSonIds(Integer projId, Integer wbsId) {

        Set<Integer> wbsIds = new HashSet<>();
        wbsIds.add(wbsId);
        Set<Projwbs> fpws = projwbsRepository.findByProjIdAndParentWbsIdIn(projId, wbsIds); //sector
        wbsIds.clear();

        wbsIds = fpws.stream().map(Projwbs::getWbsId).collect(Collectors.toSet());

        fpws = projwbsRepository.findByProjIdAndParentWbsIdIn(projId, wbsIds);//sub-sector
        wbsIds.clear();

        wbsIds = fpws.stream().map(Projwbs::getWbsId).collect(Collectors.toSet());

        fpws = projwbsRepository.findByProjIdAndParentWbsIdIn(projId, wbsIds);//system

        return fpws;
//        wbsIds.clear();
//
//        wbsIds = fpws.stream().map(Projwbs::getWbsId).collect(Collectors.toSet());


    }

    private Projwbs getToBeWbs(Integer projId, String precomm, String no) {
        Set<Integer> wbsIds = new HashSet<>();

        Projwbs projwbs = projwbsRepository.findByProjIdAndWbsShortName(projId, precomm);
        if(projwbs == null) return null;
        wbsIds.add(projwbs.getWbsId());
        Set<Projwbs> fpws = projwbsRepository.findByProjIdAndParentWbsIdIn(projId, wbsIds); //sector
        wbsIds.clear();

        wbsIds = fpws.stream().map(Projwbs::getWbsId).collect(Collectors.toSet());

        fpws = projwbsRepository.findByProjIdAndParentWbsIdIn(projId, wbsIds);//sub-sector
        wbsIds.clear();

        wbsIds = fpws.stream().map(Projwbs::getWbsId).collect(Collectors.toSet());

        Projwbs fpw = projwbsRepository.findByProjIdAndParentWbsIdInAndWbsShortName(projId, wbsIds, no);//system

        return fpw;
    }


    @Override
    public void createMcP6Wbs(String p6WbsCode, Long projectId) {
        p6WbsCode = "MADURA_FPU_COMM";
        Projwbs root = projwbsRepository.findByWbsShortName(p6WbsCode);
        Integer maxPredId = p6TaskPredRepository.getMaxId();
//        maxPredId++;
        P6Taskpred p6TaskpredMax = p6TaskPredRepository.findById(maxPredId).orElse(null);
        if(p6TaskpredMax == null) return;


        if (root == null) throw new BusinessError("CANT FIND ERROR");
        Projwbs rootF = projwbsRepository.findByWbsShortName("MADURA_FPU");
        Integer origProjId = rootF.getProjId();

        Integer projId = root.getProjId();

        Integer currentId = projwbsRepository.getMaxId();
        currentId++;

        Integer currentTaskId = p6TaskRepository.getMaxId();
        P6Task currentTask = p6TaskRepository.findById(currentTaskId).orElse(null);
        if (currentTask == null) throw new BusinessError();
        currentTaskId++;

        Integer taskCodeBase = 1000;
        Integer baseTaskId = 102176;
        String baseTask = p6TaskRepository.getMaxCode();
        taskCodeBase = Integer.parseInt(baseTask.replaceAll("A",""));

        Projwbs mcRoot = projwbsRepository.findByProjIdAndWbsShortName(projId, "COMMISSIONING");
        Projwbs mcToBeAddRoot = projwbsRepository.findByProjIdAndWbsShortName(projId, "PRECOMM");
//        Projwbs pcRoot = projwbsRepository.findByProjIdAndWbsShortName(projId, "PC_ONSHORE");
//        Projwbs commRoot = projwbsRepository.findByProjIdAndWbsShortName(projId, "COMM_ONSHORE");

        //MC PART
        Set<Projwbs> mcWbses = getSonIds(projId, mcRoot.getWbsId());

        for (Projwbs mcWbs : mcWbses) {//            Projwbs sn = projwbsRepository.findByProjIdAndWbsShortName(projId, sypn.getNo());
            if (mcWbs == null) continue;
            ProjectNode sysPn = projectNodeRepository.findByProjectIdAndNoAndDeletedIsFalse(projectId, mcWbs.getWbsShortName()).orElse(null);
            if (sysPn == null) throw new BusinessError("1");

            Projwbs toAddWbs = getToBeWbs(projId, "PRECOMM", sysPn.getNo());

            Set<ProjectNode> subSysPns = projectNodeRepository.findByProjectIdAndParentIdAndDeletedIsFalse(projectId, sysPn.getId());

            for (ProjectNode subSysPn : subSysPns) {
                SubSystemEntityBase ss = subSystemEntityRepository.findById(subSysPn.getEntityId()).orElse(null);
                P6Task p6Task = null;

                List<P6Task> origSubSysTasks = p6TaskRepository.findByProjIdAndSubSysNo(projId, subSysPn.getNo());
                P6Task origSubSysTask = null;
                if (!CollectionUtils.isEmpty(origSubSysTasks)) {
                    if(origSubSysTasks.size() == 2) {
                        origSubSysTask = origSubSysTasks.get(0);
                        p6Task = origSubSysTasks.get(1);
                    } else {
                        origSubSysTask = origSubSysTasks.get(0);
                        // 增加 TASK
                        taskCodeBase = taskCodeBase + 10;
                        String taskCode = "A" + taskCodeBase;
                        p6Task = p6TaskRepository.findByProjIdAndTaskCode(projId, taskCode);
                        if (p6Task == null) {
                            p6Task = BeanUtils.copyProperties(currentTask, new P6Task());
                            p6Task.setProjId(projId);
                            p6Task.setTaskId(currentTaskId++);
                        }
                    }
                } else {
                    continue;
                }



                p6Task.setWbsId(toAddWbs.getWbsId());
                p6Task.setTaskCode(origSubSysTask.getTaskCode());
                p6Task.setTaskName(origSubSysTask.getTaskName());
                double dura = 16d;
                int fig = -10;

                if (origSubSysTask != null) {
                    p6Task.setTargetStartDate(DateUtils.addDate(origSubSysTask.getTargetStartDate(), fig));
                    p6Task.setTargetEndDate(DateUtils.addDate(origSubSysTask.getTargetEndDate(), fig));
                }
                p6Task.setTargetDrtnHrCnt(dura);
                p6Task.setRemainDrtnHrCnt(dura);
                p6Task.setTaskType("TT_Task");
                p6Task.setDurationType("DT_FixedDUR2");
                p6Task.setGuid(CryptoUtils.hashUUID(
                    p6Task.getProjId() + p6Task.getWbsId() + p6Task.getTaskCode()
                ));
                p6TaskRepository.save(p6Task);

                P6Taskpred p6Taskpred = BeanUtils.copyProperties(p6TaskpredMax, new P6Taskpred());

                p6Taskpred.setProjId(projId);
                p6Taskpred.setCreateDate(new Date());
                p6Taskpred.setLagHrCnt(-5*8d);
                p6Taskpred.setPredTaskId(origSubSysTask.getTaskId());
                p6Taskpred.setTaskId(p6Task.getTaskId());
                p6Taskpred.setPredProjId(projId);
                p6Taskpred.setTaskPredId(++maxPredId);
                p6Taskpred.setPredType("PR_SF");
                p6TaskPredRepository.save(p6Taskpred);

            }
        }

    }
}
