package com.gmail.task_8_async

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.gmail.task_8_async.consts.CONTACT
import com.gmail.task_8_async.consts.CREATED
import com.gmail.task_8_async.entity.Contact
import com.gmail.task_8_async.entity.InfoType

class CreateContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add)
        val intent = Intent()
        findViewById<ImageButton>(R.id.backToContactsBtn).setOnClickListener {
            finish()
        }
        var contactInfoType: InfoType = InfoType.PHONE_NUMBER
        findViewById<RadioGroup>(R.id.radioGroup).setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            contactInfoType = getInfoType(radio.text.toString())
            setTypeOfContact(contactInfoType)
        })
        findViewById<ImageButton>(R.id.saveContactButton).setOnClickListener(View.OnClickListener {
            intent.putExtra(
                CONTACT,
                createContact(
                    findViewById<EditText>(R.id.nameText).text.toString(),
                    findViewById<EditText>(R.id.contactInfoText).text.toString(),
                    contactInfoType
                )
            )
            setResult(CREATED, intent)
            finish()
        })
    }

    private fun createContact(name: String, contactInfo: String, contactInfoType: InfoType): Contact {
        val newContact = Contact()
        newContact.let {
            it.name = name
            it.info = contactInfo
            it.infoType = contactInfoType
        }
        return newContact;
    }

    private fun setTypeOfContact(infoType: InfoType) {
        val editText = findViewById<EditText>(R.id.contactInfoText)
        if (infoType == InfoType.EMAIL) {
            editText.hint = InfoType.EMAIL.type
        }
    }

    private fun getInfoType(type: String): InfoType {
        var infoType = InfoType.values().find { infoType -> infoType.type == type }
        if (infoType == null) {
            infoType = InfoType.PHONE_NUMBER
        }
        return infoType
    }
}