package com.rupam.notes.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rupam.notes.R;

import java.util.HashMap;
import java.util.Map;

public class AddNewNoteActivity extends AppCompatActivity {

    private EditText addTitle;
    private EditText addBody;
    private Button addNoteBtn;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private MenuItem addNew;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

//        Add Note inside current user's id in db
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());

        addTitle = findViewById(R.id.addTitleET);
        addBody = findViewById(R.id.addBodyET);
        addNoteBtn = findViewById(R.id.addNoteBtn);
        progressDialog = new ProgressDialog(this);


        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Posting to db
                AddNote();

            }
        });
    }

    private void AddNote() {

        progressDialog.setMessage("Adding Note...");
        progressDialog.show();

        String mTitleVal = addTitle.getText().toString().trim();
        String mBodyVal = addBody.getText().toString().trim();

        if(!TextUtils.isEmpty(addTitle.getText()) &&! TextUtils.isEmpty(addBody.getText())){

            DatabaseReference addNote = databaseReference.push();

            Map<String, String> newNote = new HashMap<>();

            newNote.put("title", mTitleVal);
            newNote.put("noteBody", mBodyVal);
            newNote.put("dateAdded", String.valueOf(java.lang.System.currentTimeMillis()));
            newNote.put("key", currentUser.getUid());

            addNote.setValue(newNote);

            progressDialog.dismiss();

           Intent intent = new Intent(AddNewNoteActivity.this, NotesListActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_signout){
            firebaseAuth.signOut();
            startActivity(new Intent(AddNewNoteActivity.this, MainActivity.class));
            Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.sign_out_menu, menu);
        menu.getItem(1).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }
}
