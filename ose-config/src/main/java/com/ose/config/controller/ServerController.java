package com.ose.config.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class ServerController {

    /**
     * 取得客户端远程 IP 地址。
     *
     * @param request HTTP 请求
     * @return 客户端远程 IP 地址
     */
    private static String getRemoteAddr(HttpServletRequest request) {

        String remoteAddr = request.getHeader("X-Real-Ip");

        if (!(remoteAddr == null || "".equals(remoteAddr))) {
            return remoteAddr;
        }

        remoteAddr = request.getHeader("X-Forwarded-For");

        if (!(remoteAddr == null || "".equals(remoteAddr))) {
            return remoteAddr.split(",")[0];
        }

        return request.getRemoteAddr();
    }

    /**
     * 发送 HTTP 请求。
     *
     * @param method    请求方法
     * @param request   原始 HTTP 请求
     * @param targetURL 请求 URL
     * @param data      请求数据
     * @return 响应结果
     */
    private static Response sendHttpRequest(
        final String method,
        final HttpServletRequest request,
        final String targetURL,
        final String data
    ) {
        HttpURLConnection connection = null;
        int httpStatus = 200;

        try {

            URL url = new URL(targetURL);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("User-Agent", request.getHeader("User-Agent"));
            connection.setRequestProperty("Authorization", request.getHeader("Authorization"));
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("X-Real-Ip", getRemoteAddr(request));

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            if (data != null) {
                OutputStream os = connection.getOutputStream();
                os.write(data.getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();
            }

            BufferedReader response = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = response.readLine()) != null) {
                result.append(line).append("\n");
            }

            response.close();

            return new Response(connection.getResponseCode(), result.toString());

        } catch (IOException e) {

            if (connection != null) {
                try {
                    httpStatus = connection.getResponseCode();
                } catch (IOException e1) {
                    //
                }
            }

            return new Response(httpStatus, "{}");

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 发送 GET 请求。
     *
     * @param request   原始 HTTP 请求
     * @param targetURL 请求 URL
     * @return 响应结果
     */
    private Response sendGetRequest(
        final HttpServletRequest request,
        final String targetURL
    ) {
        return sendHttpRequest("GET", request, targetURL, null);
    }

    /**
     * 发送 POST 请求。
     *
     * @param request   原始 HTTP 请求
     * @param targetURL 请求 URL
     * @return 响应结果
     */
    private Response sendPostRequest(
        final HttpServletRequest request,
        final String targetURL
    ) {
        return sendHttpRequest("POST", request, targetURL, null);
    }

    /**
     * 发送 POST 请求。
     *
     * @param request   原始 HTTP 请求
     * @param targetURL 请求 URL
     * @param data      请求数据
     * @return 响应结果
     */
    private Response sendPostRequest(
        final HttpServletRequest request,
        final String targetURL,
        final String data
    ) {
        return sendHttpRequest("POST", request, targetURL, data);
    }

    /**
     * 设置响应数据。
     *
     * @param response 响应实例
     * @param status   HTTP 状态码
     * @param data     响应数据
     */
    private void setResponseData(HttpServletResponse response, int status, String data) {
        try {
            response.setHeader("Content-Type", "application/json");
            response.setStatus(status);
            response
                .getOutputStream()
                .write(data.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * 取得服务器执行状态信息。
     *
     * @param request  HTTP 请求实例
     * @param response HTTP 响应实例
     * @param hosts    服务器（host:port）列表
     */
    @RequestMapping(method = GET, value = "/server/status")
    public void status(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestParam String hosts
    ) {
        List<String> results = new ArrayList<>();
        int status = 200;

        for (String host : hosts.split(",")) {
            Response res = sendGetRequest(request, String.format("http://%s/server/status", host));
            status = Math.max(status, res.getStatus());
            results.add(String.format("\"%s\":%s", host, res.getResponse()));
        }

        setResponseData(
            response,
            status,
            String.format("{%s}", String.join(",", results))
        );
    }

    /**
     * 暂停服务。
     *
     * @param request  HTTP 请求实例
     * @param response HTTP 响应实例
     * @param hosts    服务器（host:port）列表
     */
    @RequestMapping(method = POST, value = "/server/suspend")
    public void suspend(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestParam String hosts
    ) {
        List<String> results = new ArrayList<>();
        int status = 200;

        for (String host : hosts.split(",")) {
            Response res = sendPostRequest(request, String.format("http://%s/server/suspend", host));
            status = Math.max(status, res.getStatus());
            results.add(String.format("\"%s\":%s", host, res.getResponse()));
        }

        setResponseData(
            response,
            status,
            String.format("{%s}", String.join(",", results))
        );
    }

    /**
     * 关闭服务。
     *
     * @param request  HTTP 请求实例
     * @param response HTTP 响应实例
     * @param hosts    服务器（host:port）列表
     */
    @RequestMapping(method = POST, value = "/server/shutdown")
    public void shutdown(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestParam String hosts
    ) {
        List<String> results = new ArrayList<>();
        int status = 200;

        for (String host : hosts.split(",")) {
            Response res = sendPostRequest(request, String.format("http://%s/server/shutdown", host));
            status = Math.max(status, res.getStatus());
            results.add(String.format("\"%s\":%s", host, res.getResponse()));
        }

        setResponseData(
            response,
            status,
            String.format("{%s}", String.join(",", results))
        );
    }

    /**
     * 恢复服务。
     *
     * @param request  HTTP 请求实例
     * @param response HTTP 响应实例
     * @param hosts    服务器（host:port）列表
     */
    @RequestMapping(method = POST, value = "/server/restore")
    public void restore(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestParam String hosts
    ) {
        List<String> results = new ArrayList<>();
        int status = 200;

        for (String host : hosts.split(",")) {
            Response res = sendPostRequest(request, String.format("http://%s/server/restore", host));
            status = Math.max(status, res.getStatus());
            results.add(String.format("\"%s\":%s", host, res.getResponse()));
        }

        setResponseData(
            response,
            status,
            String.format("{%s}", String.join(",", results))
        );
    }

    /**
     * 恢复服务。
     *
     * @param request  HTTP 请求实例
     * @param response HTTP 响应实例
     * @param hosts    服务器（host:port）列表
     */
    @RequestMapping(method = POST, value = "/authorizations")
    public void signIn(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestParam String hosts,
        @RequestBody Credentials credentials
    ) {
        String data = String.format(
            "{\"username\":\"%s\",\"password\":\"%s\"}",
            credentials.getUsername(),
            credentials.getPassword()
        );

        int status = 200;
        String result = "{}";

        for (String host : hosts.split(",")) {
            Response res = sendPostRequest(request, String.format("http://%s/authorizations", host), data);
            status = Math.max(status, res.getStatus());
            if (status / 200.0 < 1.5) {
                result = res.getResponse();
                break;
            }
        }

        setResponseData(response, status, result);
    }

    public static class Response {

        private int status;

        private String response;

        public Response(int status, String response) {
            setStatus(status);
            setResponse(response);
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }

    public static class Credentials {

        private String username;

        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
