package com.ose.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Redis 集群配置属性。
 */
@Component
@ConfigurationProperties("spring.data.redis.cluster")
public class RedisClusterConfigurationProperties {

    // 节点列表
    private List<String> nodes;

    // 最大重定向数
    private Integer maxRedirects;

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public Integer getMaxRedirects() {
        return maxRedirects;
    }

    public void setMaxRedirects(Integer maxRedirects) {
        this.maxRedirects = maxRedirects;
    }

}
