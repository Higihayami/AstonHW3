package com.example.astonhw3.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.astonhw3.model.Contact
import com.example.astonhw3.R

class EditContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact)

        val contact = intent.getSerializableExtra("CONTACT") as? Contact
        val contactId = contact?.id
        val saveButton: Button = findViewById(R.id.editSaveButton)
        val firstNameEditText: TextView = findViewById(R.id.editFirstNameEditText)
        val lastNameEditText: TextView = findViewById(R.id.editLastNameEditText)
        val phoneNumberEditText: TextView = findViewById(R.id.editPhoneNumberEditText)

        firstNameEditText.text = contact?.firstName
        lastNameEditText.text = contact?.lastName
        phoneNumberEditText.text = contact?.phoneNumber

        saveButton.setOnClickListener {
            val editedFirstName = firstNameEditText.text.toString()
            val editedLastName = lastNameEditText.text.toString()
            val editedPhoneNumber = phoneNumberEditText.text.toString()
            val resultIntent = Intent()

            resultIntent.putExtra("EDITED_FIRST_NAME", editedFirstName)
            resultIntent.putExtra("EDITED_LAST_NAME", editedLastName)
            resultIntent.putExtra("EDITED_PHONE_NUMBER", editedPhoneNumber)
            resultIntent.putExtra("CONTACT_ID", contactId)

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}