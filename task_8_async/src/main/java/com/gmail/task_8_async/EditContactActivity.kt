package com.gmail.task_8_async

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.gmail.task_8_async.entity.Contact

class EditContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove)
        val editedContact = intent.getSerializableExtra("CONTACT") as Contact
        findViewById<EditText>(R.id.nameText).setText(editedContact.name)
        if (editedContact.isPhone) {
            findViewById<EditText>(R.id.infoText).setText(editedContact.phone)
        } else {
            findViewById<EditText>(R.id.infoText).setText(editedContact.email)
        }
        val editResult = Intent(this, MainActivity::class.java)
        findViewById<ImageButton>(R.id.editContactBtn).setOnClickListener {
            editResult.putExtra("EDITED_CONTACT", editContact(editedContact))
            setResult(2, editResult)
            finish()
        }
        findViewById<Button>(R.id.removeContactBtn).setOnClickListener {
            editResult.putExtra("DELETED_ID", editedContact.id)
            setResult(0, editResult)
            finish()
        }
    }

    private fun editContact(editedContact: Contact): Contact {
        editedContact.name = findViewById<EditText>(R.id.nameText).text.toString()
        if (editedContact.isPhone) {
            editedContact.phone = findViewById<EditText>(R.id.infoText).text.toString()
        } else {
            editedContact.email = findViewById<EditText>(R.id.infoText).text.toString()
        }
        return editedContact
    }
}