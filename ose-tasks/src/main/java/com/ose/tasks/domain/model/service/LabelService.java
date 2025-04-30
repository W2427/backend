package com.ose.tasks.domain.model.service;

import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.LabelRepository;
import com.ose.tasks.dto.bpm.LabelCriteriaDTO;
import com.ose.tasks.dto.bpm.LabelDTO;
import com.ose.tasks.entity.Label;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LabelService extends StringRedisService implements LabelInterface {

    private final static Logger logger = LoggerFactory.getLogger(LabelService.class);

    private final LabelRepository labelRepository;

    @Autowired
    public LabelService(LabelRepository labelRepository,
                        StringRedisTemplate stringRedisTemplate) {
        super(stringRedisTemplate);
        this.labelRepository = labelRepository;
    }

    @Override
    public Label get(Long id) {
        Optional<Label> op = labelRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }


    @Override
    public void delete(Long id, OperatorDTO operatorDTO) {
        Optional<Label> labelOptional = labelRepository.findById(id);
        if (labelOptional.isPresent()) {
            Label label = labelOptional.get();
            label.setStatus(EntityStatus.DELETED);
            label.setDeleted(true);
            label.setDeletedAt(new Date());
            label.setDeletedBy(operatorDTO.getId());
            label.setLastModifiedBy(operatorDTO.getId());
            label.setLastModifiedAt(new Date());
            labelRepository.save(label);
        }
    }

    @Override
    public Page<Label> getList(LabelCriteriaDTO dto) {
        if (dto.getName() != null && !"".equals(dto.getName())) {
            return labelRepository.findByNameAndDeletedIsFalse(dto.getName(), dto.toPageable());
        } else {
            return labelRepository.findByDeletedIsFalse(dto.toPageable());
        }
    }

    @Override
    public Label create(OperatorDTO operatorDTO, LabelDTO labelDTO) {
        Label label = labelRepository.findFirstByNameAndDeletedIsFalse(labelDTO.getName());
        if (label != null) {
            throw new BusinessError("The label already exists！");
        }
        Label newLabel = new Label();
        newLabel.setName(labelDTO.getName());
        newLabel.setCreatedAt(new Date());
        newLabel.setCreatedBy(operatorDTO.getId());
        newLabel.setLastModifiedAt(new Date());
        newLabel.setLastModifiedBy(operatorDTO.getId());
        newLabel.setStatus(EntityStatus.ACTIVE);
        newLabel.setDeleted(false);
        return labelRepository.save(newLabel);
    }

    @Override
    public Label modify(OperatorDTO operatorDTO, Long id, LabelDTO labelDTO) {
        Optional<Label> labelOptional = labelRepository.findById(id);
        if (!labelOptional.isPresent()) {
            throw new BusinessError("This label was not found！");
        }
        Label label = labelRepository.findFirstByNameAndDeletedIsFalse(labelDTO.getName());
        if (label != null && !label.getId().equals(id)) {
            throw new BusinessError("The label already exists！");
        }
        labelOptional.get().setName(labelDTO.getName());
        labelOptional.get().setLastModifiedBy(operatorDTO.getId());
        labelOptional.get().setLastModifiedAt(new Date());
        return labelRepository.save(labelOptional.get());
    }
}
