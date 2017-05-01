package com.example.quazi.chatdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;


    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignin = (TextView)findViewById(R.id.textViewSignin);


        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
        setTitle("Sign Up Page");

    }

    @Override
    public  void onStart(){
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser !=null){
                Intent i = new Intent(getApplicationContext(),Messaging.class);
                startActivity(i);
            }
    }

    @Override
    public void onClick(View v) {
        if (v == buttonRegister){
            registerUser();

        }//if
        
        if (v == textViewSignin){
            //Go to Signin page
            Intent i = new Intent(getApplicationContext(),SignIn.class);
            startActivity(i);
        }//if
    }//OnClick

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please Enter Email",Toast.LENGTH_SHORT).show();
            return;
        }//if

        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this, "Please Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }//if

        //else

        progressDialog.setMessage("Registering User.....");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            //on sucess
                            Toast.makeText(MainActivity.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(),Messaging.class);
                            startActivity(i);

                        }//if
                        else Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                    }//public void
                });//OnComplete

    }//registerUser
}//OnCreate
