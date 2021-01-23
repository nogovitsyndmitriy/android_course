package com.gmail.task_8_async.repository.impl

import android.widget.TextView
import androidx.core.view.isVisible
import com.gmail.task_8_async.MainActivity
import com.gmail.task_8_async.database.AppDatabase
import com.gmail.task_8_async.entity.Contact
import com.gmail.task_8_async.repository.ContactRepositoryCF
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.function.Supplier

class ContactRepositoryCFImpl(
    private val db: AppDatabase,
    private val textView: TextView,
    private val newAdapter: MainActivity.ContactAdapter,
    private val mainExecutor: Executor
) : ContactRepositoryCF {

    override fun getAllContacts() {
        CompletableFuture.supplyAsync(Supplier {
            val contactListFromDb = db.contactDao().getAll().toMutableList()
            textView.isVisible = contactListFromDb.isEmpty()
            contactListFromDb
        }).thenApplyAsync { result -> newAdapter.putAllContactList(result) }
            .thenRunAsync(Runnable { }, mainExecutor)
    }

    override fun createContact(contact: Contact) {
        CompletableFuture.supplyAsync(Supplier {
            db.contactDao().saveAll(contact)
        }).thenRunAsync({
            newAdapter.insertItem(newAdapter.itemCount, contact)
            textView.isVisible = newAdapter.itemCount <= 0
        }, mainExecutor)
    }

    override fun updateContact(contact: Contact, position: Int) {
        CompletableFuture.supplyAsync(Supplier {
            db.contactDao().deleteById(contact.id)
            db.contactDao().saveAll(contact)
        }).thenApplyAsync {
            newAdapter.itemEdited(position, contact)
        }
    }

    override fun deleteContact(contactId: String, position: Int) {
        CompletableFuture.supplyAsync(Supplier {
            db.contactDao().deleteById(contactId)
        }).thenRunAsync({
            newAdapter.removeItem(position)
            textView.isVisible = newAdapter.itemCount <= 0
        }, mainExecutor)
    }
}