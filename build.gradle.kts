import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	id("org.springframework.boot") version "2.2.1.RELEASE"
	id("io.spring.dependency-management") version "1.0.8.RELEASE"
	id("com.google.cloud.tools.jib") version "1.8.0"
	kotlin("jvm") version "1.3.50"
	kotlin("plugin.spring") version "1.3.50"
	kotlin("plugin.jpa") version "1.3.61"
//	kotlin("plugin.no-arg") version "1.3.61"
}

group = "co.cobli"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

val developmentOnly by configurations.creating
configurations {
	runtimeClasspath {
		extendsFrom(developmentOnly)
	}
}

repositories {
	mavenCentral()
	jcenter()
	maven { url = uri("http://oss.jfrog.org/artifactory/oss-snapshot-local/") }

	maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")

	implementation( "org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("com.h2database:h2")
	implementation("org.flywaydb:flyway-core")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	implementation("io.springfox:springfox-swagger2:3.0.0-SNAPSHOT")
	implementation("io.springfox:springfox-spring-webflux:3.0.0-SNAPSHOT")
	implementation("io.springfox:springfox-swagger-ui:3.0.0-SNAPSHOT")

	implementation("org.hibernate:hibernate-envers")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("org.springframework.boot.experimental:spring-boot-test-autoconfigure-r2dbc")
	testImplementation("io.projectreactor:reactor-test")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.boot.experimental:spring-boot-bom-r2dbc:0.1.0.M2")
	}
}

//tasks.withType<Test> {
//	useJUnitPlatform()
//}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
tasks.getByName<BootJar>("bootJar") {
	this.archiveFileName.set("${archiveBaseName.get()}.${archiveExtension.get()}")
}
