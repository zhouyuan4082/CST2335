package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_address);
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("lab3_android", Context.MODE_PRIVATE );

        EditText input_text1 = (EditText)findViewById(R.id.input_text1);
        input_text1.setText(prefs.getString("email", ""));
        Button page1Button = findViewById(R.id.button2);
        if(page1Button != null)
            page1Button.setOnClickListener(v -> {
                Intent goToPage2 = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(goToPage2);
            });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("lab3_android", Context.MODE_PRIVATE );
        SharedPreferences.Editor edit = prefs.edit();
        EditText input_text1 = findViewById(R.id.input_text1);
        String email_address = "email";
        edit.putString(email_address, input_text1.getText().toString());
        edit.commit();
    }

}
