package com.grydl.orange.bigchaindb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BigchaindbApplication {

    public static void main(String[] args) {
        //SpringApplication.run(BigchaindbApplication.class, args);
        SpringApplication.run(BigchaindbApplication.class, args);


        try {
            //BigchaindbExemple.start();

            BigchaindbRestApi.test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
