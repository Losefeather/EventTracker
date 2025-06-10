package  cn.losefeather.library_tracker.grpc

import android.util.Log
import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.TimeoutCancellationException

fun handleException(it: Throwable, toastCallBack: (info: String) -> Unit, logout: () -> Unit) {
    Log.e("", "handleException: error $it")
    if (it is StatusException) {
        it.status.code?.apply {
            when (this) {
                Status.Code.OK -> {}
                Status.Code.CANCELLED -> {}
                Status.Code.UNKNOWN -> {}
                Status.Code.INVALID_ARGUMENT -> {
                    it.status.description?.let { it1 -> toastCallBack(it1) }
                }

                Status.Code.DEADLINE_EXCEEDED -> {}
                Status.Code.NOT_FOUND -> {
                    it.status.description?.let { it1 -> toastCallBack(it1) }
                }

                Status.Code.ALREADY_EXISTS -> {}
                Status.Code.PERMISSION_DENIED -> {}
                Status.Code.RESOURCE_EXHAUSTED -> {}
                Status.Code.FAILED_PRECONDITION -> {}
                Status.Code.ABORTED -> {}
                Status.Code.OUT_OF_RANGE -> {}
                Status.Code.UNIMPLEMENTED -> {}
                Status.Code.INTERNAL -> {
                    it.status.description?.let { it1 -> toastCallBack(it1) }
                }

                Status.Code.UNAVAILABLE -> {
                    toastCallBack("连接失败")
                }

                Status.Code.DATA_LOSS -> {}
                Status.Code.UNAUTHENTICATED -> {
                    logout()
                }

                else -> {

                }
            }
        }
    } else if (it is TimeoutCancellationException) {
        toastCallBack("连接超时")
    }

}