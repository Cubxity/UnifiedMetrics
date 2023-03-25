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
    id("fabric-loom") version "1.1.10"
    id("net.kyori.blossom")
}

val transitiveInclude: Configuration by configurations.creating {
    exclude(group = "com.mojang")
    exclude(group = "org.jetbrains.kotlin")
    exclude(group = "org.jetbrains.kotlinx")
}

dependencies {
    // https://fabricmc.net/versions.html
    minecraft("com.mojang:minecraft:1.17.1")
    mappings("net.fabricmc:yarn:1.17.1+build.65:v2")
    modImplementation("net.fabricmc:fabric-loader:0.14.18")

    modImplementation("net.fabricmc.fabric-api:fabric-api:0.46.1+1.17")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.9.2+kotlin.1.8.10")

    api(project(":unifiedmetrics-core"))

    transitiveInclude(project(":unifiedmetrics-core"))

    transitiveInclude.incoming.artifacts.forEach {
        val dependency: Any = when (val component = it.id.componentIdentifier) {
            is ProjectComponentIdentifier -> project(component.projectPath)
            else -> component.toString()
        }

        include(dependency)
    }
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
    }
}

blossom {
    replaceTokenIn("src/main/kotlin/dev/cubxity/plugins/metrics/fabric/bootstrap/UnifiedMetricsFabricBootstrap.kt")
    replaceToken("@version@", version)
}