package com.ose.auth.domain.model.repository;

import com.ose.auth.dto.CompanyDTO;
import com.ose.auth.entity.Department;
import com.ose.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class DepartmentRepositoryImpl extends BaseRepository implements DepartmentRepositoryCustom {

    /**
     * 查询
     *
     * @param dto             查询条件
     * @param pageable        分页参数
     * @return 分页数据
     */
    @Override
    public Page<Department> search(CompanyDTO dto, Pageable pageable) {

        SQLQueryBuilder<Department> sqlQueryBuilder = getSQLQueryBuilder(Department.class)
            .is("deleted",false);

        if (dto.getKeyword() != null && !"".equals(dto.getKeyword())) {
            sqlQueryBuilder.like("name", dto.getKeyword());
        }

        return sqlQueryBuilder
            .paginate(pageable)
            .exec()
            .page();
    }

}
