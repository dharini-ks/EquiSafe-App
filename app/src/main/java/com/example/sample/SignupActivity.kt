package com.example.sample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Views
        etEmail = findViewById(R.id.ettEmail)
        etPassword = findViewById(R.id.ettPassword)
        btnSignUp = findViewById(R.id.btnSignUp)

        // Sign up button click event
        btnSignUp.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validate email and password
            if (email.isEmpty()) {
                etEmail.error = "Email is required"
                etEmail.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                etPassword.error = "Password is required"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            // Create user with Firebase Auth
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign up successful, proceed to OTP verification
                    Toast.makeText(this, "Sign up successful! Proceeding to OTP verification.", Toast.LENGTH_SHORT).show()

                    // Start OtpActivity and pass the email
                    val intent = Intent(this, OtpActivity::class.java)
                    intent.putExtra("EMAIL_EXTRA", email)
                    startActivity(intent)
                    finish() // Finish SignupActivity to remove it from the back stack
                } else {
                    // Sign up failed
                    val errorMessage = task.exception?.message ?: "Unknown error"
                    Toast.makeText(this, "Sign up failed: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
