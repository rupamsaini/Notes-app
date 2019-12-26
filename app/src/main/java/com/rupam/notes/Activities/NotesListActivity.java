package com.rupam.notes.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rupam.notes.Data.NoteAdapter;
import com.rupam.notes.Model.Note;
import com.rupam.notes.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NotesListActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Notes");


        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Users").child(currentUser.getUid());
        databaseReference.keepSynced(true);

        noteList = new ArrayList<>();


        recyclerView = findViewById(R.id.recyclerViewId);
        recyclerView.setHasFixedSize(true);
      LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
      linearLayoutManager.setReverseLayout(true);
       linearLayoutManager.setStackFromEnd(true);
      recyclerView.setLayoutManager(linearLayoutManager);



        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        noteAdapter = new NoteAdapter(NotesListActivity.this, noteList);
        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(noteAdapter);
    }

    ItemTouchHelper.SimpleCallback itemTouch = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final Note deletedItem = noteList.get(viewHolder.getAdapterPosition());
//            final int deletedIndex = viewHolder.getAdapterPosition();

            String selectedKey = deletedItem.getKey();

            databaseReference.child(selectedKey).removeValue();
            noteList.remove(viewHolder.getAdapterPosition());
            noteAdapter.notifyDataSetChanged();

            Toast.makeText(NotesListActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();

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
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

       switch (item.getItemId()){

           case R.id.addNew:
               startActivity(new Intent(NotesListActivity.this, AddNewNoteActivity.class));
               break;

           case R.id.menu_signout:
               firebaseAuth.signOut();
               startActivity(new Intent(NotesListActivity.this, MainActivity.class));
               Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();

       }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.sign_out_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();

        noteList.clear();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Retrieve dataSnapshot in form of Note class

                Note note = dataSnapshot.getValue(Note.class);
                note.setKey(dataSnapshot.getKey());
                noteList.add(note);
                noteAdapter.notifyDataSetChanged();
                Log.d("Getting key of item", dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    @Override
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

