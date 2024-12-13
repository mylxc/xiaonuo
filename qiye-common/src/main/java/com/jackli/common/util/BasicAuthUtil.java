package com.jackli.common.util;

import com.jackli.common.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @description:
 * @author: luoxingcheng
 * @created: 2024/01/30 09:13
 */
@Slf4j
public class BasicAuthUtil {

    private static String getUname(){
        return "five-axis";
    }

    private static  String getUpwd(){
        return "vT0%hW4!mD";
    }


    public static String createAuthenticatedConnection(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            connection.setRequestMethod("GET");
            String authString = getUname() + ":" + getUpwd();
            String authHeader = "Basic " + Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8));
            connection.setRequestProperty("Authorization", authHeader);
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            }else{
                log.error("请求发送失败，路径：{}", apiUrl);
                throw new CommonException("远程请求发送失败");
            }
        } catch (Exception e) {
            log.error("请求发送失败，路径：{}，异常信息：{}", apiUrl, e);
            throw new CommonException("远程请求发送失败");
        }finally {
            connection.disconnect();
        }
    }

    public static void createAuthenticatedPostConnection(String apiUrl, String jsonBody) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(10000);

        try {
            connection.setRequestMethod("POST");
            String authString = getUname() + ":" + getUpwd();
            String authHeader = "Basic " + Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8));
            connection.setRequestProperty("Authorization", authHeader);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                log.info("请求发送成功，路径:{},数据:{}", apiUrl, jsonBody);
            } else {
                log.error("请求发送失败，路径：{}，参数：{}", apiUrl, jsonBody);
                throw new CommonException("远程请求发送失败");
            }
        } catch (Exception e) {
            log.error("请求发送失败，路径：{}，参数：{},异常信息:{}", apiUrl, jsonBody, e);
            throw new CommonException("远程请求发送失败");
        }finally {
            connection.disconnect();
        }
    }

    public static boolean isValidBasicAuth(String authHeader){
        String authString = "five-axis" + ":" + "vT0%hW4!mD";
        String authHeader12 = "Basic " + Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8));
        return authHeader12.equals(authHeader);
    }

    public static void main(String[] args) {
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();

        list1.add("XD 63R0.8 L=200");
        list1.add("ZXCS-HJ-ZT 7.8 L=60 S=45 ER20=220");
        list1.add("PT-ZT 7.8 L=30  ER20");
        list1.add("JX-XD 8R0 L=20 ER16=50");
        list1.add("DZ 10*90@ L=45 ER16=120 ER32");
        list1.add("DJD 20*90@ L=75 ER32=100");
        list1.add("JD 8+0.018 L=50 ER20=220");

        list2.add("XD 63R0.8 L=200");
       list2.add("ZXCS-HJ-ZT 7.8 L=60 S=45 ER20=220");
       list2.add("PT-ZT 7.8 L=30  ER20");
        list2.add("JX-XD 8R0 L=20 ER16=50");
         list2.add("DZ 10*90@ L=45 ER16=120 ER32");
        list2.add("DJD 20*90@ L=75 ER32=100");
        list2.add("JD 8+0.018 L=50 ER20=220");

        if(list1.equals(list2)){
            System.out.println("相等");
        }




/*        String apiUrl = "https://example.com/api/resource";
        String username = "your_username";
        String password = "your_password";

        try {
            HttpURLConnection connection = BasicAuthUtil.createAuthenticatedConnection(apiUrl, username, password);
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
            } else {
                System.out.println("Request failed");
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

}
