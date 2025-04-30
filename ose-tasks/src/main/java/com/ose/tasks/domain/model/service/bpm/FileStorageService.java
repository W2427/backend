package com.ose.tasks.domain.model.service.bpm;

import com.ose.dto.OperatorDTO;
import com.ose.exception.NotFoundError;
import com.ose.tasks.dto.bpm.FileMetadataDTO;
import com.ose.tasks.dto.bpm.ModelDeployDTO;
import com.ose.util.CryptoUtils;
import com.ose.util.FileUtils;
import com.ose.util.ImageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件存储服务。
 */
@Component
public class FileStorageService implements FileStorageInterface {


    private static final Pattern PATH_PARTS = Pattern.compile("..");


    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;


    @Value("${application.files.public}")
    private String publicDir;

    /**
     * 保存上传的文件。
     *
     * @param operator         上传者信息
     * @param orgId            组织 ID
     * @param fileUploadConfig 文件上传配置
     * @param multipartFile    上传的文件信息
     * @return 文件
     */
    @Override
    public File saveAsTemporaryFile(
        OperatorDTO operator,
        Long orgId,
        FileMetadataDTO.FileUploadConfig fileUploadConfig,
        MultipartFile multipartFile
    ) {

        File diskFile = FileUtils.save(multipartFile, temporaryDir);

        diskFile = FileUtils.rename(
            diskFile,
            CryptoUtils.sha(operator.getId() + CryptoUtils.md5(diskFile))
        );

        FileUtils.saveMetadata(
            diskFile,
            new FileMetadataDTO(
                operator.getId(),
                orgId,
                fileUploadConfig,
                multipartFile
            )
        );

        return diskFile;
    }

    /**
     * 处理已上传的临时文件。
     *
     * @param documentPostDTO 文档数据
     */
    public File resolveTemporaryFile(ModelDeployDTO documentPostDTO) {

        File diskFile = new File(temporaryDir, documentPostDTO.getTemporaryName());

        if (!diskFile.exists()) {

            throw new NotFoundError();
        }

        FileMetadataDTO metadata = FileUtils
            .readMetadata(diskFile, FileMetadataDTO.class);

        String targetFilePath;

        if (!metadata.getConfig().isPublic()) {
            targetFilePath = protectedDir;
        } else {
            targetFilePath = publicDir;
        }

        List<String> parts = new ArrayList<>();

        parts.add(metadata.getConfig().getBizType());

        Matcher m = PATH_PARTS.matcher(documentPostDTO.getTemporaryName());

        while (m.find()) {
            parts.add(m.group());
        }

        targetFilePath = Paths
            .get(targetFilePath, parts.toArray(new String[parts.size()]))
            .toString();

        File targetFile = new File(targetFilePath);
        File parentDir = targetFile.getParentFile();

        parentDir.mkdirs();

        if (FileUtils.isImage(metadata.getMimeType())) {

            if (metadata.getConfig().getImageCompression() != null && null != metadata.getFilename() && !metadata.getFilename().endsWith(".dwg")) {
                ImageUtils.compress(
                    diskFile,
                    targetFile.getAbsolutePath() + ".jpg",
                    metadata.getConfig().getImageCompression()
                );
            }

            if (metadata.getConfig().getThumbnail() != null && null != metadata.getFilename() && !metadata.getFilename().endsWith(".dwg")) {
                ImageUtils.compress(
                    diskFile,
                    targetFile.getAbsolutePath() + ".thumbnail.jpg",
                    metadata.getConfig().getThumbnail()
                );
            }

        } else if (FileUtils.isAudio(metadata.getMimeType())) {

        } else if (FileUtils.isVideo(metadata.getMimeType())) {

        }

        if (metadata.getConfig().isReserveOriginal()) {
            return FileUtils.move(
                diskFile,
                parentDir.getAbsolutePath(),
                targetFile.getName() + ".original" + FileUtils.extname(metadata.getFilename())
            );
        } else {
            diskFile.delete();
            return null;
        }

    }

}
