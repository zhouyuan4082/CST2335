package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.androidlabs.MyDatabaseOpenHelper.*;

public class ChatRoomActivity extends AppCompatActivity {
    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final String ITEM_SEND_RECEIVE = "SENDREC";
    public static final int EMPTY_ACTIVITY = 345;
    ArrayList<Message> objects = new ArrayList<>();

    BaseAdapter myAdapter;
    SQLiteDatabase db;
    Cursor result;
    ListView theList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        theList = findViewById(R.id.theList);
        boolean isTablet = findViewById(R.id.fragmentLocation) != null;
        theList.setAdapter( myAdapter = new MyListAdapter() );
        theList.setOnItemClickListener( ( parent,  view,  position,  id) ->{

            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED,objects.get(position).msg /*source.get(position)*/ );
            dataToPass.putInt(ITEM_POSITION, position);
            dataToPass.putLong(ITEM_ID, id);
            if (objects.get(position).sendOrReceive) {
                dataToPass.putString(ITEM_SEND_RECEIVE,"Receive");
            } else {
                dataToPass.putString(ITEM_SEND_RECEIVE,"Send");
            }

            if(isTablet)
            {
                DetailFragment dFragment = new DetailFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("AnyName") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
            }
        });
//get a database
        MyDatabaseOpenHelper dbOpener= new MyDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();
        //db.execSQL("delete from "+ TABLE_NAME);
        //query all the results from the database
        String[] colums = {COL_ID, COL_MESSAGE, COL_ISSEND};
        result = db.query(false, TABLE_NAME, colums,
                null,null,null, null,null,null);

        //find the column index
        int nameColIndex = result.getColumnIndex(COL_MESSAGE);
        int idConInex = result.getColumnIndex(COL_ID);
        int isSentConIndex = result.getColumnIndex(COL_ISSEND);

        //iterate over the results, return true if there is a next item;
        while (result.moveToNext()){
            String name = result.getString(nameColIndex);
            long id = result.getLong(idConInex);
            boolean isSent = result.getInt(isSentConIndex)>0;
            objects.add(new Message(name, isSent, id));
        }
        Button receiveButton = findViewById(R.id.receiveButton);
        receiveButton.setOnClickListener( clik -> {
            TextView msgText = findViewById(R.id.msgInputtext);
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_MESSAGE , msgText.getText().toString());
            contentValues.put(COL_ISSEND, true);
            long newId = db.insert(TABLE_NAME,null,contentValues);
            Message message = new Message(msgText.getText().toString(),true, newId);
            this.printCursor();
            objects.add(message);
            myAdapter.notifyDataSetChanged(); //update yourself
            msgText.setText("");
        } );

        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener( clik -> {
            TextView msgText = findViewById(R.id.msgInputtext);
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_MESSAGE , msgText.getText().toString());
            contentValues.put(COL_ISSEND, false);
            long newId = db.insert(TABLE_NAME,null,contentValues);
            Message message = new Message(msgText.getText().toString(),false,newId);
            this.printCursor();
            objects.add(message);
            myAdapter.notifyDataSetChanged(); //update yourself
            msgText.setText("");
        } );


    }

    private class MyListAdapter extends BaseAdapter{

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
            } else {
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

    public void printCursor() {
        Log.e("MyDatabaseFile version:", db.getVersion() + "");
        Log.e("Number of columns:", result.getColumnCount() + "");
        Log.e("Name of the columns:", Arrays.toString(result.getColumnNames()));
        Log.e("Number of results", result.getCount() + "");
        Log.e("Each row of results :", "");
        result.moveToFirst();

        //find the column index

        for (int i = 0; i < result.getCount(); i++) {
            while (!result.isAfterLast()) {

                Log.e("id", result.getString(result.getColumnIndex(COL_ID)) + "");
                Log.e("isSent", result.getString(result.getColumnIndex(COL_ISSEND)) + "");
                Log.e("message", result.getString(result.getColumnIndex(COL_MESSAGE)) + "");

                result.moveToNext();
            }
        }
    }

    //This function only gets called on the phone. The tablet never goes to a new activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EMPTY_ACTIVITY)
        {
            if(resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long id = data.getLongExtra(ITEM_ID, 0);
                deleteMessageId((int)id);
            }
        }
    }
    public void deleteMessageId(int id)
    {
        Log.i("Delete this message:" , " id="+id);
        objects.remove(id);
        //source.remove(id);
        myAdapter.notifyDataSetChanged();
    }
}
