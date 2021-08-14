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

package dev.cubxity.plugins.metrics.prometheus.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PrometheusConfig(
    val mode: PrometheusMode = PrometheusMode.Http,
    val http: PrometheusHttpConfig = PrometheusHttpConfig(),
    val pushGateway: PushGatewayConfig = PushGatewayConfig()
)

@Serializable
enum class PrometheusMode {
    @SerialName("HTTP")
    Http,

    @SerialName("PUSHGATEWAY")
    PushGateway
}

@Serializable
data class PrometheusHttpConfig(
    val host: String = "0.0.0.0",
    val port: Int = 9100
)

@Serializable
data class PushGatewayConfig(
    val job: String = "unifiedmetrics",
    val url: String = "http://pushgateway:9091",
    val authentication: PushGatewayAuthenticationConfig = PushGatewayAuthenticationConfig(),
    val interval: Long = 10
)

@Serializable
data class PushGatewayAuthenticationConfig(
    val scheme: PushGatewayAuthenticationScheme = PushGatewayAuthenticationScheme.Basic,
    val username: String = "username",
    val password: String = "password"
)

@Serializable
enum class PushGatewayAuthenticationScheme {
    @SerialName("NONE")
    None,

    @SerialName("BASIC")
    Basic
}