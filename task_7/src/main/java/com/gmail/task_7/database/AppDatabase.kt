package com.gmail.task_7.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gmail.task_7.dao.ContactDao
import com.gmail.task_7.entity.Contact

@Database(entities = [Contact::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao
}