package com.rupam.notes.Fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rupam.notes.Constants
import com.rupam.notes.R
import kotlinx.android.synthetic.main.fragment_create_acc.*


class CreateAccFragment : Fragment() {

    private lateinit var fullName: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var createBtn: Button
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var mAuth: FirebaseAuth


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_acc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.reference.child(Constants.USERS)
        fullName = fullNameID
        email = emailCreateID
        password = passCreateID
        createBtn = createAccBtnID


//        TODO: create Account by createAccount method
//        TODO: Push the details to the db
        createBtn.setOnClickListener {

            progressBarLayout.visibility = View.VISIBLE
            createNewAccount()
        }

    }


    private fun createNewAccount() {

//        final String getName = fullName.getText().toString();
        val getEmail = email.text.toString()
        val getPass = password.text.toString()
        if (!TextUtils.isEmpty(fullName.text) && !TextUtils.isEmpty(email.text) && !TextUtils.isEmpty(password.text)) {

            mAuth.createUserWithEmailAndPassword(getEmail, getPass)
                    .addOnSuccessListener { authResult ->
                        if (authResult != null) {
                            val userId = mAuth.currentUser!!.uid
                            mDatabaseReference.child(userId)
                            //                            currentUserDB.child("fullName").setValue(getName);
                            progressBarLayout.visibility = View.GONE

                        }
                    }
        }
    }

    //    TODO: Remove this method if create account has problems
    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            Toast.makeText(context, "You are already Signed in", Toast.LENGTH_SHORT).show()
        }
    }


}