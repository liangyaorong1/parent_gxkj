package cn.gxkj.utils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class StringUtil {
    /**
     * 组织动态查询条件
     *
     * @param field
     * @param dataList
     * @return
     */
    public static StringBuilder getSelectCondition(String field, List<String> dataList) {
        StringBuilder subSqlBuilder = new StringBuilder();
        subSqlBuilder.append(" AND " + field + " in (");
        AtomicInteger index = new AtomicInteger(0);
        dataList.forEach(did -> {
            if (index.get() == 0) {
                subSqlBuilder.append("'" + did + "'");
            } else {
                subSqlBuilder.append(",'" + did + "'");
            }
            index.incrementAndGet();
        });
        subSqlBuilder.append(")");
        return subSqlBuilder;
    }
    /**
     * 组织动态查询条件
     *
     * @param field
     * @param dataList
     * @return
     */
    public static StringBuilder getIntegerSelectCondition(String field, List<Integer> dataList) {
        StringBuilder subSqlBuilder = new StringBuilder();
        subSqlBuilder.append(" AND " + field + " in (");
        AtomicInteger index = new AtomicInteger(0);
        dataList.forEach(did -> {
            if (index.get() == 0) {
                subSqlBuilder.append( did );
            } else {
                subSqlBuilder.append("," + did);
            }
            index.incrementAndGet();
        });
        subSqlBuilder.append(")");
        return subSqlBuilder;
    }
}
