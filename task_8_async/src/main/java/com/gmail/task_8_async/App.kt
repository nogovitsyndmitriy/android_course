package com.gmail.task_8_async

import android.app.Application
import androidx.room.Room
import com.gmail.task_8_async.database.AppDatabase

class App : Application() {
    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "contacts"
        )
            .allowMainThreadQueries()
            .build()
    }
}