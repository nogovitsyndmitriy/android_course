package com.gmail.task_8_async.handler

import android.os.Handler
import android.os.HandlerThread


class CustomThreadHandler(name: String) : HandlerThread(name) {

    private lateinit var handler: Handler

    fun prepareLooper() {
        handler = Handler(looper)
    }

    fun postTask(runnable: Runnable) {
        handler.post(runnable)
    }
}