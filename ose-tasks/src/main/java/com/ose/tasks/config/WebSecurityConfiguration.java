package com.ose.tasks.config;

import com.ose.filter.HttpAccessControlFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Web 安全配置。
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    /**
     * 取得跨域请求头设置滤器实例。
     *
     * @return 跨域请求头设置滤器实例
     */
    @Bean
    public HttpAccessControlFilter httpAccessControlFilter() {
        return new HttpAccessControlFilter();
    }

    /**
     * 配置 HTTP 安全。
     * @param httpSecurity HTTP 安全设置
     * @throws Exception 异常
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .authorizeRequests(requests -> requests
                .anyRequest().permitAll())
            .addFilterBefore(
                httpAccessControlFilter(),
                UsernamePasswordAuthenticationFilter.class
            )
            .csrf(csrf -> csrf
                .disable());
        return httpSecurity.build();
    }

    /**
     * 配置 Web 安全。
     * @param webSecurity Web 安全设置
     */
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (webSecurity) -> {
            webSecurity.ignoring().requestMatchers("/**");
        };
    }

}
