package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore;
import android.graphics.Bitmap;

public class ProfileActivity extends AppCompatActivity {
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageButton imageButton1 ;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageButton1.setImageBitmap(imageBitmap);
        }
        Log.e(ACTIVITY_NAME, "In function:" + "onActivityResult");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile);
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("lab3_android", Context.MODE_PRIVATE );

        EditText input_text2 = (EditText)findViewById(R.id.input_text2);
        input_text2.setText(prefs.getString("email", ""));
        imageButton1 = findViewById(R.id.imageButton1);
        if(imageButton1 != null) {
            imageButton1.setOnClickListener(v -> {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            });
        }
        Log.e(ACTIVITY_NAME, "In function:" + "onCreate()");

        Button gotochat_Button = findViewById(R.id.go_to_chat_button);
        if(gotochat_Button != null) {
            gotochat_Button.setOnClickListener(v -> {
                Intent goToPage3 = new Intent(ProfileActivity.this, ChatRoomActivity.class);
                startActivity(goToPage3);
            });
        }


        Button gotoWeatherForecast_Button = findViewById(R.id.go_to_weatherForecast_button);
        if(gotoWeatherForecast_Button != null) {
            gotoWeatherForecast_Button.setOnClickListener(v -> {
                Intent goToPageWeater = new Intent(ProfileActivity.this, WeatherForecast.class);
                startActivity(goToPageWeater);
            });
        }

        Button gotoToolbarPage_Button = findViewById(R.id.go_to_Toolbar_page);
        if(gotoToolbarPage_Button != null) {
            gotoToolbarPage_Button.setOnClickListener(v -> {
                Intent goToPageToolbar = new Intent(ProfileActivity.this, TestToolbar.class);
                startActivity(goToPageToolbar);
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME, "In function:" + "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "In function:" + "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, "In function:" + "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "In function:" + "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In function:" + "onDestroy()");
    }
}
