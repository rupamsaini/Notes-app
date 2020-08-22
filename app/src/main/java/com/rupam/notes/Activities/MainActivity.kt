package com.rupam.notes.Activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.rupam.notes.R

class MainActivity : AppCompatActivity() {
    lateinit var emailIn: EditText
    lateinit var passIn: EditText
    lateinit var signInBtn: Button
    lateinit var createAccBtn: Button
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.hide()

        emailIn = findViewById(R.id.emailInID)
        passIn = findViewById(R.id.passInID)
        signInBtn = findViewById(R.id.signinBtnID)
        createAccBtn = findViewById(R.id.createAccBtnID)
        mAuth = FirebaseAuth.getInstance()

        signInBtn.setOnClickListener {
            if (!TextUtils.isEmpty(emailIn.getText()) && !TextUtils.isEmpty(passIn.getText())) {
                val getEmail = emailIn.getText().toString()
                val getPass = passIn.getText().toString()
                //                    Checks if the textBoxes are not empty and then passes the values to signIn method
                signIn(getEmail, getPass)
            }
        }

        //Go to the createAcc activity when createAcc button is clicked
        createAccBtn.setOnClickListener(View.OnClickListener { startActivity(Intent(this@MainActivity, CreateAccActivity::class.java)) })
    }

    //gets the current user and see if it is null (i.e signed in) or not
    override fun onStart() {
        super.onStart()
        val currentUser = mAuth!!.currentUser
        if (currentUser != null) {
            //User is already Signed in
            Toast.makeText(this, "User Signed In", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@MainActivity, NotesListActivity::class.java))
            finish()
        } else {
            //User is not signed in
            Toast.makeText(this, "Please Sign In First", Toast.LENGTH_SHORT).show()
        }
    }

    //The SignIn method to signIn with email and pass
    fun signIn(email: String?, pass: String?) {
        mAuth.signInWithEmailAndPassword(email!!, pass!!)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Signed In Successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@MainActivity, NotesListActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@MainActivity, "Sign-In Failed", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}