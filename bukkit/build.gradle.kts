import org.apache.tools.ant.filters.ReplaceTokens

repositories {
    maven("https://hub.spigotmc.org/nexus/content/groups/public")
}

dependencies {
    api(project(":unifiedmetrics-common"))
    compileOnly("org.spigotmc", "spigot-api", "1.8-R0.1-SNAPSHOT")
}

tasks {
    processResources {
        from("src/main/resources") {
            filter(ReplaceTokens::class, "tokens" to mapOf("version" to version))
        }
    }
}