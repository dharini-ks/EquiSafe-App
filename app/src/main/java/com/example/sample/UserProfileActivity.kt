package com.example.sample

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.InputStream

@Suppress("DEPRECATION")
class UserProfileActivity : AppCompatActivity() {

    private lateinit var ivProfilePicture: ImageView
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var btnSave: Button
    private lateinit var btnLogout: Button

    private val pimagerequest = 1

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance().reference // Firebase Storage reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        ivProfilePicture = findViewById(R.id.ivProfilePicture)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        btnSave = findViewById(R.id.btnSave)
        btnLogout = findViewById(R.id.btnLogout)

        // Load user data from Firebase
        loadUserData()

        // Set click listeners
        ivProfilePicture.setOnClickListener { openImagePicker() }  // Set click listener on profile picture
        btnSave.setOnClickListener { saveUserData() }
        btnLogout.setOnClickListener { logout() }
    }

    private fun loadUserData() {
        val user = auth.currentUser
        if (user != null) {
            etName.setText(user.displayName)
            etEmail.setText(user.email)

            firestore.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val phone = document.getString("phoneNumber")
                        val profilePictureUrl = document.getString("profilePictureUrl")
                        etPhoneNumber.setText(phone)

                        // Load profile picture with Glide and apply circular crop
                        if (profilePictureUrl != null) {
                            Glide.with(this)
                                .load(profilePictureUrl)
                                .apply(RequestOptions.circleCropTransform())  // Circular crop
                                .placeholder(R.drawable.face)  // Placeholder image
                                .into(ivProfilePicture)
                        }
                    }
                }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, pimagerequest)
    }

    @Deprecated("This method is deprecated, use the Activity Result API instead.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pimagerequest && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri: Uri = data.data!!

            // Set the profile picture with Glide and apply circular crop
            Glide.with(this)
                .load(imageUri)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.face)
                .into(ivProfilePicture)

            // Upload the image to Firebase Storage and save the URL in Firestore
            uploadProfilePicture(imageUri)
        }
    }

    private fun uploadProfilePicture(imageUri: Uri) {
        val user = auth.currentUser
        if (user != null) {
            val profilePicRef = storage.child("profile_pictures/${user.uid}.jpg")

            // Upload the image to Firebase Storage
            profilePicRef.putFile(imageUri)
                .addOnSuccessListener {
                    // Get the download URL and update Firestore
                    profilePicRef.downloadUrl.addOnSuccessListener { uri ->
                        val profilePictureUrl = uri.toString()

                        // Save the profile picture URL to Firestore
                        firestore.collection("users").document(user.uid)
                            .update("profilePictureUrl", profilePictureUrl)
                            .addOnSuccessListener {
                                // Profile picture URL updated successfully
                            }
                            .addOnFailureListener {
                                // Handle failure
                            }
                    }
                }
                .addOnFailureListener {
                    // Handle the upload failure
                }
        }
    }

    private fun saveUserData() {
        val user = auth.currentUser
        if (user != null) {
            val phoneNumber = etPhoneNumber.text.toString()

            val userData = mapOf(
                "phoneNumber" to phoneNumber
            )

            firestore.collection("users").document(user.uid).update(userData)
                .addOnSuccessListener {
                    // Data updated successfully
                }
                .addOnFailureListener {
                    // Handle failure
                }
        }
    }

    private fun logout() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
