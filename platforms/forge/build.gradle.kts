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
buildscript {
    repositories {
        maven("https://maven.minecraftforge.net")
        maven("https://maven.parchmentmc.org")
        mavenCentral()
    }
    dependencies {
        classpath(group = "net.minecraftforge.gradle", name = "ForgeGradle", version = "5.1.+") {
            isChanging = true
        }
        classpath("org.parchmentmc:librarian:1.+")
    }
}

plugins {
    id("net.kyori.blossom")
    id("com.github.johnrengelman.shadow")
}

apply {
    plugin("net.minecraftforge.gradle")
    plugin("org.parchmentmc.librarian.forgegradle")
}

apply(from = "https://raw.githubusercontent.com/thedarkcolour/KotlinForForge/site/thedarkcolour/kotlinforforge/gradle/kff-3.8.0.gradle")

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

configure<net.minecraftforge.gradle.userdev.UserDevExtension> {
    mappings("parchment", "2022.11.06-1.18.2")

    runs {
        create("server") {
            workingDirectory = project.file("run").canonicalPath
            // "SCAN": For mods scan.
            // "REGISTRIES": For firing of registry events.
            // "REGISTRYDUMP": For getting the contents of all registries.
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            mods {
                create("unifiedmetrics") {
                    source(sourceSets["main"])
                }
            }
        }
    }
}

dependencies {
    "minecraft"("net.minecraftforge:forge:1.18.2-40.2.0")
    val f = project.extensions.getByType<net.minecraftforge.gradle.userdev.DependencyManagementExtension>()
    shadow(project(":unifiedmetrics-core"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    shadowJar {
        dependsOn("reobfJar")
        archiveClassifier.set("")
        configurations =  listOf(project.configurations.shadow.get())
        relocate("retrofit2", "dev.cubxity.plugins.metrics.libs.retrofit2")
        relocate("com.charleskorn", "dev.cubxity.plugins.metrics.libs.com.charleskorn")
        relocate("com.influxdb", "dev.cubxity.plugins.metrics.libs.com.influxdb")
        relocate("okhttp", "dev.cubxity.plugins.metrics.libs.okhttp")
        relocate("okio", "dev.cubxity.plugins.metrics.libs.okio")
        relocate("io.prometheus", "dev.cubxity.plugins.metrics.libs.io.prometheus")
        exclude("com/**", "io/**", "javax/**", "kotlin/**", "kotlinx/**", "org/**")
    }

    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            expand("version" to project.version)
        }
    }

    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    jar {
        finalizedBy("reobfJar")
    }
}

blossom {
    replaceTokenIn("src/main/kotlin/dev/cubxity/plugins/metrics/forge/bootstrap/UnifiedMetricsForgeBootstrap.kt")
    replaceToken("@version@", version)
}