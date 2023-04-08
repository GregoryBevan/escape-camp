package me.elgregos.escapecamp.utils

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [BaseIntegrationTest.TestConfig::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BaseIntegrationTest {


    @TestConfiguration
    class TestConfig {

    }

}