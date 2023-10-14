import com.github.gradle.node.npm.task.NpmTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.spring") version "1.9.10"
    id("pl.allegro.tech.build.axion-release") version "1.15.0"
    id("com.gorylenko.gradle-git-properties") version "2.4.1"
    id("com.github.node-gradle.node") version "5.0.0"
}

group = "me.elgregos"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

sourceSets {
    create("integrationTest") {
        compileClasspath += sourceSets.test.get().output
        runtimeClasspath += sourceSets.test.get().output
    }
}

configurations["integrationTestRuntimeOnly"].extendsFrom(configurations.testRuntimeOnly.get())
configurations["integrationTestImplementation"].extendsFrom(configurations.testImplementation.get())

extra["testcontainersVersion"] = "1.17.6"
ext["junit-jupiter.version"] = "5.9.2"

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
}

dependencies {
    implementation("me.elgregos:reakt-eves:1.2.1")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.liquibase:liquibase-core")
    implementation("org.springframework:spring-jdbc")
    implementation("io.jsonwebtoken:jjwt-api:0.12.2")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.2")
    implementation("org.postgresql:r2dbc-postgresql")
    implementation("com.github.java-json-tools:json-patch:1.13")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.27.0")
    "integrationTestImplementation"(project)
    "integrationTestImplementation"("org.springframework.boot:spring-boot-starter-test")
    "integrationTestImplementation"("org.junit.platform:junit-platform-suite")
    "integrationTestImplementation"("io.projectreactor:reactor-test")
    "integrationTestImplementation"("org.springframework.security:spring-security-test")
    "integrationTestImplementation"("org.testcontainers:junit-jupiter")
    "integrationTestImplementation"("org.testcontainers:postgresql")
    "integrationTestImplementation"("org.testcontainers:r2dbc")
    "integrationTestImplementation"("io.cucumber:cucumber-java8:7.14.0")
    "integrationTestImplementation"("io.cucumber:cucumber-junit-platform-engine:7.14.0")
    "integrationTestImplementation"("io.cucumber:cucumber-spring:7.14.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

val integrationTest = tasks.register<Test>("integrationTest") {
    description = "Runs the integration tests."
    group = "verification"
    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
    shouldRunAfter("test")
}

tasks.check { dependsOn(integrationTest) }

springBoot {
    mainClass.set("me.elgregos.escapecamp.EscapeCampApplicationKt")
    buildInfo()
}

gitProperties {
    keys = listOf("git.branch", "git.commit.id", "git.commit.time", "git.commit.message.short", "git.branch")
    dateFormat = "yyyy-MM-dd HH:mm:ss"
}

scmVersion {
    repository {
        type.set("git") // type of repository
        directory.set(project.rootProject.file("../"))
    }
}

node {
    setVersion("18.15.0")
    nodeProjectDir.set(File("${projectDir.parent}/frontend"))
    nodeProjectDir
}

val buildTaskUsingNpm = tasks.register<NpmTask>("buildNpm") {
    dependsOn(tasks.npmInstall)
    workingDir.set(File("${projectDir.parent}/frontend"))
    npmCommand.set(listOf("run", "build"))
    args.set(listOf("--", "--out-dir", "${projectDir.parent}/frontend/dist"))
    inputs.dir("src")
}

val copyFrontendAssetsTask = tasks.register<Copy>("copyFrontendAssets") {
    dependsOn(buildTaskUsingNpm)
    from("${projectDir.parent}/frontend/dist")
    into("$projectDir/src/main/resources/static")
}

val copyServiceWorkerAssetTask = tasks.register<Copy>("copyServiceWorkerAssetTask") {
    dependsOn(copyFrontendAssetsTask)
    from("${projectDir.parent}/frontend/sw.js")
    into("$projectDir/src/main/resources/static")
}

tasks.processResources { dependsOn(copyServiceWorkerAssetTask) }
