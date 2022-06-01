package az.zero.instabugtaskaz.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object AZExecutors {
    private val diskIO: ExecutorService by lazy { Executors.newSingleThreadExecutor() }
    private val numberOfThreads by lazy { Runtime.getRuntime().availableProcessors() }
    private val networkIO: ExecutorService by lazy { Executors.newFixedThreadPool(numberOfThreads) }
    private val mainThread: MainThreadExecutor by lazy { MainThreadExecutor }

    fun executeDiskIO(action: () -> Unit) {
        diskIO.execute(action)
    }

    fun executeNetworkOP(action: () -> Unit) {
        networkIO.execute(action)
    }

    fun executeMainThread(action: () -> Unit) {
        mainThread.execute(action)
    }

    private object MainThreadExecutor : Executor {
        private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}