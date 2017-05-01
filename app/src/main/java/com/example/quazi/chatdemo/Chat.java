package com.example.quazi.chatdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Chat extends AppCompatActivity implements View.OnClickListener {
    private Button send;
    private EditText editTextSend;
    private TextView textViewChat;
    private ScrollView scrollView;

    private String roomName,userEmail;
    private DatabaseReference root;

    private String tempKey;

    private String chatMsg, chatUserEmail;

    private FirebaseAuth firebaseAuth;
    private String textAlign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        send = (Button)findViewById(R.id.send);
        editTextSend = (EditText)findViewById(R.id.editTextSend);
        textViewChat = (TextView)findViewById(R.id.textViewChat);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

       scrollView.post(new Runnable() {
           @Override
           public void run() {
               scrollView.fullScroll(ScrollView.FOCUS_DOWN);
           }
       });



        userEmail = getIntent().getExtras().get("userEmail").toString();
        roomName = getIntent().getExtras().get("roomName").toString();

        setTitle(roomName);
        root = FirebaseDatabase.getInstance().getReference().child(roomName);
        firebaseAuth = FirebaseAuth.getInstance();
        send.setOnClickListener(Chat.this);
        editTextSend.setOnClickListener(Chat.this);

        databaseChanges();

    }//onCreate

    private void databaseChanges() {
        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                appendChatConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                appendChatConversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void appendChatConversation(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();

        while(i.hasNext()){

            String username = (String) ((DataSnapshot)i.next()).getValue();
            chatUserEmail = username.substring(0,username.indexOf('@'));
            chatMsg = (String) ((DataSnapshot)i.next()).getValue();


           // textViewChat.append(chatMsg + "\n" + chatUserEmail+"\n");
//            if (username==firebaseAuth.getCurrentUser().getEmail().toString()) textAlign =

//            final SpannableString text = new SpannableString(Html.fromHtml("<b>" + chatMsg + "</b>" + "<br>"+ "<b>" + "<small>"+ "<i>" + chatUserEmail +"</small>"+ "</b>" + "<br>"));
//            text.setSpan((new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE)),chatMsg.length()+2,chatMsg.length()+ 10 + chatUserEmail.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textViewChat.append(Html.fromHtml("<b>" + chatMsg + "</b>" + "<br>"+ "<b>" + "<small>"+ "<i>" + chatUserEmail +"</small>"+ "</b>" + "<br>"));

            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }//Iterator
    }

    @Override
    public void onClick(View v) {
        if (v==send) {
            if(!((editTextSend.getText().toString()).matches(""))){
            Map<String, Object> map = new HashMap<String, Object>();
            tempKey = root.push().getKey();
            root.updateChildren(map);

            DatabaseReference messageRoot = root.child(tempKey);
            Map<String, Object> map2 = new HashMap<String, Object>();

            map2.put("email", userEmail);
            map2.put("msg", editTextSend.getText().toString());

            messageRoot.updateChildren(map2);
                editTextSend.setText("");
            }//if
        }//if send
        if (v==editTextSend){
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
    }
}//Chat
