package com.leets.xcellentbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class XcellentBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(XcellentBeApplication.class, args);
    }

}
