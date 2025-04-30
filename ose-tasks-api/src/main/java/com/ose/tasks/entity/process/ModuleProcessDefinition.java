package com.ose.tasks.entity.process;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * 项目模块工作流定义。
 */
@Entity
@Table(
    name = "project_module_process_definition",
    indexes = {
        @Index(
            name = "project_module_type",
            columnList = "orgId,projectId,funcPart,deletedAt"
        )
    }
)
public class ModuleProcessDefinition extends ModuleProcessDefinitionBase {

    private static final long serialVersionUID = 4402146985635893244L;

    @JsonIgnore
    @Lob
    @Column(length = 65535)
    private String categoryJson = null;

    @Schema(description = "任务节点 Category 属性的 ID 对应表")
    @Transient
    private Map<String, UserTaskInfo> categories = null;

    @JsonIgnore
    @Lob
    @Column(length = 65535)
    private String taskJson = null;

    @Schema(description = "任务节点 ID 属性的 Category 对应表")
    @Transient
    private Map<String, Set<String>> tasks = null;

    @JsonIgnore
    @Lob
    @Column(length = 65535)
    private String predecessorJson = null;

    @Schema(description = "前置任务节点描述")
    @Transient
    private Map<String, Set<String>> predecessors = null;

    @JsonIgnore
    @Lob
    @Column(length = 65535)
    private String successorJson = null;

    @Schema(description = "后续任务节点描述")
    @Transient
    private Map<String, Set<String>> successors = null;

    public String getCategoryJson() {
        return categoryJson;
    }

    public void setCategoryJson(String categoryJson) {
        this.categoryJson = categoryJson;
    }

    public Map<String, UserTaskInfo> getCategories() {

        if (categories == null) {
            categories = StringUtils.fromJSON(
                categoryJson,
                new TypeReference<Map<String, UserTaskInfo>>() {
                },
                new HashMap<>()
            );
        }

        return categories;
    }

    public void setCategories(Map<String, UserTaskInfo> categories) {
        this.categories = categories;
    }

    public String getTaskJson() {
        return taskJson;
    }

    public void setTaskJson(String taskJson) {
        this.taskJson = taskJson;
    }

    public Map<String, Set<String>> getTasks() {

        if (tasks == null) {
            tasks = StringUtils.fromJSON(
                taskJson,
                new TypeReference<Map<String, Set<String>>>() {
                },
                new HashMap<>()
            );
        }

        return tasks;
    }

    public void setTasks(Map<String, Set<String>> tasks) {
        this.tasks = tasks;
    }

    public String getPredecessorJson() {
        return predecessorJson;
    }

    public void setPredecessorJson(String predecessorJson) {
        this.predecessorJson = predecessorJson;
    }

    public Map<String, Set<String>> getPredecessors() {

        if (predecessors == null) {
            predecessors = StringUtils.fromJSON(
                predecessorJson,
                new TypeReference<Map<String, Set<String>>>() {
                },
                new HashMap<>()
            );
        }

        return predecessors;
    }

    public void setPredecessors(Map<String, Set<String>> predecessors) {
        this.predecessors = predecessors;
    }

    public String getSuccessorJson() {
        return successorJson;
    }

    public void setSuccessorJson(String successorJson) {
        this.successorJson = successorJson;
    }

    public Map<String, Set<String>> getSuccessors() {

        if (successors == null) {
            successors = StringUtils.fromJSON(
                successorJson,
                new TypeReference<Map<String, Set<String>>>() {
                },
                new HashMap<>()
            );
        }

        return successors;
    }

    public void setSuccessors(Map<String, Set<String>> successors) {
        this.successors = successors;
    }

    /**
     * 取得工作流定义中指定类型实体的指定工序的节点的 ID。
     *
     * @param stage         工序阶段
     * @param process       工序
     * @param entityType    实体类型
     * @param entitySubType 实体子类型
     * @return 工作流任务节点 ID
     */
    @JsonIgnore
    public String getElementIdByCategory(
        String stage,
        String process,
        String entityType,
        String entitySubType
    ) {
        UserTaskInfo userTaskInfo = getCategories().get(
            stage
                + "/" + process
                + "/" + entityType
                + (
                (entitySubType == null || entitySubType.equals(entityType))
                    ? ""
                    : ("/" + entitySubType)
            )
        );

        return userTaskInfo == null ? null : userTaskInfo.getId();
    }

    /**
     * 根据工作流任务节点 ID 取得对应的 Category 属性的集合。
     *
     * @param elementId 工作流任务节点 ID
     * @return Category 属性的集合
     */
    @JsonIgnore
    public Set<String> getCategoriesByElementId(String elementId) {
        return getTasks().get(elementId);
    }

    /**
     * 根据工作流任务节点 ID 取得对应的 Category 属性的集合。
     *
     * @param elementIDs 工作流任务节点 ID 集合
     * @return Category 属性的集合
     */
    @JsonIgnore
    public Set<String> getCategoriesByElementId(Collection<String> elementIDs) {

        if (elementIDs == null || elementIDs.size() == 0) {
            return new HashSet<>();
        }

        final Set<String> categories = new HashSet<>();

        elementIDs.forEach(elementId -> categories.addAll(getCategoriesByElementId(elementId)));

        return categories;
    }

