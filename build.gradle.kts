plugins {
    `maven-publish`
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    signing
    `java-library`
}

description = "Spring Boot AutoConfigure Support for the Structurizr Client"
group = "io.cloudflight.structurizr"

autoConfigure {
    java {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendorName.set("Cloudflight")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform(libs.spring.boot.bom))
    annotationProcessor(platform(libs.spring.boot.bom))

    api(libs.structurizr.core)
    api(libs.structurizr.export)
    implementation(libs.structurizr.client)

    // let that be an API dependency in order publish @SpringBootApplication
    api("org.springframework.boot:spring-boot-autoconfigure")
    runtimeOnly("org.springframework.boot:spring-boot-starter")

    compileOnly("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-test")

    testImplementation(platform(libs.junit.jupiter.bom))
    testImplementation(libs.architectureicons)
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set(project.name)
                description.set(project.description)
                url.set("https://github.com/cloudflightio/structurizr-autoconfigure")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                inceptionYear.set("2022")
                organization {
                    name.set("Cloudflight")
                    url.set("https://cloudflight.io")
                }
                developers {
                    developer {
                        id.set("cloudflightio")
                        name.set("Cloudflight Team")
                        email.set("opensource@cloudflight.io")
                    }
                }
                scm {
                    connection.set("scm:ggit@github.com:cloudflightio/structurizr-autoconfigure.git")
                    developerConnection.set("scm:git@github.com:cloudflightio/structurizr-autoconfigure.git")
                    url.set("https://github.com/cloudflightio/structurizr-autoconfigure")
                }
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {  //only for users registered in Sonatype after 24 Feb 2021
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(System.getenv("MAVEN_USERNAME"))
            password.set(System.getenv("MAVEN_PASSWORD"))
        }
    }
}

signing {
    setRequired {
        System.getenv("PGP_SECRET") != null
    }
    useInMemoryPgpKeys(System.getenv("PGP_SECRET"), System.getenv("PGP_PASSPHRASE"))
    sign(publishing.publications.getByName("maven"))
}
