package com.gmail.task_7.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gmail.task_7.entity.Contact

@Dao
interface ContactDao {
    @Query("SELECT * FROM contact")
    fun getAll(): List<Contact>

    @Query("SELECT * FROM contact WHERE id = :userId")
    fun findById(userId: String): Contact

    @Insert
    fun saveAll(vararg users: Contact)

    @Query("DELETE FROM contact WHERE id = :id")
    fun deleteById(id: String)

}