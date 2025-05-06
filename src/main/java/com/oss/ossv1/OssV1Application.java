package com.oss.ossv1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.validation.annotation.Validated;

@SpringBootApplication
@Validated // To enable validation globally
public class OssV1Application {

    public static void main(String[] args) {
        SpringApplication.run(OssV1Application.class, args);
    }

}
