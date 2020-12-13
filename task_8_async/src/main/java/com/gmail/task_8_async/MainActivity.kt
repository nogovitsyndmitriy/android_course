package com.gmail.task_8_async

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.task_8_async.consts.ASYNC_TYPE
import com.gmail.task_8_async.consts.CONTACT
import com.gmail.task_8_async.consts.CREATE
import com.gmail.task_8_async.consts.CREATED
import com.gmail.task_8_async.consts.DELETED
import com.gmail.task_8_async.consts.DELETED_ID
import com.gmail.task_8_async.consts.EDIT
import com.gmail.task_8_async.consts.EDITED
import com.gmail.task_8_async.consts.EDITED_CONTACT
import com.gmail.task_8_async.consts.POSITION
import com.gmail.task_8_async.database.AppDatabase
import com.gmail.task_8_async.entity.Contact
import com.gmail.task_8_async.repository.UniversalContactRepository
import com.gmail.task_8_async.repository.impl.UniversalContactRepositoryImpl
import java.util.concurrent.Executors


class MainActivity() : AppCompatActivity() {

    private lateinit var universalContactRepository: UniversalContactRepository
    private lateinit var db: AppDatabase
    private lateinit var handler: Handler
    private lateinit var recyclerView: RecyclerView
    private var contactList: MutableList<Contact> = mutableListOf()
    private lateinit var textView: TextView
    private lateinit var newAdapter: ContactAdapter
    private lateinit var type: String
    private val executor = Executors.newFixedThreadPool(5)

    interface ListItemActionListener {
        fun onItemClicked(id: String, position: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tpeh = findViewById<RadioButton>(R.id.tpehButton)
        val completable = findViewById<RadioButton>(R.id.completableFutureButton)
        val rxjava = findViewById<RadioButton>(R.id.rxJavaButton)
        db = (applicationContext as App).db
        handler = Handler(mainLooper)
        textView = findViewById<TextView>(R.id.noContactsText)
        type = getAsyncMode().toString()
        setUpAsyncMode(tpeh, completable, rxjava, type)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerContactsView)
        recyclerView.apply {
            adapter = ContactAdapter(contactList, object : ListItemActionListener {
                override fun onItemClicked(id: String, position: Int) {
                    val contactForEdit = db.contactDao().findById(id)
                    val intent = Intent(this@MainActivity, EditContactActivity::class.java)
                    intent.putExtra(CONTACT, contactForEdit)
                    intent.putExtra(POSITION, position)
                    startActivityForResult(intent, 1)
                }
            })
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        newAdapter = this.recyclerView.adapter as ContactAdapter
        universalContactRepository = UniversalContactRepositoryImpl(db, handler, textView, newAdapter, type, executor)
        universalContactRepository.getAll()
        val group = findViewById<RadioGroup>(R.id.radioGroupAsyncMethod)
        group.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            radio.setOnClickListener {
                type = radio.text.toString()
                setAsyncMode(type)
                val toast = Toast.makeText(this, type, Toast.LENGTH_LONG)
                toast.show()
            }
        })
        val addButton = findViewById<ImageButton>(R.id.addContactBtn)
        addButton.setOnClickListener {
            val intent = Intent(this, CreateContactActivity::class.java)
            startActivityForResult(intent, CREATE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            val position = data.getIntExtra(POSITION, -1)
            if (requestCode == CREATE && resultCode == CREATED) {
                universalContactRepository.save(data.getSerializableExtra(CONTACT) as Contact)
            } else if (requestCode == EDIT && resultCode == DELETED) {
                val id = data.getStringExtra(DELETED_ID).toString()
                universalContactRepository.delete(id, position)
            } else if (requestCode == EDIT && resultCode == EDITED) {
                val editedContact = data.getSerializableExtra(EDITED_CONTACT) as Contact
                universalContactRepository.update(editedContact, position)
            }
        }
    }

    private fun setUpAsyncMode(tpeh: RadioButton?, completable: RadioButton?, rxjava: RadioButton?, type: String) {
        if (tpeh != null && tpeh.text == type) {
            tpeh.isChecked = true
        } else if (completable != null && completable.text == type) {
            completable.isChecked = true
        } else if (rxjava != null && rxjava.text == type) {
            rxjava.isChecked = true
        }
    }

    private fun setAsyncMode(asyncType: String) {
        val sharedPrefs = getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putString(ASYNC_TYPE, asyncType)
        universalContactRepository = UniversalContactRepositoryImpl(db, handler, textView, newAdapter, asyncType, executor)
        editor.apply()
    }

    private fun getAsyncMode(): String? {
        val sharedPrefs = getPreferences(Context.MODE_PRIVATE)
        return sharedPrefs.getString(ASYNC_TYPE, "")
    }

    class ContactAdapter(
        private var list: MutableList<Contact>,
        private val listItemActionListener: ListItemActionListener?
    ) :
        RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ContactViewHolder(inflater, parent, listItemActionListener)
        }

        fun putAllContactList(list: MutableList<Contact>) {
            this.list = list
            notifyDataSetChanged()
        }

        fun removeItem(position: Int) {
            list.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount - position);
        }

        fun insertItem(position: Int, contact: Contact) {
            list.add(position, contact)
            notifyItemInserted(position)
        }

        fun itemEdited(position: Int, contact: Contact) {
            list.removeAt(position)
            list.add(position, contact)
            notifyItemChanged(position, contact)
        }

        override fun getItemCount(): Int = list.size
        override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
            val contact: Contact = list[position]
            holder.bind(contact, position)
        }

        class ContactViewHolder(
            inflater: LayoutInflater, parent: ViewGroup, private var listItemActionListener: ListItemActionListener? = null
        ) : RecyclerView.ViewHolder(
            inflater.inflate(R.layout.item_contact, parent, false)
        ) {
            private var avatarView = itemView.findViewById<ImageView>(R.id.avatar)
            private var contactNameView = itemView.findViewById<TextView>(R.id.contactNameItem)
            private var contactInfoView = itemView.findViewById<TextView>(R.id.contactInfoItem)

            fun bind(contact: Contact, position: Int) {
                itemView.setOnClickListener {
                    listItemActionListener?.onItemClicked(contact.id, position)
                }
                contact.infoType.iconId.let { avatarView?.setImageResource(it) }
                contactNameView?.text = contact.name
                contactInfoView?.text = contact.info
            }
        }
    }
}