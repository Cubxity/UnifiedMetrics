/*
 *     This file is part of UnifiedMetrics.
 *
 *     UnifiedMetrics is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     UnifiedMetrics is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with UnifiedMetrics.  If not, see <https://www.gnu.org/licenses/>.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20" apply false
    kotlin("kapt") version "1.6.20" apply false
    kotlin("plugin.serialization") version "1.6.20" apply false
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
    id("net.kyori.blossom") version "1.3.0" apply false
}

allprojects {
    group = "dev.cubxity.plugins"
    description = "Fully featured metrics collector agent for Minecraft servers."
    version = "0.3.6-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "signing")
    apply(plugin = "maven-publish")

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
        }
    }
    configure<JavaPluginExtension> {
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    configure<PublishingExtension> {
        repositories {
            maven {
                name = "central"
                url = if (version.toString().endsWith("SNAPSHOT")) {
                    uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                } else {
                    uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                }
                credentials {
                    username = System.getenv("MAVEN_REPO_USER")
                    password = System.getenv("MAVEN_REPO_PASS")
                }
            }
        }
    }
    afterEvaluate {
        tasks.findByName("shadowJar")?.also {
            tasks.named("assemble") { dependsOn(it) }
        }
        configure<SigningExtension> {
            sign(configurations["archives"])
        }
    }
}