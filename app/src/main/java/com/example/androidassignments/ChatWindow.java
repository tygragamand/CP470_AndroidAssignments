package com.example.androidassignments;

import static com.example.androidassignments.ChatDatabaseHelper.KEY_ID;
import static com.example.androidassignments.ChatDatabaseHelper.KEY_MESSAGE;
import static com.example.androidassignments.ChatDatabaseHelper.TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "ChatWindow";
    //variables
    private Button sendButton;
    private EditText textArea;
    private ListView listArea;
    private ArrayList<String> storeChatArray = new ArrayList<>();
    private ChatDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        dbHelper = new ChatDatabaseHelper(this);

        listArea = findViewById(R.id.id_listView);
        textArea = findViewById(R.id.id_editText);
        sendButton = findViewById(R.id.sendButton);

        //in this case, “this” is the ChatWindow, which is-A Context object
        final ChatAdapter messageAdapter = new ChatAdapter(this);
        listArea.setAdapter(messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chat = textArea.getText().toString();

                if (chat.isEmpty()) {
                    return;
                }
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues cv = new ContentValues(1);
                cv.put(KEY_MESSAGE, chat);
                db.insert(TABLE_NAME, null, cv);
                db.close();

                storeChatArray.add(chat);
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
                textArea.setText("");
            }
        });

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_MESSAGE}, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String chatMessage = cursor.getString(cursor.getColumnIndex(KEY_MESSAGE));
            storeChatArray.add(chatMessage);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + chatMessage);

            //Also, print an information message about the Cursor:
            Log.i(ACTIVITY_NAME, "Cursor’s column count =" + cursor.getColumnCount());
            cursor.moveToNext();
        }
        messageAdapter.notifyDataSetChanged();

        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, cursor.getColumnName(i));
        }
        db.close();
    }

    protected void onDestroy(){
        super.onDestroy();
        dbHelper.close();
    }

    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return storeChatArray.size();
        }

        public String getItem(int position) {
            return storeChatArray.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;

            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText(getItem(position)); // get the string at position

            return result;
        }
    }
}