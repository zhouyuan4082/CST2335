package com.example.androidlabs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatRoomActivity extends AppCompatActivity {

    ArrayList<Message> objects = new ArrayList<>();

    BaseAdapter myAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        ListView theList = findViewById(R.id.theList);
        theList.setAdapter( myAdapter = new MyListAdapter() );
        theList.setOnItemClickListener( ( parent,  view,  position,  id) ->{
            Log.i("CLicked", "You clicked item:" + position);
        });

        Button receiveButton = findViewById(R.id.receiveButton);
        receiveButton.setOnClickListener( clik -> {
            TextView msgText = findViewById(R.id.msgInputtext);
            Message message = new Message(msgText.getText().toString(),true);
            objects.add(message);
            msgText.setText("");
            myAdapter.notifyDataSetChanged(); //update yourself
        } );

        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener( clik -> {
            TextView msgText = findViewById(R.id.msgInputtext);
            Message message = new Message(msgText.getText().toString(),false);
            objects.add(message);
            msgText.setText("");
            myAdapter.notifyDataSetChanged(); //update yourself
        } );
    }

    private class MyListAdapter extends BaseAdapter {

        public int getCount() {  return objects.size();  } //This function tells how many objects to show

        public Message getItem(int position) { return objects.get(position);  }  //This returns the string at position p

        public long getItemId(int p) { return p; } //This returns the database id of the item at position p

        public View getView(int p, View recycled, ViewGroup parent)
        {

            LayoutInflater inflater = getLayoutInflater();
            View thisRow = recycled;

            if(thisRow == null) {
                if (getItem(p).sendOrReceive) {
                    thisRow = inflater.inflate(R.layout.receive_message, null);
                    TextView itemField = thisRow.findViewById(R.id.receiveTextView);
                    itemField.setText(getItem(p).msg);
                } else {
                    thisRow = inflater.inflate(R.layout.send_message, null);
                    TextView itemField = thisRow.findViewById(R.id.sendTextView);
                    itemField.setText(getItem(p).msg);
                }
            }
            return thisRow;
        }
    }

}
