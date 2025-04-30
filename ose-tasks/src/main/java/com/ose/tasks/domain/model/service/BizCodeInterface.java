package com.ose.tasks.domain.model.service;

import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.bizcode.BizCodePatchDTO;
import com.ose.tasks.dto.bizcode.BizCodePostDTO;
import com.ose.tasks.dto.bizcode.BizCodeTypeDTO;
import com.ose.tasks.dto.bpm.BpmDelegateClassDTO;
import com.ose.tasks.entity.BizCode;
import com.ose.tasks.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 业务代码接口。
 */
public interface BizCodeInterface {

    /**
     * 取得业务代码列表。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param type      业务代码类型
     * @return 业务代码列表
     */
    Page<BizCode> list(
        Long orgId,
        Long projectId,
        String type,
        Pageable pageable
    );

    /**
     * 取得业务代码大类型列表。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return 业务代码大类型列表
     */
    List<BizCodeTypeDTO> listBizCodeType(
        Long orgId,
        Long projectId
    );

    /**
     * 添加业务代码。
     *
     * @param operator       操作者信息
     * @param project        项目信息
     * @param bizCodeType    业务代码
     * @param bizCodePostDTO 业务代码信息
     * @return 业务代码信息
     */
    BizCode add(
        OperatorDTO operator,
        Project project,
        String bizCodeType,
        BizCodePostDTO bizCodePostDTO
    );

    /**
     * 添加业务代码。
     *
     * @param operator        操作者信息
     * @param project         项目信息
     * @param bizCodeType     业务代码
     * @param bizCode         业务代码
     * @param version         业务代码版本号
     * @param bizCodePatchDTO 业务代码信息
     * @return 业务代码信息
     */
    BizCode update(
        OperatorDTO operator,
        Project project,
        String bizCodeType,
        String bizCode,
        Long version,
        BizCodePatchDTO bizCodePatchDTO
    );

    /**
     * 删除业务代码。
     *
     * @param operator    操作者信息
     * @param project     项目信息
     * @param bizCodeType 业务代码
     * @param bizCode     业务代码
     * @param version     业务代码版本号
     */
    void delete(
        OperatorDTO operator,
        Project project,
        String bizCodeType,
        String bizCode,
        Long version
    );

    /**
     * 按大类型名称取得业务代码的个数。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param bizCodeType 业务代码大类型
     * @return 业务代码个数
     */
    int countByOrgIdAndProjectIdAndBizCodeType(
        Long orgId,
        Long projectId,
        String bizCodeType
    );


    /**
     * 查询任务代理类名称列表。
     *
     * @return
     */
    Set<BpmDelegateClassDTO> getTaskClasses();

    /**
     * 查询日志代理类名称列表。
     *
     * @return
     */
    Set<BpmDelegateClassDTO> getLogTaskClasses();

    /**
     * 取得结构焊口 WP02 对应号， WP03 对应号 WP04 对应号
     */
    Map<String, Map<String, String>> getStructureWeldCode(Long orgId, Long projectId);



}
