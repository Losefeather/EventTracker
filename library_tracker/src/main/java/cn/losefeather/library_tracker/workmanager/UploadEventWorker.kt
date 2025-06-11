package cn.losefeather.library_tracker.workmanager

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.work.WorkerParameters
import androidx.work.multiprocess.RemoteCoroutineWorker
import cn.losefeather.library_tracker.database.TrackerDataBaseManager
import cn.losefeather.library_tracker.entity.AppInfo
import cn.losefeather.library_tracker.entity.DeviceInfo
import cn.losefeather.library_tracker.entity.NetworkInfo
import cn.losefeather.library_tracker.entity.UserInfo
import cn.losefeather.library_tracker.grpc.TrackerGrpcService
import kotlinx.coroutines.flow.first

class UploadEventWorker(appContext: Context, workerParams: WorkerParameters) :
    RemoteCoroutineWorker(appContext, workerParams) {
    private val eventService = TrackerGrpcService(Uri.parse(""), appContext)
    override suspend fun doRemoteWork(): Result {
        val eventList = TrackerDataBaseManager.getInstance().queryNotUploadEvent().first()
        if (eventList.isEmpty()) {
            return Result.success()
        }

        // 上传操作（假设是网络请求，仍在 IO 线程）
        val result = eventService.uploadEvent(
            DeviceInfo(),
            provideAppInfo(applicationContext),
            UserInfo(1, "", ""),
            NetworkInfo("", ""),
            eventList
        )
        if (result.isSuccess) {
            // 删除操作（IO 线程）
            val number =
                TrackerDataBaseManager.getInstance().deleteEvent(*eventList.toTypedArray()).first()
            if (number == eventList.size) {
                return Result.success()
            } else {
                return Result.retry()
            }
        } else {
            return Result.failure()
        }
    }


    fun provideAppInfo(context: Context): AppInfo {
        val packageName = context.packageName
        var appVersion = ""
        var appVersionCode = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val value = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.PackageInfoFlags.of(0L)
            )
            appVersion = value.versionName
            value.longVersionCode.toString()
        } else {

        }
        return AppInfo(packageName, appVersion, appVersionCode)
    }
}