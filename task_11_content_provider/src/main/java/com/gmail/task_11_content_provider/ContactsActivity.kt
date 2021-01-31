package com.gmail.task_11_content_provider

import android.app.Activity
import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactsActivity : Activity() {

    private val uri: String = "content://com.gmail.task_7.provider.contacts"
    private lateinit var recyclerContactsView: RecyclerView
    private var contactList: MutableList<ContactEntity> = mutableListOf()
    private lateinit var adapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        recyclerContactsView = findViewById(R.id.recyclerContactsView)
        recyclerContactsView.apply {
            adapter = ContactAdapter(contactList)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        adapter = this.recyclerContactsView.adapter as ContactAdapter
        CoroutineScope(Dispatchers.Main).launch {
            getContactList(adapter, getContacts(contentResolver, uri))
        }
    }

    private fun getContactList(adapter: ContactAdapter, contacts: MutableList<ContactEntity>) {
        adapter.putAllContactList(contacts)
    }

    private suspend fun getContacts(contentResolver: ContentResolver, uri: String) = withContext(Dispatchers.IO) {
        val list = mutableListOf<ContactEntity>()
        val cursor = contentResolver.query(Uri.parse(uri), null, null, null)
        if (cursor != null) {
            val nameIndex = cursor.getColumnIndex("name")
            val phoneIndex = cursor.getColumnIndex("phone")
            val emailIndex = cursor.getColumnIndex("email")
            val isPhone = cursor.getColumnIndex("isPhone")
            val idIndex = cursor.getColumnIndex("id")
            while (cursor.moveToNext()) {
                list.add(ContactEntity(cursor.getString(idIndex), cursor.getString(nameIndex), cursor.getString(phoneIndex), cursor.getString(emailIndex), cursor.getInt(isPhone)))
            }
        }
        list
    }


    class ContactAdapter(
        private var list: List<ContactEntity>
    ) :
        RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ContactViewHolder(inflater, parent)
        }

        override fun getItemCount(): Int = list.size
        override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
            val contact: ContactEntity = list[position]
            holder.bind(contact)
        }

        fun putAllContactList(list: MutableList<ContactEntity>) {
            this.list = list
            notifyDataSetChanged()
        }

        class ContactViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup
        ) : RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.item_contact,
                parent,
                false
            )
        ) {
            private var avatarView: ImageView? = null
            private var contactNameView: TextView? = null
            private var contactInfoView: TextView? = null

            init {
                avatarView = itemView.findViewById(R.id.avatar)
                contactNameView = itemView.findViewById(R.id.contactName)
                contactInfoView = itemView.findViewById(R.id.contactInfo)
            }

            fun bind(contact: ContactEntity) {
                if (contact.isPhone == 1) {
                    avatarView?.setImageResource(R.drawable.ic_contact_phone)
                    contactNameView?.text = contact.name
                    contactInfoView?.text = contact.phone
                } else {
                    avatarView?.setImageResource(R.drawable.ic_contact_email)
                    contactNameView?.text = contact.name
                    contactInfoView?.text = contact.email
                }
            }
        }
    }
}