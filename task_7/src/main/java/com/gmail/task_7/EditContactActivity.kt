package com.gmail.task_7
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gmail.task_7.entity.Contact
import kotlinx.android.synthetic.main.activity_remove.editContactBtn
import kotlinx.android.synthetic.main.activity_remove.infoText
import kotlinx.android.synthetic.main.activity_remove.nameText
import kotlinx.android.synthetic.main.activity_remove.removeContactBtn

class EditContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove)
        val editedContact = intent.getSerializableExtra("CONTACT") as Contact
        nameText.setText(editedContact.name)
        if (editedContact.isPhone) {
            infoText.setText(editedContact.phone)
        } else {
            infoText.setText(editedContact.email)
        }
        val editResult = Intent(this, MainActivity::class.java)
        editContactBtn.setOnClickListener {
            editResult.putExtra("EDITED_CONTACT", editContact(editedContact))
            setResult(2, editResult)
            finish()
        }
        removeContactBtn.setOnClickListener {
            editResult.putExtra("DELETED_ID", editedContact.id)
            setResult(0, editResult)
            finish()
        }
    }

    private fun editContact(editedContact: Contact): Contact {
        editedContact.name = nameText.text.toString()
        if (editedContact.isPhone) {
            editedContact.phone = infoText.text.toString()
        } else {
            editedContact.email = infoText.text.toString()
        }
        return editedContact
    }
}