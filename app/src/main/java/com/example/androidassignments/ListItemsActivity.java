package com.example.androidassignments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ListItemsActivity extends AppCompatActivity {
    //constant
    protected static final String ACTIVITY_NAME = "ListItemsActivity";
    private final static int TAKE_PICTURE = 1;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        imageButton = findViewById(R.id.id_imageButton);
        imageButton.setOnClickListener(v -> openCameraApp());

        Switch switchButton = findViewById(R.id.id_switch);
        switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            switchChanged(isChecked);    });

        CheckBox checkBox = findViewById(R.id.id_checkBox);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkBoxChanged();    });
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

    private void openCameraApp() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == TAKE_PICTURE) {
            if (resultCode == RESULT_OK && intent.hasExtra("data")) {
                Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
                if (bitmap != null) {
                    imageButton.setImageBitmap(bitmap);
                }
            }
        }
    }

    protected void switchChanged(boolean isChecked){
        if(isChecked){
            Toast.makeText(this,R.string.toast_switch_on, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,R.string.toast_switch_off, Toast.LENGTH_LONG).show();
        }
    }

    protected void checkBoxChanged(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_message) //Add a dialog message to strings.xml

                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    // User clicked OK button
                    Intent resultIntent = new Intent(  );
                    resultIntent.putExtra("Response", getString(R.string.toast_LIA_response));
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    // User cancelled the dialog
                })
                .show();
    }
}