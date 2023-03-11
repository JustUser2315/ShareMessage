package com.project.share_message.spring;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
@SpringBootTest
@Sql({"classpath:data.sql"})
@ActiveProfiles(profiles = {"test"})
@Transactional
public abstract class TestBaseApplication {
   private static final  PostgreSQLContainer<?>container = new PostgreSQLContainer<>("postgres:14.4");
   @BeforeAll
    static void runContainer(){
        container.start();
    }
    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);

    }
}
