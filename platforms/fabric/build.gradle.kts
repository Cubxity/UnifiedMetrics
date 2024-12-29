import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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
    id("fabric-loom")
}

val transitiveInclude: Configuration by configurations.creating {
    exclude(group = "com.mojang")
    exclude(group = "org.jetbrains.kotlin")
    exclude(group = "org.jetbrains.kotlinx")
}

dependencies {
    // https://fabricmc.net/versions.html
    minecraft("com.mojang:minecraft:1.21.4")
    mappings("net.fabricmc:yarn:1.21.4+build.4")
    modImplementation("net.fabricmc:fabric-loader:0.16.9")

    modImplementation("net.fabricmc.fabric-api:fabric-api:0.113.0+1.21.4")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.11.0+kotlin.2.0.0")

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
    serverOnlyMinecraftJar()
}

tasks {
    compileKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    }
    processResources {
        filesMatching("fabric.mod.json") {
            expand(
                "version" to project.version
            )
        }
    }
    compileJava {
        options.encoding = "UTF-8"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
