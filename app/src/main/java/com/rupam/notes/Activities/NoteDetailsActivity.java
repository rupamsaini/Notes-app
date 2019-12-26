package com.rupam.notes.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rupam.notes.R;

import java.util.HashMap;
import java.util.Map;

public class NoteDetailsActivity extends AppCompatActivity {

    private TextView title;
    private TextView body;
    private Button editBtn;
    private Button dltBtn;
    private Bundle extras;

    private AlertDialog alertDialog;
    private AlertDialog.Builder dialogBuilder;
    private EditText edit_title;
    private EditText edit_body;
    private Button save_note;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private String getTitle;
    private String getBody;
    private String key;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("Users").child(currentUser.getUid());

        extras = getIntent().getExtras();

        title = findViewById(R.id.titleDET);
        body = findViewById(R.id.bodyDET);
        editBtn = findViewById(R.id.editBtn);
        dltBtn = findViewById(R.id.dltBtn);
        progressDialog = new ProgressDialog(this);


        getTitle = extras.getString("title");
        getBody = extras.getString("body");

        title.setText(getTitle);
        body.setText(getBody);

        body.setMovementMethod(new ScrollingMovementMethod());

//        Getting key to determine which Unique key to be delete from FB
        key = extras.getString("userKey");

//       Implement modification button
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(NoteDetailsActivity.this, key, Toast.LENGTH_SHORT).show();
                createPopup();
            }
        });

        dltBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.child(key).removeValue();
                Intent goBack = new Intent(NoteDetailsActivity.this, NotesListActivity.class);
                goBack.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goBack);
                Toast.makeText(NoteDetailsActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createPopup() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.modify_note, null);

        edit_title = view.findViewById(R.id.title_mod);
        edit_body = view.findViewById(R.id.body_mod);
        save_note = view.findViewById(R.id.save_mod);

        dialogBuilder.setView(view);
        alertDialog = dialogBuilder.create();

        edit_title.setText(getTitle);
        edit_body.setText(getBody);

        alertDialog.show();

        save_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNote();
            }
        });
    }

    private void AddNote() {

        progressDialog.setMessage("Adding Note...");
        progressDialog.show();

        String mTitleVal = edit_title.getText().toString().trim();
        String mBodyVal = edit_body.getText().toString().trim();

        if (!TextUtils.isEmpty(edit_title.getText()) && !TextUtils.isEmpty(edit_body.getText())) {

//            DatabaseReference addNote = mRef.child(key).push();

            Map<String, Object> newNote = new HashMap<>();

            newNote.put("title", mTitleVal);
            newNote.put("noteBody", mBodyVal);
//            newNote.put("dateAdded", String.valueOf(java.lang.System.currentTimeMillis()));
            newNote.put("key", currentUser.getUid());

            mRef.child(key).updateChildren(newNote);

            progressDialog.dismiss();

            startActivity(new Intent(NoteDetailsActivity.this, NotesListActivity.class));
            finish();
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        }

    }

}
