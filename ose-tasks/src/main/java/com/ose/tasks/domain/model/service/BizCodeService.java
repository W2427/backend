package com.ose.tasks.domain.model.service;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.ConflictError;
import com.ose.exception.DuplicatedError;
import com.ose.exception.NotFoundError;
import com.ose.tasks.domain.model.repository.BizCodeRepository;
import com.ose.tasks.dto.bizcode.BizCodePatchDTO;
import com.ose.tasks.dto.bizcode.BizCodePostDTO;
import com.ose.tasks.dto.bizcode.BizCodeTypeDTO;
import com.ose.tasks.dto.bpm.BpmDelegateClassDTO;
import com.ose.tasks.entity.BizCode;
import com.ose.tasks.entity.Project;
import com.ose.tasks.vo.setting.BizCodeType;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.ose.util.ClassUtils.getClassName;

/**
 * 业务代码服务。
 */
@Component
public class BizCodeService implements BizCodeInterface {


    private BizCodeRepository bizCodeRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public BizCodeService(BizCodeRepository bizCodeRepository) {
        this.bizCodeRepository = bizCodeRepository;
    }

    /**
     * 取得业务代码列表。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param type      业务代码类型
     * @return 业务代码列表
     */
    @Override
    public Page<BizCode> list(
        Long orgId,
        Long projectId,
        String type,
        Pageable pageable
    ) {
        return bizCodeRepository
            .findByOrgIdAndProjectIdAndTypeAndDeletedIsFalseOrderBySortAsc(orgId, projectId, type, pageable);
    }

    /**
     * 取得结构焊口 WP02 对应号， WP03 对应号 WP04 对应号
     */
    @Override
    public Map<String, Map<String, String>> getStructureWeldCode(Long orgId, Long projectId) {

        PageDTO pageDTO = new PageDTO();
        pageDTO.setFetchAll(true);
        Map<String, String> wp02Code = new HashMap<>();
        Map<String, String> wp03Code = new HashMap<>();
        Map<String, String> wp04Code = new HashMap<>();
        Map<String, Map<String, String>> structWeldCode = new HashMap<>();

        List<BizCode> wp02CodeList = list(orgId, projectId, "WP02", pageDTO.toPageable()).getContent();

        wp02CodeList.forEach(wp02 -> wp02Code.put(wp02.getCode(), wp02.getName()));


        List<BizCode> wp03CodeList = list(orgId, projectId, "WP03", pageDTO.toPageable()).getContent();

        wp03CodeList.forEach(wp03 -> wp03Code.put(wp03.getCode(), wp03.getName()));


        List<BizCode> wp04CodeList = list(orgId, projectId, "WP04", pageDTO.toPageable()).getContent();

        wp04CodeList.forEach(wp04 -> wp04Code.put(wp04.getCode(), wp04.getName()));

        structWeldCode.put("WP02", wp02Code);
        structWeldCode.put("WP03", wp03Code);
        structWeldCode.put("WP04", wp04Code);

        return structWeldCode;
    }

    /**
     * 取得业务代码大类型列表。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return 业务代码大类型列表
     */
    @Override
    public List<BizCodeTypeDTO> listBizCodeType(Long orgId, Long projectId) {

        return bizCodeRepository.findTypeByOrgIdAndProjectIdAndDeletedIsFalse(orgId, projectId);
    }

    /**
     * 取得业务代码信息。
     *
     * @param project     项目信息
     * @param bizCodeType 业务代码类型
     * @param code        业务代码
     * @param version     业务代码更新版本号
     * @return 业务代码数据实体
     */
    private BizCode get(Project project, String bizCodeType, String code, Long version) {

        BizCode bizCodeEntity = bizCodeRepository
            .findByOrgIdAndProjectIdAndTypeAndCodeAndDeletedIsFalse(
                project == null ? null : project.getOrgId(),
                project == null ? null : project.getId(),
                bizCodeType,
                code
            )
            .orElse(null);

        if (version != null) {

            if (bizCodeEntity == null) {
                throw new NotFoundError();
            }

            if (!version.equals(bizCodeEntity.getVersion())) {
                throw new ConflictError();
            }

        }

        return bizCodeEntity;
    }

