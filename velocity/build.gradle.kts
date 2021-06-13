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
    kotlin("kapt")
    id("com.github.johnrengelman.shadow")
    id("net.kyori.blossom")
}

repositories {
    maven("https://nexus.velocitypowered.com/repository/maven-public/")
}

dependencies {
    api(project(":unifiedmetrics-core"))

    compileOnly("com.velocitypowered:velocity-api:1.1.3")
    kapt("com.velocitypowered:velocity-api:1.1.3")
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        relocate("com.moandjiezana.toml", "dev.cubxity.plugins.metrics.lib.toml")
    }
}

blossom {
    replaceTokenIn("src/main/kotlin/dev/cubxity/plugins/metrics/velocity/bootstrap/UnifiedMetricsVelocityBootstrap.kt")
    replaceToken("@version@", version)
}
