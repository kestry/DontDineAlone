package com.hu.tyler.dontdinealone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class MatchingActivty extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference MatchUsers = db.collection("Matched"); // User to extend to chatty Collection where users can chat with one an other
    private CollectionReference DeleteMe = db.collection("Matched"); // reference for deleting onDestroy

    Chat text;
    String docID,displayName; //Just strings for
    TextView key;

    Button leaveButton; //button to go back to Lobby Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_activty);
        Intent intent = getIntent();
        docID = intent.getStringExtra("key"); //if it's a string you stored.
        key = findViewById(R.id.matchActivityText);
        key.setText(docID);
        displayName = intent.getStringExtra("name");
        text = new Chat(displayName, "Here!", 10);
        MatchUsers = MatchUsers.document(docID).collection("Chatty");

        Chat x = new Chat(displayName, "Eat with me!", 11);
        MatchUsers.add(x);

        leaveButton = findViewById(R.id.matching_leave);
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaveMatching();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(db != null && MatchUsers != null)
        MatchUsers.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e!=null)
                    return;

                Log.d("XXX", "Message fetch executed!");

                    String data = "Data inside:\n\n";
                    List<DocumentSnapshot> x= documentSnapshots.getDocuments();
                    for(int i = 0; i < x.size(); i++)
                    {
                        Log.d("XXX", "For Loop On Start!");
                        Chat j = x.get(i).toObject(Chat.class);
                        Log.d("XXX", "for loop users"+i + ": " + j.getTitle());
                        j.setDocumentId(x.get(i).getId());

                        data += j.getTitle() + ": " + j.getMessage() + "\n";
                    }
                    TextView onlineCount = findViewById(R.id.matchingChatting);
                    onlineCount.setText(data);
                }

        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // TODO:Figure out a way to trigger if only 1 person is in Matching Activity.
//        DocumentReference ref =  DeleteMe.document(docID);
//        ref.delete();

    }

    public void leaveMatching()
    {
        finish();
        startActivity(new Intent(getApplicationContext(), LobbyActivity.class));
    }
}
