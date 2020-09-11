package com.rupam.notes.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.rupam.notes.Constants
import com.rupam.notes.Data.NoteAdapter
import com.rupam.notes.Model.Note
import com.rupam.notes.R
import kotlinx.android.synthetic.main.content_notes_list.*
import java.util.ArrayList


class NotesListFragment : Fragment() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var currentUser: FirebaseUser
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteList: MutableList<Note?>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser!!
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child(Constants.USERS).child(currentUser.uid)
        databaseReference.keepSynced(true)

        noteList = ArrayList()
        recyclerView = recyclerViewId
        recyclerView.setHasFixedSize(true)

        val linearLayoutManager = GridLayoutManager(context, 2)
//        linearLayoutManager.reverseLayout = true
//        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
//        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        noteAdapter = NoteAdapter(requireContext(), noteList)
        ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView)
        recyclerView.adapter = noteAdapter
    }

    private var itemTouch: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val deletedItem = noteList[viewHolder.adapterPosition]
            //            final int deletedIndex = viewHolder.getAdapterPosition();
            val selectedKey = deletedItem!!.key
            if (selectedKey != null) {
                databaseReference.child(selectedKey).removeValue()
            }
            noteList.removeAt(viewHolder.adapterPosition)
            noteAdapter.notifyDataSetChanged()
            Toast.makeText(context, "Note Deleted", Toast.LENGTH_SHORT).show()

//            Snackbar snackbar = Snackbar
//                    .make(coordinatorLayout, " removed from cart!", Snackbar.LENGTH_LONG);
//            snackbar.setAction("UNDO", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    // undo is selected, restore the deleted item
////                    noteAdapter.restoreItem(deletedItem, deletedIndex);
//
//                    noteList.add(deletedIndex, deletedItem);
//                }
//            });
//            snackbar.setActionTextColor(Color.YELLOW);
//            snackbar.show();
        }
    }


    override fun onStart() {
        super.onStart()
        noteList.clear()
        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                //Retrieve dataSnapshot in form of Note class
                val note = dataSnapshot.getValue(Note::class.java)
                note!!.key = dataSnapshot.key
                noteList.add(note)
                noteAdapter.notifyDataSetChanged()
                Log.d("Getting key of item", dataSnapshot.key)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


}