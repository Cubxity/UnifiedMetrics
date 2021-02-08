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

import org.apache.tools.ant.filters.ReplaceTokens

repositories {
    maven("https://hub.spigotmc.org/nexus/content/groups/public")
}

dependencies {
    api(project(":unifiedmetrics-core"))
    compileOnly("org.spigotmc", "spigot-api", "1.8-R0.1-SNAPSHOT")
}

tasks {
    processResources {
        from("src/main/resources") {
            filter(ReplaceTokens::class, "tokens" to mapOf("version" to version))
        }
    }
}