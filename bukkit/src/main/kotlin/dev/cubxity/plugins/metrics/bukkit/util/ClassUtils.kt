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

package dev.cubxity.plugins.metrics.bukkit.util

fun classExists(className: String): Boolean = try {
    Class.forName(className)
    true
} catch (e: ClassNotFoundException) {
    false
}

fun declaredMethodExists(className: String, methodName: String, vararg parameterTypes: Class<*>): Boolean = try {
    Class.forName(className).getDeclaredMethod(methodName, *parameterTypes)
    true
} catch (e: ReflectiveOperationException) {
    false
}
