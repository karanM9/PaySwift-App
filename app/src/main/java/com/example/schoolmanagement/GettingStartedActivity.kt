package com.example.schoolmanagement

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GettingStartedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_getting_started)

        val signupbutton = findViewById<Button>(R.id.signupbutton)
        val signinbutton = findViewById<Button>(R.id.signinbutton)

        signinbutton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        signupbutton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.gettingstarted)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}