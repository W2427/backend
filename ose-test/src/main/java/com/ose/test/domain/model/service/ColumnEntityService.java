package com.ose.test.domain.model.service;

import com.ose.test.domain.model.repository.*;
import com.ose.test.entity.ColumnEntity;
import com.ose.test.entity.ColumnNewEntity;
import com.ose.test.entity.DuplicatedKeyEntity;
import com.ose.test.entity.ResultEntity;
import com.ose.util.CollectionUtils;
import com.ose.util.CryptoUtils;
import com.ose.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;

/**
 * 业务代码服务。
 */
@Component
public class ColumnEntityService implements ColumnEntityInterface {

    // 业务代码数据仓库
    private final ColumnEntityRepository columnEntityRepository;

    private final TableDelEntityRepository tableDelEntityRepository;

    private final ColumnNewEntityRepository columnNewEntityRepository;

    private final ResultEntityRepository resultEntityRepository;

    private final DuplicatedKeyRepository duplicatedKeyRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public ColumnEntityService(ColumnEntityRepository columnEntityRepository,
                               TableDelEntityRepository tableDelEntityRepository,
                               ColumnNewEntityRepository columnNewEntityRepository,
                               ResultEntityRepository resultEntityRepository,
                               DuplicatedKeyRepository duplicatedKeyRepository) {
        this.columnEntityRepository = columnEntityRepository;
        this.tableDelEntityRepository = tableDelEntityRepository;
        this.columnNewEntityRepository = columnNewEntityRepository;
        this.resultEntityRepository = resultEntityRepository;
        this.duplicatedKeyRepository = duplicatedKeyRepository;
    }

    /**
     * 取得业务代码列表。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param type      业务代码类型
     * @return 业务代码列表
     */
    @Override
    public Page<ColumnEntity> list(
        Long orgId,
        Long projectId,
        String type,
        Pageable pageable
    ) {
        return null;
    }

    @Override
    public void checkColumn() {

        columnEntityRepository.findAll().forEach(
            columnEntity -> {
                ColumnNewEntity columnNewEntity = columnNewEntityRepository.findByDbAndTbAndColumn(
                                                        columnEntity.getDb(),
                                                    columnEntity.getTb(),
                                                    columnEntity.getColumn()
                                                ).orElse(null);
                // 不存在 或者 （存在 db，tb，column相等 当columnType 不相等）
                if(columnNewEntity == null) {
                    columnEntity.setMark("NOT_EXIST");
                    columnEntityRepository.save(columnEntity);
                } else {
                    String columnType = columnEntity.getColumnType().replaceAll("\\(\\d+\\)","");
                    String newColumnType = columnNewEntity.getColumnType().replaceAll("\\(\\d+\\)","");

                    if (!columnType.equalsIgnoreCase(newColumnType)) {
                        columnEntity.setMark("TYPE_CHANGE");
                        columnNewEntity.setMark("TYPE_CHANGE");
                        columnEntity.setNewColumnType(columnNewEntity.getColumnType());
                        columnEntityRepository.save(columnEntity);
                        columnNewEntityRepository.save(columnNewEntity);
                    } else {
                        columnEntity.setMark("NO_CHANGE");
                        columnNewEntity.setMark("NO_CHANGE");
                        columnEntityRepository.save(columnEntity);
                        columnNewEntityRepository.save(columnNewEntity);
                    }
                }

            }
        );

        //CHANGE_NAME

        columnNewEntityRepository.findByMarkIsNull().forEach(columnNewEntity -> {

            ColumnEntity columnEntity = new ColumnEntity();
            BeanUtils.copyProperties(columnNewEntity, columnEntity);
            columnEntity.setHandled(false);
            columnEntity.setMark("ADD");
            columnEntityRepository.save(columnEntity);
        });

    }

