package com.gmail.task_8_async.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gmail.task_8_async.dao.ContactDao
import com.gmail.task_8_async.entity.Contact


@Database(entities = [Contact::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao
}