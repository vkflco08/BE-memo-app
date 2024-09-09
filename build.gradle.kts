plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
	kotlin("plugin.jpa") version "1.9.25"
}

group = "com.be-memo-app"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
//	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//	implementation("org.springframework.boot:spring-boot-starter-security")
//	implementation("org.jetbrains.kotlin:kotlin-reflect")
//	runtimeOnly("com.mysql:mysql-connector-j")
//	testImplementation("org.springframework.boot:spring-boot-starter-test")
//	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
//	testImplementation("org.springframework.security:spring-security-test")
//	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
//
//	// lombok
//	compileOnly("org.projectlombok:lombok:1.18.28")
//	annotationProcessor("org.projectlombok:lombok:1.18.28")
//
//	// jwt
//	implementation("org.springframework.boot:spring-boot-starter-validation")
//	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
//	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
//	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	compileOnly("org.projectlombok:lombok")
	implementation("com.mysql:mysql-connector-j")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// jwt
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")


}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
