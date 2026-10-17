package com.nailstudio.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nailstudio.app.R
import com.nailstudio.app.data.AppData

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        val name = findViewById<EditText>(R.id.nameInput)
        val email = findViewById<EditText>(R.id.emailInput)
        val password = findViewById<EditText>(R.id.passwordInput)

        findViewById<Button>(R.id.loginButton).setOnClickListener {
            val error = AppData.loginUser(this, email.text.toString(), password.text.toString())
            if (error != null) Toast.makeText(this, error, Toast.LENGTH_LONG).show() else openMain()
        }
        findViewById<Button>(R.id.registerButton).setOnClickListener {
            val error = AppData.registerUser(this, name.text.toString(), email.text.toString(), password.text.toString())
            if (error != null) Toast.makeText(this, error, Toast.LENGTH_LONG).show() else {
                Toast.makeText(this, "Account created successfully.", Toast.LENGTH_SHORT).show()
                openMain()
            }
        }
    }

    private fun openMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
