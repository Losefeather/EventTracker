package  cn.losefeather.library_tracker.grpc


import EventTrackerServiceGrpcKt
import android.content.Context
import android.net.Uri
import android.util.Log
import io.grpc.ConnectivityState
import io.grpc.android.AndroidChannelBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.cancel
import uploadEventReq
import java.io.Closeable
import java.util.concurrent.TimeUnit

/**
 * PDA Grpc Service
 */
class TrackerGrpcService constructor(private var uri: Uri, private val context: Context) :
    Closeable {
    val TAG = "TrackerGrpcService"

    // 创建 gRPC 通道
    // 创建 Android 专用的 gRPC 通道
    private val androidChannel by lazy {
        val builder =
            AndroidChannelBuilder.forAddress(uri.host, uri.port).context(context.applicationContext)
                .apply {
                    if (uri.scheme == "https") {
                        this.useTransportSecurity() // 使用安全传输
                    } else {
                        this.usePlaintext() // 使用明文传输
                    }
                }
        builder.executor(Dispatchers.IO.asExecutor()).build()
    }

    // 定义 gRPC 服务的存根
    private val trackerService =
        EventTrackerServiceGrpcKt.EventTrackerServiceCoroutineStub(androidChannel)

    // 定义协程作用域
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        listenToChannelStateChanges()
    }

    fun listenToChannelStateChanges() {
        androidChannel.notifyWhenStateChanged(ConnectivityState.IDLE) {
            handleStateChange(ConnectivityState.IDLE)
        }
        androidChannel.notifyWhenStateChanged(ConnectivityState.CONNECTING) {
            handleStateChange(ConnectivityState.CONNECTING)
        }
        androidChannel.notifyWhenStateChanged(ConnectivityState.READY) {
            handleStateChange(ConnectivityState.READY)
        }
        androidChannel.notifyWhenStateChanged(ConnectivityState.TRANSIENT_FAILURE) {
            handleStateChange(ConnectivityState.TRANSIENT_FAILURE)
        }
        androidChannel.notifyWhenStateChanged(ConnectivityState.SHUTDOWN) {
            handleStateChange(ConnectivityState.SHUTDOWN)
        }
    }

    private fun handleStateChange(state: ConnectivityState) {
        when (state) {
            ConnectivityState.IDLE -> {
                Log.e(TAG, "handleStateChange: androidChannel is idle")
                // Optionally, trigger a reconnection attempt or notify the user
            }

            ConnectivityState.CONNECTING -> {
                Log.e(TAG, "handleStateChange: androidChannel is connecting")
                // Update UI to show a loading indicator
            }

            ConnectivityState.READY -> {
                Log.e(TAG, "handleStateChange: androidChannel is ready")
                // Connection is established, proceed with gRPC calls
            }

            ConnectivityState.TRANSIENT_FAILURE -> {
                Log.e(TAG, "handleStateChange: androidChannel is a transient failure")
                // Implement retry logic or notify the user of a temporary issue
            }

            ConnectivityState.SHUTDOWN -> {
                Log.e(TAG, "handleStateChange: androidChannel is shutdown")
                // Clean up resources or notify the user that the connection is closed
            }
        }
    }

    private val grpcRequestManager by lazy { GrpcRequestManager() }

    suspend fun uploadEvent() {
        val request = uploadEventReq { }
        trackerService.sendEvent(request)
    }
    // 修改流式请求方法示例
//    fun tallyScanAssBill(
//        billNo: String,
//        assBillNo: String
//    ): Flow<GrpcResult<Bill.TallyScanAssBillRes>> {
//        return grpcRequestManager.executeFlow {
//            val requests = listOf(tallyScanAssBillReq {
//                this.billNo = billNo
//                this.assBillNo = assBillNo
//            }).asFlow()
//            billService.tallyScanAssBill(requests = requests, headers = tokenHeaders())
//        }
//    }


    // 关闭通道
    override fun close() {
        androidChannel.shutdownNow()
    }

    // 取消协程作用域
    fun onDestroy() {
        scope.cancel("cancel")
    }

    // 检查 gRPC 连接状态
    fun checkGrpcConnection(): Boolean {
        return try {
            val state = androidChannel.getState(false)
            println("当前通道状态: $state")
            if (state == ConnectivityState.READY) {
                println("gRPC 连接已建立")
                true
            } else if (state == ConnectivityState.IDLE || state == ConnectivityState.CONNECTING || state == ConnectivityState.TRANSIENT_FAILURE) {
                println("gRPC 正在连接或连接失败，尝试等待一段时间后再次检查...")
                try {
                    TimeUnit.SECONDS.sleep(5)
                    val newState = androidChannel.getState(false)
                    if (newState == ConnectivityState.READY) {
                        println("gRPC 连接已建立")
                        true
                    } else {
                        println("gRPC 连接未建立")
                        false
                    }
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    println("等待过程中被中断: ${e.message}")
                    false
                }
            } else {
                println("gRPC 连接未建立")
                false
            }
        } finally {
            androidChannel.shutdownNow()
        }
    }
}