    /**
     * 取得前置任务 ID 集合。
     *
     * @param successorId 后续任务 ID
     * @return 前置任务 ID 集合
     */
    @JsonIgnore
    public Set<String> getPredecessorIDs(String successorId) {
        return getPredecessors().get(successorId);
    }

    /**
     * 取得所有前置任务的 Category 的集合。
     *
     * @param successorId 后续任务 ID
     * @return 前置任务 Category 集合
     */
    @JsonIgnore
    public Set<String> getPredecessorCategories(String successorId) {
        return getCategoriesByElementId(getPredecessorIDs(successorId));
    }

    /**
     * 根据后续任务的 Category（工序阶段/工序/实体类型/实体子类型）取得所有前置任务的 Category。
     *
     * @param stage         工序阶段
     * @param process       工序
     * @param entityType    实体类型
     * @param entitySubType 实体子类型
     * @return 前置任务 Category 集合
     */
    public Set<PredecessorCategory> getPredecessorCategoriesBySuccessorCategory(
        String stage,
        String process,
        String entityType,
        String entitySubType
    ) {
        // 取得后续任务在工作流定义中的 UserTask 的 ID
        final String successorId = getElementIdByCategory(stage, process, entityType, entitySubType.toUpperCase());

        // 初始化所有前置任务 Category 的集合
        final Set<String> predecessorCategories = new HashSet<>();

        getSuccessors().forEach((predecessorId, successorIDs) -> {
            if (successorIDs.contains(successorId)) {
                predecessorCategories.addAll(getCategoriesByElementId(predecessorId));
            }
        });

        // 取得必要前置任务 Category 的集合
        Set<String> requiredPredecessors = getPredecessorCategories(successorId);

        // 初始化返回结果
        final Set<PredecessorCategory> predecessors = new HashSet<>();

        predecessorCategories.forEach(predecessorCategory -> predecessors.add(new PredecessorCategory(
            predecessorCategory,
            !requiredPredecessors.contains(predecessorCategory)
        )));

        return predecessors;
    }

    /**
     * 取得后续任务 ID 集合。
     *
     * @param predecessorId 前置任务 ID
     * @return 后续任务 ID 集合
     */
    @JsonIgnore
    public Set<String> getSuccessorIDs(String predecessorId) {
        return getSuccessors().get(predecessorId);
    }

    /**
     * 取得所有后续任务的 Category 的集合。
     *
     * @param predecessorId 前置任务 ID
     * @return 后续任务 Category 集合
     */
    @JsonIgnore
    public Set<String> getSuccessorCategories(String predecessorId) {
        return getCategoriesByElementId(getSuccessorIDs(predecessorId));
    }

    /**
     * 根据前置任务的 Category 取得后续任务的 Category 集合。
     *
     * @param stage         前置任务的工序阶段
     * @param process       前置任务的工序
     * @param entityType    前置任务的实体分类
     * @param entitySubType 前置任务的实体子分类
     * @return 后续任务 Category 集合
     */
    public Set<String> getSuccessorCategoriesByPredecessorCategory(
        String stage,
        String process,
        String entityType,
        String entitySubType
    ) {
        return getSuccessorCategories(getElementIdByCategory(
            stage, process, entityType, entitySubType
        ));
    }

    /**
     * 序列化节点关系数据。
     */
    public void serializeJSON() {
        categoryJson = StringUtils.toJSON(categories);
        taskJson = StringUtils.toJSON(tasks);
        predecessorJson = StringUtils.toJSON(predecessors);
        successorJson = StringUtils.toJSON(successors);
    }

    /**
     * BPMN UserTask 信息。
     */
    public static class UserTaskInfo implements Serializable {

        private static final long serialVersionUID = -419197451642440201L;

        @Schema(description = "UserTask ID")
        private String id;

        @Schema(description = "UserTask 在工作流的深度")
        private int depth;

        public UserTaskInfo() {
        }

        public UserTaskInfo(String id, int depth) {
            setId(id);
            setDepth(depth);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getDepth() {
            return depth;
        }

        public void setDepth(int depth) {
            this.depth = depth;
        }
    }

    /**
     * 前置任务分类。
     */
    public static class PredecessorCategory implements Serializable {

        private static final long serialVersionUID = 5734579247388757058L;

        // 阶段/工序/实体类型[/实体子类型1[:实体子类型2[...]]]
        @Schema(description = "工序描述")
        private String category;

        // 当为可选前置任务时，其启动状态不为其后续任务的启动条件的表要条件
        @Schema(description = "是否为可选前置任务")
        private Boolean optional = false;

        public PredecessorCategory() {
        }

        public PredecessorCategory(String category) {
            setEntitySubType(category);
        }

        public PredecessorCategory(String category, Boolean optional) {
            this(category);
            setOptional(optional);
        }

        public String getEntitySubType() {
            return category;
        }

        public void setEntitySubType(String category) {
            this.category = category;
        }

        public Boolean getOptional() {
            return optional;
        }

        public void setOptional(Boolean optional) {
            this.optional = optional;
        }
    }

}
