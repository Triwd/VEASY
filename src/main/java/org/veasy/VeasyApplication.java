package org.veasy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.veasy")
public class VeasyApplication {

    public static void main(String[] args) {
        SpringApplication.run(VeasyApplication.class, args);
    }

}
