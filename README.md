# UnifiedMetrics (W.I.P)

UnifiedMetrics is a fully-featured free and open-source metrics collection plugin for Spigot-based Minecraft servers.
This project is licensed under [GNU AGPLv3](LICENSE)

## Features
- Supports 1.8+ Spigot-based Minecraft servers
- Server metrics collection (TPS, Players, Plugins)
- World metrics collection (Entities, Chunks)
- JVM metrics collection (Memory, CPU Load, Threads, Uptime)
- More features upcoming (Logging to ElasticSearch, Player flow, etc)

## Installation

- Add the plugin to your `plugins` folder.
- Configure the plugin. (See [Configuration](#Configuration)).
- Restart the server. **(NOTE: Reload is not supported and may cause issues)**

## Configuration

[InfluxDB](https://www.influxdata.com/) is required to collect metrics.
We recommend using an internal network for InfluxDB.

```yaml
influx:
  enabled: true
  url: "http://influxdb:8086" # InfluxDB's URL
  bucket: "unifiedmetrics" # The Database/Bucket to use
  server: "main" # The server's name, this is useful for multiple servers
  username: "influx"
  password: "influx"
  interval: 10 # Collection interval in seconds
```

## Data visualization and analysis
We recommend using [Grafana](https://grafana.com/) as it provides highly customizable diagrams.
Grafana provides out-of-box support for InfluxDB.