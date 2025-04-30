package com.ose.auth.domain.model.repository;

import com.ose.auth.dto.CompanyDTO;
import com.ose.auth.entity.Company;
import com.ose.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class CompanyRepositoryImpl extends BaseRepository implements CompanyRepositoryCustom {

    /**
     * 查询
     *
     * @param dto             查询条件
     * @param pageable        分页参数
     * @return 分页数据
     */
    @Override
    public Page<Company> search(CompanyDTO dto, Pageable pageable) {

        SQLQueryBuilder<Company> sqlQueryBuilder = getSQLQueryBuilder(Company.class)
            .is("deleted",false);

        if (dto.getKeyword() != null && !"".equals(dto.getKeyword())) {
            sqlQueryBuilder.like("name", dto.getKeyword());
        }

        if (dto.getCountry() != null && !"".equals(dto.getCountry())) {
            sqlQueryBuilder.is("country", dto.getCountry());
        }

        return sqlQueryBuilder
            .paginate(pageable)
            .exec()
            .page();
    }

}
