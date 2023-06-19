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

rootProject.name = "UnifiedMetrics"

val modulePrefix = ":unifiedmetrics-"
val platformPrefix = "platform-"
val driverPrefix = "driver-"

include(modulePrefix + "api")
include(modulePrefix + "common")
include(modulePrefix + "core")

include(modulePrefix + platformPrefix + "bukkit")
include(modulePrefix + platformPrefix + "minestom")
include(modulePrefix + platformPrefix + "velocity")
include(modulePrefix + platformPrefix + "bungee")
include(modulePrefix + platformPrefix + "fabric")

include(modulePrefix + driverPrefix + "influx")
include(modulePrefix + driverPrefix + "prometheus")
include(modulePrefix + driverPrefix + "datadog")

project(modulePrefix + "api").projectDir = File(rootDir, "api")
project(modulePrefix + "common").projectDir = File(rootDir, "common")
project(modulePrefix + "core").projectDir = File(rootDir, "core")

val platformsDir = File(rootDir, "platforms")
project(modulePrefix + platformPrefix + "bukkit").projectDir = File(platformsDir, "bukkit")
project(modulePrefix + platformPrefix + "minestom").projectDir = File(platformsDir, "minestom")
project(modulePrefix + platformPrefix + "velocity").projectDir = File(platformsDir, "velocity")
project(modulePrefix + platformPrefix + "bungee").projectDir = File(platformsDir, "bungee")
project(modulePrefix + platformPrefix + "fabric").projectDir = File(platformsDir, "fabric")

val driversDir = File(rootDir, "drivers")
project(modulePrefix + driverPrefix + "influx").projectDir = File(driversDir, "influx")
project(modulePrefix + driverPrefix + "prometheus").projectDir = File(driversDir, "prometheus")
project(modulePrefix + driverPrefix + "datadog").projectDir = File(driversDir, "datadog")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
    }
}
