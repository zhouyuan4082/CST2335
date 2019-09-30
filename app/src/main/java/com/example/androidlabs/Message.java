package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

public class Message extends AppCompatActivity {

    public String msg;
    public boolean sendOrReceive;//0 send, 1 receive
    public Message(String msgContent, boolean seOrRe) {
        msg = msgContent;
        sendOrReceive = seOrRe;
    }
}
