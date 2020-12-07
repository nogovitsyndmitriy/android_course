package com.gmail.task_8_async

import android.content.Intent
import android.os.Build
import android.os.Bundle
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

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var recyclerView: RecyclerView

    interface ListItemActionListener {
        fun onItemClicked(id: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = (applicationContext as App).db
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.noContactsText).isVisible = db.contactDao().getAll().toMutableList().isNotEmpty()
        recyclerView = findViewById<RecyclerView>(R.id.recyclerContactsView)
        recyclerView.apply {
            adapter = ContactAdapter(db.contactDao().getAll().toMutableList(), object : ListItemActionListener {
                override fun onItemClicked(id: String) {
                    val contactForEdit = db.contactDao().findById(id)
                    val intent = Intent(context, EditContactActivity::class.java)
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
        } else if (requestCode == 1 && resultCode == DELETED && data != null) {
            db.contactDao().deleteById(data.getStringExtra("DELETED_ID").toString())
        } else if (requestCode == 1 && resultCode == EDITED && data != null) {
            val editedContact = data.getSerializableExtra("EDITED_CONTACT") as Contact
            db.contactDao().deleteById(editedContact.id)
            db.contactDao().saveAll(editedContact)
        }
        findViewById<TextView>(R.id.noContactsText)?.isVisible = db.contactDao().getAll().toMutableList().isEmpty()
        recyclerView.apply {
            adapter = ContactAdapter(db.contactDao().getAll().toMutableList(), object : ListItemActionListener {
                override fun onItemClicked(id: String) {
                    val contactForEdit = db.contactDao().findById(id)
                    val intent = Intent(context, EditContactActivity::class.java)
                    intent.putExtra("CONTACT", contactForEdit)
                    startActivityForResult(intent, 1)
                }
            })
        }
    }

    class ContactAdapter(
        private val list: List<Contact>,
        private val listItemActionListener: ListItemActionListener?
    ) :
        RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ContactViewHolder(inflater, parent, listItemActionListener)
        }

        override fun getItemCount(): Int = list.size
        override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
            val contact: Contact = list[position]
            holder.bind(contact)
        }

        class ContactViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,
            private var listItemActionListener: ListItemActionListener? = null
        ) : RecyclerView.ViewHolder(
            inflater.inflate(R.layout.item_contact, parent, false)
        ) {
            private var avatarView = itemView.findViewById<ImageView>(R.id.avatar)
            private var contactNameView = itemView.findViewById<TextView>(R.id.contactName)
            private var contactInfoView = itemView.findViewById<TextView>(R.id.contactInfo)

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