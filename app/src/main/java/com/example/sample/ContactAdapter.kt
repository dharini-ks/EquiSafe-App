package com.example.sample


import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(private val contacts: MutableList<Contact>, private val activity: ContactBookActivity) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvContactName: TextView = itemView.findViewById(R.id.tvContactName)
        val tvContactNumber: TextView = itemView.findViewById(R.id.tvContactNumber)
        val btnCall: ImageButton = itemView.findViewById(R.id.btnCall)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_item, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.tvContactName.text = contact.name
        holder.tvContactNumber.text = contact.number

        holder.btnCall.setOnClickListener {
            activity.makePhoneCall(contact.number)
        }

        holder.btnEdit.setOnClickListener {
            showEditContactDialog(contact, position)
        }

        holder.btnDelete.setOnClickListener {
            deleteContact(position)
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    private fun showEditContactDialog(contact: Contact, position: Int) {
        val dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_add_contact, null)
        val contactNameEditText = dialogView.findViewById<EditText>(R.id.etContactName)
        val contactNumberEditText = dialogView.findViewById<EditText>(R.id.etContactNumber)
        val createButton = dialogView.findViewById<Button>(R.id.btnCreate)

        contactNameEditText.setText(contact.name)
        contactNumberEditText.setText(contact.number)

        val dialog = AlertDialog.Builder(activity)
            .setView(dialogView)
            .create()

        createButton.text = "Save"
        createButton.setOnClickListener {
            val name = contactNameEditText.text.toString()
            val number = contactNumberEditText.text.toString()

            if (name.isNotEmpty() && number.isNotEmpty()) {
                contact.name = name
                contact.number = number
                notifyItemChanged(position)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun deleteContact(position: Int) {
        contacts.removeAt(position)
        notifyItemRemoved(position)
    }
}
