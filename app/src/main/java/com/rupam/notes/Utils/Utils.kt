package com.rupam.notes.Utils

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog

object Utils {

    fun createDialogBox(view:View, context:Context, title:String, msg:String, positiveMsg:String, negativeMsg:String, neutralMsg:String){

        val builder = AlertDialog.Builder(context)
        with(builder){

            setTitle(title)
            setMessage(msg)
            setPositiveButton(positiveMsg) { dialog, which ->


            }
        }

    }

}