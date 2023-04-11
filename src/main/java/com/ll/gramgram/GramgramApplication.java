package com.ll.gramgram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // @EntityListeners(AuditingEntityListener.class) 가 작동하도록 허용
public class GramgramApplication {

    public static void main(String[] args) {
        //SpringApplication.run(GramgramApplication.class, args);
        SpringApplication app = new SpringApplication(GramgramApplication.class);
        app.addListeners(new ApplicationPidFileWriter()); // ApplicationPidFileWriter 설정
        app.run(args);
    }

}
