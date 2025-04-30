package com.ose.tasks.entity.wbs.entry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.entity.BaseEntity;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import net.sf.mpxj.*;

import jakarta.persistence.Column;
import jakarta.persistence.*;
import java.util.*;

/**
 * WBS 条目数据。
 */
@Entity
@jakarta.persistence.Table(
    name = "wbs_entry_blob",
    indexes = {
        @Index(columnList = "wbsEntryId",unique = true)

    }
)
public class WBSEntryBlob extends BaseEntity {


    private static final long serialVersionUID = -1477141623444034794L;
    @Column
    private Long wbsEntryId;

    @Schema(description = "组织 ID")
    @Column
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "WBS 条目路径")
    @Column
    private String path;

    @Schema(description = "编号（全）")
    @Column
    private String wbs;

    @Schema(description = "备注")
    @Lob
    @Column(length = 4096)
    private String remarks;

    @Schema(description = "是否活跃")
    @Column(columnDefinition = "BIT(1) DEFAULT 0")
    private Boolean active;

    /**
     * 从 P6 导出文件解析计划条目的 WBS 信息。
     *
     * @param task 从 P6 导出文件解析的任务节点信息
     * @return WBS 信息
     */

    public static String repairWBS(Task task) {

        Task parent = task.getParentTask();
        String wbs = task.getWBS();

        // 若存在上级任务且当前任务的 WBS 不以上级任务的 WBS 开始则重新设置当前任务的 WBS
        // 使当前任务的 WBS 以其上级任务的 WBS 开始（P6 v8.x 导出文件 BUG 容错）
        if (parent != null && !wbs.startsWith(parent.getWBS())) {

            // 设置上级任务的 WBS（递归）
            parent.setWBS(repairWBS(parent));

            List<String> parentWBS = new ArrayList<>(Arrays.asList(parent.getWBS().split("\\.")));
            List<String> segments = new ArrayList<>();
            String prefix = null;

            // 从当前任务的 WBS 的前部取得与其上级任务的 WBS 的后部重合的部分
            for (int i = parentWBS.size() - 1; i > 1; i--) {
                segments.add(0, parentWBS.get(i));
                if (wbs.startsWith(String.join(".", segments))) {
                    prefix = String.join(".", segments);
                }
            }

            // 若当前任务的 WBS 存在与其上级任务的 WBS 存在重合的部分
            // 则将当前任务的 WBS 中与其上级任务的 WBS 重合的部分替换为其上级任务的 WBS
            if (prefix != null) {
                task.setWBS(parent.getWBS() + wbs.substring(prefix.length()));
            }
        }

        return task.getWBS();
    }


    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getWbs() {
        return wbs;
    }

    public void setWbs(String wbs) {
        this.wbs = wbs;
    }

    /**
     * 取得上级条目 ID 的集合。
     *
     * @return 上级条目 ID 的集合
     */
    @JsonIgnore
    public Set<Long> getParentIDs() {

        if (StringUtils.isEmpty(path)) {
            return null;
        }

        Set<String> tmpParentIDs = new HashSet<>(Arrays.asList(path.split("/")));

        tmpParentIDs.removeAll(Arrays.asList("", null));

        Set<Long> parentIDs = new HashSet<>();

        tmpParentIDs.forEach(parentID -> {
            parentIDs.add(LongUtils.parseLong(parentID));
        });

        return parentIDs;
    }

    public Long getWbsEntryId() {
        return wbsEntryId;
    }

    public void setWbsEntryId(Long wbsEntryId) {
        this.wbsEntryId = wbsEntryId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
