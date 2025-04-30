package com.ose.material.domain.model.service;

import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.material.domain.model.repository.MmMaterialCertificateRepository;
import com.ose.material.dto.MmMaterialCertificateDTO;
import com.ose.material.dto.MmMaterialCertificateFileCreateDTO;
import com.ose.material.dto.MmMaterialCertificateSearchDTO;
import com.ose.material.entity.MmMaterialCertificate;
import com.ose.response.JsonObjectResponseBody;
import com.ose.util.LongUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 材料证书。
 */
@Component
public class MmMaterialCertificateService implements MmMaterialCertificateInterface {

    // 材质数据仓库
    private final MmMaterialCertificateRepository mmMaterialCertificateRepository;

    private final UploadFeignAPI uploadFeignAPI;

    /**
     * 构造方法。
     */
    @Autowired
    public MmMaterialCertificateService(
        MmMaterialCertificateRepository mmMaterialCertificateRepository,
        UploadFeignAPI uploadFeignAPI
    ) {
        this.mmMaterialCertificateRepository = mmMaterialCertificateRepository;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    /**
     * 查询材质证书列表。
     *
     * @param orgId
     * @param projectId
     * @param searchBaseDTO
     * @return
     */
    @Override
    public Page<MmMaterialCertificate> search(
        Long orgId,
        Long projectId,
        MmMaterialCertificateSearchDTO searchBaseDTO
    ) {
        return mmMaterialCertificateRepository.search(orgId, projectId,searchBaseDTO);
    }

    /**
     * 创建材质证书。
     *
     * @param orgId
     * @param projectId
     * @param mmMaterialCertificateDTO
     * @param contextDTO
     */
    @Override
    public void create(
        Long orgId,
        Long projectId,
        MmMaterialCertificateDTO mmMaterialCertificateDTO,
        ContextDTO contextDTO
    ) {
        if (mmMaterialCertificateDTO.getNo() == null || "".equals(mmMaterialCertificateDTO.getNo())) {
            throw new BusinessError("证书编号不为空");
        }

        MmMaterialCertificate materialCertificateFind = mmMaterialCertificateRepository.findByOrgIdAndProjectIdAndNoAndStatus(
            orgId,
            projectId,
            mmMaterialCertificateDTO.getNo(),
            EntityStatus.ACTIVE
        );

        if (materialCertificateFind != null) {
            throw new BusinessError("证书编号已存在");
        }

        MmMaterialCertificate materialCertificate = new MmMaterialCertificate();

        JsonObjectResponseBody<FileES> responseBody =
            uploadFeignAPI.save(orgId.toString(), projectId.toString(), mmMaterialCertificateDTO.getPath(), new FilePostDTO());
        FileES fileES = responseBody.getData();
        if (fileES != null && fileES.getId() != null) {
            materialCertificate.setFileId(LongUtils.parseLong(fileES.getId()));
            materialCertificate.setFileName(fileES.getName());
            materialCertificate.setPath(fileES.getPath());
        }
        materialCertificate.setOrgId(orgId);
        materialCertificate.setProjectId(projectId);
        materialCertificate.setNo(mmMaterialCertificateDTO.getNo());
        materialCertificate.setCreatedAt(new Date());
        materialCertificate.setCreatedBy(contextDTO.getOperator().getId());
        materialCertificate.setLastModifiedAt(new Date());
        materialCertificate.setLastModifiedBy(contextDTO.getOperator().getId());
        materialCertificate.setStatus(EntityStatus.ACTIVE);
        mmMaterialCertificateRepository.save(materialCertificate);
    }

    /**
     * 材质证书详情。
     *
     * @param orgId
     * @param projectId
     * @param mmMaterialCertificateId
     * @return
     */
    @Override
    public MmMaterialCertificate detail(
        Long orgId,
        Long projectId,
        Long mmMaterialCertificateId
    ) {
        MmMaterialCertificate materialCertificateFind = mmMaterialCertificateRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            mmMaterialCertificateId,
            EntityStatus.ACTIVE
        );

        if (materialCertificateFind != null) {
            throw new BusinessError("证书编号不存在");
        } else {
            return materialCertificateFind;
        }
    }

    /**
     * 删除材质证书。
     *
     * @param orgId
     * @param projectId
     * @param mmMaterialCertificateId
     * @param contextDTO
     */
    @Override
    public void delete(
        Long orgId,
        Long projectId,
        Long mmMaterialCertificateId,
        ContextDTO contextDTO
    ) {
        MmMaterialCertificate materialCertificateFind = mmMaterialCertificateRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            mmMaterialCertificateId,
            EntityStatus.ACTIVE
        );

        if (materialCertificateFind == null) {
            throw new BusinessError("证书编号不存在");
        }

        materialCertificateFind.setLastModifiedAt(new Date());
        materialCertificateFind.setLastModifiedBy(contextDTO.getOperator().getId());
        materialCertificateFind.setStatus(EntityStatus.DELETED);
        materialCertificateFind.setDeleted(true);
        materialCertificateFind.setDeletedAt(new Date());
        materialCertificateFind.setDeletedBy(contextDTO.getOperator().getId());
        mmMaterialCertificateRepository.save(materialCertificateFind);
    }

    /**
     * 上传材质证书文件。
     *
     * @param orgId
     * @param projectId
     * @param id
     * @param mmMaterialCertificateFileCreateDTO
     * @param contextDTO
     */
    @Override
    public void uploadFile(
        Long orgId,
        Long projectId,
        Long id,
        MmMaterialCertificateFileCreateDTO mmMaterialCertificateFileCreateDTO,
        ContextDTO contextDTO
    ) {

        MmMaterialCertificate materialCertificateFind = mmMaterialCertificateRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            id,
            EntityStatus.ACTIVE
        );

        if (materialCertificateFind == null) {
            throw new BusinessError("未找到相应的证书信息");
        }

        JsonObjectResponseBody<FileES> responseBody =
            uploadFeignAPI.save(orgId.toString(), projectId.toString(), mmMaterialCertificateFileCreateDTO.getFileName(), new FilePostDTO());
        FileES fileES = responseBody.getData();
        if (fileES != null && fileES.getId() != null) {
            materialCertificateFind.setFileId(LongUtils.parseLong(fileES.getId()));
            materialCertificateFind.setFileName(fileES.getName());
            materialCertificateFind.setPath(fileES.getPath());
        }
        materialCertificateFind.setLastModifiedAt(new Date());
        materialCertificateFind.setLastModifiedBy(contextDTO.getOperator().getId());
        mmMaterialCertificateRepository.save(materialCertificateFind);
    }


}
