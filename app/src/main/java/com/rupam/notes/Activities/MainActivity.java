package com.rupam.notes.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.rupam.notes.R;

public class MainActivity extends AppCompatActivity {

    private EditText emailIn;
    private EditText passIn;
    private Button signInBtn;
    private Button createAccBtn;
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        emailIn = findViewById(R.id.emailInID);
        passIn = findViewById(R.id.passInID);
        signInBtn = findViewById(R.id.signinBtnID);
        createAccBtn = findViewById(R.id.createAccBtnID);

        mAuth = FirebaseAuth.getInstance();

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(emailIn.getText()) &&! TextUtils.isEmpty(passIn.getText())){

                    String getEmail = emailIn.getText().toString();
                    String getPass = passIn.getText().toString();
//                    Checks if the textBoxes are not empty and then passes the values to signIn method
                    signIn(getEmail, getPass);
                }
            }
        });

        //Go to the createAcc activity when createAcc button is clicked
        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateAccActivity.class));
            }
        });
    }

    //gets the current user and see if it is null (i.e signed in) or not
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //User is already Signed in
            Toast.makeText(this, "User Signed In", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, NotesListActivity.class));
            finish();
        }else{
            //User is not signed in
            Toast.makeText(this, "Please Sign In First", Toast.LENGTH_SHORT).show();
        }
    }

    //The SignIn method to signIn with email and pass
    public void signIn(String email, String pass){
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, NotesListActivity.class));
                            finish();
                        }else{
                            Toast.makeText(MainActivity.this, "Sign-In Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
