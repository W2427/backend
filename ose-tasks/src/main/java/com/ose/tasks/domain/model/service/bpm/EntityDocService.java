package com.ose.tasks.domain.model.service.bpm;

import com.ose.tasks.domain.model.repository.bpm.BpmEntityDocsMaterialsRepository;
import com.ose.tasks.dto.bpm.ExInspDocDTO;
import com.ose.tasks.entity.bpm.BpmEntityDocsMaterials;
import com.ose.tasks.vo.bpm.ActInstDocType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class EntityDocService implements EntityDocInterface {


    private final BpmEntityDocsMaterialsRepository docsMaterialsRepository;

    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;

    /**
     * 构造方法。
     */
    @Autowired
    public EntityDocService(BpmEntityDocsMaterialsRepository docsMaterialsRepository) {
        this.docsMaterialsRepository = docsMaterialsRepository;
    }

    /**
     * 根据实体ID，文档类型获取实体文档列表。
     *
     * @param entityIds 实体ID
     * @param type      文档类型
     * @return 实体文档列表
     */
    @Override
    public List<BpmEntityDocsMaterials> getBpmEntityDocsList(
        List<Long> entityIds,
        ActInstDocType type) {
        return docsMaterialsRepository.findByEntityIdInAndType(
            entityIds,
            type);
    }

    /**
     * 生成临时Zip文件。
     *
     * @param orgId         组织ID
     * @param projectId     工程ID
     * @param exInspDocDTOS 外检报告列表
     * @return zip文件
     */
    @Override
    public File createDownloadZipFile(Long orgId,
                                      Long projectId,
                                      List<ExInspDocDTO> exInspDocDTOS) {


        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String zipFileName = formatter.format(new Date()) + ".zip";
        String zipPath = temporaryDir + zipFileName;


        ZipOutputStream zipOutputStream = null;
        FileInputStream zipSource = null;
        BufferedInputStream bufferStream = null;
        File zipFile = new File(zipPath);

        try {

            zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
            if (exInspDocDTOS != null && exInspDocDTOS.size() != 0) {
                for (ExInspDocDTO dto : exInspDocDTOS) {
                    String reportPath = dto.getFilePath();
                    File reportFile = new File(protectedDir, reportPath);

                    if (reportFile.exists()) {
                        zipSource = new FileInputStream(reportFile);
                        ZipEntry zipEntry = new ZipEntry(dto.getReportName());
                        zipOutputStream.putNextEntry(zipEntry);
                        bufferStream = new BufferedInputStream(zipSource, 1024 * 10);
                        int read = 0;
                        byte[] buf = new byte[1024 * 10];
                        while ((read = bufferStream.read(buf, 0, 1024 * 10)) != -1) {
                            zipOutputStream.write(buf, 0, read);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {

            try {
                if (null != bufferStream) bufferStream.close();
                if (null != zipOutputStream) {
                    zipOutputStream.flush();
                    zipOutputStream.close();
                }
                if (null != zipSource) zipSource.close();
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
        }
        return zipFile;
    }
}
