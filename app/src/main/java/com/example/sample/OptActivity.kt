package com.example.sample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class OtpActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var btnSendOtp: Button
    private lateinit var btnVerifyOtp: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opt)

        etEmail = findViewById(R.id.etEmail)
        btnSendOtp = findViewById(R.id.btnSendOtp)
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp)

        auth = FirebaseAuth.getInstance()

        // Get email from the intent
        val email = intent.getStringExtra("EMAIL_EXTRA")
        if (email != null) {
            etEmail.setText(email)
        }

        btnSendOtp.setOnClickListener {
            val email = etEmail.text.toString().trim()
            if (email.isNotEmpty()) {
                sendVerificationEmail(email)
            } else {
                Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show()
            }
        }

        btnVerifyOtp.setOnClickListener {
            val user = auth.currentUser
            if (user != null) {
                checkEmailVerified(user)
            } else {
                Toast.makeText(this, "No user signed in", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendVerificationEmail(email: String) {
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Verification email sent to $email", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to send email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkEmailVerified(user: FirebaseUser) {
        user.reload().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (user.isEmailVerified) {
                    Toast.makeText(this, "Email verified, proceeding to login", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Email not verified yet", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Failed to reload user: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
