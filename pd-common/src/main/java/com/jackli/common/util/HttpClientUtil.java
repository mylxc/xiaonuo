package com.jackli.common.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: httpClient工具类
 * @author: luoxingcheng
 * @created: 2023/12/16 10:28
 */
@Slf4j
public class HttpClientUtil {
    private static final HttpClient httpClient = HttpClients.createDefault();

    public static HttpResponse sendGetRequest(String url, Map<String, String> params) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            uriBuilder.setParameter(entry.getKey(), entry.getValue());
        }

        URI uri = uriBuilder.build();
        HttpGet request = new HttpGet(uri);

        HttpResponse response = httpClient.execute(request);
        return response;
    }

    public static String sendJsonPostRequest(String url, String json) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        StringEntity requestEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
        httpPost.setEntity(requestEntity);

        HttpResponse response = httpClient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode >= 200 && statusCode < 300) {
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                return EntityUtils.toString(responseEntity);
            }
        }
        return null;
    }


    public static String sendPostRequest(String url, Map<String, String> params) throws Exception {
        HttpPost request = new HttpPost(url);

        List<NameValuePair> formParams = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        request.setEntity(new UrlEncodedFormEntity(formParams));

        HttpResponse response = httpClient.execute(request);
        return readResponse(response);
    }

    private static String readResponse(HttpResponse response) throws Exception {
        HttpEntity entity = response.getEntity();
        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));

        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }

    public static void main(String[] args) {
        try {
            Map<String, String> getParams1 = new HashMap<>();
            getParams1.put("param1","value1");
            getParams1.put("param2","value2");

            System.out.println(JSON.toJSONString(getParams1));


            HttpClientUtil myHttpClient = new HttpClientUtil();

            // 发送GET请求
            String getUrl = "https://api.example.com/data";
            Map<String, String> getParams = new HashMap<>();
            getParams.put("param1","value1");
            getParams.put("param2","value2");

            String getResponse = "";//myHttpClient.sendGetRequest(getUrl, getParams);
            System.out.println("GET Response: " + getResponse);

            // 发送POST请求
            String postUrl = "https://api.example.com/create";
            Map<String, String> postParams = new HashMap<>();
            postParams.put("name","John");
            postParams.put("age","30");
            String postResponse = "";//myHttpClient.sendPostRequest(postUrl, postParams);
            System.out.println("POST Response: " + postResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
