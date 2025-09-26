package com.lzx.utils;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.Timeout;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HTTP 工具类（基于 Apache HttpClient 5.x）
 * 提供 GET / POST（表单、JSON）/ 文件上传 / 文件下载功能
 * 内部使用连接池管理 HTTP 连接，提高性能
 */
public class HttpUtil {

    /**
     * 全局共享的 HttpClient 实例（同步）
     */
    private static final HttpClient HTTP_CLIENT;

    static {
        // 创建连接池管理器
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100); // 最大连接数
        cm.setDefaultMaxPerRoute(20); // 每个路由最大连接数

        // 设置 socket 超时时间
        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(Timeout.ofSeconds(10)) // 读取超时
                .build();
        cm.setDefaultSocketConfig(socketConfig);

        // 构建 HttpClient 实例
        HTTP_CLIENT = HttpClients.custom()
                .setConnectionManager(cm)
                .evictIdleConnections(Timeout.ofSeconds(30)) // 清理空闲连接
                .build();
    }

    // ==================== 公共方法 ====================

    /**
     * 执行 HTTP 请求并返回字符串结果
     *
     * @param request HTTP 请求对象
     * @return 响应字符串
     * @throws Exception 异常
     */
    private static String executeRequest(HttpUriRequest request) throws Exception {
        return HTTP_CLIENT.execute(request, response -> {
            int statusCode = response.getCode();
            if (statusCode >= 200 && statusCode < 300) {
                return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            } else {
                throw new RuntimeException("HTTP 请求失败: " + statusCode + " " + response.getReasonPhrase());
            }
        });
    }

    /**
     * 增加请求头到 HttpUriRequest 对象
     *
     * @param request HTTP 请求对象
     * @param headers 请求头映射
     */
    private static void addHeaders(HttpUriRequest request, Map<String, String> headers) {
        if (headers != null) {
            headers.forEach(request::addHeader);
        }
    }

    /**
     * 创建并配置 HttpGet 请求
     *
     * @param url     请求地址
     * @param headers 请求头
     * @return HttpGet 对象
     */
    private static HttpGet createHttpGet(String url, Map<String, String> headers) {
        HttpGet get = new HttpGet(url);
        addHeaders(get, headers);
        return get;
    }

    /**
     * 创建并配置 HttpPost 请求
     *
     * @param url     请求地址
     * @param headers 请求头
     * @return HttpPost 对象
     */
    private static HttpPost createHttpPost(String url, Map<String, String> headers) {
        HttpPost post = new HttpPost(url);
        addHeaders(post, headers);
        return post;
    }

    // ==================== GET 请求 ====================

    /**
     * 发送 GET 请求
     *
     * @param url 请求地址
     * @return 响应字符串
     * @throws Exception 异常
     */
    public static String get(String url) throws Exception {
        return get(url, null);
    }

    /**
     * 发送 GET 请求（带请求头）
     *
     * @param url     请求地址
     * @param headers 请求头
     * @return 响应字符串
     * @throws Exception 异常
     */
    public static String get(String url, Map<String, String> headers) throws Exception {
        HttpGet get = createHttpGet(url, headers);
        return executeRequest(get);
    }

    // ==================== POST Form 请求 ====================

    /**
     * 发送 POST 请求（表单形式）
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应字符串
     * @throws Exception 异常
     */
    public static String postForm(String url, Map<String, String> params) throws Exception {
        return postForm(url, params, null);
    }

    /**
     * 发送 POST 请求（表单形式，带请求头）
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @return 响应字符串
     * @throws Exception 异常
     */
    public static String postForm(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
        HttpPost post = createHttpPost(url, headers);

        List<NameValuePair> formParams = new ArrayList<>();
        params.forEach((k, v) -> formParams.add(new BasicNameValuePair(k, v)));

        post.setEntity(new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8));

        return executeRequest(post);
    }

    // ==================== POST JSON 请求 ====================

    /**
     * 发送 POST 请求（JSON 形式）
     *
     * @param url      请求地址
     * @param jsonBody JSON 请求体
     * @return 响应字符串
     * @throws Exception 异常
     */
    public static String postJson(String url, String jsonBody) throws Exception {
        return postJson(url, jsonBody, null);
    }

    /**
     * 发送 POST 请求（JSON 形式，带请求头）
     *
     * @param url      请求地址
     * @param jsonBody JSON 请求体
     * @param headers  请求头
     * @return 响应字符串
     * @throws Exception 异常
     */
    public static String postJson(String url, String jsonBody, Map<String, String> headers) throws Exception {
        HttpPost post = createHttpPost(url, headers);
        post.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
        return executeRequest(post);
    }

    // ==================== 文件上传 ====================

    /**
     * 文件上传（支持多文件 + 普通参数）
     *
     * @param url    请求地址
     * @param params 普通参数
     * @param files  文件参数
     * @return 响应字符串
     * @throws Exception 异常
     */
    public static String uploadFile(String url, Map<String, String> params, Map<String, File> files) throws Exception {
        return uploadFile(url, params, files, null);
    }

    /**
     * 文件上传（支持多文件 + 普通参数，带请求头）
     *
     * @param url     请求地址
     * @param params  普通参数
     * @param files   文件参数
     * @param headers 请求头
     * @return 响应字符串
     * @throws Exception 异常
     */
    public static String uploadFile(String url, Map<String, String> params, Map<String, File> files, Map<String, String> headers) throws Exception {
        HttpPost post = createHttpPost(url, headers);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        // 添加普通参数
        if (params != null) {
            params.forEach((k, v) -> builder.addTextBody(k, v, ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8)));
        }

        // 添加文件
        if (files != null) {
            files.forEach((k, file) -> builder.addBinaryBody(k, file, ContentType.APPLICATION_OCTET_STREAM, file.getName()));
        }

        post.setEntity(builder.build());

        return executeRequest(post);
    }

    // ==================== 文件下载 ====================

    /**
     * 文件下载
     *
     * @param url      请求地址
     * @param savePath 保存路径
     * @throws Exception 异常
     */
    public static void downloadFile(String url, String savePath) throws Exception {
        downloadFile(url, savePath, null);
    }

    /**
     * 文件下载（带请求头）
     *
     * @param url      请求地址
     * @param savePath 保存路径
     * @param headers  请求头
     * @throws Exception 异常
     */
    public static void downloadFile(String url, String savePath, Map<String, String> headers) throws Exception {
        HttpGet get = createHttpGet(url, headers);

        HTTP_CLIENT.execute(get, response -> {
            int statusCode = response.getCode();
            if (statusCode >= 200 && statusCode < 300) {
                HttpEntity entity = response.getEntity();
                try (FileOutputStream fos = new FileOutputStream(savePath)) {
                    entity.writeTo(fos);
                }
                EntityUtils.consume(entity);
                return null;
            } else {
                throw new RuntimeException("文件下载失败: " + statusCode + " " + response.getReasonPhrase());
            }
        });
    }
}