package com.rupam.notes.Fragments

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
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.rupam.notes.R
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {

    lateinit var emailIn: EditText
    lateinit var passIn: EditText
    lateinit var signInBtn: Button
    lateinit var createAccBtn: Button
    lateinit var mAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailIn = emailInID
        passIn = passInID
        signInBtn = signinBtnID
        createAccBtn = createAccBtnID
        mAuth = FirebaseAuth.getInstance()

        signInBtn.setOnClickListener {
            if (!TextUtils.isEmpty(emailIn.getText()) && !TextUtils.isEmpty(passIn.text)) {
                val getEmail = emailIn.text.toString()
                val getPass = passIn.text.toString()

                progressBarLayout.visibility = View.VISIBLE

                //Checks if the textBoxes are not empty and then passes the values to signIn method
                signIn(getEmail, getPass)
            }
        }

        //Go to the createAcc activity when createAcc button is clicked
//        createAccBtn.setOnClickListener(View.OnClickListener { startActivity(Intent(this@MainActivity, CreateAccActivity::class.java)) })
    }


    //gets the current user and see if it is null (i.e signed in) or not
    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            //User is already Signed in
            Toast.makeText(context, "User Signed In", Toast.LENGTH_SHORT).show()
            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_notesListFragment)

        } else {
            //User is not signed in
            Toast.makeText(context, "Please Sign In First", Toast.LENGTH_SHORT).show()
        }
    }

    //The SignIn method to signIn with email and pass
    private fun signIn(email: String?, pass: String?) {
        mAuth.signInWithEmailAndPassword(email!!, pass!!)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_notesListFragment)
                        progressBarLayout.visibility = View.GONE
                    } else {
                        Toast.makeText(context, "Sign-In Failed", Toast.LENGTH_SHORT).show()
                    }
                }
    }


}