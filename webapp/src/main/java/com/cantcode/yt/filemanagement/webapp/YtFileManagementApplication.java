package com.cantcode.yt.filemanagement.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class YtFileManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(YtFileManagementApplication.class, args);
    }

}
