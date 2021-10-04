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

plugins {
    id("fabric-loom") version "0.9.9"
    id("net.kyori.blossom")
}

dependencies {
    // https://fabricmc.net/versions.html
    minecraft("com.mojang:minecraft:1.17.1")
    mappings("net.fabricmc:yarn:1.17.1+build.61:v2")
    modImplementation("net.fabricmc:fabric-loader:0.11.7")

    modImplementation("net.fabricmc.fabric-api:fabric-api:0.40.1+1.17")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.6.5+kotlin.1.5.31")

    api(project(":unifiedmetrics-core"))

    include(project(":unifiedmetrics-core"))
    include(project(":unifiedmetrics-common"))
    include("com.charleskorn.kaml:kaml:0.36.0")
    include("com.charleskorn.kaml:kaml-jvm:0.36.0")
    include("org.snakeyaml:snakeyaml-engine:2.3")
    include(project(":unifiedmetrics-api"))
    include(project(":unifiedmetrics-driver-influx"))
    include("com.influxdb:influxdb-client-java:3.3.0")
    include("com.influxdb:influxdb-client-core:3.3.0")
    include("com.influxdb:influxdb-client-utils:3.3.0")
    include("com.google.code.findbugs:jsr305:3.0.2")
    include("com.squareup.retrofit2:retrofit:2.9.0")
    include("com.squareup.okhttp3:okhttp:4.7.2")
    include("com.squareup.okio:okio:2.6.0")
    include("com.squareup.okhttp3:logging-interceptor:4.7.2")
    include("org.apache.commons:commons-csv:1.8")
    include("io.reactivex.rxjava2:rxjava:2.2.19")
    include("org.reactivestreams:reactive-streams:1.0.3")
    include("io.swagger:swagger-annotations:1.6.1")
    include("io.gsonfire:gson-fire:1.8.4")
    include("com.squareup.retrofit2:converter-scalars:2.9.0")
    include("com.squareup.retrofit2:converter-gson:2.9.0")
    include(project(":unifiedmetrics-driver-prometheus"))
    include("io.prometheus:simpleclient_httpserver:0.12.0")
    include("io.prometheus:simpleclient:0.12.0")
    include("io.prometheus:simpleclient_tracer_otel:0.12.0")
    include("io.prometheus:simpleclient_tracer_common:0.12.0")
    include("io.prometheus:simpleclient_tracer_otel_agent:0.12.0")
    include("io.prometheus:simpleclient_common:0.12.0")
    include("io.prometheus:simpleclient_pushgateway:0.12.0")
}

loom {
    runs {
        named("server") {
            isIdeConfigGenerated = true
        }
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }

    compileJava {
        options.encoding = "UTF-8"
        options.release.set(8)
    }
}

blossom {
    replaceTokenIn("src/main/kotlin/dev/cubxity/plugins/metrics/fabric/bootstrap/UnifiedMetricsFabricBootstrap.kt")
    replaceToken("@version@", version)
}