package com.example.quazi.chatdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Messaging extends AppCompatActivity implements View.OnClickListener{

    private Button addRoom;
    private TextView signOut;
    private EditText roomName;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList<>();

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        signOut = (TextView)findViewById(R.id.signOut);
        addRoom = (Button) findViewById(R.id.addRoom);
        roomName = (EditText) findViewById(R.id.roomName);
        listView = (ListView) findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_rooms);
        listView.setAdapter(arrayAdapter);

        signOut.setOnClickListener(Messaging.this);
        addRoom.setOnClickListener(Messaging.this);
        setTitle("Rooms Available");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), Chat.class);
                i.putExtra("roomName", ((TextView)view).getText().toString());
                i.putExtra("userEmail", firebaseAuth.getCurrentUser().getEmail().toString());
                startActivity(i);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();


        availableRoom();


    }//onCreate

    private void availableRoom() {
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                    list_of_rooms.clear();
                    list_of_rooms.addAll(set);
                    arrayAdapter.notifyDataSetChanged();
                }//hasNext
            }//Ondatachange

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }//databaseError
        });//EventListener
    }//available room

    @Override
    public void onClick(View v) {
        if(v==signOut){
            firebaseAuth.signOut();
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);

        }//if

        if(v==addRoom){
            if(!((roomName.getText().toString()).matches(""))) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(roomName.getText().toString(), "");
                root.updateChildren(map);
                roomName.setText("");
            }

        }//if


    }//onClick

}//class
