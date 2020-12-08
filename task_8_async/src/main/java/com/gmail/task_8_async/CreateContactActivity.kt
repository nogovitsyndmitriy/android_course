package com.gmail.task_8_async

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.gmail.task_8_async.entity.Contact

class CreateContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add)
        val intent = Intent()
        findViewById<ImageButton>(R.id.backToContactsBtn).setOnClickListener {
            finish()
        }
        var contactInfoType: String = "Phone Number"
        findViewById<RadioGroup>(R.id.radioGroup).setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            contactInfoType = radio.text.toString()
            setTypeOfContact(contactInfoType)
        })
        findViewById<ImageButton>(R.id.saveContactButton).setOnClickListener(View.OnClickListener {
            intent.putExtra(
                "CONTACT",
                createContact(
                    findViewById<EditText>(R.id.nameText).text.toString(),
                    findViewById<EditText>(R.id.contactInfoText).text.toString(),
                    contactInfoType
                )
            )
            setResult(666, intent)
            finish()
        })
    }

    private fun createContact(name: String, contactInfo: String, contactInfoType: String): Contact {
        val newContact = Contact()
        if (contactInfoType == "Email") {
            newContact.let {
                it.name = name
                it.email = contactInfo
                it.isPhone = false
            }
        } else {
            newContact.let {
                it.name = name
                it.phone = contactInfo
            }
        }
        return newContact;
    }

    private fun setTypeOfContact(text: String) {
        val editText = findViewById<EditText>(R.id.contactInfoText)
        if (text == "Email") {
            editText.hint = "Email"
        } else {
            editText.hint = "Phone number"
        }
    }
}