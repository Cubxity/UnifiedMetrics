![UnifiedMetrics](.github/assets/banner.png)

[![License](https://img.shields.io/github/license/Cubxity/UnifiedMetrics?style=flat-square)](COPYING.LESSER)
[![Issues](https://img.shields.io/github/issues/Cubxity/UnifiedMetrics?style=flat-square)](https://github.com/Cubxity/UnifiedMetrics/issues)
[![Workflow Status](https://img.shields.io/github/workflow/status/Cubxity/UnifiedMetrics/gradle-preview-ci/master?style=flat-square)](https://github.com/Cubxity/UnifiedMetrics/actions)
[![Discord](https://img.shields.io/badge/join-discord-blue?style=flat-square)](https://discord.gg/kDDhqJmPpA)

UnifiedMetrics is a fully-featured free and open-source metrics collection plugin for Minecraft servers. This project is
licensed under [GNU LGPLv3](COPYING.LESSER).

![Grafana Dashboard](.github/assets/grafana.png)
<p align="center">
    <a href="https://snapshot.raintank.io/dashboard/snapshot/i0Btqtz01e4DkmW3N89y61wc5tIMJZKm">
        Click here for preview!
    </a>
</p>

## Compatibility

**Server:**

- 1.8+ Spigot servers
- Minestom (Beta)
- Velocity
- BungeeCord

**Metrics:**

- Prometheus
- InfluxDB

## Features

- Server metrics collection (TPS, MSPT, Players, Plugins)
- World metrics collection (Entities, Chunks)
- Events metrics collection (Player flow, chat, pings)
- JVM metrics collection (Memory, CPU Load, Threads, GC, Uptime)
- Extensible API (Custom metrics, samples, metrics driver)
- More features upcoming...

## Getting started

Read the [wiki](https://github.com/Cubxity/UnifiedMetrics/wiki) for instructions.

## Data visualization and analysis

We recommend using [Grafana](https://grafana.com/) as it provides highly customizable diagrams. Grafana provides
out-of-box support for Prometheus and InfluxDB.

See the [wiki](https://github.com/Cubxity/UnifiedMetrics/wiki/Grafana) for detailed instructions.

> **Note:** InfluxDB Grafana dashboard may be inaccurate due to complications with Flux queries.

### UnifiedMetrics 0.3.x (stable)

- [InfluxDB (Flux)](https://grafana.com/grafana/dashboards/14755)
- [Prometheus](https://grafana.com/grafana/dashboards/14756)

### UnifiedMetrics 0.2.x (legacy)

- [InfluxDB (InfluxQL)](https://grafana.com/grafana/dashboards/13860)
- [Prometheus](https://grafana.com/grafana/dashboards/14017)

## Building from source

**Requirements:**

- JDK 11+
- Git (Optional)

To build UnifiedMetrics, you need to obtain the source code first. You can download the source from GitHub or use the
Git CLI.

```bash
$ git clone https://github.com/Cubxity/UnifiedMetrics && cd UnifiedMetrics
```

Open a terminal in the cloned directory and run the following command. The following command will build all subprojects.

```bash
$ ./gradlew shadowJar
```

To build a specific subproject, you can prefix it with the subproject path. For example:

```bash
$ `./gradlew :unifiedmetrics-platform-bukkit:shadowJar`
```

The output artifacts can be found in `subproject/build/libs`.

## API

Add `:unifiedmetrics-api` as a dependency (compileOnly/provided).

```kotlin
import dev.cubxity.plugins.metrics.api.UnifiedMetricsProvider

/* ... */

val api = UnifiedMetricsProvider.get()
```

## Servers using UnifiedMetrics

<p float="left">
  <a href="https://craftadia.com">
    <img
      src="https://craftadia.com/content/images/2021/01/Webp.net-resizeimage--1-.png"
      alt="Craftadia"
      width="200"
    />
  </a>
  <a href="https://octanemc.net">
    <img
      src="https://octanemc.net/assets/images/logo-large.png"
      alt="OctaneMC"
      width="200"
    />
  </a>
</p>

> **Pssst!** You can add your server here by submitting a *Pull Request*!
