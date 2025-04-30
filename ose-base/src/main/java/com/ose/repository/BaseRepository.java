package com.ose.repository;

import com.ose.util.DataTypeUtils;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.springframework.data.domain.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.util.*;

/**
 * 数据仓库基类。
 */
public abstract class BaseRepository {

    // 数据实体管理器
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 取得数据实体管理器。
     *
     * @return 数据实体管理器
     */
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * 取得 SQL 查询构建器。
     *
     * @param <T>         实体范型
     * @param entityClass 实体类型
     * @return SQL 查询构建器
     */
    protected <T> SQLQueryBuilder<T> getSQLQueryBuilder(
        Class<T> entityClass
    ) {
        return new SQLQueryBuilder<>(entityManager, entityClass);
    }

    /**
     * SQL 查询构建器。
     *
     * @param <T> 查询目标数据实体范型
     */
    protected static class SQLQueryBuilder<T> {

        // 实体管理器
        private final EntityManager entityManager;

        // 查询目标实体的类型
        private final Class<T> entityClass;

        // 查询条件构造器
        private final CriteriaBuilder criteriaBuilder;

        // 实体条件查询
        private final JpaCriteriaQuery<T> entityCriteriaQuery;


        // FROM 语句中的根类型
        private final Root<T> entityRootType;

        // 查询条件中的断言
        private Predicate predicate;

        // 查询排序字段列表
        private List<Sort.Order> orderList = new ArrayList<>();

        // 分页参数：页号
        private int pageNo = 0;

        // 分页参数：分页大小
        private int pageSize = 1;

        // 实体查询
        private TypedQuery<T> entitySearchQuery = null;

        // 符合查询条件的数据总量
        private Long count = null;

        /**
         * 获取根类型。
         */
        public Root<T> getEntityRootType() {
            return entityRootType;
        }

        /**
         * 判断值是否为空对象或空字符串。
         *
         * @param value 值
         * @return 是否为空对象或空字符串
         */
        private static boolean isEmpty(Object value) {
            return value == null
                || (value instanceof String s
                && "".equals(s.trim()));
        }

        /**
         * 判断值是否为空列表。
         *
         * @param value 值
         * @return 是否为空列表
         */
        private static boolean isListEmpty(List<?> value) {
            return value == null || value.size() == 0;
        }

        /**
         * 压缩列表。
         *
         * @param values 值列表
         */
        private static <VT> List<VT> compressList(List<VT> values) {

            if (isListEmpty(values)) {
                return values;
            }

            values.removeIf(SQLQueryBuilder::isEmpty);

            Set<VT> set = new HashSet<>(values);

            values.clear();
            values.addAll(set);

            return values;
        }

        /**
         * 去除字符串首尾空白字符。
         *
         * @param value 值
         * @return 去除首尾空格后的值
         */
        private static Object trim(Object value) {
            return (!(value instanceof String))
                ? value
                : ((String) value).trim();
        }

        /**
         * 转义 LIKE 值。
         *
         * @param value 值
         * @return 转义后的值
         */
        public static String escapeLikeValue(String value) {

            if (isEmpty(value)) {
                return value;
            }

            return value
                .replaceAll("\\\\", "\\\\")
                .replaceAll("%", "\\\\%")
                .replaceAll("_", "\\\\_")
                .replaceAll("\\[", "\\\\[");

        }

        /**
         * 构造方法。
         *
         * @param entityManager 数据实体管理器
         * @param entityClass   目标数据实体类型
         */
        private SQLQueryBuilder(
            final EntityManager entityManager,
            final Class<T> entityClass
        ) {

            this.entityManager = entityManager;
            this.entityClass = entityClass;

            criteriaBuilder = entityManager.getCriteriaBuilder();
            entityCriteriaQuery = (JpaCriteriaQuery<T>)criteriaBuilder.createQuery(this.entityClass);
            entityRootType = entityCriteriaQuery.from(this.entityClass);
            predicate = criteriaBuilder.conjunction();

            entityRootType.alias("__tbl");
        }

