package cn.losefeather.library_tracker.entity

import android.content.Context
import android.net.wifi.WifiManager
import java.net.InetAddress
import java.nio.ByteBuffer

data class NetworkInfo(private val ip: String, val mac: String) {

    fun getIp(context: Context): String {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val ipAddress = InetAddress.getByAddress(
            ByteBuffer.allocate(4).putInt(wifiInfo.ipAddress).array()
        ).hostAddress
        return ipAddress ?: ""
    }
}
