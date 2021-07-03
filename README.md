![UnifiedMetrics](.github/assets/banner.png)

[![License](https://img.shields.io/github/license/Cubxity/UnifiedMetrics?style=flat-square)](LICENSE)
[![Issues](https://img.shields.io/github/issues/Cubxity/UnifiedMetrics?style=flat-square)](https://github.com/Cubxity/UnifiedMetrics/issues)
[![Workflow Status](https://img.shields.io/github/workflow/status/Cubxity/UnifiedMetrics/gradle-preview-ci/master?style=flat-square)](https://github.com/Cubxity/UnifiedMetrics/actions)

UnifiedMetrics is a fully-featured free and open-source metrics collection plugin for Minecraft servers.
This project is licensed under [GNU AGPLv3](LICENSE)

![Grafana Dashboard](.github/assets/grafana.png)

## Compatbility
**Server:**
- 1.8+ Spigot servers
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
We recommend using [Grafana](https://grafana.com/) as it provides highly customizable diagrams.
Grafana provides out-of-box support for Prometheus and InfluxDB.

- Add Prometheus/InfluxDB datasource to Grafana
- Import our [Prometheus dashboard](https://grafana.com/grafana/dashboards/14017) or [InfluxDB dashboard](https://grafana.com/grafana/dashboards/13860)
- Configure the dashboard and set up alerts *(optional)*

## API
Add `:unifiedmetrics-api` as a dependency (compileOnly/provided).

```kotlin
import dev.cubxity.plugins.metrics.api.UnifiedMetricsProvider

/* ... */

val api = UnifiedMetricsProvider.get()
```
