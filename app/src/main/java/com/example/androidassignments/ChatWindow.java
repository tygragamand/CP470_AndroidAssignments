package com.example.androidassignments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    //variables
    private Button sendButton;
    private EditText textArea;
    private ListView listArea;
    private ArrayList<String> storeChatArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        listArea = findViewById(R.id.id_listView);
        textArea = findViewById(R.id.id_editText);
        sendButton = findViewById(R.id.sendButton);

        //in this case, “this” is the ChatWindow, which is-A Context object
        final ChatAdapter messageAdapter = new ChatAdapter( this );
        listArea.setAdapter(messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chat = textArea.getText().toString();

                if (chat.isEmpty()) {
                    return;
                }

                storeChatArray.add(chat);
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
                textArea.setText("");
            }
        });
    }

    private class ChatAdapter extends ArrayAdapter<String>{

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return storeChatArray.size();
        }

        public String getItem(int position){
            return storeChatArray.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null ;

            if(position%2 == 0){
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            }
            else{
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position)); // get the string at position

            return result;
        }
    }
}