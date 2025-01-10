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

import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.gradle.vanilla.repository.MinecraftPlatform
import org.spongepowered.plugin.metadata.model.PluginDependency
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.spongepowered.gradle.plugin") version("2.0.2")
    id("org.spongepowered.gradle.vanilla") version "0.2.1-SNAPSHOT"
    id("com.github.johnrengelman.shadow")
}

minecraft {
    latestRelease()
    platform(MinecraftPlatform.SERVER)
}

val mixinConfigsAttribute: String by extra { "unifiedmetrics.mixins.json" }
tasks.withType<Jar> {
    manifest {
        attributes(
            mapOf("MixinConfigs" to mixinConfigsAttribute)
        )
    }
}

sponge {
    apiVersion("12.0.0-SNAPSHOT")
    loader {
        name(PluginLoaders.JAVA_PLAIN)
        version("1.0.0-SNAPSHOT")
    }
    plugin("unifiedmetrics") {
        displayName("UnifiedMetrics")
        entrypoint("dev.cubxity.plugins.metrics.sponge.bootstrap.UnifiedMetricsSpongeBootstrap")
        description("Fully-featured metrics plugin for Minecraft servers")
        license("GPL-3")
        dependency("spongeapi") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
    }
}

dependencies {
    api(project(":unifiedmetrics-core"))
    compileOnly("org.spongepowered:mixin:0.8.6-SNAPSHOT")
}

tasks {
    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("")
        relocate("retrofit2", "dev.cubxity.plugins.metrics.libs.retrofit2")
        relocate("com.charleskorn", "dev.cubxity.plugins.metrics.libs.com.charleskorn")
        relocate("com.influxdb", "dev.cubxity.plugins.metrics.libs.com.influxdb")
        relocate("okhttp", "dev.cubxity.plugins.metrics.libs.okhttp")
        relocate("okio", "dev.cubxity.plugins.metrics.libs.okio")
        relocate("io.prometheus", "dev.cubxity.plugins.metrics.libs.io.prometheus")
        relocate("com.google.gson", "dev.cubxity.plugins.metrics.libs.gson")
        relocate("org.apache.commons", "dev.cubxity.plugins.metrics.libs.commons")
        relocate("org.intellij.lang.annotations", "dev.cubxity.plugins.metrics.libs.intellij.annotations")
        relocate("org.jetbrains.annotations", "dev.cubxity.plugins.metrics.libs.jetbrains.annotations")
        relocate("javax.annotation", "dev.cubxity.plugins.metrics.libs.javax.annotations")
        relocate("org.reactivestreams", "dev.cubxity.plugins.metrics.libs.reactivestreams")
        relocate("net.thauvin.erik.urlencoder", "dev.cubxity.plugins.metrics.libs.urlencoder")
        relocate("it.krzeminski.snakeyaml", "dev.cubxxity.plugins.metrics.libs.snakeyaml")
        relocate("io.reactivex.rxjava3", "dev.cubxity.plugins.metrics.libs.rxjava")
        relocate("kotlin", "dev.cubxity.plugins.metrics.libs.kotlin")
        relocate("kotlinx", "dev.cubxity.plugins.metrics.libs.kotlinx")
    }

    compileKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    }

    compileJava {
        options.encoding = "UTF-8"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}