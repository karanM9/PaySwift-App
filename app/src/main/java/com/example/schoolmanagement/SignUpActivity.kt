package com.example.schoolmanagement

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        val name = findViewById<EditText>(R.id.et_name)
        val email = findViewById<EditText>(R.id.et_email)
        val password = findViewById<EditText>(R.id.et_password)
        val signupBtn = findViewById<Button>(R.id.btn_signup)
        val loginLink = findViewById<TextView>(R.id.tv_login)
        val radioPolicy = findViewById<RadioButton>(R.id.policyButton)

        signupBtn.isEnabled = false
        radioPolicy.setOnCheckedChangeListener { _, isChecked ->
            signupBtn.isEnabled = isChecked
        }
        loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()}

        signupBtn.setOnClickListener {
            val emailTxt = email.text.toString()
            val passTxt = password.text.toString()
            val nameTxt = name.text.toString()

            if (nameTxt.isEmpty() || emailTxt.isEmpty() || passTxt.isEmpty()) {
                Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show()
            }
            else
            {
                auth.createUserWithEmailAndPassword(emailTxt, passTxt)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {


                            val user = FirebaseAuth.getInstance().currentUser
                            if (user != null) {
                                val firestore = FirebaseFirestore.getInstance()
                                val uid = user.uid
                                val email = user.email ?: "noemail"
                                val name = nameTxt
                                val upiTemp = email.substringBefore("@")
                                val upiId = upiTemp.lowercase() + "@payswift"

                                val db = Firebase.firestore

                                val users = db.collection("users")

                                val userData = hashMapOf(
                                    "name" to name,
                                    "email" to email,
                                    "upi_id" to upiId,
                                    "balance" to 0,
                                    "created_at" to FieldValue.serverTimestamp()
                                )
                                users.document(uid).set(userData)

                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            this,
                                            "Sign UP Successful\nUser saved in Firestore!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        startActivity(Intent(this, LoginActivity::class.java))
                                        finish()
                                        // Move to dashboard here
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(
                                            this,
                                            "Error saving user: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                            }
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "User already exists in Firestore.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                    }
                }
            }
        }
    }