        /**
         * 查询或。
         *
         * @param orCriteria 或查询条件
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> or(Map<String, Map<String, String>> orCriteria) {

            if (orCriteria.size() == 0) {
                return this;
            }

            Predicate orPredicate = criteriaBuilder.disjunction();

            for (Map.Entry<String, Map<String, String>> criterionMap : orCriteria.entrySet()) {

                // 获取条件的KEY
                String key = criterionMap.getKey();

                // 条件的操作及值
                if (criterionMap.getValue() != null && criterionMap.getValue().size() != 0) {

                    for (Map.Entry<String, String> criterion : criterionMap.getValue().entrySet()) {

                        // 获取条件的值
                        String value = criterion.getValue();

                        // 根据操作符匹配操作
                        switch (criterion.getKey()) {
                            case "$is":
                                if (!DataTypeUtils.isPrimitive(value) || isEmpty(value)) {
                                    continue;
                                }
                                orPredicate = criteriaBuilder.or(
                                    orPredicate,
                                    criteriaBuilder.equal(entityRootType.get(key), trim(value))
                                );
                                break;
                            case "$like":
                                if (isEmpty(value)) {
                                    continue;
                                }
                                orPredicate = criteriaBuilder.or(
                                    orPredicate,
                                    criteriaBuilder.like(
                                        entityRootType.get(key),
                                        "%" + escapeLikeValue((String) trim(value)) + "%",
                                        '\\'
                                    )
                                );
                                break;
                            case "$isNotNull":
                                orPredicate = criteriaBuilder.or(
                                    orPredicate,
                                    criteriaBuilder.isNotNull(entityRootType.get(key))
                                );
                                break;
                            case "$isNot":
                                orPredicate = criteriaBuilder.or(
                                    orPredicate,
                                    criteriaBuilder.notEqual(entityRootType.get(key), trim(value))
                                );
                                break;
                            case "$gt":
                                if (isEmpty(value)) {
                                    continue;
                                }
                                orPredicate = criteriaBuilder.or(
                                    orPredicate,
                                    criteriaBuilder.gt(entityRootType.get(key), Double.parseDouble(value))
                                );
                                break;
                        }
                    }
                }
            }

            predicate = criteriaBuilder.and(predicate, orPredicate);

            return this;
        }

        /**
         * 查询或。
         *
         * @param orCriteria 或查询条件
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> orObj(Map<String, Map<String, Object>> orCriteria) {

            if (orCriteria.size() == 0) {
                return this;
            }

            Predicate orPredicate = criteriaBuilder.disjunction();

            for (Map.Entry<String, Map<String, Object>> criterionMap : orCriteria.entrySet()) {

                // 获取条件的KEY
                String key = criterionMap.getKey();

                // 条件的操作及值
                if (criterionMap.getValue() != null && criterionMap.getValue().size() != 0) {

                    for (Map.Entry<String, Object> criterion : criterionMap.getValue().entrySet()) {

                        // 获取条件的值
                        Object value = criterion.getValue();

                        // 根据操作符匹配操作
                        switch (criterion.getKey()) {
                            case "$is":
                                if (isEmpty(value) || (!DataTypeUtils.isPrimitive(value) && !(value instanceof Enum))) {
                                    continue;
                                }
                                orPredicate = criteriaBuilder.or(
                                    orPredicate,
                                    criteriaBuilder.equal(entityRootType.get(key), trim(value))
                                );
                                break;
                            case "$like":
                                if (isEmpty(value)) {
                                    continue;
                                }
                                orPredicate = criteriaBuilder.or(
                                    orPredicate,
                                    criteriaBuilder.like(
                                        entityRootType.get(key),
                                        "%" + escapeLikeValue((String) trim(value)) + "%",
                                        '\\'
                                    )
                                );
                                break;
                            case "$isNotNull":
                                orPredicate = criteriaBuilder.or(
                                    orPredicate,
                                    criteriaBuilder.isNotNull(entityRootType.get(key))
                                );
                                break;
                            case "$isNull":
                                orPredicate = criteriaBuilder.or(
                                    orPredicate,
                                    criteriaBuilder.isNull(entityRootType.get(key))
                                );
                                break;
                            case "$isNot":
                                orPredicate = criteriaBuilder.or(
                                    orPredicate,
                                    criteriaBuilder.notEqual(entityRootType.get(key), trim(value))
                                );
                                break;
                            case "$gt":
                                if (isEmpty(value)) {
                                    continue;
                                }
                                orPredicate = criteriaBuilder.or(
                                    orPredicate,
                                    criteriaBuilder.gt(entityRootType.get(key), Double.parseDouble(value.toString()))
                                );
                                break;
                        }
                    }
                }
            }

            predicate = criteriaBuilder.and(predicate, orPredicate);

            return this;
        }

        /**
         * 查询条件：=。
         *
         * @param key   字段名
         * @param value 值
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> is(String key, Object value) {
            return is(key, value, entityRootType);
        }

        /**
         * 查询条件：=。
         *
         * @param key   字段名
         * @param value 值
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> is(String key, Object value, From from) {

            // Enum要处理
            if (isEmpty(value) || (!DataTypeUtils.isPrimitive(value) && !(value instanceof Enum))) {
                return this;
            }

            Path finalPath = null;
            if (!key.contains(".")) {
                finalPath = from.get(key);
            } else {
                String[] pathStr = key.split("\\.");
                Path tempPath = from.get(pathStr[0]);
                for (int i = 1; i < pathStr.length; i++) {
                    tempPath = tempPath.get(pathStr[i]);
                }
                finalPath = tempPath;
            }

            predicate = criteriaBuilder.and(
                predicate,
                criteriaBuilder.equal(finalPath, trim(value))
            );

            return this;
        }

        /**
         * 查询条件：!=。
         *
         * @param key   字段名
         * @param value 值
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> isNot(String key, Object value) {

            if (isEmpty(value) || (!DataTypeUtils.isPrimitive(value) && !(value instanceof Enum))) {
                return this;
            }
            predicate = criteriaBuilder.and(
                predicate,
                criteriaBuilder.notEqual(entityRootType.get(key), trim(value))
            );

            return this;
        }

        /**
         * 查询条件：IS NOT NULL。
         *
         * @param key 字段名
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> isNotNull(String key) {

            predicate = criteriaBuilder.and(
                predicate,
                criteriaBuilder.isNotNull(entityRootType.get(key))
            );

            return this;
        }

        /**
         * 查询条件：isNull
         *
         * @param key 字段名
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> isNull(String key) {

            predicate = criteriaBuilder.and(
                predicate,
                criteriaBuilder.isNull(entityRootType.get(key))
            );

            return this;
        }

        /**
         * 查询条件：<。
         *
         * @param key   字段名
         * @param value 值
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> lt(String key, Number value) {

            if (isEmpty(value)) {
                return this;
            }

            predicate = criteriaBuilder.and(
                predicate,
                criteriaBuilder.lt(entityRootType.get(key), value)
            );

            return this;
        }

        /**
         * 查询条件：<=。
         *
         * @param key   字段名
         * @param value 值
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> lte(String key, Number value) {

            if (isEmpty(value)) {
                return this;
            }

            predicate = criteriaBuilder.and(
                predicate,
                criteriaBuilder.le(entityRootType.get(key), value)
            );

            return this;
        }

        /**
         * 查询条件：>。
         *
         * @param key   字段名
         * @param value 值
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> gt(String key, Number value) {

            if (isEmpty(value)) {
                return this;
            }

            predicate = criteriaBuilder.and(
                predicate,
                criteriaBuilder.gt(entityRootType.get(key), value)
            );

            return this;
        }

        /**
         * 查询条件：>=。
         *
         * @param key   字段名
         * @param value 值
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> gte(String key, Number value) {

            if (isEmpty(value)) {
                return this;
            }

            predicate = criteriaBuilder.and(
                predicate,
                criteriaBuilder.ge(entityRootType.get(key), value)
            );

            return this;
        }

        /**
         * 查询条件：IN。
         *
         * @param key    字段名
         * @param values 值
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> in(String key, Object[] values) {

            if (values == null || values.length == 0) {
                return this;
            }

            in(key, new ArrayList<>(Arrays.asList(values)));

            return this;
        }

        /**
         * 查询条件：IN。
         *
         * @param key    字段名
         * @param values 值
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> in(String key, List<?> values) {

            if (isListEmpty(compressList(values))) {
                return this;
            }

            Path finalPath = null;
            if (!key.contains(".")) {
                finalPath = entityRootType.get(key);
            } else {
                String[] pathStr = key.split("\\.");
                Path tempPath = entityRootType.get(pathStr[0]);
                for (int i = 1; i < pathStr.length; i++) {
                    tempPath = tempPath.get(pathStr[i]);
                }
                finalPath = tempPath;
            }

            predicate = criteriaBuilder.and(
                predicate,
                finalPath.in(values)
            );

            return this;
        }

        /**
         * 查询条件：NOT IN。
         *
         * @param key    字段名
         * @param values 值
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> notIn(String key, List<?> values) {

            if (isListEmpty(compressList(values))) {
                return this;
            }

            Path finalPath = null;
            if (!key.contains(".")) {
                finalPath = entityRootType.get(key);
            } else {
                String[] pathStr = key.split("\\.");
                Path tempPath = entityRootType.get(pathStr[0]);
                for (int i = 1; i < pathStr.length; i++) {
                    tempPath = tempPath.get(pathStr[i]);
                }
                finalPath = tempPath;
            }

            predicate = criteriaBuilder.and(
                predicate,
                finalPath.in(values).not()
            );

            return this;
        }

        /**
         * 查询条件：LIKE。
         *
         * @param key   字段名
         * @param value 值
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> like(String key, String value) {

            if (isEmpty(value)) {
                return this;
            }

            predicate = criteriaBuilder.and(
                predicate,
                criteriaBuilder.like(
                    entityRootType.get(key),
                    "%" + escapeLikeValue((String) trim(value)) + "%",
                    '\\'
                )
            );

            return this;
        }

        /**
         * 范围之间。
         *
         * @param key       字段名
         * @param startTime 开始时间
         * @param endTime   结束时间
         * @return 查询构建器
         */
        public SQLQueryBuilder<T> between(String key, Date startTime, Date endTime) {
            if (isEmpty(key) || (isEmpty(startTime) && isEmpty(endTime))) {
                return this;
            }
            if (!isEmpty(startTime) && isEmpty(endTime)) {
                predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.greaterThanOrEqualTo(entityRootType.get(key), startTime)
                );
            } else if (isEmpty(startTime) && !isEmpty(endTime)) {
                predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.lessThanOrEqualTo(entityRootType.get(key), endTime)
                );
            } else {
                predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.between(entityRootType.get(key), startTime, endTime)
                );
            }
            return this;
        }

        /**
         * 分组：groupBy。
         *
         * @param key 字段名
         * @return 查询构建器
         */
        public SQLQueryBuilder<T> groupBy(String key) {
            if (isEmpty(key)) {
                return this;
            }
            entityCriteriaQuery.groupBy(entityRootType.get(key));
            return this;
        }

        /**
         * 升序。
         *
         * @param key 字段名
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> asc(String key) {
            orderList.add(new Sort.Order(Sort.Direction.ASC, key));
            return this;
        }

        /**
         * 降序。
         *
         * @param key 字段名
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> desc(String key) {
            orderList.add(new Sort.Order(Sort.Direction.DESC, key));
            return this;
        }

        /**
         * 设置排序。
         *
         * @param order 字段排序设置
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> sort(Sort.Order order) {

            if (order.getDirection() == Sort.Direction.ASC) {
                asc(order.getProperty());
            } else if (order.getDirection() == Sort.Direction.DESC) {
                desc(order.getProperty());
            }

            return this;
        }

        /**
         * 设置排序。
         *
         * @param sortOrder 排序设置
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> sort(Sort sortOrder) {
            if (sortOrder == null) {
                return this;
            }
            for (Sort.Order order : sortOrder) {
                sort(order);
            }
            return this;
        }

        /**
         * 设置页号。
         *
         * @param pageNo 页号
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> page(int pageNo) {
            this.pageNo = Math.max(0, pageNo);
            return this;
        }


        /**
         * 设置分页大小。
         *
         * @param pageSize 分页大小
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> limit(int pageSize) {
            this.pageSize = Math.max(1, pageSize);
            return this;
        }

        /**
         * 设置分页（设置排序、页号、分页大小等信息）。
         *
         * @param pageable 分页设置
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> paginate(Pageable pageable) {
            if (pageable == null) {
                pageable = PageRequest.of(0, Integer.MAX_VALUE);
            }
            return this
                .sort(pageable.getSort())
                .page(pageable.getPageNumber())
                .limit(pageable.getPageSize());
        }

        /**
         * 执行查询。
         *
         * @return SQL 查询构建器
         */
        public SQLQueryBuilder<T> exec() {

            entityCriteriaQuery.where(predicate);

            if ((count = entityManager.createQuery(entityCriteriaQuery.createCountQuery()).getSingleResult()) == 0) {
                return this;
            }

            if (orderList.size() > 0) {

                List<Order> orders = new ArrayList<>();

                for (Sort.Order order : orderList) {
                    if (order.getDirection() == Sort.Direction.ASC) {
                        orders.add(
                            criteriaBuilder.asc(
                                entityRootType.get(order.getProperty())
                            )
                        );
                    } else if (order.getDirection() == Sort.Direction.DESC) {
                        orders.add(
                            criteriaBuilder.desc(
                                entityRootType.get(order.getProperty())
                            )
                        );
                    }
                }

                entityCriteriaQuery.orderBy(orders);

            } else {

                entityCriteriaQuery.orderBy(
                    criteriaBuilder.desc(entityRootType.get("id"))
                );

            }

            entitySearchQuery = entityManager
                .createQuery(entityCriteriaQuery)
                .setFirstResult(pageNo * pageSize)
                .setMaxResults(pageSize);

            return this;
        }

        /**
         * 取得符合条件的数据的总数。
         *
         * @return 符合条件的数据的总数
         */
        public Long count() {
            return count;
        }

        /**
         * 取得查询结果列表。
         *
         * @return 查询结果列表
         */
        public List<T> list() {
            return entitySearchQuery == null
                ? new ArrayList<>()
                : entitySearchQuery.getResultList();
        }

        /**
         * 取得查询结果的分页数据。
         *
         * @return 查询结果的分页数据
         */
        public Page<T> page() {

            if (count() == null) {
                return null;
            }

            return new PageImpl<>(
                list(),
                PageRequest.of(pageNo, pageSize, Sort.by(orderList)),
                count()
            );

        }

    }

}
