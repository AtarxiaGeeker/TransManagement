package com.example.trans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class TransManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransManagementApplication.class, args);
    }

}
