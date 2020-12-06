package com.example.task_4_2_contacts

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task_4_2_contacts.entity.Contact
import kotlinx.android.synthetic.main.activity_main.addContactBtn
import kotlinx.android.synthetic.main.activity_main.noContactsText
import kotlinx.android.synthetic.main.activity_main.recyclerContactsView

class MainActivity : AppCompatActivity() {

    var contacts = mutableListOf<Contact>()

    interface ListItemActionListener {
        fun onItemClicked(id: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (contacts.isNotEmpty()) {
            noContactsText.isVisible = false
        }
        addContactBtn.setOnClickListener {
            var intent = Intent(this, CreateContactActivity::class.java)
            startActivityForResult(intent, 555)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 555 && resultCode == 666 && data != null) {
            contacts.add(data.getSerializableExtra("CONTACT") as Contact)
        } else if (requestCode == 1 && resultCode == 0 && data != null) {
            contacts.removeAll { contact -> contact.id.contentEquals(data.getStringExtra("DELETED_ID").toString()) }
        } else if (requestCode == 1 && resultCode == 2 && data != null) {
            val editedContact = data.getSerializableExtra("EDITED_CONTACT") as Contact
            contacts.removeAll { contact -> contact.id.contentEquals(editedContact.id) }
            contacts.add(editedContact)
        }
        noContactsText.isVisible = contacts.isEmpty()
        val recyclerView = recyclerContactsView
        recyclerView.apply {
            adapter = ContactAdapter(contacts, object : ListItemActionListener {
                override fun onItemClicked(id: String) {
                    var contactForEdit = contacts.find { contact -> contact.id.equals(id) }
                    var intent = Intent(context, EditContactActivity::class.java)
                    intent.putExtra("CONTACT", contactForEdit)
                    startActivityForResult(intent, 1)
                }
            })
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    companion object {
        class ContactAdapter(private val list: List<Contact>, private val listItemActionListener: ListItemActionListener?) :
            RecyclerView.Adapter<ContactAdapter.Companion.ContactViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return ContactViewHolder(inflater, parent, listItemActionListener)
            }

            override fun getItemCount(): Int = list.size
            override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
                val contact: Contact = list[position]
                holder.bind(contact)
            }

            companion object {
                class ContactViewHolder(inflater: LayoutInflater, parent: ViewGroup, private var listItemActionListener: ListItemActionListener? = null) : RecyclerView.ViewHolder(
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
    }
}