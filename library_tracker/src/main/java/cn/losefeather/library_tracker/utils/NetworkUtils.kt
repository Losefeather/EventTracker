package cn.losefeather.library_tracker.utils

import android.content.Context
import android.os.Build
import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.Collections

object NetworkUtils {

    /**
     * 获取设备 IPv4 地址（优先 WiFi 地址，无 WiFi 时返回移动网络地址）
     */
    fun getIPv4Address(context: Context): String? {
        // 遍历所有网络接口
        val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
        for (networkInterface in interfaces) {
            // 跳过回环接口
            if (networkInterface.isLoopback) continue

            // 遍历接口的 IP 地址
            val addresses = Collections.list(networkInterface.inetAddresses)
            for (address in addresses) {
                // 仅返回 IPv4 地址且非回环
                if (!address.isLoopbackAddress && address is Inet4Address) {
                    // 优先返回 WiFi 接口的地址（名称通常为 wlan0）
                    if (networkInterface.name.startsWith("wlan")) {
                        return address.hostAddress
                    }
                    // 无 WiFi 时返回其他接口（如移动网络）
                    if (address.hostAddress != "127.0.0.1") {
                        return address.hostAddress
                    }
                }
            }
        }
        return null
    }

    /**
     * 获取设备 MAC 地址（兼容 Android 7~14）
     */
    fun getMacAddress(context: Context): String? {
        // Android 6+ 无法通过 WifiManager 直接获取真实 MAC，改用 NetworkInterface
        return try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (networkInterface in interfaces) {
                // 优先获取 WiFi 接口（名称通常为 wlan0）
                if (networkInterface.name.equals("wlan0", ignoreCase = true)) {
                    val macBytes = networkInterface.hardwareAddress ?: return null
                    val builder = StringBuilder()
                    for (b in macBytes) {
                        builder.append(String.format("%02X:", b))
                    }
                    if (builder.isNotEmpty()) {
                        builder.deleteCharAt(builder.length - 1)
                    }
                    return builder.toString()
                }
            }
            // 无 WiFi 接口时尝试其他接口（如蓝牙）
            for (networkInterface in interfaces) {
                if (networkInterface.hardwareAddress != null) {
                    val macBytes = networkInterface.hardwareAddress
                    val builder = StringBuilder()
                    for (b in macBytes) {
                        builder.append(String.format("%02X:", b))
                    }
                    if (builder.isNotEmpty()) {
                        builder.deleteCharAt(builder.length - 1)
                    }
                    return builder.toString()
                }
            }
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 检查是否需要请求位置权限（Android 10+）
     */
    fun needLocationPermission(context: Context): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }
}