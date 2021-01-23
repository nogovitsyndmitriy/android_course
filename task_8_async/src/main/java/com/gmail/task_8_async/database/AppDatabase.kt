package com.gmail.task_8_async.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gmail.task_8_async.converters.InfoTypeConverters
import com.gmail.task_8_async.dao.ContactDao
import com.gmail.task_8_async.entity.Contact


@Database(entities = [Contact::class], version = 1)
@TypeConverters(InfoTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao
}