    /**
     * 添加业务代码。
     *
     * @param operator       操作者信息
     * @param project        项目信息
     * @param bizCodeType    业务代码
     * @param bizCodePostDTO 业务代码信息
     * @return 业务代码信息
     */
    @Override
    public BizCode add(
        OperatorDTO operator,
        Project project,
        String bizCodeType,
        BizCodePostDTO bizCodePostDTO
    ) {


        BizCodeType[] bizCodeTypes = BizCodeType.values();
        if (Arrays.deepToString(bizCodeTypes).contains(bizCodeType)) {
            throw new BusinessError("error.biz-code.is-enum");
        }

        BizCode bizCodeEntity = get(
            project,
            bizCodeType,
            bizCodePostDTO.getCode(),
            null
        );


        if (bizCodeEntity != null) {
            throw new DuplicatedError();
        }

        bizCodeEntity = new BizCode();
        bizCodeEntity = BeanUtils.copyProperties(bizCodePostDTO, bizCodeEntity);
        bizCodeEntity.setType(bizCodeType);

        bizCodeEntity.setCompanyId(project == null ? null : project.getCompanyId());
        bizCodeEntity.setOrgId(project == null ? null : project.getOrgId());
        bizCodeEntity.setProjectId(project == null ? null : project.getId());
        bizCodeEntity.setCreatedBy(operator.getId());
        bizCodeEntity.update(operator, bizCodePostDTO);
        bizCodeEntity.setSort(0);
        bizCodeEntity.setStatus(EntityStatus.ACTIVE);

        return bizCodeRepository.save(bizCodeEntity);
    }

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
    @Override
    public BizCode update(
        OperatorDTO operator,
        Project project,
        String bizCodeType,
        String bizCode,
        Long version,
        BizCodePatchDTO bizCodePatchDTO
    ) {
        return bizCodeRepository.save(
            get(project, bizCodeType, bizCode, version)
                .update(operator, bizCodePatchDTO)
        );
    }

    /**
     * 删除业务代码。
     *
     * @param operator    操作者信息
     * @param project     项目信息
     * @param bizCodeType 业务代码
     * @param bizCode     业务代码
     * @param version     业务代码版本号
     */
    @Override
    public void delete(
        OperatorDTO operator,
        Project project,
        String bizCodeType,
        String bizCode,
        Long version
    ) {
        bizCodeRepository.delete(
            get(project, bizCodeType, bizCode, version)
                .delete(operator)
        );
    }

    /**
     * 按大类型名称取得业务代码的个数。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param bizCodeType 业务代码大类型
     * @return 业务代码个数
     */
    @Override
    public int countByOrgIdAndProjectIdAndBizCodeType(Long orgId, Long projectId, String bizCodeType) {
        return bizCodeRepository.countByOrgIdAndProjectIdAndDeletedIsFalse(orgId, projectId, bizCodeType);
    }

