package com.ose.tasks.domain.model.service.wbs;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.MaterialInfoDTO;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.entity.drawing.SubDrawingHistory;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.vo.wbs.WBSEntryRunningStatus;
import org.springframework.data.domain.Page;

import java.io.File;
import java.util.*;

/**
 * WBS 实体操作服务接口。
 *
 * @param <T> WBS 实体范型
 */
public interface BaseWBSEntityInterface<T extends WBSEntityBase, S extends WBSEntryCriteriaBaseDTO> {


    Set<WBSEntryRunningStatus> RUNNING_STATUS_CANNOT_BE_REMOVED = new HashSet<>(
        Arrays.asList(
            WBSEntryRunningStatus.RUNNING,
            WBSEntryRunningStatus.REJECTED,
            WBSEntryRunningStatus.APPROVED
        )
    );
    /**
     * 查询 WBS 实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param pageDTO   分页参数
     * @return WBS 实体分页数据
     */
    Page<? extends T> search(Long orgId, Long projectId, S criteriaDTO, PageDTO pageDTO);

    /**
     * 查询 任务包添加的 WBS 实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param pageDTO   分页参数
     * @return WBS 实体分页数据
     */
    default Page<? extends T> searchTaskPackageEntities(Long orgId, Long projectId, S criteriaDTO, PageDTO pageDTO) {
        return null;
    }


    /**
     * 取得 WBS 实体详细信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  WBS 实体 ID
     * @return WBS 实体详细信息
     */
    T get(Long orgId, Long projectId, Long entityId);

    /**
     * 删除 WBS 实体。
     *
     * @param operator 操作者信息
     * @param orgId    组织 ID
     * @param project  项目
     * @param entityId WBS 实体 ID
     */
    void delete(OperatorDTO operator, Long orgId, Project project, Long entityId);

    /**
     * 插入 WBS 实体。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entity    WBS 实体
     */
    void insert(OperatorDTO operator, Long orgId, Long projectId, T entity);

    /**
     * 更新 WBS 实体。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entity    WBS 实体
     */
    T update(OperatorDTO operator, Long orgId, Long projectId, T entity);

    /**
     * 判断 WBS 实体是否存在。
     *
     * @param entityNO  WBS 实体 ID
     * @param projectId 项目 ID
     * @return 存在:true; 不存在:false
     */
    boolean existsByEntityNo(String entityNO, Long projectId);

    /**
     * 设定父级信息
     *
     * @param parentNodeNo 父级节点号
     * @param entity       需要设置父级的实体
     * @param projectId    项目ID
     * @param operator     操作者信息
     * @param orgId        组织信息
     */
    default void setParentInfo(String parentNodeNo,
                               T entity,
                               Long projectId,
                               Long orgId,
                               OperatorDTO operator) {
    }

    /**
     * 取得实体对应的图纸
     *
     * @param orgId     组织信息
     * @param projectId 项目ID
     * @param id        实体ID
     * @return
     */
    default SubDrawingHistory findSubDrawing(
        Long id,
        Long orgId,
        Long projectId
    ) {
        return null;
    }

    /**
     * 取得实体对应的材料信息。
     *
     * @param id        实体ID
     * @param orgId     组织信息
     * @param projectId 项目ID
     * @return 实体信息
     */
    default List<MaterialInfoDTO> getMaterialInfo(
        Long id,
        Long orgId,
        Long projectId
    ) {
        return null;
    }

    /**
     * 取得实体对应的图纸信息。
     *
     * @param id        实体ID
     * @param orgId     组织信息
     * @param projectId 项目ID
     * @param actInst   工作流实体信息
     * @return 图纸实体信息
     */
    default List<SubDrawing> getDrawingInfo(Long orgId,
                                            Long projectId,
                                            Long id,
                                            BpmActivityInstanceBase actInst) {
        return null;
    }

    /**
     * 保存实体下载临时文件。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param operatorId  项目ID
     * @return 实体下载临时文件
     */
    File saveDownloadFile(Long orgId, Long projectId, S criteriaDTO, Long operatorId);

    /**
     * 根据二维码查询实体信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param qrcode
     * @return
     */
    default T getByQrCode(Long orgId, Long projectId, String qrcode) {
        return null;
    }


    /**
     * 设置 ISO PN ID号，更新WBS-ENTRY
     */
    default void setIsoIdsAndWbs(Long projectId){}

    /**
     * 设置 ISO PN ID号，更新WBS-ENTRY
     */
    default void setIsoIdsAndWbs(Long projectId, Long entityId){}

}
