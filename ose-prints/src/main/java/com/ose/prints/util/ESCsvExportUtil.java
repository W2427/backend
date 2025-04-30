package com.ose.prints.util;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

public class ESCsvExportUtil {

    public static void main(String[] args) throws Exception {
        // 1. 连接到 Elasticsearch 集群
        Settings settings = Settings.builder()
            .put("cluster.name", "elasticsearch").build();
        TransportClient client = new PreBuiltTransportClient(settings)
            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        // 修正后的代码
//        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        // 2. 获取所有索引
        String[] indices = client.admin().indices().prepareGetIndex().get().getIndices();

        // 3. 遍历索引并导出数据
        for (String index : indices) {
            System.out.println("Exporting data from index: " + index);
            exportIndexToCSV(client, index, "/var/www/"+index + ".csv");
        }

        // 4. 关闭客户端
        client.close();
    }

    private static void exportIndexToCSV(TransportClient client, String index, String csvFilePath) {
        ElasticsearchClient es8Client = es8Login();
        try (FileWriter writer = new FileWriter(csvFilePath)) {
            // 搜索所有文档
            SearchResponse response = client.prepareSearch(index)
                .setSearchType(SearchType.DEFAULT)
                .setQuery(QueryBuilders.matchAllQuery())
                .setSize(10000) // 每次查询的文档数量
                .get();

            // 写入 CSV 文件
            boolean isHeaderWritten = false;
            for (SearchHit hit : response.getHits().getHits()) {
                Map<String, Object> source = hit.getSourceAsMap();

                // 写入表头
//                if (!isHeaderWritten) {
//                    writer.write(String.join(",", source.keySet()) + "\n");
//                    isHeaderWritten = true;
//                }

                // 写入数据行
//                writer.write(String.join(",", source.values().toString()) + "\n");

//                es8Client.index(i -> i.index("ose").id(source.get("id").toString()).document(source));
                IndexResponse resp = es8Client.index(i -> i
                    .index("ose")
                    .id(source.get("id").toString())
                    .document(source)
                );
            }

            System.out.println("Data exported to: " + csvFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //////ES8 login
    public static ElasticsearchClient es8Login() {
        // 设置用户名和密码
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
            new UsernamePasswordCredentials("elastic", "1qazxsw2"));

//// 加载 CA 证书
//        Path caCertificatePath = Paths.get("path/to/http_ca.crt");
//        CertificateFactory factory = CertificateFactory.getInstance("X.509");
//        Certificate trustedCa;
//        try (InputStream is = Files.newInputStream(caCertificatePath)) {
//            trustedCa = factory.generateCertificate(is);
//        }
//
//// 配置 SSL 上下文
//        KeyStore trustStore = KeyStore.getInstance("pkcs12");
//        trustStore.load(null, null);
//        trustStore.setCertificateEntry("ca", trustedCa);
//        SSLContextBuilder sslContextBuilder = SSLContexts.custom()
//            .loadTrustMaterial(trustStore, null);
//        final SSLContext sslContext = sslContextBuilder.build();

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
        ElasticsearchClient client = new ElasticsearchClient(transport);
        return client;
    }
}
