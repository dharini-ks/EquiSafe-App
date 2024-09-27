package com.example.sample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.app.AlertDialog
import java.util.Locale

class ContactBookActivity : AppCompatActivity() {

    private lateinit var rvContactList: RecyclerView
    private lateinit var btnAddContact: Button
    private lateinit var searchView: SearchView
    private lateinit var contactAdapter: ContactAdapter
    private val contacts = mutableListOf<Contact>()
    private val filteredContacts = mutableListOf<Contact>()

    private val requestCallPermission = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_book)

        rvContactList = findViewById(R.id.rvContactList)
        btnAddContact = findViewById(R.id.btnAddContact)
        searchView = findViewById(R.id.searchView)

        // Adding default emergency contacts
        addDefaultEmergencyContacts()

        filteredContacts.addAll(contacts) // Initialize filteredContacts with all contacts
        contactAdapter = ContactAdapter(filteredContacts, this)
        rvContactList.layoutManager = LinearLayoutManager(this)
        rvContactList.adapter = contactAdapter

        btnAddContact.setOnClickListener {
            showAddContactDialog()
        }

        setupSearchView()
    }

    private fun addDefaultEmergencyContacts() {
        contacts.add(Contact("Police", "100"))
        contacts.add(Contact("Fire Department", "101"))
        contacts.add(Contact("Ambulance", "102"))
        contacts.add(Contact("Disaster Management", "108"))
        contacts.add(Contact("Women Helpline", "1091"))
        contacts.add(Contact("Child Helpline", "1098"))
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterContacts(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterContacts(newText)
                return true
            }
        })
    }

    private fun filterContacts(query: String?) {
        filteredContacts.clear()
        if (query.isNullOrEmpty()) {
            filteredContacts.addAll(contacts)
        } else {
            val lowerCaseQuery = query.lowercase(Locale.getDefault())
            for (contact in contacts) {
                if (contact.name.lowercase(Locale.getDefault()).contains(lowerCaseQuery)) {
                    filteredContacts.add(contact)
                }
            }
        }
        contactAdapter.notifyDataSetChanged()
    }

    private fun showAddContactDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_contact, null)
        val contactNameEditText = dialogView.findViewById<EditText>(R.id.etContactName)
        val contactNumberEditText = dialogView.findViewById<EditText>(R.id.etContactNumber)
        val createButton = dialogView.findViewById<Button>(R.id.btnCreate)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        createButton.setOnClickListener {
            val name = contactNameEditText.text.toString()
            val number = contactNumberEditText.text.toString()

            if (name.isNotEmpty() && number.isNotEmpty()) {
                val contact = Contact(name, number)
                contacts.add(contact)
                filterContacts(searchView.query.toString()) // Re-filter with the current search query
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCallPermission) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can proceed with the call
            } else {
                // Permission denied
                // Show a message or handle the denial here
            }
        }
    }

    fun makePhoneCall(phoneNumber: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), requestCallPermission)
        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNumber")
            startActivity(callIntent)
        }
    }
}
