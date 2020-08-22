package com.rupam.notes.Activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rupam.notes.R

class CreateAccActivity : AppCompatActivity() {
    private lateinit var fullName: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var createBtn: Button
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_acc)

        supportActionBar!!.hide()

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.reference.child("Users")
        progressDialog = ProgressDialog(this)
        fullName = findViewById(R.id.fullNameID)
        email = findViewById(R.id.emailCreateID)
        password = findViewById(R.id.passCreateID)
        createBtn = findViewById(R.id.createAccBtnID)


//        TODO: create Account by createAccount method
//        TODO: Push the details to the db
        createBtn.setOnClickListener(View.OnClickListener { createNewAccount() })
    }

    private fun createNewAccount() {

//        final String getName = fullName.getText().toString();
        val getEmail = email.text.toString()
        val getPass = password.text.toString()
        if (!TextUtils.isEmpty(fullName.text) && !TextUtils.isEmpty(email.text) && !TextUtils.isEmpty(password.text)) {
            progressDialog.setMessage("Creating Account")
            progressDialog.show()
            mAuth.createUserWithEmailAndPassword(getEmail, getPass)
                    .addOnSuccessListener { authResult ->
                        if (authResult != null) {
                            val userId = mAuth.currentUser!!.uid
                            mDatabaseReference.child(userId)
                            //                            currentUserDB.child("fullName").setValue(getName);
                            progressDialog.dismiss()

                            //Send user to NotesList
                            val intent = Intent(this@CreateAccActivity, NotesListActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                        }
                    }
        }
    }

    //    TODO: Remove this method if create account has problems
    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this@CreateAccActivity, NotesListActivity::class.java))
            finish()
            Toast.makeText(this, "You are already Signed in", Toast.LENGTH_SHORT).show()
        }
    }
}