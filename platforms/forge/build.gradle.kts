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
    id("dev.architectury.loom") version "0.12.0-SNAPSHOT"
    id("net.kyori.blossom")
}

val transitiveInclude: Configuration by configurations.creating {
    exclude(group = "com.mojang")
    exclude(group = "org.jetbrains.kotlin")
    exclude(group = "org.jetbrains.kotlinx")
}

repositories {
    // Add KFF Maven repository
    maven {
        name = "Kotlin for Forge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:1.18.2")
    mappings("net.fabricmc:yarn:1.18.2+build.4:v2")
    forge("net.minecraftforge:forge:1.18.2-40.1.73")
    modApi("thedarkcolour:kotlinforforge:3.8.0")

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
    // since loom 0.10, you are **required** to use the
    // "forge" block to configure forge-specific features,
    // such as the mixinConfigs array or datagen
    forge {
        // specify the mixin configs used in this mod
        // this will be added to the jar manifest as well!
        mixinConfigs(
            "unifiedmetrics.mixins.json"
        )

        // missing access transformers?
        // don't worry, you can still use them!
        // note that your AT *MUST* be located at
        // src/main/resources/META-INF/accesstransformer.cfg
        // to work as there is currently no config option to change this.
        // also, any names used in your access transformer will need to be
        // in SRG mapped ("func_" / "field_" with MCP class names) to work!
        // (both of these things may be subject to change in the future)

        // this will create a data generator configuration
        // that you can use to automatically generate assets and data
        // using architectury loom. Note that this currently *only* works
        // for forge projects made with architectury loom!
        dataGen {
            mod("unifiedmetrics")
        }
    }

    // This allows you to modify your launch configurations,
    // for example to add custom arguments. In this case, we want
    // the data generator to check our resources directory for
    // existing files. (see Forge's ExistingFileHelper for more info)
    launches {
        named("data") {
            arg("--existing", file("src/main/resources").absolutePath)
        }
    }

    runs {
        named("server") {
            isIdeConfigGenerated = true
        }
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            expand("version" to project.version)
        }
    }

    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

}

blossom {
    replaceTokenIn("src/main/kotlin/dev/cubxity/plugins/metrics/forge/bootstrap/UnifiedMetricsForgeBootstrap.kt")
    replaceToken("@version@", version)
}