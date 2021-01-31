package com.gmail.task_7

import android.app.Application
import androidx.room.Room
import com.gmail.task_7.database.AppDatabase

class App : Application() {
    lateinit var db: AppDatabase
    lateinit var provider: ContactsProvider

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "contacts"
        )
            .allowMainThreadQueries()
            .build()
        provider = ContactsProvider()
    }
}