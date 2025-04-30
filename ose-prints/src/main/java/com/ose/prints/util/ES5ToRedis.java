package com.ose.prints.util;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import redis.clients.jedis.Jedis;

import java.net.InetAddress;

public class ES5ToRedis {

    public static void main(String[] args) throws Exception {
        // 1. 连接到 Elasticsearch 5.5 集群
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch").build();
        TransportClient client5 = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

        // 2. 连接到 Redis
        Jedis jedis = new Jedis("localhost", 6301);
        System.out.println("Connected to Redis successfully!");
        jedis.auth("1qazxsw2");

        // 3. 获取 5.5 中的所有索引
        String[] indices = client5.admin().indices().prepareGetIndex().get().getIndices();

        // 4. 遍历索引并迁移数据到 Redis
        for (String index : indices) {
            System.out.println("Migrating data from index: " + index);
            migrateIndexDataToRedis(client5, jedis, index);
        }

        // 5. 关闭客户端
        client5.close();
        jedis.close();
    }

    private static void migrateIndexDataToRedis(TransportClient client5, Jedis jedis, String index) {
        try {
            // 1. 初始化 Scroll
            String scrollId = null;
            long scrollTimeout = 5 * 60 * 10;//00; // 5 分钟超时

            SearchResponse response = client5.prepareSearch(index)
                    .setSearchType(SearchType.DEFAULT)
                    .setQuery(QueryBuilders.matchAllQuery())
                    .setSize(10000) // 每次查询的文档数量 (不超过 index.max_result_window)
                    .setScroll(TimeValue.timeValueMillis(scrollTimeout))
                    .get();

            scrollId = response.getScrollId();
            System.out.println(scrollId);
            // 2. 循环读取数据，直到没有数据
            while (response.getHits().getHits().length > 0) {
                SearchHit[] shl = response.getHits().getHits();
                for (SearchHit hit : shl) {
                    String documentId = hit.getId(); // 文档 ID
                    String hitJson = hit.getSourceAsString(); // hit 的 JSON 字符串

                    // 将数据存储为 Redis 的 Hash
                    jedis.hset(index, documentId, hitJson); // Key: 索引名, Field: 文档 ID, Value: hit 的 JSON 字符串

                    System.out.println("Stored document to Redis: " + index + ":" + documentId);
                }

                // 3. 获取下一批数据
                response = client5.prepareSearchScroll(scrollId)
                        .setScroll(TimeValue.timeValueMillis(scrollTimeout))
                        .get();

                scrollId = response.getScrollId();
                System.out.println(scrollId);

            }

            // 4. 清除 Scroll
            client5.clearScroll(new ClearScrollRequest());

            System.out.println("Data migrated for index: " + index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
