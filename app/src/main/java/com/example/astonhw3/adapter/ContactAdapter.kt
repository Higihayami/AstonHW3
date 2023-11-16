package com.example.astonhw3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.astonhw3.R
import com.example.astonhw3.model.Contact
import java.util.*

class ContactAdapter(
    private val onItemClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private val selectedContacts = mutableSetOf<Int>()
    private var contacts: List<Contact> = emptyList()
    private var isSelectionModeEnabled = false

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idTextView: TextView = itemView.findViewById(R.id.idTextView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val phoneNumberTextView: TextView = itemView.findViewById(R.id.phoneNumberTextView)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.idTextView.text = contact.id.toString()
        holder.nameTextView.text = "${contact.firstName} ${contact.lastName}"
        holder.phoneNumberTextView.text = contact.phoneNumber
        holder.itemView.setOnClickListener {
            if(!isSelectionModeEnabled)
                onItemClick(contact)
        }

        if (isSelectionModeEnabled) {
            holder.checkBox.visibility = View.VISIBLE
            holder.checkBox.isChecked = selectedContacts.contains(contact.id)
            holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedContacts.add(contact.id)
                } else {
                    selectedContacts.remove(contact.id)
                }
            }
        } else {
            holder.checkBox.visibility = View.GONE
            selectedContacts.remove(contact.id)
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    fun submitList(newContacts: List<Contact>) {
        val diffResult = DiffUtil.calculateDiff(ContactDiffCallback(contacts, newContacts))
        contacts = newContacts
        diffResult.dispatchUpdatesTo(this)
    }

    fun getCurrentList(): List<Contact> {
        return contacts.toList()
    }

    fun enableSelection(enable: Boolean) {
        isSelectionModeEnabled = enable
        notifyDataSetChanged()
    }

    fun deleteSelectedContacts() {
        val newList = contacts.toMutableList()
        val removedContacts = mutableListOf<Contact>()

        for (contact in contacts) {
            if (selectedContacts.contains(contact.id)) {
                removedContacts.add(contact)
            }
        }

        newList.removeAll(removedContacts)
        selectedContacts.clear()

        val diffResult = DiffUtil.calculateDiff(ContactDiffCallback(contacts, newList))
        contacts = newList
        diffResult.dispatchUpdatesTo(this)
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        var contactList = contacts.toMutableList()
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(contactList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(contactList, i, i - 1)
            }
        }
        contacts = contactList
        notifyItemMoved(fromPosition, toPosition)
    }
}

private class ContactDiffCallback(
    private val oldList: List<Contact>,
    private val newList: List<Contact>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}