package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class TestToolbar extends AppCompatActivity {
    String  item1String = "This is the initial message";
    Toolbar tBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        Toast.makeText(getApplicationContext(),"You clicked on the overflow menu",Toast.LENGTH_SHORT).show();
    }
    //Inflate the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lab7_menu, menu);
        return true;
    }

    //Handling Action Bar button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            //Back button
            case R.id.skip_to_start:
                //If this activity started from other activity
                Toast.makeText(this, item1String, Toast.LENGTH_LONG).show();
                break;
            case R.id.pause:
                //addfav (heart icon) was clicked, Insert your after click code here.
                Toast.makeText(this, "You clicked on the pause icon", Toast.LENGTH_LONG).show();

                View viewDialog = getLayoutInflater().inflate(R.layout.view_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                EditText edit = (EditText)viewDialog.findViewById(R.id.view_edit_text);
                builder.setMessage("The Message")
                        .setView(viewDialog)
                        .setPositiveButton("Positive", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                item1String = edit.getText().toString();
                                // What to do on Accept
                            }
                        })
                        .setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // What to do on Cancel
                            }
                        });

                builder.create().show();
                break;
            case R.id.fast_forward:
                //addfav (heart icon) was clicked, Insert your after click code here.
                //Toast.makeText(this, "You clicked on the fast forward icon", Toast.LENGTH_LONG).show();

                Snackbar sb = Snackbar.make(tBar, "Go Back?", Snackbar.LENGTH_LONG)
                        .setAction("Action text", e -> finish());
                sb.show();
                break;
        }
        return true;
    }
}