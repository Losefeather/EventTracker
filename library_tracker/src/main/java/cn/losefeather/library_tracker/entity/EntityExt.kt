// 扩展函数文件（新增）
package cn.losefeather.library_tracker.entity

import android.content.Context
import appInfo
import com.google.protobuf.Value
import deviceInfo
import eventInfo
import networkInfo
import userInfo

// DeviceInfo 扩展函数：转换为 gRPC 的 DeviceInfo 消息
fun DeviceInfo.toGrpcDeviceInfo() = deviceInfo {
    device = this@toGrpcDeviceInfo.device
    id = this@toGrpcDeviceInfo.id
    osType = this@toGrpcDeviceInfo.osType
    hardware = this@toGrpcDeviceInfo.hardware
    board = this@toGrpcDeviceInfo.board
    manufacturer = this@toGrpcDeviceInfo.manufacturer
    cpuArchitecture = this@toGrpcDeviceInfo.cpuArchitecture
    serial = this@toGrpcDeviceInfo.serial
    osVersion = this@toGrpcDeviceInfo.osVersion
    sdkVersion = this@toGrpcDeviceInfo.sdkVersion
    screenWidth = this@toGrpcDeviceInfo.screenWidth
    screenHeight = this@toGrpcDeviceInfo.screenHeight
    screenDensity = this@toGrpcDeviceInfo.screenDensity
}

// AppInfo 扩展函数：转换为 gRPC 的 AppInfo 消息
fun AppInfo.toGrpcAppInfo() = appInfo {
    packName = this@toGrpcAppInfo.packName
    appVersion = this@toGrpcAppInfo.appVersion
    appVersionCode = this@toGrpcAppInfo.appVersionCode
}

// UserInfo 扩展函数：转换为 gRPC 的 UserInfo 消息
fun UserInfo.toGrpcUserInfo() = userInfo {
    uid = this@toGrpcUserInfo.uid
    userName = this@toGrpcUserInfo.userName
    userId = this@toGrpcUserInfo.userId
}

// NetworkInfo 扩展函数：转换为 gRPC 的 NetworkInfo 消息
fun NetworkInfo.toGrpcNetworkInfo(context: Context) = networkInfo {
    ip = this@toGrpcNetworkInfo.getIp(context)
    mac = this@toGrpcNetworkInfo.mac
}

// EventInfo 扩展函数：转换为 gRPC 的 Event 消息
fun EventInfo.toGrpcEvent() = eventInfo {
    uid = this@toGrpcEvent.uid
    eventName = this@toGrpcEvent.eventName
    eventCategory = this@toGrpcEvent.eventCategory
    eventTime = this@toGrpcEvent.eventTime
    eventUploadStatus = this@toGrpcEvent.eventUploadStatus
    eventProp.putAll(this@toGrpcEvent.eventProp.mapValues { it.value as Value })
}