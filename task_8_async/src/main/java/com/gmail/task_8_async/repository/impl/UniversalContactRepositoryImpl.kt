package com.gmail.task_8_async.repository.impl

import android.os.Handler
import android.widget.TextView
import com.gmail.task_8_async.MainActivity
import com.gmail.task_8_async.consts.COMPLETABLE_FUTURE
import com.gmail.task_8_async.consts.RX_JAVA
import com.gmail.task_8_async.consts.TPEH
import com.gmail.task_8_async.database.AppDatabase
import com.gmail.task_8_async.entity.Contact
import com.gmail.task_8_async.repository.UniversalContactRepository
import java.util.concurrent.Executor

class UniversalContactRepositoryImpl(
    private val db: AppDatabase,
    private val handler: Handler,
    private val textView: TextView,
    private val newAdapter: MainActivity.ContactAdapter,
    private val asyncMode: String,
    private val mainExecutor: Executor,
    private val contactRepositoryTPEH: ContactRepositoryTPEHImpl = ContactRepositoryTPEHImpl(db, handler, textView, newAdapter),
    private val contactRepositoryCF: ContactRepositoryCFImpl = ContactRepositoryCFImpl(db, textView, newAdapter, mainExecutor),
    private val contactRepositoryRX: ContactRepositoryRXImpl = ContactRepositoryRXImpl(db, textView, newAdapter)
) : UniversalContactRepository {

    override fun getAll() {
        when (asyncMode) {
            TPEH -> contactRepositoryTPEH.getAllContacts()
            COMPLETABLE_FUTURE -> contactRepositoryCF.getAllContacts()
            RX_JAVA -> contactRepositoryRX.getAllContacts()
        }
    }

    override fun save(contact: Contact) {
        when (asyncMode) {
            TPEH -> contactRepositoryTPEH.createContact(contact)
            COMPLETABLE_FUTURE -> contactRepositoryCF.createContact(contact)
            RX_JAVA -> contactRepositoryRX.createContact(contact)
        }
    }

    override fun delete(contactId: String, position: Int) {
        when (asyncMode) {
            TPEH -> contactRepositoryTPEH.deleteContact(contactId, position)
            COMPLETABLE_FUTURE -> contactRepositoryCF.deleteContact(contactId, position)
            RX_JAVA -> contactRepositoryRX.deleteContact(contactId, position)
        }
    }

    override fun update(contact: Contact, position: Int) {
        when (asyncMode) {
            TPEH -> contactRepositoryTPEH.updateContact(contact, position)
            COMPLETABLE_FUTURE -> contactRepositoryCF.updateContact(contact, position)
            RX_JAVA -> contactRepositoryRX.updateContact(contact, position)
        }
    }
}