package com.grydl.orange.bigchaindb;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class BigchaindbRestApi {

    private static RestTemplate restTemplate = new RestTemplate();
    private static String bigchaindburl   = "http://localhost:9984";
    private static String api = "/api/v1/";


    @Autowired
    ObjectMapper objectMapper;

    public  static void test() {

        JsonNode result = restTemplate.exchange(bigchaindburl, HttpMethod.GET, null, JsonNode.class).getBody();
        System.out.println(ResponseEntity.ok().body(result));

    }

}
