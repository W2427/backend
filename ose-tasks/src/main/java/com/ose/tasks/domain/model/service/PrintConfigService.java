package com.ose.tasks.domain.model.service;

import com.ose.dto.PageDTO;
import com.ose.exception.ValidationError;
import com.ose.tasks.domain.model.repository.PrintConfigRepository;
import com.ose.tasks.dto.PrintConfigDTO;
import com.ose.tasks.entity.PrintConfig;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PrintConfigService implements PrintConfigInterface {

    private PrintConfigRepository printConfigRepository;

    @Autowired
    public PrintConfigService(
        PrintConfigRepository printConfigRepository
    ) {
        this.printConfigRepository = printConfigRepository;
    }


    @Override
    public PrintConfig get(Long orgId, Long projectId, Long id) {
        Optional<PrintConfig> op = printConfigRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }

    @Override
    public PrintConfig create(Long orgId, Long projectId, PrintConfigDTO configDTO) {

     /*   PrintConfig config = printConfigRepository.findByOrgIdAndProjectIdAndServiceAndStatus(orgId, projectId, configDTO.getService(), EntityStatus.ACTIVE);
        if(config!=null) {
            throw new ValidationError("服务地址已存在.");
        }
        */
        PrintConfig config = printConfigRepository.findByOrgIdAndProjectIdAndNameAndStatus(orgId, projectId, configDTO.getName(), EntityStatus.ACTIVE);
        if (config != null) {
            throw new ValidationError("名称已存在.");
        }

        /*Long index = printConfigRepository.getCountByOrgIdAndProjectIdAndNameAndType(
                orgId, projectId, configDTO.getName(), configDTO.getType());*/
        Long index = printConfigRepository.getCountByOrgIdAndProjectId(orgId, projectId);
        int no = index.intValue() + 1;

        config = BeanUtils.copyProperties(configDTO, new PrintConfig());
        config.setNo(no);
        config.setProjectId(projectId);
        config.setOrgId(orgId);
        config.setCreatedAt();
        config.setStatus(EntityStatus.ACTIVE);
        return printConfigRepository.save(config);
    }

    @Override
    public Page<PrintConfig> getList(Long orgId, Long projectId, PageDTO page) {
        return printConfigRepository.findByOrgIdAndProjectIdAndStatus(orgId, projectId, EntityStatus.ACTIVE, page.toPageable());
    }

    @Override
    public boolean delete(Long orgId, Long projectId, Long id) {
        Optional<PrintConfig> op = printConfigRepository.findById(id);
        if (op.isPresent()) {
            PrintConfig config = op.get();
            config.setStatus(EntityStatus.DELETED);
            config.setLastModifiedAt();
            printConfigRepository.save(config);
            return true;
        }
        return false;
    }

    @Override
    public PrintConfig modify(Long orgId, Long projectId, Long id, PrintConfigDTO configDTO) {
        Optional<PrintConfig> op = printConfigRepository.findById(id);
        if (op.isPresent()) {
            PrintConfig config = BeanUtils.copyProperties(configDTO, op.get());
            config.setLastModifiedAt();
            return printConfigRepository.save(config);
        }
        return null;
    }


    @Override
    public List<PrintConfig> searchByType(Long orgId, Long projectId, String type) {
        return printConfigRepository.findByOrgIdAndProjectIdAndTypeAndStatus(orgId, projectId, type, EntityStatus.ACTIVE);
    }

}
