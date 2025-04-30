package com.ose.docs.util;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RedisToES8 {

    public static void main(String[] args) throws Exception {
        // 1. 连接到 Redis
        // 设置连接超时为 2000 毫秒，读取超时为 5000 毫秒
        Jedis jedis = new Jedis("127.0.0.1", 6301, 10000);
        jedis.auth("1qazxsw2");

        System.out.println("Connected to Redis successfully!");

        // 2. 连接到 Elasticsearch 8.x
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", "1qazxsw2") // 用户名和密码
        );
        RestClientBuilder builder = RestClient.builder(
                        new HttpHost("127.0.0.1", 9200, "http"))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
//                .setSSLContext(sslContext)
                        .setDefaultCredentialsProvider(credentialsProvider));
        RestClient restClient = builder.build();

        System.out.println("Connected to es successfully!");

        // 创建 Elasticsearch 客户端
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());
        ElasticsearchClient client8 = new ElasticsearchClient(transport);

        System.out.println("Created to es client successfully!");

        // 3. 从 Redis 读取数据并写入 Elasticsearch 8.x
        migrateDataFromRedisToES8(jedis, client8, "ose");

        // 4. 关闭客户端
        jedis.close();
        restClient.close();
    }

    private static void migrateDataFromRedisToES8(Jedis jedis, ElasticsearchClient client8, String idx) {
        try {
            // 扫描 Redis 中的所有 Key（索引名）
            // 3. 获取 Redis 中所有 key 为索引的哈希
            Set<String> indices = jedis.keys(idx); // 获取所有 key

            for (String index : indices) {
                if (jedis.type(index).equals("hash")) { // 检查 key 是否为哈希类型
                    System.out.println("Started read redis index");
//                    Map<String, String> data = jedis.hgetAll(index); // 获取哈希中的所有数据
                    Map<String, String> data = scanHash(jedis, index);

                    System.out.println("Ended read redis index");

                    for (Map.Entry<String, String> entry : data.entrySet()) {
                        String id = entry.getKey(); // 文档 ID
                        String source = entry.getValue(); // 文档内容 (JSON 字符串)

                        // 4. 将 JSON 字符串反序列化为 JSONObject (remains the same)
                        JSONObject jsonObject = new JSONObject(source);

                        // 5. 创建 IndexRequest 并将数据存储到 ES (Corrected)
                        try (ByteArrayInputStream input = new ByteArrayInputStream(jsonObject.toString().getBytes(StandardCharsets.UTF_8))) {
                            IndexRequest<Object> request = new IndexRequest.Builder<Object>()
                                    .index(index)
                                    .id(id)
                                    .withJson(input)
                                    .build();

                            IndexResponse response = client8.index(request);
                            System.out.println("Indexed document with ID: " + response.id() + " to index: " + index);
                        }
                    }
                }
            }

            System.out.println("Data migration from Redis to Elasticsearch completed!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用 HSCAN 方式遍历哈希表，避免 hgetAll 导致超时
     */
    private static Map<String, String> scanHash(Jedis jedis, String hashKey) {
        Map<String, String> data = new HashMap<>();
        String cursor = "0"; // 游标初始值
        ScanParams scanParams = new ScanParams().count(1); // 每次获取 1000 条
        System.out.println("Started read 1000 redis info");
        do {
            ScanResult<Map.Entry<String, String>> scanResult = jedis.hscan(hashKey, cursor, scanParams);
            System.out.println("Ended read 1000 redis info");
            for (Map.Entry<String, String> entry : scanResult.getResult()) {
                data.put(entry.getKey(), entry.getValue());
            }
            cursor = scanResult.getCursor(); // 更新游标
        } while (!cursor.equals("0")); // 游标返回 0 表示遍历结束

        return data;
    }
}
