import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
	id("pl.allegro.tech.build.axion-release") version "1.15.0"
	id("com.gorylenko.gradle-git-properties") version "2.4.1"
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
	implementation("me.elgregos:events-k:1.0.2")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.liquibase:liquibase-core")
	implementation("org.springframework:spring-jdbc")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("org.postgresql:r2dbc-postgresql")
	testImplementation("org.jetbrains.kotlin:kotlin-test")
	testImplementation("org.junit.jupiter:junit-jupiter-params")
	"integrationTestImplementation"(project)
	"integrationTestImplementation"("org.springframework.boot:spring-boot-starter-test")
	"integrationTestImplementation"("org.junit.platform:junit-platform-suite")
	"integrationTestImplementation"("io.projectreactor:reactor-test")
	"integrationTestImplementation"("org.springframework.security:spring-security-test")
	"integrationTestImplementation"("org.testcontainers:junit-jupiter")
	"integrationTestImplementation"("org.testcontainers:postgresql")
	"integrationTestImplementation"("org.testcontainers:r2dbc")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
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
