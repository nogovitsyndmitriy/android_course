package com.gmail.task_8_async

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.gmail.task_8_async.consts.CONTACT
import com.gmail.task_8_async.consts.DELETED
import com.gmail.task_8_async.consts.DELETED_ID
import com.gmail.task_8_async.consts.EDITED
import com.gmail.task_8_async.consts.EDITED_CONTACT
import com.gmail.task_8_async.consts.POSITION
import com.gmail.task_8_async.entity.Contact


class EditContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove)
        val editedContact = intent.getSerializableExtra(CONTACT) as Contact
        val position = intent.getIntExtra(POSITION, -1)
        findViewById<EditText>(R.id.nameText).setText(editedContact.name)
        findViewById<EditText>(R.id.infoText).setText(editedContact.info)
        val editResult = Intent()
        findViewById<ImageButton>(R.id.editContactBtn).setOnClickListener {
            editResult.putExtra(EDITED_CONTACT, editContact(editedContact))
            editResult.putExtra(POSITION, position)
            setResult(EDITED, editResult)
            finish()
        }
        findViewById<Button>(R.id.removeContactBtn).setOnClickListener {
            editResult.putExtra(DELETED_ID, editedContact.id)
            editResult.putExtra(POSITION, position)
            setResult(DELETED, editResult)
            finish()
        }
    }

    private fun editContact(editedContact: Contact): Contact {
        editedContact.name = findViewById<EditText>(R.id.nameText).text.toString()
        editedContact.info = findViewById<EditText>(R.id.infoText).text.toString()
        return editedContact
    }
}