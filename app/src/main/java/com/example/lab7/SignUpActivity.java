package com.example.lab7;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    private EditText edUsername, edPassword, edConfirmPassword;
    private Button btnCreateUser;
    private final String PREFS_NAME = "UserCredentials";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edUsername = findViewById(R.id.ed_username);
        edPassword = findViewById(R.id.ed_password);
        edConfirmPassword = findViewById(R.id.ed_confirm_pwd);
        btnCreateUser = findViewById(R.id.btn_create_user);

        btnCreateUser.setOnClickListener(v -> {
            String pass = edPassword.getText().toString();
            String confirmPass = edConfirmPassword.getText().toString();

            if (pass.equals(confirmPass)) {
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("username", edUsername.getText().toString());
                editor.putString("password", pass);
                editor.apply();
                Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Пароли не совпадают!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