    @Override
    public void handle() {
//        columnEntityRepository.updateColumnEntityNull();

        columnEntityRepository.findByMarkAndIsHandledIsNull("TYPE_CHANGE").forEach(columnEntity -> {
            String db = columnEntity.getDb();
            String tb = columnEntity.getTb();
            String cl = columnEntity.getColumn();
            String nll = columnEntity.getNll();
            if(nll == null || "NULL".equalsIgnoreCase(nll)) {
                nll = "";
            }
            String clType = columnEntity.getNewColumnType();
            String errorStr = "";

            try {
                if (columnEntity.getNewColumnType().equalsIgnoreCase("bigint(20)")) {

                    errorStr = String.format("ALTER TABLE %s.%s MODIFY COLUMN %s varchar(20) %s ;",db,tb,cl,nll);
//                    CONCAT("ALTER TABLE
//                        `", DB,"`.`", TB, "` MODIFY COLUMN `",CL,"` varchar(20) ",
//                    NLL, ";");
                    columnEntityRepository.changeColumn(db, tb, cl, nll);

                    errorStr = String.format("UPDATE %s.%s SET %s = CONV(LEFT(%s,12),36,10);",db,tb,cl,cl);

//                    CONCAT("UPDATE
//                        `", DB,"`.`", TB, "` SET `",CL,"` = CONV(LEFT(`",
//                        CL,"`,12),36,10)");

                    columnEntityRepository.updateColumn(db, tb, cl);

                    errorStr = String.format("ALTER TABLE %s.%s MODIFY COLUMN %s %s %s;",db,tb,cl,clType,nll);
//                    CONCAT("ALTER TABLE
//                        `", DB,"`.`", TB, "` MODIFY COLUMN `",CL,"` ",CL_TYPE,
//                    NLL, ";");

                    columnEntityRepository.changeColumnType(db, tb, cl, nll, clType);
                } else {
                    errorStr = String.format("ALTER TABLE %s.%s MODIFY COLUMN %s %s %s;",db,tb,cl,clType,nll);
                    columnEntityRepository.changeColumnType(db, tb, cl, nll, clType);

                }
                columnEntity.setHandled(true);
                columnEntityRepository.save(columnEntity);
            } catch (Exception e) {
                columnEntity.setAction(errorStr);
                try {
                    columnEntity.setExecResult(((DataIntegrityViolationException) e).getRootCause().getMessage());
                } catch (Exception ee) {
                    try {
                       columnEntity.setExecResult(((InvalidDataAccessResourceUsageException) e).getRootCause().getMessage());
                    } catch (Exception eee) {
                        columnEntity.setExecResult("ERROR");
                    }
                    ee.printStackTrace();
                }
                columnEntityRepository.save(columnEntity);
                System.out.println(e.getMessage());
            }

        });
    }

