package  cn.losefeather.library_tracker.grpc

import io.grpc.Status
import io.grpc.StatusException
import io.grpc.StatusRuntimeException

sealed class GrpcResult<out T> {
    data class Success<out T>(val data: T) : GrpcResult<T>()
    data class Error(val exception: Throwable, val grpcCode: Status.Code? = null) :
        GrpcResult<Nothing>()
}

class GrpcRequestManager {
    suspend fun <T> request(
        request: suspend () -> T,
    ): GrpcResult<T> {

        return try {
            val result = request()
            GrpcResult.Success(result)
        } catch (e: Exception) {
            handleGrpcError(e)
        }
    }


    private fun handleGrpcError(e: Throwable): GrpcResult.Error {
        return when (e) {
            is StatusException -> GrpcResult.Error(e, e.status.code).also {
                logError(e.status.code, e.message)
            }

            is StatusRuntimeException -> GrpcResult.Error(e, e.status.code).also {
                logError(e.status.code, e.message)
            }

            else -> GrpcResult.Error(e).also {
                logError(null, e.message)
            }
        }
    }

    private fun logError(code: Status.Code?, message: String?) {
        // 这里可以添加更详细的日志记录
        println("gRPC Error - Code: ${code?.name}, Message: $message")
    }

    // 添加这个扩展函数到GrpcRequestManager类中
    suspend fun <T> withLoading(
        isLoading: (Boolean) -> Unit,
        block: suspend () -> GrpcResult<T>
    ): GrpcResult<T> {
        isLoading(true)
        return try {
            block().also { isLoading(false) }
        } catch (e: Exception) {
            isLoading(false)
            throw e
        }
    }
}