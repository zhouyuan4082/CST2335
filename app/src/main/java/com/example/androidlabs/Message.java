package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

public class Message extends AppCompatActivity {

    public String msg;
    public boolean sendOrReceive;//0 send, 1 receive
    public long id;
    public Message(String msgContent, boolean seOrRe , long id) {
        msg = msgContent;
        sendOrReceive = seOrRe;
        this.id = id;
    }
}
