package com.ose.tasks.dto.process;

import com.ose.tasks.entity.process.EntityProcess;
import com.ose.tasks.entity.process.EntityType;
import com.ose.tasks.entity.process.ProcessEntityType;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class EntityProcessRelationsDTO {

    // 实体类型集合（实体类型/实体子类型）
    private final Set<String> entityTypes = new HashSet<>();

    // 实体工序集合（工序阶段/工序）
    private final Set<String> processes = new HashSet<>();

    // 实体类型的工序集合（工序阶段/工序/实体类型/实体子类型）
    private final Set<String> entityProcesses = new HashSet<>();

    /**
     * 构造方法。
     */
    public EntityProcessRelationsDTO() {
    }

    /**
     * 构造方法。
     */
    public EntityProcessRelationsDTO(
        List<EntityType> entityTypes,
        List<EntityProcess> processes,
        List<ProcessEntityType> processEntityTypes
    ) {

        entityTypes.forEach(entityType -> {
            this.entityTypes.add(entityType.getEntityType());
            this.entityTypes.add(entityType.getId());
        });

        processes.forEach(process -> {
            this.processes.add(process.getStageName());
            this.processes.add(process.getId());
        });

        processEntityTypes.forEach(processEntityType -> {
            if (processEntityType.getId() != null) {
                this.entityProcesses.add(processEntityType.getId());
            }
        });

    }

    public boolean doesEntityTypeExist(String entityType) {
        return entityTypes.contains(entityType);
    }

    public boolean doesEntitySubTypeExist(String entityType, String entitySubType) {
        return entitySubType == null
            ? doesEntityTypeExist(entityType)
            : entityTypes.contains(entityType + "/" + entitySubType);
    }

    public boolean doEntitySubTypesExist(String entityType, Set<String> entitySubTypes) {

        Iterator<String> it = entitySubTypes.iterator();

        while (it.hasNext()) {
            if (!doesEntitySubTypeExist(entityType, it.next())) {
                return false;
            }
        }

        return true;
    }

    public boolean doesProcessStageExist(String stageName) {
        return processes.contains(stageName);
    }

    public boolean doesProcessExist(String stageName, String processName) {
        return processName == null
            ? doesProcessStageExist(stageName)
            : processes.contains(stageName + "/" + processName);
    }

    public boolean doesProcessEntityRelationExist(
        String stageName,
        String processName,
        String entityType,
        String entitySubType
    ) {
        return entityProcesses.contains(
            stageName
                + "/" + processName
                + "/" + entityType
                + (
                (entitySubType == null || entitySubType.equals(entityType))
                    ? ""
                    : ("/" + entitySubType)
            )
        );
    }

    public boolean doesProcessEntityRelationsExist(
        String stageName,
        String processName,
        String entityType,
        Set<String> entitySubTypes
    ) {

        if (entitySubTypes != null && entitySubTypes.size() > 0) {

            Iterator<String> it = entitySubTypes.iterator();

            while (it.hasNext()) {
                if (!doesProcessEntityRelationExist(stageName, processName, entityType, it.next())) {
                    return false;
                }
            }

        } else {
            return doesProcessEntityRelationExist(stageName, processName, entityType, null);
        }

        return true;
    }


}
