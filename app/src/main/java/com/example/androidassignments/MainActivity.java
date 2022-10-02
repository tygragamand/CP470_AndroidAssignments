package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "MainActivity";
    protected static final int REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        Button bttn = findViewById(R.id.button);
        bttn.setOnClickListener(v -> openListItemsActivity());
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

    private void openListItemsActivity(){
        Intent intent = new Intent(MainActivity.this, ListItemsActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);

        if (requestCode == REQUEST_CODE && responseCode == Activity.RESULT_OK) {
            Log.i(ACTIVITY_NAME, "Returned to MainActivity.onActivityResult");
            String messagePassed = data.getStringExtra("Response");
            Toast.makeText(this,getString(R.string.toast_MA_response) + messagePassed, Toast.LENGTH_LONG).show();
        }
    }

    public void print(String text){
        Toast.makeText(this,text, Toast.LENGTH_LONG).show();
    }
}