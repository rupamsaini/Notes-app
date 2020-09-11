package com.rupam.notes.Activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.rupam.notes.R

class MainActivity : AppCompatActivity() {

    private lateinit var navController:NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        supportActionBar!!.hide()



        val toolbar:Toolbar = findViewById<Toolbar>(R.id.customToolbar)
        toolbar.title = ""

        setSupportActionBar(toolbar)
        toolbar?.navigationIcon = ContextCompat.getDrawable(this, R.drawable.back_button)

        navController = findNavController(R.id.fragmentContainer)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sign_out_menu, menu)

//        val getAddNewItem:MenuItem = menu!!.findItem(R.id.addNew)
//
//
//        var gt = this.getDrawable(R.drawable.add_new_btn)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.title){

            "Add New" -> navController.navigate(R.id.addNewNoteFragment)
            "Sign Out" -> Log.d("TAG", "onOptionsItemSelected: ")

        }
        return super.onOptionsItemSelected(item)
    }




}