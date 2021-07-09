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

rootProject.name = "UnifiedMetrics"

val modulePrefix = ":unifiedmetrics-"

include(modulePrefix + "api")
include(modulePrefix + "common")
include(modulePrefix + "core")
include(modulePrefix + "bukkit")
include(modulePrefix + "velocity")
include(modulePrefix + "bungee")
include(modulePrefix + "minestom")

include(modulePrefix + "influx-driver")
include(modulePrefix + "prometheus-driver")

project(modulePrefix + "api").projectDir = File(rootDir, "api")
project(modulePrefix + "common").projectDir = File(rootDir, "common")
project(modulePrefix + "core").projectDir = File(rootDir, "core")
project(modulePrefix + "bukkit").projectDir = File(rootDir, "bukkit")
project(modulePrefix + "velocity").projectDir = File(rootDir, "velocity")
project(modulePrefix + "bungee").projectDir = File(rootDir, "bungee")
project(modulePrefix + "minestom").projectDir = File(rootDir, "minestom")

val driversDir = File(rootDir, "drivers")
project(modulePrefix + "influx-driver").projectDir = File(driversDir, "influx")
project(modulePrefix + "prometheus-driver").projectDir = File(driversDir, "prometheus")