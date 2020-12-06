package com.gmail.task_7

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add.*
import com.gmail.task_7.entity.Contact

class CreateContactActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        var intent = Intent(this, MainActivity::class.java)

        backToContactsBtn.setOnClickListener {
            startActivity(intent)
            finish()
        }
        var contactInfoType: String = "Phone Number"
        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            contactInfoType = radio.text.toString()
            setTypeOfContact(contactInfoType)
        })

        saveContactBtn.setOnClickListener(View.OnClickListener {
            intent.putExtra(
                "CONTACT", createContact(nameEditText.text.toString(), contactInfo.text.toString(), contactInfoType)
            )
            setResult(666, intent)
            finish()
        })
    }

    fun createContact(name: String, contactInfo: String, contactInfoType: String): Contact? {
        val newContact = Contact()
        if (contactInfoType.equals("Email")) {
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

    fun setTypeOfContact(text: String) {
        val editText = contactInfo
        if (text.equals("Email")) {
            editText.hint = "Email"
        } else {
            editText.hint = "Phone number"
        }

    }
}