package com.group8.meetingall.utils;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;


public class HttpUtil {
    public static final String REQUSET_METHOD_POST = "POST";

    public static String post(String url, MultiValueMap<String, String> param) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/x-www-form-urlencoded; charset=UTF-8"));
        org.springframework.http.HttpEntity<MultiValueMap> requestEntity = new org.springframework.http.HttpEntity<>(param, headers);
        ResponseEntity<String>
                responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<String>() {
        });
        return responseEntity.getBody();
    }

    public static ResponseEntity post(String url, byte[] body, HttpHeaders headers) {
        RestTemplate restTemplate = new RestTemplate();
        org.springframework.http.HttpEntity<byte[]> requestEntity = new org.springframework.http.HttpEntity<>(body, headers);
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Object>() {
        });
    }

    public static String postMulti(String url, Map<String, String> param, byte[] body) {
        String result = null;
        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
        reqEntity.addPart("content", new ByteArrayBody(body, ContentType.DEFAULT_BINARY, param.get("slice_id")));

        for (Map.Entry<String, String> entry : param.entrySet()) {
            StringBody value = new StringBody(entry.getValue(), ContentType.create("text/plain", Consts.UTF_8));
            reqEntity.addPart(entry.getKey(), value);
        }
        HttpEntity httpEntiy = reqEntity.build();

        try {
            httpPost.setEntity(httpEntiy);
            httpResponse = httpClient.execute(httpPost);
            result = EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
