package com.example.quazi.chatdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignUp;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        editTextEmail = (EditText) findViewById(R.id.editTextEmailSignIn);
        editTextPassword = (EditText) findViewById(R.id.editTextPasswordSignIn);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);


        progressDialog = new ProgressDialog(SignIn.this);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonSignIn.setOnClickListener(SignIn.this);
        textViewSignUp.setOnClickListener(SignIn.this);

        setTitle("Sign In Page");
    }//onCreate

    @Override
    public void onClick(View v) {
        if(v==buttonSignIn){
            signIn();
        }
        if(v==textViewSignUp){
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
        }
    }


    private void signIn() {
        progressDialog.setMessage("Signing In");
        progressDialog.show();

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent i = new Intent(getApplicationContext(), Messaging.class);
                            startActivity(i);
                        }
                        else  Toast.makeText(getApplicationContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                });

    }

}//Sign - Class
