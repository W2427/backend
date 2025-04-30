package com.ose.tasks.domain.model.service.worksite;

import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.worksite.WorkSiteMoveDTO;
import com.ose.tasks.dto.worksite.WorkSitePatchDTO;
import com.ose.tasks.dto.worksite.WorkSitePostDTO;
import com.ose.tasks.entity.worksite.WorkSite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 工作场地管理服务接口。
 */
public interface WorkSiteInterface {

    /**
     * 查询工作场地信息。
     *
     * @param companyId 公司 ID
     * @param parentId  上级 ID
     * @param projectId 项目 ID
     * @param pageable  分页参数
     * @return 工作场地分页数据
     */
    Page<WorkSite> search(Long companyId, Long parentId, Long projectId, Pageable pageable);

    /**
     * 取得工作场地详细信息。
     *
     * @param companyId  公司 ID
     * @param projectId  项目 ID
     * @param workSiteId 工作场地 ID
     * @return 工作场地详细信息
     */
    WorkSite get(Long companyId, Long projectId, Long workSiteId);

    /**
     * 创建工作场地信息。
     *
     * @param operator    操作者信息
     * @param companyId   公司 ID
     * @param projectId   项目 ID
     * @param parentId    上级 ID
     * @param workSiteDTO 工作场地数据传输对象
     * @return 工作场地信息
     */
    WorkSite create(OperatorDTO operator, Long companyId, Long projectId, Long parentId, WorkSitePostDTO workSiteDTO);

    /**
     * 更新工作场地信息。
     *
     * @param operator    操作者信息
     * @param companyId   公司 ID
     * @param projectId   项目 ID
     * @param workSiteId  工作场地 ID
     * @param version     更新版本号
     * @param workSiteDTO 工作场地数据传输对象
     * @return 工作场地信息
     */
    WorkSite update(OperatorDTO operator, Long companyId, Long projectId, Long workSiteId, long version, WorkSitePatchDTO workSiteDTO);

    /**
     * 移动工作场地。
     *
     * @param operator    操作者信息
     * @param companyId   公司 ID
     * @param projectId   项目 ID
     * @param workSiteId  工作场地 ID
     * @param version     更新版本号
     * @param workSiteDTO 工作场地数据传输对象
     * @return 工作场地信息
     */
    WorkSite move(OperatorDTO operator, Long companyId, Long projectId, Long workSiteId, long version, WorkSiteMoveDTO workSiteDTO);

    /**
     * 删除工作场地信息。
     *
     * @param operator   操作者信息
     * @param companyId  公司 ID
     * @param projectId  项目 ID
     * @param workSiteId 工作场地 ID
     * @param version    更新版本号
     */
    void delete(OperatorDTO operator, Long companyId, Long projectId, Long workSiteId, long version);

}
