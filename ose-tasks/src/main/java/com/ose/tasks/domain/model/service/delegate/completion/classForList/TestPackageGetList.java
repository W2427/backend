package com.ose.tasks.domain.model.service.delegate.completion.classForList;

import com.ose.tasks.domain.model.repository.HierarchyNodeRelationRepository;
import com.ose.tasks.entity.HierarchyNodeRelation;
import com.ose.tasks.entity.wbs.entity.ComponentEntity;
import com.ose.tasks.entity.wbs.entity.ISOEntity;
import com.ose.tasks.entity.wbs.entity.LineEntity;
import com.ose.tasks.entity.wbs.entity.SpoolEntity;
import com.ose.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 外检申请 代理服务。
 */
@Component
public class TestPackageGetList implements BaseClassForListInterface {

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;


    @Autowired
    public TestPackageGetList(
                              HierarchyNodeRelationRepository hierarchyNodeRelationRepository) {
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;
    }


    @Override
    public List<List> getItemLists(Long projectId, Long superEntityId) {


        List<ISOEntity> isos = new ArrayList<>();// isoEntityRepository.getIsosByTestPackageId(projectId, superEntityId);

        List<SpoolEntity> spools = new ArrayList<>();// spoolEntityRepository.getSpoolsByTestPackageId(projectId, superEntityId);

        List<ComponentEntity> componentEntities = new ArrayList<>();//componentEntityRepository.getComponentsByTestPackageId(projectId, superEntityId);

        List<HierarchyNodeRelation> hnrs = hierarchyNodeRelationRepository.
            findByProjectIdAndAncestorEntityIdAndEntityType(projectId, superEntityId, "SPOOL");


        List<HierarchyNodeRelation> isoHnrs = hierarchyNodeRelationRepository.
            findByProjectIdAndAncestorEntityIdAndEntityType(projectId, superEntityId, "ISO");


        Map<Long, LineEntity> lineIdMap = new HashMap<>();
        Map<Long, ISOEntity> isoIdMap = new HashMap<>();
        Map<Long, SpoolEntity> spoolIdMap = new HashMap<>();
        Map<Long, ComponentEntity> componentIdMap = new HashMap<>();
        spools.forEach(spoolEntity -> {
            spoolIdMap.put(spoolEntity.getId(), spoolEntity);
        });
        isos.forEach(iso->isoIdMap.put(iso.getId(), iso));
        componentEntities.forEach(componentEntity -> componentIdMap.put(componentEntity.getId(), componentEntity));

        List<String> spoolNos = new ArrayList<>();
        Map<String, Long> spoolNoIdMap = new HashMap<>();
        Map<Long, Long> spoolIdIsoIdMap = new HashMap<>();
        Map<Long, Long> isoIdLineIdMap = new HashMap<>();
        Map<Long, Long> spoolIdLineIdMap = new HashMap<>();
        Set<Long> usedLineIds = new HashSet<>();

        List<Map> spoolInfos = new ArrayList<>();

        hnrs.forEach(hnr-> {
            SpoolEntity se = spoolIdMap.get(hnr.getEntityId());
            if(se == null) return;
            spoolNos.add(se.getNo());
            spoolNoIdMap.put(se.getNo(), se.getId());

        });

        Collections.sort(spoolNos);
        spoolNos.forEach(spoolNo->{

            Long spId = spoolNoIdMap.get(spoolNo);
            Long isoId = 0L;
            Long lineId = 0L;

            List<HierarchyNodeRelation> hss = hierarchyNodeRelationRepository.findHnrsByProjectIdAndEntityId(projectId, spId);
            for (HierarchyNodeRelation hs : hss) {
                if ("ISO".equalsIgnoreCase(hs.getAncestorEntityType())) {
                    isoId = hs.getAncestorEntityId();
                } else if ("LINE".equalsIgnoreCase(hs.getAncestorEntityType())) {
                    lineId = hs.getAncestorEntityId();
                }
            }
            Map<String, String> spoolLine = new HashMap<>();
            String isoNo = isoIdMap.get(isoId) == null? null: isoIdMap.get(isoId).getNo();
            LineEntity le = lineIdMap.get(lineId);
            String lineNo = le == null? null: le.getNo();
            String pid = le == null? null: le.getPidDrawing() + "_Rev. " + le.getRevision();
            spoolLine.put("pid", pid);
            spoolLine.put("lineNo", lineNo);
            spoolLine.put("isoNo", isoNo);
            spoolLine.put("spoolNo", spoolNo);
            spoolIdIsoIdMap.put(spId, isoId);
            spoolIdLineIdMap.put(spId, lineId);
            usedLineIds.add(lineId);
            spoolInfos.add(spoolLine);

        });


        List<String> isoNos = new ArrayList<>();
        Map<String, Long> isoNoIdMap = new HashMap<>();
        isoHnrs.forEach(isoHnr-> {
            ISOEntity ie = isoIdMap.get(isoHnr.getEntityId());
            if(ie == null) return;
            if(spoolIdIsoIdMap.values().contains(ie.getId())) return;
            isoNos.add(ie.getNo());
            isoNoIdMap.put(ie.getNo(), ie.getId());
        });
        List<Map> isoInfos = new ArrayList<>();
        Collections.sort(isoNos);
        isoNos.forEach(isoNo-> {

            Long isoId = isoNoIdMap.get(isoNo);
            Long lineId = 0L;

            List<HierarchyNodeRelation> hss = hierarchyNodeRelationRepository.findHnrsByProjectIdAndEntityId(projectId, isoId);
            for (HierarchyNodeRelation hs : hss) {
                if ("LINE".equalsIgnoreCase(hs.getAncestorEntityType())) {
                    lineId = hs.getAncestorEntityId();
                }
            }
            Map<String, String> isoLine = new HashMap<>();
            LineEntity le = lineIdMap.get(lineId);
            String lineNo = le == null ? null : le.getNo();
            String pid = le == null ? null : le.getPidDrawing() + "_Rev. " + le.getRevision();
            isoLine.put("pid", pid);
            isoLine.put("lineNo", lineNo);
            isoLine.put("isoNo", isoNo);
            isoLine.put("spoolNo", "");
            usedLineIds.add(lineId);
            isoInfos.add(isoLine);
        });

        spoolInfos.addAll(isoInfos);

        List<String> entityTypes = new ArrayList<>();
        entityTypes.add("PIPE_COMPONENT");
        entityTypes.add("COMPONENT");
        entityTypes.add("VALVE");


        Set<Long> lineIds = new HashSet<>(spoolIdLineIdMap.values());
        lineIdMap.values().forEach(lid -> lineIds.add(lid.getId()));
        List<HierarchyNodeRelation> hnrComs = hierarchyNodeRelationRepository.
            findByProjectIdAndAncestorEntityIdInAndEntityTypeIn(
                projectId, lineIds, entityTypes);

        List<Map> componentInfos = new ArrayList<>();
        hnrComs.forEach(hnrCom ->{
            Map<String, String> componentInfo = new HashMap<>();
            ComponentEntity component = componentIdMap.get(hnrCom.getEntityId());
            if(component == null) return;
            LineEntity lett = lineIdMap.get(hnrCom.getAncestorEntityId());
            if(lett == null) return;
            componentInfo.put("pid", lett.getPidDrawing() + "_Rev. " + lett.getRevision());
            componentInfo.put("lineNo", lett.getNo());
            componentInfo.put("componentNo", component.getNo());
            componentInfo.put("description", component.getShortCode());
            componentInfos.add(componentInfo);
        });
        if(CollectionUtils.isEmpty(componentInfos)) {
            Map<String, String> componentInfo = new HashMap<>();
            componentInfo.put("pid", "");
            componentInfo.put("lineNo", "");
            componentInfo.put("componentNo", "");
            componentInfo.put("description", "");
            componentInfos.add(componentInfo);
        }

        List<List> list = new ArrayList<>();
        list.add(spoolInfos);
        list.add(componentInfos);


        return list;
    }

    /*
    //        pageIs.addAll(pageIndexes1);

        List<ISOEntity> isos = isoEntityRepository.getIsosByTestPackageId(projectId, testPackageId);
        Set<String> isoNos = isos.stream().map(ISOEntity::getNo).collect(Collectors.toSet());

        List<SpoolEntity> spools = spoolEntityRepository.getSpoolsByTestPackageId(projectId, testPackageId);
        Set<String> spoolNos = spools.stream().map(SpoolEntity::getNo).collect(Collectors.toSet());

        List<ComponentEntity> componentEntities = componentEntityRepository.getComponentsByTestPackageId(projectId, testPackageId);
        Set<String> componentNos = componentEntities.stream().map(ComponentEntity::getNo).collect(Collectors.toSet());

     */
}
