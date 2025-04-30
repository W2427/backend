package com.ose.tasks.domain.model.service.holiday.impl;

import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.tasks.domain.model.repository.holiday.HolidayDateRepository;
import com.ose.tasks.domain.model.service.holiday.HolidayInterface;
import com.ose.tasks.dto.holiday.HolidayCreateDTO;
import com.ose.tasks.dto.holiday.HolidaySearchDTO;
import com.ose.tasks.entity.holiday.HolidayData;
import com.ose.vo.EntityStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Component
public class HolidayService implements HolidayInterface {

    private final HolidayDateRepository holidayDateRepository;
    @Autowired
    public HolidayService(HolidayDateRepository holidayDateRepository) {
        this.holidayDateRepository = holidayDateRepository;
    }

    @Override
    public Page<HolidayData> search(
        HolidaySearchDTO holidaySearchDTO
    ) {
        return holidayDateRepository.search(
            holidaySearchDTO
        );
    }

    @Override
    public HolidayData create(
        HolidayCreateDTO holidayCreateDTO,
        ContextDTO contextDTO
    ) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String holidayDate = formatter.format(holidayCreateDTO.getHolidayDate());

        HolidayData holidayDataFind = holidayDateRepository.findByHolidayDateAndCountryAndDeletedIsFalse(
            holidayDate,
            holidayCreateDTO.getCountry()
        );
        if (holidayDataFind != null) {
            throw new BusinessError("holiday already exists");
        }

        HolidayData holidayData = new HolidayData();
        BeanUtils.copyProperties(holidayCreateDTO, holidayData);
        holidayData.setHolidayDate(holidayDate);
        holidayData.setStatus(EntityStatus.ACTIVE);
        holidayData.setCreatedAt(new Date());
        holidayData.setCreatedBy(contextDTO.getOperator().getId());
        holidayData.setDeleted(false);
        holidayData.setLastModifiedAt(new Date());
        holidayData.setLastModifiedBy(contextDTO.getOperator().getId());

        return holidayDateRepository.save(holidayData);
    }

    /**
     * 更新节假日。
     *
     * @param id
     * @param holidayCreateDTO
     * @param contextDTO
     * @return
     */
    @Override
    public HolidayData update(
        Long id,
        HolidayCreateDTO holidayCreateDTO,
        ContextDTO contextDTO
    ) {
        Optional<HolidayData> holidayDataOptional = holidayDateRepository.findById(id);
        if (!holidayDataOptional.isPresent()) {
            throw new BusinessError("holiday does not exist");
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String holidayDate = formatter.format(holidayCreateDTO.getHolidayDate());

        HolidayData holidayDataFind = holidayDateRepository.findByHolidayDateAndCountryAndDeletedIsFalse(
            holidayDate,
            holidayCreateDTO.getCountry()
        );
        if (holidayDataFind != null && !holidayCreateDTO.getHolidayName().equals(holidayDataFind.getHolidayName())) {
            throw new BusinessError("holiday already exists");
        }

        HolidayData holidayData = holidayDataOptional.get();
        BeanUtils.copyProperties(holidayCreateDTO, holidayData);
        holidayData.setHolidayDate(holidayDate);
        holidayData.setLastModifiedBy(contextDTO.getOperator().getId());
        holidayData.setLastModifiedAt(new Date());
        return holidayDateRepository.save(holidayData);
    }

    /**
     * 删除节假日。
     *
     * @param id
     * @param contextDTO
     */
    @Override
    public void delete(
        Long id,
        ContextDTO contextDTO
    ) {
        Optional<HolidayData> holidayDataFind = holidayDateRepository.findById(id);
        if (holidayDataFind.isPresent()) {
            HolidayData holidayData = holidayDataFind.get();
            holidayData.setDeleted(true);
            holidayData.setStatus(EntityStatus.DELETED);
            holidayData.setDeletedAt(new Date());
            holidayData.setDeletedBy(contextDTO.getOperator().getId());
            holidayData.setLastModifiedAt(new Date());
            holidayData.setLastModifiedBy(contextDTO.getOperator().getId());
            holidayDateRepository.save(holidayData);
        } else {
            throw new BusinessError("holiday does not exist");
        }

    }

    @Override
    public HolidayData detail(Long id) {
        Optional<HolidayData> holidayDataFind = holidayDateRepository.findById(id);
        if (!holidayDataFind.isPresent()) {
            throw new BusinessError("holiday does not exist");
        }
        return holidayDataFind.get();
    }
}
