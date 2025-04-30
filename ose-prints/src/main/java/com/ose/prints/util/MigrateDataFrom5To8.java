package com.ose.prints.util;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

public class MigrateDataFrom5To8 {

    public static void main(String[] args) throws Exception {
        // 1. 连接到 Elasticsearch 5.5 集群
        Settings settings = Settings.builder()
            .put("cluster.name", "elasticsearch").build();
        TransportClient client5 = new PreBuiltTransportClient(settings)
            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

        // 2. 连接到 Elasticsearch 8.15.2 集群（带认证）
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
            AuthScope.ANY,
            new UsernamePasswordCredentials("elastic", "1qazxsw2") // 用户名和密码
        );



        //////
        // 构建 RestClient
        RestClientBuilder builder = RestClient.builder(
                new HttpHost("localhost", 9220, "http"))
            .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
//                .setSSLContext(sslContext)
                .setDefaultCredentialsProvider(credentialsProvider));
//                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE));

        RestClient restClient = builder.build();

// 创建 Elasticsearch 客户端
        ElasticsearchTransport transport = new RestClientTransport(
            restClient, new JacksonJsonpMapper());
        ElasticsearchClient client8 = new ElasticsearchClient(transport);

        ///////

        // 3. 获取 5.5 中的所有索引
        String[] indices = client5.admin().indices().prepareGetIndex().get().getIndices();

        // 4. 遍历索引并迁移数据
        for (String index : indices) {
            System.out.println("Migrating data from index: " + index);
            migrateIndexData(client5, client8, index);
        }

        // 5. 关闭客户端
        client5.close();
        restClient.close();
    }

    private static void migrateIndexData(TransportClient client5, ElasticsearchClient client8, String index) {
        try {
            // 检查 8.15.2 中是否存在该索引，如果不存在则创建
            if (!client8.indices().exists(ExistsRequest.of(e -> e.index(index))).value()) {
                client8.indices().create(CreateIndexRequest.of(c -> c.index(index)));
            }

            // 从 5.5 中读取数据
            SearchResponse response = client5.prepareSearch(index)
                .setSearchType(SearchType.DEFAULT)
                .setQuery(QueryBuilders.matchAllQuery())
                .setSize(10000) // 每次查询的文档数量
                .get();

            // 将数据写入 8.15.2
            for (SearchHit hit : response.getHits().getHits()) {
                Map<String, Object> source = hit.getSourceAsMap();
                IndexRequest<Map<String, Object>> indexRequest = IndexRequest.of(i -> i
                    .index(index)
                    .id(hit.getId()) // 保留文档 ID
                    .document(source));
                client8.index(indexRequest);
            }

            System.out.println("Data migrated for index: " + index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
