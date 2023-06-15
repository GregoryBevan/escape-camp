package me.elgregos.escapecamp.features

import io.cucumber.spring.CucumberContextConfiguration
import me.elgregos.escapecamp.game.api.GameClient
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.web.reactive.server.WebTestClient


@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureWebTestClient(timeout = "360000")
class SpringContextConfiguration {

    @TestConfiguration
    class TestConfig {

        @Bean
        fun gameClient(webTestClient: WebTestClient) = GameClient(webTestClient)
//
//        @Bean
//        fun dbInitializer(): ResourceDatabasePopulator = ResourceDatabasePopulator(ClassPathResource("data.sql"))
//
//        @Bean
//        fun dbCleaner(): ResourceDatabasePopulator = ResourceDatabasePopulator(ClassPathResource("cleaner.sql"))


    }
}