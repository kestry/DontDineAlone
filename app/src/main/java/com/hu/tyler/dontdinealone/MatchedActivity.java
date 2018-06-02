package com.hu.tyler.dontdinealone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.firebase.firestore.CollectionReference;
import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.data.model.Chat;
import com.hu.tyler.dontdinealone.data.model.Collections;
import com.hu.tyler.dontdinealone.domain.NotificationService;
import com.hu.tyler.dontdinealone.net.Session;
import com.hu.tyler.dontdinealone.net.Writer;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Locale;


public class MatchedActivity extends AppCompatActivity {

    private Collections collections = Collections.getInstance();

    private Intent notificationService;

    Button sendMessage;
    EditText messageBoard;
    TextView messages;
    Button leaveButton; //button to go back to Lobby Activity

    // Lifecycle Methods -------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Session.setActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched);
        Intent intent = getIntent();
        messages = findViewById(R.id.matchingChatting);
        notificationService = new Intent(this, NotificationService.class);

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        String date = s.format(new Date());


        messageBoard = findViewById(R.id.matchingActivityEditText);
        messageBoard.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    sendMessage();
                    return true;
                }
                return false;
            }
        });

        leaveButton = findViewById(R.id.matching_leave);
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaveMatching();
            }
        });

        sendMessage = findViewById(R.id.matchingAcitiySendButton);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void sendMessage()
    {
        String str = messageBoard.getText().toString();
        Writer w = new Writer((short) 0x04);
        w.writeStr(str);
        Session.getCon().send(w);
        messageBoard.getText().clear();
    }

    public void postMessage(String message)
    {
        messages.append(message + "\r\n");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        //Starts the NotificationService in the background.
        //notificationService.putExtra(NotificationService.NOTIFICATION_TYPE, NotificationService.MESSAGE_NOTIFICATION);
        //notificationService.putExtra(NotificationService.CHAT_ID, docID);
        startService(notificationService);

    }

    @Override
    public void onResume(){
        super.onResume();
        //stopService(notificationService);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // Presenter Methods -------------------------------------------------------------------------

    public void leaveMatching()
    {
        Writer w = new Writer((short)0x05);
        w.write8((byte)1);
        Session.getCon().send(w);
        finish();
        startActivity(new Intent(getApplicationContext(), LobbyActivity.class));
    }
}
