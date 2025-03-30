package com.example.lab7;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

class LoginActivity : AppCompatActivity() {
    private lateinit var edUsername: EditText
    private lateinit var edPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button
    private val PREFS_NAME = "UserCredentials"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edUsername = findViewById(R.id.ed_username)
        edPassword = findViewById(R.id.ed_password)
        btnLogin = findViewById(R.id.btn_login)
        btnSignUp = findViewById(R.id.btn_signup)

        // Переход к регистрации
        btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        // Проверка логина/пароля
        btnLogin.setOnClickListener {
            val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            val savedUser = prefs.getString("username", "")
            val savedPass = prefs.getString("password", "")

            val inputUser = edUsername.text.toString()
            val inputPass = edPassword.text.toString()

            if (inputUser == savedUser && inputPass == savedPass) {
                Toast.makeText(this, "Вход выполнен!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, FileActivity::class.java))
            } else {
                Toast.makeText(this, "Неверные данные!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}