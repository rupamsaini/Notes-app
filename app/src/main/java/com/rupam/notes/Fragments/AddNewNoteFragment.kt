package com.rupam.notes.Fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rupam.notes.R
import kotlinx.android.synthetic.main.fragment_add_new_note.*
import java.util.HashMap


class AddNewNoteFragment : Fragment() {

    private lateinit var addTitle: EditText
    private lateinit var addBody: EditText
    private lateinit var addNoteButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var currentUser: FirebaseUser
    private lateinit var addNew: MenuItem
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser!!

//        Add Note inside current user's id in db
        databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(currentUser.uid)
        addTitle = addTitleET
        addBody = addBodyET
        addNoteButton = addNoteBtn
        progressDialog = ProgressDialog(context)
        addNoteBtn.setOnClickListener(View.OnClickListener { //Posting to db
            AddNote()
        })
    }

    private fun AddNote() {
        progressDialog.setMessage("Adding Note...")
        progressDialog.show()
        val mTitleVal = addTitle.text.toString().trim { it <= ' ' }
        val mBodyVal = addBody.text.toString().trim { it <= ' ' }
        if (!TextUtils.isEmpty(addTitle.text) && !TextUtils.isEmpty(addBody.text)) {
            val addNote = databaseReference.push()
            val newNote: MutableMap<String, String> = HashMap()
            newNote["title"] = mTitleVal
            newNote["noteBody"] = mBodyVal
            newNote["dateAdded"] = System.currentTimeMillis().toString()
            newNote["key"] = currentUser.uid
            addNote.setValue(newNote)
            progressDialog.dismiss()
//            val intent = Intent(context, NotesListActivity::class.java)
            //            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent)
            Toast.makeText(context, "Note Saved", Toast.LENGTH_SHORT).show()
        }
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.menu_signout) {
//            firebaseAuth.signOut()
//            startActivity(Intent(this@AddNewNoteActivity, MainActivity::class.java))
//            Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show()
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.sign_out_menu, menu)
//        menu.getItem(1).isVisible = false
//        return super.onCreateOptionsMenu(menu)
//    }

}