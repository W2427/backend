package com.ose.docs.domain.model.service;

import com.ose.docs.constant.upload.*;
import com.ose.docs.domain.model.repository.*;
import com.ose.docs.domain.model.repository.project.*;
import com.ose.docs.dto.FileMetadataDTO;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.*;
import com.ose.docs.entity.project.*;
import com.ose.docs.vo.FileCategory;
import com.ose.dto.OperatorDTO;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.util.CryptoUtils;
import com.ose.util.FileUtils;
import com.ose.util.ImageUtils;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件存储服务。
 */
@Component
public class FileStorageService implements FileStorageInterface {

    private final static Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    // 按每两个字符拆分成一级路径
    private static final Pattern PATH_PARTS = Pattern.compile("..");

    // 上传文件的临时路径
    @Value("${application.files.temporary}")
    private String temporaryDir;

    // 受保护文件路径
    @Value("${application.files.protected}")
    private String protectedDir;

    // 公开文件路径
    @Value("${application.files.public}")
    private String publicDir;

    // 项目模块流程定义文件数据仓库
    private final ProjectModuleProcessDefinitionRepository projectModuleProcessDefinitionRepository;

    // 项目层级结构导入文件数据仓库
    private final HierarchyESRepository hierarchyESRepository;

    // WBS 实体导入文件数据仓库
    private final WBSEntitiesESRepository wbsEntitiesESRepository;

    // 项目计划导入文件数据仓库
    private final PlanESRepository planESRepository;

    // 项目文档文件数据仓库
    private final ProjectDocumentESRepository projectDocumentESRepository;

    // 问题反馈附件数据仓库
    private final IssueAttachmentRepository issueAttachmentRepository;

    // 焊工证书文件数据仓库
    private final WelderESRepository welderESRepository;

    // 焊接流程说明书数据仓库
    private final WpsESRepository wpsESRepository;

    // 遗留问题导入数据仓库
    private final IssueImportESRepository issueImportESRepository;

    // 经验教训附件数据仓库
    private final ExperienceAttachmentRepository experienceAttachmentRepository;

    // WPS附件数据仓库
    private WpsAttachmentESRepository wpsAttachmentRepository;

    // WPQR附件数据仓库
    private WpqrAttachmentESRepository wpqrAttachmentRepository;

    // 分包商附件数据仓库
    private SubConAttachmentESRepository subConAttachmentESRepository;

    // 分包商附件数据仓库
    private NdtInspectorCertificateESRepository ndtInspectorCertificateESRepository;

    // 材料代码别称与材料分组对应关系数据仓库
    private MaterialCodeAliasGroupESRepository materialCodeAliasGroupESRepository;

    // 定额工时数据仓库
    private RatedTimeESRepository ratedTimeESRepository;

