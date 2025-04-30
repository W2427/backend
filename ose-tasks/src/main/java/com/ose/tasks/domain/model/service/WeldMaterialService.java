package com.ose.tasks.domain.model.service;
import com.ose.dto.PageDTO;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.tasks.domain.model.repository.WeldMaterialRepository;
import com.ose.tasks.domain.model.repository.wps.simple.WpsSimpleRepository;
import com.ose.tasks.dto.WeldMaterialDTO;
import com.ose.tasks.dto.WpsWeldMaterialDTO;
import com.ose.tasks.entity.WeldMaterial;
import com.ose.tasks.entity.wps.simple.WpsSimplified;
import com.ose.tasks.vo.WeldMaterialType;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class WeldMaterialService implements WeldMaterialInterface{

    private final static Logger logger = LoggerFactory.getLogger(SubconService.class);

    private final WeldMaterialRepository weldMaterialRepository;

    private final WpsSimpleRepository wpsSimpleRepository;

    public WeldMaterialService(WeldMaterialRepository weldMaterialRepository, WpsSimpleRepository wpsSimpleRepository) {
        this.weldMaterialRepository = weldMaterialRepository;
        this.wpsSimpleRepository = wpsSimpleRepository;
    }

    /**
     * 创建焊材信息。
     *
     * @param operatorId 操作人ID
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param weldMaterialDTO  焊材信息
     */
    @Override
    public void create(
        Long operatorId,
        Long orgId,
        Long projectId,
        WeldMaterialDTO weldMaterialDTO
    ) {
        WeldMaterial weldMaterial = new WeldMaterial();
        Date now = new Date();
        BeanUtils.copyProperties(weldMaterialDTO, weldMaterial);

        WeldMaterial weld = weldMaterialRepository.findByProjectIdAndBatchNoAndDeletedIsFalse(projectId, weldMaterialDTO.getBatchNo());
        if (weld != null) {
            throw new ValidationError("Data already exists");
        }

        weldMaterial.setOrgId(orgId);
        weldMaterial.setProjectId(projectId);
        weldMaterial.setDeleted(false);
        weldMaterial.setCreatedAt(now);
        weldMaterial.setCreatedBy(operatorId);
        weldMaterial.setLastModifiedAt(now);
        weldMaterial.setLastModifiedBy(operatorId);
        weldMaterial.setStatus(EntityStatus.ACTIVE);
        weldMaterialRepository.save(weldMaterial);
    }

    /**
     * 获取焊材列表（分页）。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param pageDTO   分页参数
     * @return 带分页的焊材列表
     */
    @Override
    public Page<WeldMaterial> search(Long orgId, Long projectId, String batchNo, PageDTO pageDTO) {
        if (batchNo == null) {
            return  weldMaterialRepository
                .findByOrgIdAndProjectIdAndDeletedIsFalse(orgId, projectId, pageDTO.toPageable());
        } else {
            return weldMaterialRepository
                .findByOrgIdAndProjectIdAndBatchNoAndDeletedIsFalse(orgId, projectId, batchNo, pageDTO.toPageable());
        }
    }

    /**
     * 获取焊材详情。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param batchNo  批次号
     * @return 焊材详情
     */
    @Override
    public WeldMaterial get(
        Long orgId,
        Long projectId,
        String batchNo
    ) {

        return weldMaterialRepository.findByProjectIdAndBatchNoAndDeletedIsFalse(projectId, batchNo);
    }

    /**
     * 根据焊材类型筛选
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return 焊材详情
     */
    @Override
    public List<WeldMaterial> getList(
        Long orgId,
        Long projectId,
        WeldMaterialDTO weldMaterialDTO
    ) {
        if (weldMaterialDTO.getWeldMaterialType() != null) {
            return weldMaterialRepository.findByProjectIdAndWeldMaterialTypeAndDeletedIsFalse(projectId, weldMaterialDTO.getWeldMaterialType());
        } else {
            return null;
        }
    }

    /**
     * 获取焊材详情。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return 焊材详情
     */
    @Override
    public List<WeldMaterial> getDetail(
        Long orgId,
        Long projectId,
        WpsWeldMaterialDTO wpsWeldMaterialDTO
    ) {
        if (wpsWeldMaterialDTO.getWpsNo() != null && !"".equals(wpsWeldMaterialDTO.getWpsNo())) {

            List<WpsSimplified> wpsList = new ArrayList<>();
            for (String wpsNo : wpsWeldMaterialDTO.getWpsNo().split(",")) {
                WpsSimplified wps = wpsSimpleRepository.findByOrgIdAndProjectIdAndNoAndStatus(orgId, projectId, wpsNo, EntityStatus.ACTIVE);
                if (wps.getWeldMaterialType() == null || "".equals(wps.getWeldMaterialType())) {
                    continue;
                }
                wpsList.add(wps);
            }
            List<WeldMaterial> result = weldMaterialRepository.search(orgId, projectId, wpsWeldMaterialDTO);
            // 过滤SAW_FLUX包含SAW的问题
            boolean sawFlag = false;
            boolean sawFluxFlag = false;

            if (result.size() > 0) {
                for (WpsSimplified wpsMaterialTypes : wpsList) {
                    for (String material : wpsMaterialTypes.getWeldMaterialType().split(",")) {
                        if ((WeldMaterialType.SAW.toString()).equals(material)) {
                            sawFlag = true;
                        }
                        if ((WeldMaterialType.SAW_FLUX.toString()).equals(material)) {
                            sawFluxFlag = true;
                        }
                    }
                }
                if (!sawFlag && sawFluxFlag) {
                    // 判断查询单一焊材类型还是所有数据
                    if (wpsWeldMaterialDTO.getWeldMaterialType() != null && !"".equals(wpsWeldMaterialDTO.getWeldMaterialType())) {
                        for (WeldMaterial list : result) {
                            if (list.getWeldMaterialType() == WeldMaterialType.SAW) {
                                return null;
                            }
                        }
                    } else {
                        for (int i = 0; i < result.size(); i++) {
                            if (result.get(i).getWeldMaterialType() == WeldMaterialType.SAW) {
                                result.remove(result.get(i));
                                i--;
                            }
                        }
                    }
                }
            }
            return result;
        } else {
            return null;
        }
    }

    /**
     * 更新焊材信息。
     *
     * @param operatorId 操作人ID
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param weldMaterialDTO  更新信息
     */
    @Override
    public void update(
        Long operatorId,
        Long orgId,
        Long projectId,
        String batchNo,
        WeldMaterialDTO weldMaterialDTO
    ) {

        WeldMaterial weldMaterial = weldMaterialRepository.findByProjectIdAndBatchNoAndDeletedIsFalse(projectId, batchNo);

        if (weldMaterial == null) {
            throw new NotFoundError("weldMaterial is not found");
        }
        weldMaterial.setWeldMaterialType(WeldMaterialType.valueOf(weldMaterialDTO.getWeldMaterialType().getDisplayName()));
        weldMaterial.setFlux(weldMaterialDTO.getFlux());
        weldMaterialRepository.save(weldMaterial);
    }

    /**
     * 删除焊材。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param batchNo  批次号
     */
    @Override
    public void delete(
        Long operatorId,
        Long orgId,
        Long projectId,
        String batchNo
    ) {

        WeldMaterial weldMaterial = weldMaterialRepository.findByProjectIdAndBatchNoAndDeletedIsFalse(projectId, batchNo);

        if (weldMaterial == null) {
            throw new NotFoundError("weldMaterial is not found");
        }

        weldMaterial.setDeleted(true);
        weldMaterial.setStatus(EntityStatus.DELETED);

        weldMaterialRepository.save(weldMaterial);
    }
}
