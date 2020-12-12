package com.gmail.task_8_async.repository

import android.os.Handler
import android.widget.TextView
import com.gmail.task_8_async.MainActivity
import com.gmail.task_8_async.database.AppDatabase
import com.gmail.task_8_async.entity.Contact

class UniversalContactRepositoryImpl(
    private val db: AppDatabase,
    private val handler: Handler,
    private val textView: TextView,
    private val newAdapter: MainActivity.ContactAdapter,
    private val contactRepositoryTPEH: ContactRepositoryTPEHImpl = ContactRepositoryTPEHImpl(db, handler, textView, newAdapter)
) : UniversalContactRepository {

    override fun getAll() {
        contactRepositoryTPEH.getAllContacts()
    }

    override fun save(contact: Contact) {
        contactRepositoryTPEH.createContact(contact)
    }

    override fun delete(contactId: String, position: Int) {
        contactRepositoryTPEH.deleteContact(contactId, position)
    }

    override fun update(contact: Contact, position: Int) {
        contactRepositoryTPEH.updateContact(contact, position)
    }
}