    private MaterialStructureESRepository materialStructureESRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public FileStorageService(
        ProjectModuleProcessDefinitionRepository projectModuleProcessDefinitionRepository,
        HierarchyESRepository hierarchyESRepository,
        WBSEntitiesESRepository wbsEntitiesESRepository,
        PlanESRepository planESRepository,
        ProjectDocumentESRepository projectDocumentESRepository,
        IssueAttachmentRepository issueAttachmentRepository,
        WelderESRepository welderESRepository,
        WpsESRepository wpsESRepository,
        IssueImportESRepository issueImportESRepository,
        ExperienceAttachmentRepository experienceAttachmentRepository,
        WpsAttachmentESRepository wpsAttachmentRepository,
        WpqrAttachmentESRepository wpqrAttachmentRepository,
        SubConAttachmentESRepository subConAttachmentESRepository,
        NdtInspectorCertificateESRepository ndtInspectorCertificateESRepository,
        MaterialCodeAliasGroupESRepository materialCodeAliasGroupESRepository,
        RatedTimeESRepository ratedTimeESRepository,
        MaterialStructureESRepository materialStructureESRepository

    ) {
        this.projectModuleProcessDefinitionRepository = projectModuleProcessDefinitionRepository;
        this.hierarchyESRepository = hierarchyESRepository;
        this.wbsEntitiesESRepository = wbsEntitiesESRepository;
        this.planESRepository = planESRepository;
        this.projectDocumentESRepository = projectDocumentESRepository;
        this.issueAttachmentRepository = issueAttachmentRepository;
        this.welderESRepository = welderESRepository;
        this.wpsESRepository = wpsESRepository;
        this.issueImportESRepository = issueImportESRepository;
        this.experienceAttachmentRepository = experienceAttachmentRepository;
        this.wpsAttachmentRepository = wpsAttachmentRepository;
        this.wpqrAttachmentRepository = wpqrAttachmentRepository;
        this.ndtInspectorCertificateESRepository = ndtInspectorCertificateESRepository;
        this.subConAttachmentESRepository = subConAttachmentESRepository;
        this.materialCodeAliasGroupESRepository = materialCodeAliasGroupESRepository;
        this.ratedTimeESRepository = ratedTimeESRepository;
        this.materialStructureESRepository = materialStructureESRepository;
    }

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
        String orgId,
        FileMetadataDTO.FileUploadConfig fileUploadConfig,
        MultipartFile multipartFile
    ) {
        logger.info("上传服务器->保存文件开始->保存临时文件开始");
        File diskFile = FileUtils.save(multipartFile, temporaryDir);

        diskFile = FileUtils.rename(
            diskFile,
            CryptoUtils.sha(operator.getId() + CryptoUtils.md5(diskFile))
        );
        logger.info("上传服务器->保存文件开始->保存临时文件开始中");
        FileUtils.saveMetadata(
            diskFile,
            new FileMetadataDTO(
                operator.getId().toString(),
                orgId,
                fileUploadConfig,
                multipartFile
            )
        );
        logger.info("上传服务器->保存文件开始->保存临时文件结束");

        return diskFile;
    }

    /**
     * 处理已上传的临时文件。
     *
     * @param operator          操作者信息
     * @param projectId         项目 ID
     * @param temporaryFileName 临时文件名
     * @param filePostDTO       文件附加信息
     * @return 文档数据实体
     */
    @Override
    public FileES resolveTemporaryFile(
        OperatorDTO operator,
        String projectId,
        String temporaryFileName,
        FilePostDTO filePostDTO
    ) {
        logger.info("上传服务器->保存文件开始->保存文件到本地开始");
        Boolean isIndexed = true;
        // 取得已上传的临时文件
        File diskFile = new File(temporaryDir, temporaryFileName);

        if (!diskFile.exists()) {
            // TODO 使用业务错误
            throw new NotFoundError();
        }

        // 读取上传文件的元数据
        FileMetadataDTO metadata = FileUtils
            .readMetadata(diskFile, FileMetadataDTO.class);

        ElasticsearchRepository repository;
        FileDetailES documentES;

        switch (metadata.getConfig().getBizType()) {
            case ProjectModuleProcessDefinitionFile.BIZ_TYPE:
                repository = projectModuleProcessDefinitionRepository;
                documentES = new ProjectModuleProcessDefinitionES();
                ((ProjectModuleProcessDefinitionES) documentES).setProjectId(projectId.toString());
                break;
            case ProjectHierarchyImportFile.BIZ_TYPE:
                repository = hierarchyESRepository;
                documentES = new HierarchyES();
                ((HierarchyES) documentES).setProjectId(projectId.toString());
                break;
            case ProjectEntitiesImportFile.BIZ_TYPE:
                repository = wbsEntitiesESRepository;
                documentES = new WBSEntitiesES();
                ((WBSEntitiesES) documentES).setProjectId(projectId.toString());
                break;
            case ProjectPlanImportFile.BIZ_TYPE:
                repository = planESRepository;
                documentES = new PlanES();
                ((PlanES) documentES).setProjectId(projectId.toString());
                break;
            case ProjectDocumentFile.BIZ_TYPE:
                repository = projectDocumentESRepository;
                documentES = new ProjectDocumentES();
                ((ProjectDocumentES) documentES).setProjectId(projectId.toString());
                break;
            case IssueAttachment.BIZ_TYPE:
                repository = issueAttachmentRepository;
                documentES = new IssueAttachmentES();
                isIndexed = false;
                break;
            case WelderExportFile.BIZ_TYPE:
                repository = welderESRepository;
                documentES = new WelderES();
                break;
            case WpsExportFile.BIZ_TYPE:
                repository = wpsESRepository;
                documentES = new WpsES();
                break;
            case IssueImportFile.BIZ_TYPE:
                repository = issueImportESRepository;
                documentES = new IssueImportFileES();
                break;
            case ExperienceAttachment.BIZ_TYPE:
                repository = experienceAttachmentRepository;
                documentES = new ExperienceAttachmentES();
                break;
            case WpsAttachment.BIZ_TYPE:
                repository = wpsAttachmentRepository;
                documentES = new WpsAttachmentES();
                break;
            case WpqrAttachment.BIZ_TYPE:
                repository = wpqrAttachmentRepository;
                documentES = new WpqrAttachmentES();
                break;
            case SubConAttachment.BIZ_TYPE:
                repository = subConAttachmentESRepository;
                documentES = new SubConAttachmentES();
                break;
            case WelderAttachment.BIZ_TYPE:
                repository = welderESRepository;
                documentES = new WelderES();
                break;
            case NdtInspectorCertificateFile.BIZ_TYPE:
                repository = ndtInspectorCertificateESRepository;
                documentES = new NdtInspectorCertificateES();
                break;
            case MaterialCodeAliasGroupExportFile.BIZ_TYPE:
                repository = materialCodeAliasGroupESRepository;
                documentES = new MaterialCodeAliasGroupES();
                break;
            case RatedTimeImportFile.BIZ_TYPE:
                repository = ratedTimeESRepository;
                documentES = new RatedTimeES();
                break;
            case MaterialStructureNestImportFile.BIZ_TYPE:
                repository = materialStructureESRepository;
                documentES = new MaterialStructureES();
                break;

            default:
                throw new ValidationError("invalid biz type"); // TODO
        }

        documentES.setCompanyId("COMPANY_ID"); // TODO
        documentES.setOrgId(metadata.getOrgId());
        documentES.setName(metadata.getFilename());
        documentES.setMimeType(metadata.getMimeType());
        documentES.setKeywords(filePostDTO.getKeywords());
        documentES.setTags(filePostDTO.getTags());
        documentES.setRemarks(filePostDTO.getRemarks());
        documentES.setCommittedAt(new Date().getTime());
        documentES.setCommittedBy(operator.getId().toString());

        // 读取文本、Microsoft Office（Word、Excel、PowerPoint）文档、PDF、图像（仅 EXIF 信息）文件的内容
        if (isIndexed &&
            (new HashSet<>(Arrays.asList(
                FileCategory.TEXT,
                FileCategory.WORD,
                FileCategory.EXCEL,
                FileCategory.POWERPOINT,
                FileCategory.PDF,
                FileCategory.IMAGE
            ))).retainAll(documentES.getCategories())
        ) {
            try {
                if (null != metadata.getFilename() && !metadata.getFilename().endsWith(".dwg")) {
                    Tika tika = new Tika();
                    documentES.addContent(tika.parseToString(diskFile));
                }
            } catch (IOException | TikaException e) {
                e.printStackTrace(System.out);
            }
        }

        String basePath;

        if (!metadata.getConfig().isPublic()) {
            basePath = protectedDir;
        } else {
            basePath = publicDir;
        }

        List<String> parts = new ArrayList<>();

        Matcher m = PATH_PARTS.matcher(temporaryFileName);

        while (m.find()) {
            parts.add(m.group());
        }

        String targetFilePath = basePath + String.join("/", parts);
        File targetFile = new File(targetFilePath);
        File parentDir = targetFile.getParentFile();
        String defaultPath = null;
        String thumbnailPath = null;
        String originalPath = null;

        // 创建存放路径
        try {
            parentDir.mkdirs();
        } catch (Exception e) {
            logger.error("Can not mk dir");
        }

        // 图像文件时生成压缩图及缩略图
        if (FileUtils.isImage(metadata.getMimeType())) {

            if (metadata.getConfig().getImageCompression() != null && null != metadata.getFilename() && !metadata.getFilename().endsWith(".dwg")) {

                defaultPath = targetFile.getAbsolutePath() + ".jpg";

                ImageUtils.compress(
                    diskFile,
                    defaultPath,
                    metadata.getConfig().getImageCompression()
                );
            }

            if (metadata.getConfig().getThumbnail() != null && null != metadata.getFilename() && !metadata.getFilename().endsWith(".dwg")) {
                thumbnailPath = targetFile.getAbsolutePath() + ".thumbnail.jpg";

                ImageUtils.compress(
                    diskFile,
                    thumbnailPath,
                    metadata.getConfig().getThumbnail()
                );
            }

            // 音频文件时压缩音频文件
        } else if (FileUtils.isAudio(metadata.getMimeType())) {
            // TODO 音频压缩
            // 视频文件时压缩视频文件
        } else if (FileUtils.isVideo(metadata.getMimeType())) {
            // TODO 视频压缩
        }

        // 若保留原始文件则将原始文件移动到目标路径下
        if (metadata.getConfig().isReserveOriginal()) {

            String originalFileName = targetFile.getName()
                + ".original"
                + FileUtils.extname(metadata.getFilename());

            originalPath = parentDir.getAbsolutePath() + "/" + originalFileName;

            FileUtils.move(
                diskFile,
                parentDir.getAbsolutePath(),
                originalFileName
            );

            // 否则删除原始文件
        } else {
            try {
                diskFile.delete();
            } catch (Exception e) {
                logger.error("Can not delete file");
            }
        }

        // 移动元数据文件
        FileUtils.move(
            new File(diskFile.getAbsolutePath() + ".json"),
            parentDir.getAbsolutePath(),
            targetFile.getName() + ".metadata.json"
        );

        // 设置文件在服务器上的路径
        documentES.setPaths(basePath, defaultPath, originalPath, thumbnailPath);

        // 保存数据实体
        repository.save(documentES);

        logger.info("上传服务器->保存文件开始->保存文件到本地结束");
        return documentES;
    }

}
