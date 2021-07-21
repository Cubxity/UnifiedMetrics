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
    id("com.github.johnrengelman.shadow")
}

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    api(project(":unifiedmetrics-core"))
    compileOnly("io.papermc.paper", "paper-api", "1.17.1-R0.1-SNAPSHOT")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "16"
    }
    shadowJar {
        archiveClassifier.set("")
    }
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        from("src/main/resources") {
            expand("version" to version)
            include("plugin.yml")
        }
    }
}