    /**
     * 查询任务代理类名称列表。
     *
     * @return 任务代理集合
     */
    @Override
    public Set<BpmDelegateClassDTO> getTaskClasses() {

        Set<BpmDelegateClassDTO> bpmDelegateClasses = new HashSet<>();

        getClassName("com.ose.tasks.domain.model.service.delegate", true)
            .forEach(clazz -> {
                if (!clazz.toLowerCase().contains("interface") && !clazz.equalsIgnoreCase("BaseBpmTaskDelegate")) {
                    BpmDelegateClassDTO bpmDelegateClass = new BpmDelegateClassDTO();
                    bpmDelegateClass.setDelegateClassFullName(clazz);
                    if (clazz.contains("/"))
                        bpmDelegateClass.setDelegateClassName(clazz.substring(clazz.lastIndexOf("/") + 1));
                    else
                        bpmDelegateClass.setDelegateClassName(clazz.substring(clazz.lastIndexOf(".") + 1));
                    bpmDelegateClasses.add(bpmDelegateClass);
                }
            });

        getClassName("com.ose.tasks.domain.model.service.report.pipereport", true)
            .forEach(clazz -> {
                if (!clazz.toLowerCase().contains("interface") && !clazz.equalsIgnoreCase("AirTightnessExInspReport")) {
                    BpmDelegateClassDTO bpmDelegateClass = new BpmDelegateClassDTO();
                    bpmDelegateClass.setDelegateClassFullName(clazz);
                    if (clazz.contains("/"))
                        bpmDelegateClass.setDelegateClassName(clazz.substring(clazz.lastIndexOf("/") + 1));
                    else
                        bpmDelegateClass.setDelegateClassName(clazz.substring(clazz.lastIndexOf(".") + 1));
                    bpmDelegateClasses.add(bpmDelegateClass);
                }
            });

        getClassName("com.ose.tasks.domain.model.service.report.structurereport", true)
            .forEach(clazz -> {
                if (!clazz.toLowerCase().contains("interface") && !clazz.equalsIgnoreCase("AirTightnessExInspReport")) {
                    BpmDelegateClassDTO bpmDelegateClass = new BpmDelegateClassDTO();
                    bpmDelegateClass.setDelegateClassFullName(clazz);
                    if (clazz.contains("/"))
                        bpmDelegateClass.setDelegateClassName(clazz.substring(clazz.lastIndexOf("/") + 1));
                    else
                        bpmDelegateClass.setDelegateClassName(clazz.substring(clazz.lastIndexOf(".") + 1));
                    bpmDelegateClasses.add(bpmDelegateClass);
                }
            });

        getClassName("com.ose.tasks.domain.model.service.report.materialreport", true)
            .forEach(clazz -> {
                if (!clazz.toLowerCase().contains("interface") && !clazz.equalsIgnoreCase("AirTightnessExInspReport")) {
                    BpmDelegateClassDTO bpmDelegateClass = new BpmDelegateClassDTO();
                    bpmDelegateClass.setDelegateClassFullName(clazz);
                    if (clazz.contains("/"))
                        bpmDelegateClass.setDelegateClassName(clazz.substring(clazz.lastIndexOf("/") + 1));
                    else
                        bpmDelegateClass.setDelegateClassName(clazz.substring(clazz.lastIndexOf(".") + 1));
                    bpmDelegateClasses.add(bpmDelegateClass);
                }
            });

        return bpmDelegateClasses;
    }

    /**
     * 查询日志代理类名称列表。
     *
     * @return 日志代理集合
     */
    @Override
    public Set<BpmDelegateClassDTO> getLogTaskClasses() {

        Set<BpmDelegateClassDTO> bpmDelegateClasses = new HashSet<>();

        getClassName("com.ose.tasks.domain.model.service.constructlog.pipingConstructionLog", true)
            .forEach(clazz -> {
                if (!clazz.toLowerCase().contains("interface") && !clazz.equalsIgnoreCase("BaseBpmTaskDelegate")) {
                    BpmDelegateClassDTO bpmDelegateClass = new BpmDelegateClassDTO();
                    bpmDelegateClass.setDelegateClassFullName(clazz);
                    if (clazz.contains("/")) {
                        bpmDelegateClass.setDelegateClassName(clazz.substring(clazz.lastIndexOf("/") + 1));
                    } else {
                        bpmDelegateClass.setDelegateClassName(clazz.substring(clazz.lastIndexOf(".") + 1));
                    }
                    bpmDelegateClasses.add(bpmDelegateClass);
                }
            });

        getClassName("com.ose.tasks.domain.model.service.constructlog.structureConstructionLog", true)
            .forEach(clazz -> {
                if (!clazz.toLowerCase().contains("interface") && !clazz.equalsIgnoreCase("BaseBpmTaskDelegate")) {
                    BpmDelegateClassDTO bpmDelegateClass = new BpmDelegateClassDTO();
                    bpmDelegateClass.setDelegateClassFullName(clazz);
                    if (clazz.contains("/")) {
                        bpmDelegateClass.setDelegateClassName(clazz.substring(clazz.lastIndexOf("/") + 1));
                    } else {
                        bpmDelegateClass.setDelegateClassName(clazz.substring(clazz.lastIndexOf(".") + 1));
                    }
                    bpmDelegateClasses.add(bpmDelegateClass);
                }
            });

        return bpmDelegateClasses;
    }


}
