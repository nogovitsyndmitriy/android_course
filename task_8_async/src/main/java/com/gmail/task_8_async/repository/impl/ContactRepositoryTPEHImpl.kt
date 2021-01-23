package com.gmail.task_8_async.repository.impl

import android.os.Handler
import android.widget.TextView
import androidx.core.view.isVisible
import com.gmail.task_8_async.MainActivity
import com.gmail.task_8_async.database.AppDatabase
import com.gmail.task_8_async.entity.Contact
import com.gmail.task_8_async.repository.ContactRepositoryTPEH
import java.util.concurrent.Executors

class ContactRepositoryTPEHImpl(
    private val db: AppDatabase,
    private val handler: Handler,
    private val textView: TextView,
    private val newAdapter: MainActivity.ContactAdapter
) : ContactRepositoryTPEH {

    private val executor = Executors.newFixedThreadPool(10)
    private var contactListFromDb: MutableList<Contact> = mutableListOf()

    override fun getAllContacts() {
        executor.submit(Runnable {
            contactListFromDb = getContactList()
            handler.post(Runnable {
                textView.isVisible = contactListFromDb.isEmpty()
                newAdapter.putAllContactList(contactListFromDb)
            })
        })
    }

    override fun deleteContact(contactId: String, position: Int) {
        executor.submit(Runnable {
            db.contactDao().deleteById(contactId)
            handler.post(Runnable {
                newAdapter.removeItem(position)
                textView.isVisible = newAdapter.itemCount <= 0
            })
        })
    }

    override fun createContact(contact: Contact) {
        executor.submit(Runnable {
            db.contactDao().saveAll(contact)
            handler.post(Runnable {
                newAdapter.insertItem(newAdapter.itemCount, contact)
                textView.isVisible = newAdapter.itemCount <= 0
            })
        })
    }

    override fun updateContact(contact: Contact, position: Int) {
        executor.submit(Runnable {
            db.contactDao().deleteById(contact.id)
            db.contactDao().saveAll(contact)
            handler.post(Runnable {
                newAdapter.itemEdited(position, contact)
            })
        })
    }

    private fun getContactList() = db.contactDao().getAll().toMutableList()
}
