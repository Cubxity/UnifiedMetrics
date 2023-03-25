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
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("net.kyori.blossom")
    id("com.github.johnrengelman.shadow")
    id("dev.architectury.loom") version "1.1-SNAPSHOT"
}

//apply(from = "https://raw.githubusercontent.com/thedarkcolour/KotlinForForge/site/thedarkcolour/kotlinforforge/gradle/kff-3.8.0.gradle")

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}



loom {
    silentMojangMappingsLicense()

    forge {
        mixinConfigs("unifiedmetrics.mixins.json")
    }
}

repositories {
    maven {
        name = "ParchmentMC"
        url = uri("https://maven.parchmentmc.org")
    }

    maven {
        name = "Kotlin for Forge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
    }
}


dependencies {
    minecraft("com.mojang:minecraft:1.18.2")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-1.18.2:2022.11.06@zip")
    })

    forge("net.minecraftforge:forge:1.18.2-40.2.0")

    implementation("thedarkcolour:kotlinforforge:3.8.0")

    shadow(project(":unifiedmetrics-core"))

    forgeRuntimeLibrary(project(":unifiedmetrics-core"))
    forgeRuntimeLibrary("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0")
    forgeRuntimeLibrary("org.jetbrains.kotlin:kotlin-reflect:1.8.0")
    forgeRuntimeLibrary("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    forgeRuntimeLibrary("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.5.2")
    forgeRuntimeLibrary("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    remapJar {
        archiveClassifier.set("remap")
        dependsOn(shadowJar)
        mustRunAfter(shadowJar)
        inputFile.set(shadowJar.get().archiveFile.get())
    }

    shadowJar {
        archiveClassifier.set("all")
        configurations = listOf(project.configurations.shadow.get())
        relocate("retrofit2", "dev.cubxity.plugins.metrics.libs.retrofit2")
        relocate("com.charleskorn", "dev.cubxity.plugins.metrics.libs.com.charleskorn")
        relocate("com.influxdb", "dev.cubxity.plugins.metrics.libs.com.influxdb")
        relocate("okhttp", "dev.cubxity.plugins.metrics.libs.okhttp")
        relocate("okio", "dev.cubxity.plugins.metrics.libs.okio")
        relocate("io.prometheus", "dev.cubxity.plugins.metrics.libs.io.prometheus")
        relocate("org.yaml.snakeyaml", "dev.cubxity.plugins.metrics.libs.org.yaml.snakeyaml")
        relocate("org.snakeyaml", "dev.cubxity.plugins.metrics.libs.org.snakeyaml")
        relocate("com.google.gson", "dev.cubxity.plugins.metrics.libs.org.yaml.snakeyaml")
        relocate("io.reactivex", "dev.cubxity.plugins.metrics.libs.io.reactivex")
        relocate("org.apache.common", "dev.cubxity.plugins.metrics.libs.org.apache.common")
        relocate("org.reactivestreams", "dev.cubxity.plugins.metrics.libs.org.reactivestreams")
        exclude("javax/**", "kotlin/**", "kotlinx/**", "org/jetbrains/**", "org/intellij/**")
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
}

blossom {
    replaceTokenIn("src/main/kotlin/dev/cubxity/plugins/metrics/forge/bootstrap/UnifiedMetricsForgeBootstrap.kt")
    replaceToken("@version@", version)
}
