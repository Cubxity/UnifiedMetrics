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
    kotlin("kapt")
    id("com.github.johnrengelman.shadow")
    id("net.kyori.blossom")
}

repositories {
    maven("https://repo.kryptonmc.org/releases")
}

dependencies {
    api(project(":unifiedmetrics-core"))

    compileOnly("org.kryptonmc:krypton-api:0.60.2")
    kapt("org.kryptonmc:krypton-annotation-processor:0.60.2")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    shadowJar {
        archiveClassifier.set("")
    }
}

blossom {
    replaceTokenIn("src/main/kotlin/dev/cubxity/plugins/metrics/krypton/bootstrap/UnifiedMetricsKryptonBootstrap.kt")
    replaceToken("@version@", version)
}
