package com.example.astonhw3.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.astonhw3.model.Contact
import com.example.astonhw3.R
import com.example.astonhw3.adapter.ContactAdapter
import io.github.serpro69.kfaker.Faker

class MainActivity : AppCompatActivity() {

    private val contacts = mutableListOf<Contact>()
    private val adapter = ContactAdapter(
        onItemClick = { contact ->
            val editIntent = Intent(this, EditContactActivity::class.java)
            editIntent.putExtra("CONTACT", contact)
            startActivityForResult(editIntent, EDIT_CONTACT_REQUEST)
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val cancelButton: Button = findViewById(R.id.btn_cancel)
        val deleteButton: Button = findViewById(R.id.btn_delete)
        val choiceButton: Button = findViewById(R.id.btn_choice)
        val addContactButton: Button = findViewById(R.id.btn_add)
        val contacts = generateContacts()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.submitList(contacts)


        addContactButton.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            startActivityForResult(intent, ADD_CONTACT_REQUEST)
        }

        choiceButton.setOnClickListener {
            adapter.enableSelection(true)
            cancelButton.visibility = View.VISIBLE
            deleteButton.visibility = View.VISIBLE
            addContactButton.visibility = View.GONE
        }

        deleteButton.setOnClickListener {
            adapter.enableSelection(false)
            cancelButton.visibility = View.GONE
            deleteButton.visibility = View.GONE
            addContactButton.visibility = View.VISIBLE
            adapter.deleteSelectedContacts()
        }

        cancelButton.setOnClickListener {
            adapter.enableSelection(false)
            cancelButton.visibility = View.GONE
            deleteButton.visibility = View.GONE
            addContactButton.visibility = View.VISIBLE
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                return makeMovementFlags(dragFlags, 0)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                adapter.onItemMove(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

            override fun isLongPressDragEnabled(): Boolean {
                return true
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun generateContacts(): List<Contact> {
        val faker = Faker()

        for (i in 1..100) {
            val firstName = faker.name.firstName()
            val lastName = faker.name.lastName()
            val phoneNumber = faker.phoneNumber.phoneNumber()

            contacts.add(Contact(i, firstName, lastName, phoneNumber))
        }

        return contacts
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_CONTACT_REQUEST && resultCode == Activity.RESULT_OK) {
            val firstName = data?.getStringExtra("FIRST_NAME")
            val lastName = data?.getStringExtra("LAST_NAME")
            val phoneNumber = data?.getStringExtra("PHONE_NUMBER")
            val currentList = adapter.getCurrentList().toMutableList()
            val newContact = Contact(currentList[currentList.lastIndex].id + 1, firstName, lastName, phoneNumber)

            currentList.add(newContact)
            adapter.submitList(currentList)
        }

        if (requestCode == EDIT_CONTACT_REQUEST && resultCode == Activity.RESULT_OK) {
            val firstName = data?.getStringExtra("EDITED_FIRST_NAME")
            val lastName = data?.getStringExtra("EDITED_LAST_NAME")
            val phoneNumber = data?.getStringExtra("EDITED_PHONE_NUMBER")
            val editedContactId = data?.getIntExtra("CONTACT_ID", -1)

            editedContactId?.let { contactId ->
                val currentList = adapter.getCurrentList().toMutableList()
                val editedContactIndex = currentList.indexOfFirst { it.id == contactId }

                if (editedContactIndex != -1) {
                    val editedContact = Contact(contactId, firstName, lastName, phoneNumber)
                    currentList[editedContactIndex] = editedContact
                    adapter.submitList(currentList)
                }
            }
        }
    }

    companion object {
        const val ADD_CONTACT_REQUEST = 1
        const val EDIT_CONTACT_REQUEST = 2
    }
}