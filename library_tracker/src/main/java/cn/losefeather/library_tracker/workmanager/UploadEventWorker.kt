package cn.losefeather.library_tracker.workmanager

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.work.WorkerParameters
import androidx.work.multiprocess.RemoteCoroutineWorker
import cn.losefeather.library_tracker.database.TrackerDataBaseManager
import cn.losefeather.library_tracker.entity.AppInfo
import cn.losefeather.library_tracker.entity.DeviceInfo
import cn.losefeather.library_tracker.entity.NetworkInfo
import cn.losefeather.library_tracker.entity.UserInfo
import cn.losefeather.library_tracker.grpc.TrackerGrpcService
import cn.losefeather.library_tracker.utils.NetworkUtils
import kotlinx.coroutines.flow.first

class UploadEventWorker(appContext: Context, workerParams: WorkerParameters) :
    RemoteCoroutineWorker(appContext, workerParams) {
    private val TAG = this::class.java.canonicalName
    private val eventService = TrackerGrpcService(Uri.parse(""), appContext)
    override suspend fun doRemoteWork(): Result {
        Log.e(TAG, "doRemoteWork: start")
        val eventList = TrackerDataBaseManager.getInstance().queryNotUploadEvent().first()
        if (eventList.isEmpty()) {
            Log.e(TAG, "doRemoteWork: no event is need upload")
            return Result.success()
        }
        Log.e(TAG, "doRemoteWork: start upload")
        // 上传操作（假设是网络请求，仍在 IO 线程）
        val result = eventService.uploadEvent(
            DeviceInfo(),
            provideAppInfo(applicationContext),
            UserInfo(1, "", ""),
            provideNetworkInfo(applicationContext),
            eventList
        )
        if (result.isSuccess) {
            // 删除操作（IO 线程）
            Log.e(TAG, "doRemoteWork: upload successful")
            Log.e(TAG, "doRemoteWork: start delete upload events from database")
            val number =
                TrackerDataBaseManager.getInstance().deleteEvent(*eventList.toTypedArray())
            Log.e(TAG, "doRemoteWork: delete events completed")
            if (number == eventList.size) {
                return Result.success()
            } else {
                return Result.retry()
            }
        } else {
            return Result.failure()
        }
    }


    private fun provideAppInfo(context: Context): AppInfo {
        val packageName = context.packageName
        var appVersion = ""
        var appVersionCode = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val value = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.PackageInfoFlags.of(0L)
            )
            appVersion = value.versionName ?: ""
            value.longVersionCode.toString()
        } else {

        }
        return AppInfo(packageName, appVersion, appVersionCode)
    }

    private fun provideNetworkInfo(context: Context): NetworkInfo {
        val ip = NetworkUtils.getIPv4Address(context) ?: ""
        val mac = NetworkUtils.getMacAddress(context) ?: ""
        return NetworkInfo(ip, mac)
    }
}