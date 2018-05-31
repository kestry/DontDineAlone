package com.hu.tyler.dontdinealone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hu.tyler.dontdinealone.data.model.Chat;
import com.hu.tyler.dontdinealone.data.model.Collections;
import com.hu.tyler.dontdinealone.domain.NotificationService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MatchedActivity extends AppCompatActivity {

    private Collections collections = Collections.getInstance();
    // User to extend to chatty Collection where users can chat with one an other
//    private CollectionReference MatchUsers = db.collection("Matched");
    private CollectionReference MatchUsers = collections.getMatchedCRef();
    // reference for deleting onDestroy
    private CollectionReference DeleteMe = collections.getMatchedCRef();

    private Intent notificationService;

    Button sendMessage;
    Chat text , x;
    String docID,displayName; //Just strings for
    String chatDocID; // delete this later
//    DocumentReference chatRef;
    int textCounter = 0;
    TextView key;
    EditText messageBoard;
    Boolean addtoCounter = true;
    TextView messages;
    Button leaveButton; //button to go back to Lobby Activity

    // Lifecycle Methods -------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched);
        Intent intent = getIntent();
        docID = intent.getStringExtra("key");
        key = findViewById(R.id.matchActivityText);
        key.setText(docID);
        messages = findViewById(R.id.matchingChatting);
        notificationService = new Intent(this, NotificationService.class);

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        String date = s.format(new Date());
        displayName = intent.getStringExtra("name");

        text = new Chat(displayName, "Here!", 0,date);
        MatchUsers = MatchUsers.document(docID).collection("Chatty");

        x = new Chat(displayName, "Eat with me!", 0, date);
        MatchUsers.add(x);

//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                chatDocID = documentReference.getId().toString();
//                chatRef = MatchUsers.document(chatDocID);
//                Log.d("XXX", "ID for Chat " +chatDocID);
//
//            }
//        });

        messageBoard = findViewById(R.id.matchingActivityEditText);
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

                String text = messageBoard.getText().toString();
                textCounter++;
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
                String date = s.format(new Date());
                x = new Chat(displayName, text, textCounter, date);
                addtoCounter = false;
                MatchUsers.add(x);
                messageBoard.setText("");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(MatchUsers != null)
        MatchUsers.orderBy("priority").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e!=null)
                    return;
                String data = "";
                if(addtoCounter){
                    textCounter++;
                }else{
                    addtoCounter = true;
                }
                Log.d("XXX", "Message fetch executed!");

                    List<DocumentSnapshot> x= documentSnapshots.getDocuments();
                    for(int i = 0; i < x.size(); i++)
                    {
                        Log.d("XXX", "For Loop On Start!");

                            Chat j = x.get(i).toObject(Chat.class);
                            j.setDocumentId(x.get(i).getId());
                            Log.d("XXX", "for loop users"+i + ": " + j.getTitle());
                            Log.d("XXX", "Message fetch executed!");
                            data += j.getTitle() + "(" + j.getTime() +")"+": " + j.getMessage() + "\n";

                    }
                    messages.setText(data);
                }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        //Starts the NotificationService in the background.
        notificationService.putExtra(NotificationService.NOTIFICATION_TYPE, NotificationService.MESSAGE_NOTIFICATION);
        notificationService.putExtra(NotificationService.CHAT_ID, docID);
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

        // TODO:Figure out a way to trigger if only 1 person is in Matching Activity.
//        DocumentReference ref =  DeleteMe.document(docID);
//        ref.delete();

    }

    // Presenter Methods -------------------------------------------------------------------------

    public void leaveMatching()
    {
        textCounter++;
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        String date = s.format(new Date());
        x = new Chat(displayName, "GoodBye!\n\n" + displayName + "Has Left the Room", textCounter, date);

        addtoCounter = false;
        MatchUsers.add(x);
        messageBoard.setText("");
        finish();
        startActivity(new Intent(getApplicationContext(), LobbyActivity.class));
    }
}
