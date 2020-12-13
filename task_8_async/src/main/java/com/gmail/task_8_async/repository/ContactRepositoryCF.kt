package com.gmail.task_8_async.repository

import com.gmail.task_8_async.entity.Contact

interface ContactRepositoryCF {

    fun getAllContacts()

    fun deleteContact(contactId: String, position: Int)

    fun createContact(contact: Contact)

    fun updateContact(contact: Contact, position: Int)
}