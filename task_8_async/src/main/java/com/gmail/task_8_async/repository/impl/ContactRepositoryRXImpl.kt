package com.gmail.task_8_async.repository.impl

import android.util.Log
import android.widget.TextView
import androidx.core.view.isVisible
import com.gmail.task_8_async.MainActivity
import com.gmail.task_8_async.database.AppDatabase
import com.gmail.task_8_async.entity.Contact
import com.gmail.task_8_async.repository.ContactRepositoryRX
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class ContactRepositoryRXImpl(
    private val db: AppDatabase,
    private val textView: TextView,
    private val newAdapter: MainActivity.ContactAdapter
) : ContactRepositoryRX {
    private var contactListFromDb: MutableList<Contact> = mutableListOf()

    override fun getAllContacts() {
        Observable.create<MutableList<Contact>> { emitter -> emitter.onNext(db.contactDao().getAll().toMutableList()) }
            .map { result -> contactListFromDb = result }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                newAdapter.putAllContactList(contactListFromDb)
                textView.isVisible = contactListFromDb.isEmpty()
            },
                { throwable -> Log.e("ERROR", throwable.toString()) }
            )
    }

    override fun deleteContact(contactId: String, position: Int) {
        Observable.create<Unit> { emitter -> emitter.onNext(db.contactDao().deleteById(contactId)) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                newAdapter.removeItem(position)
                textView.isVisible = contactListFromDb.isEmpty()
                textView.isVisible = newAdapter.itemCount <= 0
            },
                { throwable -> Log.e("ERROR", throwable.toString()) }
            )
    }

    override fun createContact(contact: Contact) {
        Observable.create<Unit> { emitter -> emitter.onNext(db.contactDao().saveAll(contact)) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                newAdapter.insertItem(newAdapter.itemCount, contact)
                textView.isVisible = newAdapter.itemCount <= 0
            },
                { throwable -> Log.e("ERROR", throwable.toString()) }
            )
    }

    override fun updateContact(contact: Contact, position: Int) {
        Observable.create<Unit> { emitter -> emitter.onNext(db.contactDao().deleteById(contact.id)) }
            .map { db.contactDao().saveAll(contact) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                newAdapter.itemEdited(position, contact)
            },
                { throwable -> Log.e("ERROR", throwable.toString()) }
            )
    }
}