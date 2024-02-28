/*
 * Copyright (c) 2023, Melxin <https://github.com/melxin>
 * All rights reserved.
 *
 * This code is licensed under GPL3, see the complete license in
 * the LICENSE file in the root directory of this source tree.
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Collections.emptyList

plugins {
    id("java-gradle-plugin")
    kotlin("jvm") version "1.7.0"
    `maven-publish`
    `pmd`
}

group = "com.openosrs"
version = "1.0.1"

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://raw.githubusercontent.com/melxin/hosting/master")
    }
}

dependencies {
    annotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.20")
    compileOnly(group = "org.projectlombok", name = "lombok", version = "1.18.20")
    implementation(group = "org.tomlj", name = "tomlj", version = "1.1.0")
    implementation(group = "com.squareup", name = "javapoet", version = "1.13.0")
    implementation("net.sourceforge.pmd:pmd-core:6.44.0")
    implementation("net.sourceforge.pmd:pmd-java:6.44.0")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Pmd> {
    ruleSetFiles = files("${project.projectDir}/pmd-ruleset.xml")
    ruleSets = emptyList()
    ignoreFailures = false
    isConsoleOutput = true
    enabled = true
}

gradlePlugin {
    plugins {
        create("interfaceParserPlugin") {
            id = "com.openosrs.interfaceparser"
            implementationClass = "com.openosrs.interfaceparser.InterfaceParserPlugin"
        }
    }
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

publishing {
    repositories {
        maven {
            url = uri("$buildDir/repo")
        }
        if (System.getenv("REPO_URL") != null) {
            maven {
                url = uri(System.getenv("REPO_URL"))
                credentials {
                    username = System.getenv("REPO_USERNAME")
                    password = System.getenv("REPO_PASSWORD")
                }
            }
        }
    }
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
}
