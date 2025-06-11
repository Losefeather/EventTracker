package cn.losefeather.library_tracker.entity

import android.content.res.Resources
import android.os.Build

data class DeviceInfo(
     val device: String = Build.DEVICE,
     val id: String = Build.ID,
     val hardware: String = Build.HARDWARE,
     val board: String = Build.BOARD,
     val manufacturer: String = Build.MANUFACTURER,
     val cpuArchitecture: String = Build.SUPPORTED_ABIS.first(),
     val serial: String = Build.SERIAL,
     val osType: String = "Android",
     val osVersion: String = Build.VERSION.RELEASE,
     val sdkVersion: Int = Build.VERSION.SDK_INT,
     val screenWidth: Int = Resources.getSystem().displayMetrics.widthPixels,
     val screenHeight: Int = Resources.getSystem().displayMetrics.heightPixels,
     val screenDensity: Int = Resources.getSystem().displayMetrics.densityDpi,
)


