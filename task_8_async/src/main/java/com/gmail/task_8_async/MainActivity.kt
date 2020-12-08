package com.gmail.task_8_async

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.task_8_async.database.AppDatabase
import com.gmail.task_8_async.entity.Contact
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var handler: Handler
    private lateinit var recyclerView: RecyclerView
    private var contactList: MutableList<Contact> = mutableListOf()
    private val executor = Executors.newFixedThreadPool(10)


    interface ListItemActionListener {
        fun onItemClicked(id: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = (applicationContext as App).db
        setContentView(R.layout.activity_main)
        contactList = db.contactDao().getAll().toMutableList()

        findViewById<TextView>(R.id.noContactsText).isVisible = contactList.isEmpty()
        recyclerView = findViewById<RecyclerView>(R.id.recyclerContactsView)
        recyclerView.apply {
            adapter = ContactAdapter(contactList, object : ListItemActionListener {
                override fun onItemClicked(id: String) {
                    val contactForEdit = db.contactDao().findById(id)
                    val intent = Intent(this@MainActivity, EditContactActivity::class.java)
                    intent.putExtra("CONTACT", contactForEdit)
                    startActivityForResult(intent, 1)
                }
            })
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        val addButton = findViewById<ImageButton>(R.id.addContactBtn)
        addButton.setOnClickListener {
            val intent = Intent(this, CreateContactActivity::class.java)
            startActivityForResult(intent, 555)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 555 && resultCode == 666 && data != null) {
            db.contactDao().saveAll(data.getSerializableExtra("CONTACT") as Contact)
            contactList = db.contactDao().getAll().toMutableList()
        } else if (requestCode == 1 && resultCode == DELETED && data != null) {
            val id = data.getStringExtra("DELETED_ID").toString()
            db.contactDao().deleteById(id)
            contactList = db.contactDao().getAll().toMutableList()
        } else if (requestCode == 1 && resultCode == EDITED && data != null) {
            val editedContact = data.getSerializableExtra("EDITED_CONTACT") as Contact
            db.contactDao().deleteById(editedContact.id)
            db.contactDao().saveAll(editedContact)
            this.contactList = db.contactDao().getAll().toMutableList()
        }
        val newAdapter = this.recyclerView.adapter as ContactAdapter
        newAdapter.putItem(contactList)
        newAdapter.notifyDataSetChanged()
        findViewById<TextView>(R.id.noContactsText)?.isVisible = contactList.isEmpty()
    }

    class ContactAdapter(
        private var list: List<Contact>,
        private val listItemActionListener: ListItemActionListener?
    ) :
        RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ContactViewHolder(inflater, parent, listItemActionListener)
        }

        fun putItem(list: List<Contact>) {
            this.list = list
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int = list.size
        override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
            val contact: Contact = list[position]
            holder.bind(contact)
        }

        class ContactViewHolder(
            inflater: LayoutInflater, parent: ViewGroup, private var listItemActionListener: ListItemActionListener? = null
        ) : RecyclerView.ViewHolder(
            inflater.inflate(R.layout.item_contact, parent, false)
        ) {
            private var avatarView = itemView.findViewById<ImageView>(R.id.avatar)
            private var contactNameView = itemView.findViewById<TextView>(R.id.contactNameItem)
            private var contactInfoView = itemView.findViewById<TextView>(R.id.contactInfoItem)

            fun bind(contact: Contact) {
                itemView.setOnClickListener {
                    listItemActionListener?.onItemClicked(contact.id)
                }
                if (contact.isPhone) {
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