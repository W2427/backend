package com.ose.materialspm.domain.model.repository;

import com.ose.exception.BusinessError;
import com.ose.materialspm.dto.ReceiveReceiptResultDTO;
import com.ose.repository.BaseRepository;
import org.hibernate.Session;

import jakarta.transaction.Transactional;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.concurrent.atomic.AtomicReference;

/**
 * SPM 执行反写处理。
 */
public class TransRepositoryImpl extends BaseRepository implements TransRepositoryCustom {

    @Override
    @Transactional
    public ReceiveReceiptResultDTO callProcedure(String mrrNumber, String projId, String transType) {

        AtomicReference<String> result = new AtomicReference<>("");
        AtomicReference<String> message = new AtomicReference<>("");

        String sql;
        switch (transType) {
            // 入库
            case "MRR":
                sql = "{ ? = call W_PCK_SPM_OSE.TRANS_MRR(?,?,?,?) }";

                break;
            // 出库
            case "MIR":
                sql = "{ ? = call W_PCK_SPM_OSE.TRANS_MIR(?,?,?,?) }";

                break;
            // 退库
            case "RTI":
                sql = "{ ? = call W_PCK_SPM_OSE.TRANS_RTI(?,?,?,?) }";

                break;
            default:
                // TODO
                throw new BusinessError();
        }
        Session session = this.getEntityManager().unwrap(Session.class);

        String finalSql = sql;
        session.doReturningWork(
            connection -> {
                try {
                    CallableStatement function = connection.prepareCall(finalSql);
                    function.registerOutParameter(1, Types.INTEGER);
                    function.registerOutParameter(4, Types.INTEGER);
                    function.registerOutParameter(5, Types.VARCHAR);
                    function.setString(2, mrrNumber);
                    function.setString(3, projId);
                    function.execute();
                    System.out.println("[R#1]" + function.getInt(1));
                    System.out.println("[R#4]" + function.getInt(4));
                    System.out.println("[R#5]" + function.getString(5));

                    result.set(function.getInt(4) + "");
                    message.set(function.getString(5));

                    return function.getInt(1);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                return 0;
            });
        return new ReceiveReceiptResultDTO(result.get(), message.get());
    }

}
