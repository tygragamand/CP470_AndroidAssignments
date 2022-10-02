package com.example.androidassignments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    //constants
    private static final String ACTIVITY_NAME = "LoginActivity";
    private static final String KEY_EMAIL = "email";

    private EditText edit_email, edit_password;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        edit_email = findViewById(R.id.email);
        edit_email.setText(prefs.getString(KEY_EMAIL,"defaulttest@test.ca"));

        edit_password = findViewById(R.id.password);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> doLogin());
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

    private void doLogin(){
        String email = edit_email.getText().toString();
        String password = edit_password.getText().toString();

        if (!isEmailValid(email)) {
            Toast.makeText(this,R.string.toast_invalid_email, Toast.LENGTH_LONG).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this,R.string.toast_invalid_password, Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_EMAIL, email);
        editor.apply();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}