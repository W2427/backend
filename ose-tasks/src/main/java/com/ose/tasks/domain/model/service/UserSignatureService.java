package com.ose.tasks.domain.model.service;

import com.ose.util.FileUtils;
import com.ose.util.LongUtils;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FileMetadataDTO;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.UserSignatureRepository;
import com.ose.tasks.dto.SignatureDTO;
import com.ose.tasks.entity.UserSignature;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class UserSignatureService implements UserSignatureInterface {

    private final static Logger logger = LoggerFactory.getLogger(UserSignatureService.class);

    @Value("${application.files.temporary}")
    private String temporaryDir;

    @Value("${application.files.protected}")
    private String protectedDir;

    private UserSignatureRepository userSignatureRepository;

    /**
     * 构造方法
     */
    @Autowired
    public UserSignatureService(
        UserSignatureRepository userSignatureRepository
    ) {
        this.userSignatureRepository = userSignatureRepository;
    }

    @Override
    public UserSignature uploadSignature(
        SignatureDTO signatureDTO,
        UploadFeignAPI uploadFeignAPI,
        OperatorDTO operatorDTO
    ) {

        FileES f = uploadSignatureFile(
            signatureDTO,
            uploadFeignAPI,
            operatorDTO
        );

        UserSignature userSignature = new UserSignature();
        userSignature.setFileId(
            LongUtils.parseLong(f.getId())
        );
        userSignature.setFilePath(f.getPath());
        userSignature.setUserId(signatureDTO.getUserId());
        userSignature.setCreatedBy(operatorDTO.getId());
        userSignature.setLastModifiedBy(operatorDTO.getId());
        userSignature.setStatus(EntityStatus.ACTIVE);

        return userSignatureRepository.save(userSignature);
    }

    private FileES uploadSignatureFile(
        SignatureDTO signatureDTO,
        UploadFeignAPI uploadFeignAPI,
        OperatorDTO operatorDTO
    ) {
        String temporaryFileName = signatureDTO.getFileName();

        File diskFile = new File(temporaryDir, temporaryFileName);

        FileMetadataDTO metadata = FileUtils.readMetadata(diskFile, FileMetadataDTO.class);
        if (!FileUtils.isImage(metadata.getMimeType())) {
            throw new BusinessError("请上传图片文件");
        }
        logger.error("上传签名1 保存docs服务->开始");
        JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(
            LongUtils.toString(operatorDTO.getId()),
            signatureDTO.getFileName(),
            new FilePostDTO()
        );
        logger.error("上传签名1 保存docs服务->结束");
        return responseBody.getData();
    }

    @Override
    public UserSignature userSignature(Long userId) {
        UserSignature userSignature = userSignatureRepository.findByUserId(userId);
        if (userSignature != null) {
            return userSignature;
        }
        return null;
    }

    @Override
    public UserSignature updateUserSignature(
        Long id,
        SignatureDTO signatureDTO,
        UploadFeignAPI uploadFeignAPI,
        OperatorDTO operator
    ) {
        UserSignature userSignature = userSignatureRepository
            .findById(id)
            .orElse(null);
        if (userSignature == null) {
            throw new NotFoundError();
        }

        FileES f = uploadSignatureFile(
            signatureDTO,
            uploadFeignAPI,
            operator
        );

        userSignature.setFileId(LongUtils.parseLong(f.getId()));
        userSignature.setFilePath(f.getPath());
        userSignature.setLastModifiedAt();
        userSignature.setLastModifiedBy(operator.getId());
        userSignature.setVersion(userSignature.getLastModifiedAt().getTime());

        return userSignatureRepository.save(userSignature);
    }
}
