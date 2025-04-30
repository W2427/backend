package com.ose.tasks.domain.model.service;

import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FileMetadataDTO;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.SuggestionRepository;
import com.ose.tasks.dto.*;
import com.ose.tasks.entity.*;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.util.FileUtils;
import com.ose.util.LongUtils;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;
import java.util.Optional;

@Component
public class SuggestionService extends StringRedisService implements SuggestionInterface {

    private final static Logger logger = LoggerFactory.getLogger(SuggestionService.class);

    private final BatchTaskInterface batchTaskService;

    private final SuggestionRepository suggestionRepository;

    private final ProjectInterface projectService;

    private final UploadFeignAPI uploadFeignAPI;

    @Value("${application.files.temporary}")
    private String temporaryDir;

    @Value("${application.files.protected}")
    private String protectedDir;

    @Value("${spring.servlet.multipart.location}")
    private String multipartFormDataDir;


    @Autowired
    public SuggestionService(SuggestionRepository suggestionRepository,
                             StringRedisTemplate stringRedisTemplate,
                             BatchTaskInterface batchTaskService,
                             ProjectInterface projectService,
                             UploadFeignAPI uploadFeignAPI) {
        super(stringRedisTemplate);
        this.suggestionRepository = suggestionRepository;
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    @Override
    public Suggestion create(OperatorDTO operatorDTO, SuggestionAddDTO dto, ContextDTO context) {
        Project project = projectService.get(1624840920328845260L, 1624840920575068904L);

        Suggestion suggestion = new Suggestion();

        suggestion.setSummary(dto.getSummary());
        suggestion.setDescription(dto.getDescription());
        suggestion.setProposer(operatorDTO.getId());
        suggestion.setProposerName(operatorDTO.getName());
        suggestion.setStatus(EntityStatus.ACTIVE);
        suggestion.setCreatedAt(new Date());
        suggestion.setLastModifiedAt(new Date());

        if (dto.getFileName() != null) {
            batchTaskService.runConstructTaskExecutor(
                null,
                project,
                BatchTaskCode.DOCUMENT_UPLOAD,
                false,
                context,
                batchTask -> {

                    try {
                        // 1、上传的原始文件保存、并创建历史记录
                        System.out.println("开始外检上传文件" + new Date());
                        String temporaryFileName = dto.getFileName();
                        File diskFileTemp = new File(temporaryDir, temporaryFileName);
                        if (!diskFileTemp.exists()) {
                            throw new NotFoundError();
                        }

                        FileMetadataDTO metadata = FileUtils.readMetadata(diskFileTemp, FileMetadataDTO.class);
//                    if (!metadata.getFilename().endsWith(".xlsx") && !metadata.getFilename().endsWith(".pdf")) {
//                        throw new NotFoundError();
//                    }
                        FilePostDTO filePostDTO = new FilePostDTO();
                        filePostDTO.setContext(context);


                        logger.info("外检上传 保存docs服务->开始");
                        JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.
                            save(String.valueOf(1624840920575068904L), String.valueOf(1624840920575068904L), temporaryFileName, filePostDTO);
                        logger.info("外检上传 保存docs服务->结束");

                        FileES fileES = fileESResBody.getData();
                        suggestion.setFileId(LongUtils.parseLong(fileES.getId()));
                        suggestion.setFileName(metadata.getFilename());
                        suggestion.setFilePath(fileES.getPath());

                        suggestionRepository.save(suggestion);

                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                    }
                    return new BatchResultDTO();
                });
        }

        suggestionRepository.save(suggestion);
        return suggestion;
    }

    @Override
    public Page<Suggestion> getList(SuggestionSearchDTO dto, Long operatorId) {
        return suggestionRepository.getList(dto, operatorId);
    }

    @Override
    public Suggestion get(Long id) {
//        Optional<Suggestion> optional = suggestionRepository.findById(id);
//        if (!optional.isPresent()){
//            throw  new BusinessError("Can't find this suggestion");
//        }
        return suggestionRepository.findById(id).get();
    }

    @Override
    public Suggestion modify(OperatorDTO operatorDTO, ContextDTO context, Long id, SuggestionEditDTO dto) {
        Optional<Suggestion> optional = suggestionRepository.findById(id);
        if (!optional.isPresent()) {
            throw new BusinessError("Can't find this suggestion");
        }
        Suggestion suggestion = optional.get();
        if (dto.getSummary()!=null){
            suggestion.setSummary(dto.getSummary());
        }
        if (dto.getDescription()!=null){
            suggestion.setDescription(dto.getDescription());
        }
        if (dto.getPriority()!=null){
            suggestion.setPriority(dto.getPriority());
        }
        if (dto.getFileName()!=null){
            Project project = projectService.get(1624840920328845260L, 1624840920575068904L);
            batchTaskService.runConstructTaskExecutor(
                null,
                project,
                BatchTaskCode.DOCUMENT_UPLOAD,
                false,
                context,
                batchTask -> {

                    try {
                        // 1、上传的原始文件保存、并创建历史记录
                        System.out.println("开始外检上传文件" + new Date());
                        String temporaryFileName = dto.getFileName();
                        File diskFileTemp = new File(temporaryDir, temporaryFileName);
                        if (!diskFileTemp.exists()) {
                            throw new NotFoundError();
                        }

                        FileMetadataDTO metadata = FileUtils.readMetadata(diskFileTemp, FileMetadataDTO.class);
//                    if (!metadata.getFilename().endsWith(".xlsx") && !metadata.getFilename().endsWith(".pdf")) {
//                        throw new NotFoundError();
//                    }
                        FilePostDTO filePostDTO = new FilePostDTO();
                        filePostDTO.setContext(context);


                        logger.info("外检上传 保存docs服务->开始");
                        JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.
                            save(String.valueOf(1624840920575068904L), String.valueOf(1624840920575068904L), temporaryFileName, filePostDTO);
                        logger.info("外检上传 保存docs服务->结束");

                        FileES fileES = fileESResBody.getData();
                        suggestion.setFileId(LongUtils.parseLong(fileES.getId()));
                        suggestion.setFileName(metadata.getFilename());
                        suggestion.setFilePath(fileES.getPath());

                        suggestion.setLastModifiedAt(new Date());
                        suggestion.setLastModifiedBy(operatorDTO.getId());

                        suggestionRepository.save(suggestion);

                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                    }
                    return new BatchResultDTO();
                });
        }

        suggestion.setLastModifiedAt(new Date());
        suggestion.setLastModifiedBy(operatorDTO.getId());

        suggestionRepository.save(suggestion);

        return suggestion;
    }

    @Override
    public Suggestion close(OperatorDTO operatorDTO, Long id) {
        Optional<Suggestion> optional = suggestionRepository.findById(id);
        if (!optional.isPresent()) {
            throw new BusinessError("Can't find this suggestion");
        }
        Suggestion suggestion = optional.get();

        suggestion.setStatus(EntityStatus.CLOSED);
        suggestion.setLastModifiedAt(new Date());
        suggestion.setLastModifiedBy(operatorDTO.getId());
        return suggestionRepository.save(suggestion);
    }
}
