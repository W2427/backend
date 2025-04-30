package com.ose.tasks.vo.wbs;

import com.ose.tasks.entity.wbs.entity.*;
import com.ose.tasks.entity.wbs.structureEntity.*;
import com.ose.util.StringUtils;
import com.ose.vo.ValueObject;
import jakarta.persistence.Table;

import java.util.*;

/**
 * 实体类型。
 */
public enum WBSEntityType implements ValueObject {

    /*
    对于 焊口，管件等 所挂层级不确定的部件，设定 isFixedLevel 属性为false，即 所挂载的层级不确定
     */
    COMPONENT("组件", ComponentEntity.class, null, false),
    PIPE_COMPONENT("组件", ComponentEntity.class, null, false),
    PIPE_PIECE("管段", PipePieceEntity.class),
    WELD_JOINT("管系焊口", WeldEntity.class, null, false),
    STRUCT_WELD_JOINT("结构焊口", StructureWeldEntity.class, null, false),
    SPOOL("单管", SpoolEntity.class, new WBSEntityType[]{PIPE_PIECE, WELD_JOINT, COMPONENT}),
    ISO("管线", ISOEntity.class, new WBSEntityType[]{SPOOL, PIPE_PIECE, WELD_JOINT, COMPONENT}),
    PRESSURE_TEST_PACKAGE("试压包", PressureTestPackageEntityBase.class, new WBSEntityType[]{ISO}),
    CLEAN_PACKAGE("清洁包", CleanPackageEntityBase.class, new WBSEntityType[]{ISO}),
    SUB_SYSTEM("子系统", SubSystemEntityBase.class, new WBSEntityType[]{ISO}),
    WP05("零件", Wp05Entity.class),
    WP04("构件", Wp04Entity.class, new WBSEntityType[]{STRUCT_WELD_JOINT, WP05}),
    WP03("片体", Wp03Entity.class, new WBSEntityType[]{STRUCT_WELD_JOINT, WP04, WP05}),
    WP02("分段", Wp02Entity.class, new WBSEntityType[]{STRUCT_WELD_JOINT, WP03, WP04, WP05}),
    WP01("模块", Wp01Entity.class, new WBSEntityType[]{STRUCT_WELD_JOINT, WP02, WP03, WP04, WP05}),
    PUNCH("遗留问题", null),
    SHOP_DRAWING("生产设计图纸文档", null);

    private String displayName;

    private Class entityClass;

    private String tableName = null;

    private List<WBSEntityType> childTypes = null;

    private boolean isFixedLevel = true;

    private static final Map<String, WBSEntityType> PARENT_MAP = new HashMap<>();

    static {
        PARENT_MAP.put("WP05", WP04);
        PARENT_MAP.put("WP04", WP03);
        PARENT_MAP.put("WP03", WP02);
        PARENT_MAP.put("WP02", WP01);
        PARENT_MAP.put("PIPE_PIECE", SPOOL);
        PARENT_MAP.put("SPOOL", ISO);
    }


    /**
     * 构造方法。
     *
     * @param displayName 实体名称
     * @param entityClass 数据实体类型
     */
    WBSEntityType(String displayName, Class entityClass) {
        this(displayName, entityClass, null);
    }

    /**
     * 构造方法。
     *
     * @param displayName 实体名称
     * @param entityClass 数据实体类型
     * @param childTypes  接受的子节点的实体类型数组
     */
    WBSEntityType(String displayName, Class entityClass, WBSEntityType[] childTypes) {
        this(displayName, entityClass, childTypes, true);

    }


    /**
     * 构造方法。
     *
     * @param displayName 实体名称
     * @param entityClass 数据实体类型
     * @param childTypes  接受的子节点的实体类型数组
     */
    WBSEntityType(String displayName, Class entityClass, WBSEntityType[] childTypes, boolean isFixedLevel) {

        this.displayName = displayName;

        this.entityClass = entityClass;

        this.isFixedLevel = isFixedLevel;

        if (entityClass != null) {
            Table tableInfo = (Table) entityClass.getAnnotation(Table.class);
            if (tableInfo != null) {
                this.tableName = tableInfo.name();
            }
        }

        if (childTypes != null) {
            this.childTypes = Arrays.asList(childTypes);
        } else {
            this.childTypes = new ArrayList<>();
        }

    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 取得数据实体类型。
     *
     * @return 数据实体类型
     */
    public Class getEntityClass() {
        return entityClass;
    }

    /**
     * 取得子实体类型列表。
     *
     * @return 子实体类型列表
     */
    public List<WBSEntityType> getChildTypes() {
        return childTypes;
    }

    /**
     * 判断当前节点下是否可以添加指定类型的子节点。
     *
     * @param childType 子节点实体类型
     * @return 当前节点下是否可以添加指定类型的子节点
     */
    public boolean isParentOf(WBSEntityType childType) {
        return childTypes != null && childTypes.indexOf(childType) >= 0;
    }

    public WBSEntityType getParentType(WBSEntityType type) {


        return PARENT_MAP.get(type.name());


    }

    /**
     * 根据名称取得枚举值。
     *
     * @param name 名称
     * @return 枚举值
     */
    public static WBSEntityType getByName(String name) {

        if (StringUtils.isEmpty(name)) {
            return null;
        }

        for (WBSEntityType type : WBSEntityType.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    public String getTableName() {
        return tableName;
    }

    public Boolean isFixedLevel() {
        return isFixedLevel;
    }

    public static List<String> getEntityTypeNames() {

        List<String> entityTypeNames = new ArrayList<>();

        for(WBSEntityType entityType : WBSEntityType.values()) {
            entityTypeNames.add(entityType.name());
        }

        return entityTypeNames;
    }
}
