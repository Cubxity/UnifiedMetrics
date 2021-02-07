![UnifiedMetrics](.github/assets/banner.png)

[![License](https://img.shields.io/github/license/Cubxity/UnifiedMetrics?style=flat-square)](LICENSE)
[![Issues](https://img.shields.io/github/issues/Cubxity/UnifiedMetrics?style=flat-square)](https://github.com/Cubxity/kdp/issues)
[![Workflow Status](https://img.shields.io/github/workflow/status/Cubxity/UnifiedMetrics/gradle-preview-ci/master?style=flat-square)](https://github.com/Cubxity/kdp/actions)

UnifiedMetrics is a fully-featured free and open-source metrics collection plugin for Minecraft servers.
This project is licensed under [GNU AGPLv3](LICENSE)

![Grafana Dashboard](.github/assets/grafana.png)

## Compatbility
- 1.8+ Spigot servers
- Velocity (coming soon)

## Features
- Server metrics collection (TPS, Players, Plugins)
- World metrics collection (Entities, Chunks)
- Events metrics collection (Player flow, chat)
- JVM metrics collection (Memory, CPU Load, Threads, Uptime)
- Extensible API (Custom metrics, measurements, metrics driver) 
- More features upcoming (Logging to ElasticSearch, etc)

## API
Coming soon

## Installation

- Add the plugin to your `plugins` folder.
- Configure the plugin. (See [Configuration](#Configuration)).
- Restart the server. **(NOTE: Reload is not supported and may cause issues)**

## Configuration

Currently, [InfluxDB](https://www.influxdata.com/) is required to collect metrics.
We recommend using an internal network for InfluxDB.

```toml
[server]
server = "main"

[metrics]
enabled = true
driver = "influx"

[metrics.influx]
url = "http://influxdb:8086"
bucket = "unifiedmetrics"
influx = "influx"
password = "influx"
interval = 10 # Interval in seconds
```

## Data visualization and analysis
We recommend using [Grafana](https://grafana.com/) as it provides highly customizable diagrams.
Grafana provides out-of-box support for InfluxDB. A guide is coming soon.