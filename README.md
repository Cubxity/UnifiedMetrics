![UnifiedMetrics](.github/assets/banner.png)

[![License](https://img.shields.io/github/license/Cubxity/UnifiedMetrics?style=flat-square)](LICENSE)
[![Issues](https://img.shields.io/github/issues/Cubxity/UnifiedMetrics?style=flat-square)](https://github.com/Cubxity/UnifiedMetrics/issues)
[![Workflow Status](https://img.shields.io/github/workflow/status/Cubxity/UnifiedMetrics/gradle-preview-ci/master?style=flat-square)](https://github.com/Cubxity/UnifiedMetrics/actions)

UnifiedMetrics is a fully-featured free and open-source metrics collection plugin for Minecraft servers.
This project is licensed under [GNU AGPLv3](LICENSE)

![Grafana Dashboard](.github/assets/grafana.png)

## Compatbility
- 1.8+ Spigot servers
- Velocity

## Features
- Server metrics collection (TPS, MSPT, Players, Plugins)
- World metrics collection (Entities, Chunks)
- Events metrics collection (Player flow, chat, pings)
- JVM metrics collection (Memory, CPU Load, Threads, Uptime)
- Extensible API (Custom metrics, measurements, metrics driver) 
- More features upcoming (Logging to ElasticSearch, etc)

## API
Add `:unifiedmetrics-api` as a dependency (compileOnly/provided).

```kotlin
import dev.cubxity.plugins.metrics.api.UnifiedMetricsProvider

/* ... */

val api = UnifiedMetricsProvider.get()
```

## Installation

- Add the plugin to your `plugins` folder.
- Configure the plugin. (See [Configuration](#Configuration)).
- Restart the server. **(NOTE: Reload is not supported and may cause issues)**

## Configuration

Currently, [InfluxDB](https://www.influxdata.com/) is required to collect metrics.
We recommend using an internal network for InfluxDB.

**config.toml**
```toml
[server]
server = "main"

[metrics]
enabled = true
driver = "influx"
```

**driver/influx.toml**
```toml
[influx]
url = "http://influxdb:8086"
bucket = "unifiedmetrics"
influx = "influx"
password = "influx"
interval = 10 # Interval in seconds
```

## Data visualization and analysis
We recommend using [Grafana](https://grafana.com/) as it provides highly customizable diagrams.
Grafana provides out-of-box support for InfluxDB.

- Add InfluxDB datasource to Grafana
- Import our [dashboard](https://grafana.com/grafana/dashboards/13860)
- Configure the dashboard and set up alerts *(optional)*