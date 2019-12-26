package com.rupam.notes.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rupam.notes.R;

public class CreateAccActivity extends AppCompatActivity {

    private EditText fullName;
    private EditText email;
    private EditText password;
    private Button createBtn;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);

        getSupportActionBar().hide();


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("Users");

        progressDialog = new ProgressDialog(this);

        fullName = findViewById(R.id.fullNameID);
        email = findViewById(R.id.emailCreateID);
        password = findViewById(R.id.passCreateID);
        createBtn = findViewById(R.id.createAccBtnID);



//        TODO: create Account by createAccount method
//        TODO: Push the details to the db

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();
            }
        });

    }

    private void createNewAccount() {

//        final String getName = fullName.getText().toString();
        String getEmail = email.getText().toString();
        String getPass = password.getText().toString();

        if(!TextUtils.isEmpty(fullName.getText()) && !TextUtils.isEmpty(email.getText()) &&! TextUtils.isEmpty(password.getText())){

            progressDialog.setMessage("Creating Account");
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(getEmail, getPass)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if(authResult != null){

                            String userId = mAuth.getCurrentUser().getUid();
                            mDatabaseReference.child(userId);
//                            currentUserDB.child("fullName").setValue(getName);

                            progressDialog.dismiss();

                            //Send user to NotesList

                                Intent intent = new Intent(CreateAccActivity.this, NotesListActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }
                    });
        }
    }

//    TODO: Remove this method if create account has problems
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(CreateAccActivity.this, NotesListActivity.class));
            finish();
            Toast.makeText(this, "You are already Signed in", Toast.LENGTH_SHORT).show();
        }
    }
}
