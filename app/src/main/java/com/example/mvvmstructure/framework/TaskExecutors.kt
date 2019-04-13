package com.example.mvvmstructure.framework

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject

class TaskExecutors
/*
     * methods to dispatch tasks on demanded thread.*/
@Inject
constructor() {
    val diskOperationThread: Executor
    val networkOperationThread: Executor
    val mainThread: Executor

    init {
        this.diskOperationThread = Executors.newSingleThreadExecutor()
        this.networkOperationThread = Executors.newFixedThreadPool(3)
        this.mainThread = MainThreadExecutor()
    }

    private inner class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}