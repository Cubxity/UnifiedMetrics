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

dependencies {
    api(kotlin("stdlib"))
    api("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.5.2")
    api("org.jetbrains.kotlinx", "kotlinx-serialization-core", "1.2.2")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }

            pom {
                name.set("UnifiedMetrics")
                description.set("UnifiedMetrics is a fully-featured free and open-source metrics collection plugin for Minecraft servers.")
                url.set("https://github.com/Cubxity/UnifiedMetrics/")

                licenses {
                    license {
                        name.set("GNU Lesser General Public License v3.0")
                        url.set("https://github.com/Cubxity/UnifiedMetrics/blob/dev/0.3.x/COPYING.LESSER")
                    }
                }
                developers {
                    developer {
                        id.set("cubxity")
                        name.set("Cubxity")
                        email.set("contact@cubxity.dev")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/Cubxity/UnifiedMetrics.git")
                    developerConnection.set("scm:git:ssh://github.com/Cubxity/UnifiedMetrics.git")
                    url.set("https://github.com/Cubxity/UnifiedMetrics/")
                }
            }
        }
    }
}