/*
 *     UnifiedMetrics is a fully-featured metrics collection plugin for Minecraft servers.
 *     Copyright (C) 2021  Cubxity
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.cubxity.plugins.metrics.common.plugin.logger

import dev.cubxity.plugins.metrics.api.logging.Logger
import java.util.logging.Level

class JavaLogger(private val logger: java.util.logging.Logger) : Logger {
    override fun info(message: String) {
        logger.log(Level.INFO, message)
    }

    override fun warn(message: String) {
        logger.log(Level.WARNING, message)
    }

    override fun warn(message: String, error: Throwable) {
        logger.log(Level.WARNING, message, error)
    }

    override fun severe(message: String) {
        logger.log(Level.SEVERE, message)
    }

    override fun severe(message: String, error: Throwable) {
        logger.log(Level.SEVERE, message, error)
    }
}