package com.rupam.notes.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.rupam.notes.Data.NoteAdapter
import com.rupam.notes.Model.Note
import com.rupam.notes.R
import java.util.*

class NotesListActivity : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var currentUser: FirebaseUser
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteList: MutableList<Note?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Your Notes"

        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser!!
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Users").child(currentUser.uid)
        databaseReference.keepSynced(true)
        noteList = ArrayList()
        recyclerView = findViewById(R.id.recyclerViewId)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.setLayoutManager(linearLayoutManager)
        recyclerView.setItemAnimator(DefaultItemAnimator())
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        noteAdapter = NoteAdapter(this@NotesListActivity, noteList)
        ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView)
        recyclerView.adapter = noteAdapter
    }

    var itemTouch: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
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
            Toast.makeText(this@NotesListActivity, "Note Deleted", Toast.LENGTH_SHORT).show()

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addNew -> startActivity(Intent(this@NotesListActivity, AddNewNoteActivity::class.java))
            R.id.menu_signout -> {
                firebaseAuth.signOut()
                startActivity(Intent(this@NotesListActivity, MainActivity::class.java))
                Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.sign_out_menu, menu)
        return super.onCreateOptionsMenu(menu)
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
    } //    @Override
    //    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
    //
    //        if (viewHolder instanceof NoteAdapter.ViewHolder) {
    //            // get the removed item name to display it in snack bar
    //            String name = noteList.get(viewHolder.getAdapterPosition()).getName();
    //
    //            // backup of removed item for undo purpose
    //            final Note deletedItem = noteList.get(viewHolder.getAdapterPosition());
    //            final int deletedIndex = viewHolder.getAdapterPosition();
    //
    //            // remove the item from recycler view
    //            noteAdapter.removeItem(viewHolder.getAdapterPosition());
    //            Note selectedItem = noteList.get(viewHolder.getAdapterPosition());
    //            String selectedKey = selectedItem.getKey();
    //
    //            databaseReference.child(selectedKey).removeValue();
    //            noteList.remove(viewHolder.getAdapterPosition());
    //            noteAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
    //
    //            // showing snack bar with Undo option
    //            Snackbar snackbar = Snackbar
    //                    .make(coordinatorLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
    //            snackbar.setAction("UNDO", new View.OnClickListener() {
    //                @Override
    //                public void onClick(View view) {
    //
    //                    // undo is selected, restore the deleted item
    //                    noteAdapter.restoreItem(deletedItem, deletedIndex);
    //                }
    //            });
    //            snackbar.setActionTextColor(Color.YELLOW);
    //            snackbar.show();
    //        }
    //}
}