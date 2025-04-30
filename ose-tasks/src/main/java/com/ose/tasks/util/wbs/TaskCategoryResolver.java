package com.ose.tasks.util.wbs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 任务节点分类解析器。
 */
public class TaskCategoryResolver {

    // 任务节点分类值格式：工序阶段/工序/实体类型[/子实体类型1[:子实体类型2[:...]]]
    private static final Pattern TASK_CATEGORY_PATTERN = Pattern
        .compile("^([^\\/]+)/([^\\/]+)/([^\\/]+)(/([^\\/]+))?$");

    // 输入的值是否有效
    private Boolean valid = null;

    // 工序阶段
    private String stage = "";

    // 工序
    private String process = "";

    // 实体类型
    private String entityType = "";

    private String entityTypeEnum = null;

    // 实体子类型集合
    private Set<String> entitySubTypes = new HashSet<>();

    // 实体子类型枚举集合
    private Set<String> entitySubTypeEnums = new HashSet<>();

    /**
     * 构造方法。
     *
     * @param taskCategory 任务节点分类
     */
    public TaskCategoryResolver(String taskCategory) {

        if (taskCategory == null) {
            return;
        }

        Matcher matcher = TASK_CATEGORY_PATTERN.matcher(taskCategory);

        if (!matcher.matches()) {
            valid = false;
            return;
        }

        valid = true;
        stage = matcher.group(1);
        process = matcher.group(2);
        entityType = matcher.group(3);
        entityTypeEnum = entityType;

        if (matcher.groupCount() < 5 || matcher.group(5) == null) {
            return;
        }

        entitySubTypes.addAll(Arrays.asList(matcher.group(5).split(":")));

        entitySubTypeEnums.addAll(entitySubTypes);
    }

    /**
     * 取得有效性检查结果。
     *
     * @return 任务节点分类是否有效
     */
    public Boolean getValid() {
        return valid;
    }

    /**
     * 取得工序阶段名称。
     *
     * @return 工序阶段名称
     */
    public String getStage() {
        return stage;
    }

    /**
     * 取得工序名称。
     *
     * @return 工序名称
     */
    public String getProcess() {
        return process;
    }

    /**
     * 取得实体类型。
     *
     * @return 实体类型
     */
    public String getEntityType() {
        return entityType;
    }

    /**
     * 取得实体类型。
     *
     * @return 实体类型
     */
    public String getEntityTypeEnum() {
        return entityTypeEnum;
    }

    /**
     * 取得实体子类型集合。
     *
     * @return 实体子类型集合
     */
    public Set<String> getEntitySubTypes() {
        return entitySubTypes;
    }

    /**
     * 取得实体子类型集合。
     *
     * @return 实体子类型集合
     */
    public Set<String> getEntitySubTypeEnums() {
        return entitySubTypeEnums;
    }

    /**
     * 判断给定的任务节点分类是否与当前分类匹配。
     *
     * @param targetTaskCategory 任务节点分类
     * @return 是否匹配
     */
    public boolean matches(String targetTaskCategory) {
        return matches(new TaskCategoryResolver(targetTaskCategory));
    }

    /**
     * 判断给定的任务节点分类是否与当前分类匹配。
     *
     * @param target 任务节点分类解析结果
     * @return 是否匹配
     */
    private boolean matches(TaskCategoryResolver target) {

        Set<String> intersection = new HashSet<>(entitySubTypes);

        intersection.removeAll(target.entitySubTypes);

        return stage.equals(target.stage)
            && process.equals(target.process)
            && entityType.equals(target.entityType)
            && (entitySubTypes.size() == target.entitySubTypes.size())
            && (entitySubTypes.size() == 0 || intersection.size() > 0);
    }

    /**
     * 检查实体类型是否一致。
     *
     * @param category1 实体类型1
     * @param category2 实体类型2
     * @return 是否一致
     */
    public static boolean entityMatches(String category1, String category2) {
        return (new TaskCategoryResolver(category1)).entityMatches(category2);
    }

    /**
     * 检查实体类型是否与当前实体类型一致。
     *
     * @param category 实体类型
     * @return 是否一致
     */
    public boolean entityMatches(String category) {
        return entityMatches(new TaskCategoryResolver(category));
    }

    /**
     * 检查实体类型是否与当前实体类型一致。
     *
     * @param target 实体类型
     * @return 是否一致
     */
    public boolean entityMatches(TaskCategoryResolver target) {
        return entityType.equals(target.getEntityType())
            && (entitySubTypes.size() == target.entitySubTypes.size())
            && (entitySubTypes.size() == 0 || (new HashSet<>(entitySubTypes)).containsAll(target.entitySubTypes));
    }

    public static void main(String[] args){

        String taskCategory = "/MC/TEST_PACKAGE_MC_ITR/TEST_PACKAGE/TEST_PACKAGE";
        Matcher matcher = TASK_CATEGORY_PATTERN.matcher(taskCategory);

        if (!matcher.matches()) {
            return;
        }

    }
}
