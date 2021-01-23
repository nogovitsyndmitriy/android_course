package com.gmail.task_8_async.repository

import com.gmail.task_8_async.entity.Contact

interface UniversalContactRepository {

    fun getAll()

    fun save(contact: Contact)

    fun delete(contactId: String, position: Int)

    fun update(contact: Contact, position: Int)
}