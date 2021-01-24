# UnifiedMetrics (W.I.P)

UnifiedMetrics is a fully-featured free and open-source metrics collection plugin for Minecraft servers.
This project is licensed under [GNU AGPLv3](LICENSE)

## Compatbility
- 1.8+ Spigot servers
- Velocity (coming soon)

## Features
- Server metrics collection (TPS, Players, Plugins)
- World metrics collection (Entities, Chunks)
- JVM metrics collection (Memory, CPU Load, Threads, Uptime)
- More features upcoming (Logging to ElasticSearch, Player flow, etc)

## API
Coming soon

## Installation

- Add the plugin to your `plugins` folder.
- Configure the plugin. (See [Configuration](#Configuration)).
- Restart the server. **(NOTE: Reload is not supported and may cause issues)**

## Configuration

[InfluxDB](https://www.influxdata.com/) is required to collect metrics.
We recommend using an internal network for InfluxDB.

```toml
[server]
server = "main"

[metrics]
enabled = true

[metrics.influx]
url = "http://influxdb:8086"
bucket = "unifiedmetrics"
influx = "influx"
password = "influx"
interval = 10 # Interval in seconds
```

## Data visualization and analysis
We recommend using [Grafana](https://grafana.com/) as it provides highly customizable diagrams.
Grafana provides out-of-box support for InfluxDB.