    @Override
    public void delTable() {
            tableDelEntityRepository.findAll().forEach(tableDelEntity -> {
                if (tableDelEntity.getExecResult() != null) {
                    return;
                }
                try {
                    columnEntityRepository.delTable(tableDelEntity.getDb(), tableDelEntity.getTb());
                    tableDelEntity.setExecResult("Done");
                    tableDelEntityRepository.save(tableDelEntity);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            });

    }

    @Override
    public void delColumn() {
        System.out.println("DELCOL");
        columnEntityRepository.findByMark("NOT_EXIST").forEach(columnEntity -> {
            if(columnEntity.getHandled()!=null) {return;}
            try {
                columnEntityRepository.delColumn(columnEntity.getDb(), columnEntity.getTb(), columnEntity.getColumn());
                columnEntity.setHandled(true);
                columnEntityRepository.save(columnEntity);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        });
    }

    @Override
    public void handleDuplicate() {
        columnEntityRepository.findByColumnAndMark("id", "TYPE_CHANGE").
            forEach(columnEntity -> {
                String db = columnEntity.getDb();
                String tb = columnEntity.getTb();
                String cl = columnEntity.getColumn();

                columnEntityRepository.findData("SELECT LEFT(id, 12) AS ID1 FROM "+ db + "." + tb + " GROUP BY ID1 HAVING COUNT(ID1)>1").
                    forEach(data -> {
                        List<ResultEntity> resultEntities =resultEntityRepository.findByDbAndTbAndColumn(db, tb, cl); //如果存在重复，在result表中加入字段，先检查是否有重复
                        //如果没有就插入一条记录
                        if(CollectionUtils.isEmpty(resultEntities)){
                            ResultEntity resultEntity = new ResultEntity();
                            resultEntity.setAction("DuplicateAdd");
                            resultEntity.setColumn(cl);
                            resultEntity.setTb(tb);
                            resultEntity.setDb(db);
                            resultEntity.setColumnType(columnEntity.getColumnType());
                            resultEntityRepository.save(resultEntity);
                        }

                        String duplicateId12 = (String) data.get(0);
                        List<List<Object>> dupIDs = columnEntityRepository.findData("SELECT id FROM "+ db + "." + tb + " WHERE id like '" + duplicateId12 + "%'");
                        int i = 0;
                        for(List<Object> dupId : dupIDs) {
                            if(i==0) {
                                i++;
                                continue;
                            }
                            String id = (String) dupId.get(0);
                            Long idVal = CryptoUtils.th36To10(duplicateId12);
                            while (true) { //ID +1 直至没有重复
                                idVal = idVal + i;
                                String nDuplicateId12 = Long.toString(idVal, 36).toUpperCase();
//                                    CryptoUtils.DeciamlToThirtySix(idVal);
                                List<List<Object>> tmpDup = columnEntityRepository.findData("SELECT id FROM "+ db + "." + tb + " WHERE LEFT(id,12) = '" + nDuplicateId12 + "'");
                                if(CollectionUtils.isEmpty(tmpDup)) {
                                    //如果已经不重复了
                                    String nId = nDuplicateId12 + id.substring(12,16);
                                    //插入记录表
                                    DuplicatedKeyEntity duplicatedKeyEntity = duplicatedKeyRepository.findByOldValue(id);
                                    if(duplicatedKeyEntity == null) {
                                        duplicatedKeyEntity = new DuplicatedKeyEntity();
                                    }
                                    duplicatedKeyEntity.setDb(db);
                                    duplicatedKeyEntity.setTb(tb);
                                    duplicatedKeyEntity.setColumn(cl);
                                    duplicatedKeyEntity.setOldValue(id);
                                    duplicatedKeyEntity.setNewValue(nId);
                                    duplicatedKeyRepository.save(duplicatedKeyEntity);
                                    //替换表中的值为新值
                                    columnEntityRepository.updateDuplicateColumn(db, tb, "id", id, nId);
                                    break;
                                }
                                i++;
                            }
                        }

                    });
            });
    }

    @Override
    public void handleRelatedField() {
        resultEntityRepository.findByExecResultIsNull().forEach(
            resultEntity -> {

                String relatedCl = resultEntity.getToReplacedColumn();
                List<ColumnEntity> columns = columnEntityRepository.findByColumnAndMark(relatedCl, "TYPE_CHANGE");

                columns.forEach(columnEntity -> {
                    String db = columnEntity.getDb();
                    String tb = columnEntity.getTb();
                    duplicatedKeyRepository.findByDbAndTbAndColumnAndExecResultIsNull(db, resultEntity.getTb(), resultEntity.getColumn()).
                        forEach(duplicatedKeyEntity -> {

                            columnEntityRepository.updateDuplicateColumn(db, tb, relatedCl, duplicatedKeyEntity.getOldValue(),duplicatedKeyEntity.getNewValue());
                            duplicatedKeyEntity.setExecResult("DONE");
                            duplicatedKeyRepository.save(duplicatedKeyEntity);
                        });
                });

                resultEntity.setExecResult("DONE");
                resultEntityRepository.save(resultEntity);
            }
        );
    }

    @Override
    public void handlePath() {
//        3. ose_auth.organizations, ose_tasks.hierarchy_node, ose_tasks.wbs_entry
//        Ose_tasks.work_site
        List<ColumnEntity> pathColumnEntities = new ArrayList<>();
        ColumnEntity cl1 = new ColumnEntity();
        cl1.setDb("ose_auth");
        cl1.setTb("organizations");
        cl1.setColumn("path");
        pathColumnEntities.add(cl1);

        ColumnEntity cl2 = new ColumnEntity();
        cl2.setDb("ose_tasks");
        cl2.setTb("hierarchy_node");
        cl2.setColumn("path");
        pathColumnEntities.add(cl2);


        ColumnEntity cl3 = new ColumnEntity();
        cl3.setDb("ose_tasks");
        cl3.setTb("wbs_entry");
        cl3.setColumn("path");
        pathColumnEntities.add(cl3);


        ColumnEntity cl4 = new ColumnEntity();
        cl4.setDb("ose_tasks");
        cl4.setTb("work_site");
        cl4.setColumn("path");
        pathColumnEntities.add(cl4);

        ColumnEntity cl5 = new ColumnEntity();
        cl5.setDb("ose_auth");
        cl5.setTb("organizations");
        cl5.setColumn("children");
        pathColumnEntities.add(cl5);

        ColumnEntity cl6 = new ColumnEntity();
        cl6.setDb("ose_issue");
        cl6.setTb("issue_tags");
        cl6.setColumn("children");
        pathColumnEntities.add(cl6);

        ColumnEntity cl7 = new ColumnEntity();
        cl7.setDb("ose_tasks");
        cl7.setTb("penalty_inspection");
        cl7.setColumn("children");
        pathColumnEntities.add(cl7);

        ColumnEntity cl8 = new ColumnEntity();
        cl8.setDb("ose_auth");
        cl8.setTb("roles");
        cl8.setColumn("org_path");
        pathColumnEntities.add(cl8);

        Map<String, String> duplicateMap = new HashMap<>();

        duplicatedKeyRepository.findAll().forEach(duplicatedKeyEntity -> {
            if(!StringUtils.isEmpty(duplicatedKeyEntity.getOldValue()) && !StringUtils.isEmpty(duplicatedKeyEntity.getNewValue())) {
                duplicateMap.put(duplicatedKeyEntity.getOldValue(), duplicatedKeyEntity.getNewValue());
            }
        });

        pathColumnEntities.forEach(pathColumnEntity -> {
            String db = pathColumnEntity.getDb();
            String tb = pathColumnEntity.getTb();
            String cl = pathColumnEntity.getColumn();

            columnEntityRepository.findData("SELECT "+cl+", id FROM "+ db + "." + tb).forEach(pathObject ->{
                String tmpPath = (String)pathObject.get(0);
                if(!StringUtils.isEmpty(tmpPath)) {
                    tmpPath = tmpPath.replaceAll("null","/");
                }
                String path = tmpPath;
                Long id = ((BigInteger) pathObject.get(1)).longValue();
                if(!StringUtils.isEmpty(path)) {

                    List<String> pathArr = Arrays.asList(path.split("/"));
                    if(cl.equalsIgnoreCase("children")) {
                        pathArr = Arrays.asList(path.split(","));
                    }
                    List<String> newPathArr = new ArrayList<>();
                    pathArr.forEach(pathElement -> {
                        if(StringUtils.isEmpty(pathElement)) {
                            return;
                        }
                        if(duplicateMap.get(pathElement) != null) {
                            pathElement = duplicateMap.get(pathElement);
                        }
                        System.out.println(path);
                        newPathArr.add(CryptoUtils.th36To10(pathElement.substring(0,12)).toString());
                    });
                    String newPath = "/" + String.join("/", newPathArr) + "/";
                    if(cl.equalsIgnoreCase("children")) {
                        newPath = String.join(",",newPathArr);
                    }
                    if(newPath.equalsIgnoreCase("//")) {
                        newPath = "/";
                    }
//                    CONCAT("UPDATE
//                        `", DB,"`.`", TB, "` SET `",CL,"` = '", NEW_VALUE,"' WHERE `id`=",_ID,"");
                    String errorStr = "UPDATE `" + db +"`.`" + tb + "` SET `"+ cl + "` = '" + newPath + "' WHERE `id`="+ id.toString();
                    columnEntityRepository.updatePathColumn(db,tb,cl,id,newPath);
                }
            });
        });


    }

    @Override
    public void handleBpmField() {

        Set<String> bpmColumns = new HashSet<String>(){{
            add("assignee_");
            add("tenant_id_");
        }};
        columnEntityRepository.findByDbAndColumnIn("ose_bpm", bpmColumns).
            forEach(columnEntity -> {
                String db = columnEntity.getDb();
                String tb = columnEntity.getTb();
                String cl = columnEntity.getColumn();
                String nll = columnEntity.getNll();
                if(nll == null || "NULL".equalsIgnoreCase(nll)) {
                    nll = "";
                }
                String clType = columnEntity.getNewColumnType();
                String errorStr = "";
                try {
                    errorStr = String.format("ALTER TABLE %s.%s MODIFY COLUMN %s varchar(20) %s ;", db, tb, cl, nll);
                    if(!cl.equalsIgnoreCase("assignee_")) {

                        columnEntityRepository.changeColumn(db, tb, cl, nll);
                    } else {
                        List<List<Object>> assigneeObjs = columnEntityRepository.findData("SELECT assignee_ FROM "+ db + "." + tb + " WHERE assignee_ like '%,%'");
                        for(List<Object> assigneeObj : assigneeObjs) {

                            String assigneeStr = (String) assigneeObj.get(0);
                            if(StringUtils.isEmpty(assigneeStr)) {continue;}

                            if(!assigneeStr.substring(0,1).matches("\\w")) {continue;}
                            List<String> assignees = Arrays.asList(assigneeStr.split(","));
                            List<String> newAssignees = new ArrayList<>();
                            String newAssigneeStr = "";
                            assignees.forEach(assignee ->{
                                newAssignees.add(CryptoUtils.th36To10(assignee.substring(0,12)).toString());
                            });
                            newAssigneeStr = String.join(",",newAssignees);
                            errorStr = "UPDATE `" + db +"`.`" + tb + "` SET `"+ cl + "` = '" + newAssigneeStr + "' WHERE `assignee_`='"+ assigneeStr + "'";
                            columnEntityRepository.updateAssigneeColumn(db,tb,"assignee_",assigneeStr,newAssigneeStr);
                        }
                    }

                    errorStr = String.format("UPDATE %s.%s SET %s = CONV(LEFT(%s,12),36,10);", db, tb, cl, cl);
                    columnEntityRepository.updateColumn(db, tb, cl);
                } catch (Exception e) {
                    columnEntity.setAction(e.getMessage());
                    columnEntityRepository.save(columnEntity);
                    e.printStackTrace();
                }

            });

    }

}
