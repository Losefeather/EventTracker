package cn.losefeather.library_tracker.entity

import android.content.res.Resources
import android.os.Build

data class DeviceInfo(
    private val device: String = Build.DEVICE,
    private val id: String = Build.ID,
    private val hardware: String = Build.HARDWARE,
    private val board: String = Build.BOARD,
    private val manufacturer: String = Build.MANUFACTURER,
    private val cpuArchitecture: String = Build.SUPPORTED_ABIS.first(),
    private val serial: String = Build.SERIAL,
    private val osType: String = "Android",
    private val osVersion: String = Build.VERSION.RELEASE,
    private val sdkVersion: Int = Build.VERSION.SDK_INT,
    private val screenWidth: Int = Resources.getSystem().displayMetrics.widthPixels,
    private val screenHeight: Int = Resources.getSystem().displayMetrics.heightPixels,
    private val screenDensity: Int = Resources.getSystem().displayMetrics.densityDpi,
)


