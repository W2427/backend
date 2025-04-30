package com.ose.tasks.domain.model.service.deliverydoc;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.web.context.request.RequestAttributes;

import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.deliverydoc.DeliveryDocModulesDTO;
import com.ose.tasks.dto.deliverydoc.GenerateDocDTO;
import com.ose.tasks.entity.deliverydoc.DeliveryDocument;

/**
 * 文档包服务接口。
 */
public interface DeliveryDocInterface {

    /**
     * 生成指定模块工序文档包
     *
     * @param operatorDTO
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param dto
     * @param attributes
     */
    void generateDoc(OperatorDTO operatorDTO, Long orgId, Long projectId, GenerateDocDTO dto, RequestAttributes attributes);

    /**
     * 生成指定模块文档包
     *
     * @param operator
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param module
     * @param attributes
     */
    void generateDoc(OperatorDTO operator, Long orgId, Long projectId, String module, RequestAttributes attributes);

    /**
     * 生成项目文档包
     *
     * @param operator
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param attributes
     */
    void generateDoc(OperatorDTO operator, Long orgId, Long projectId, RequestAttributes attributes);

    /**
     * 查询模块列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param keyword
     * @return
     */
    List<DeliveryDocModulesDTO> getModules(Long orgId, Long projectId, String keyword);

    /**
     * 查询模块文档包
     *
     * @param operatorDTO
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param module
     * @return
     */
    List<DeliveryDocument> getDeliveryDocs(OperatorDTO operatorDTO, Long orgId, Long projectId, String module);

    /**
     * 获取模块完工文档zip包
     *
     * @param operatorDTO
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param module
     * @return
     * @throws IOException
     */
    File getModuleZipFile(OperatorDTO operatorDTO, Long orgId, Long projectId, String module) throws IOException;

}
