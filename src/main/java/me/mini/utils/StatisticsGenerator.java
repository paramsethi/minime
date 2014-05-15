package me.mini.utils;

import me.mini.bean.SystemStatics;
import me.mini.cassandra.query.CassandraUrlQueryUtil;

public class StatisticsGenerator {

    public static SystemStatics systemStats() throws MinimeException {
        SystemStatics entity = new SystemStatics();
        long startTime = System.currentTimeMillis();
        long count = CassandraUrlQueryUtil.getInstance().countQuery();
        long totalTime = (System.currentTimeMillis() - startTime);
        entity.setTotalUrls(count);
        entity.setTimeTakenInMillis(totalTime);
        entity.setTotalMemory(Runtime.getRuntime().totalMemory());
        entity.setFreeMemory(Runtime.getRuntime().freeMemory());
        return entity;
    }
}
