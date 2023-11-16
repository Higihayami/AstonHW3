package com.example.astonhw3.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.astonhw3.R

class AddContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        val saveButton: Button = findViewById(R.id.saveButton)
        val firstNameEditText: TextView = findViewById(R.id.firstNameEditText)
        val lastNameEditText: TextView = findViewById(R.id.lastNameEditText)
        val phoneNumberEditText: TextView = findViewById(R.id.phoneNumberEditText)

        saveButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val phoneNumber = phoneNumberEditText.text.toString()
            val resultIntent = Intent()

            resultIntent.putExtra("FIRST_NAME", firstName)
            resultIntent.putExtra("LAST_NAME", lastName)
            resultIntent.putExtra("PHONE_NUMBER", phoneNumber)

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}