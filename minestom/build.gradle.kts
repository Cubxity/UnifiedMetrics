/*
 *     UnifiedMetrics is a fully-featured metrics collection plugin for Minecraft servers.
 *     Copyright (C) 2021  Cubxity
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

plugins {
    id("com.github.johnrengelman.shadow")
}

repositories {
    maven("https://repo.spongepowered.org/maven")
    maven("https://jitpack.io")
}

dependencies {
    api(project(":unifiedmetrics-core"))

    compileOnly("com.github.Minestom:Minestom:-SNAPSHOT")
    testImplementation("com.github.Minestom:Minestom:-SNAPSHOT")
}

tasks {
    shadowJar {
        archiveClassifier.set("")

        dependencies {
            include(dependency(":unifiedmetrics-api"))
            include(dependency(":unifiedmetrics-common"))
            include(dependency(":unifiedmetrics-influx-driver"))
            include(dependency(":unifiedmetrics-prometheus-driver"))
            include(dependency(":unifiedmetrics-core"))
        }
    }
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        from("src/main/resources") {
            expand("version" to version)
            include("extension.json")
        }
    }
}
