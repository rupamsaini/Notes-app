package com.rupam.notes.Data

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rupam.notes.Model.Note
import com.rupam.notes.R
import java.text.DateFormat
import java.util.*

class NoteAdapter(private val context: Context, private val noteList: MutableList<Note?>) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.note_row, parent, false)
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = noteList[position]
        holder.title.text = note?.title?.toUpperCase()
        holder.body.text = note?.noteBody
        val dateFormat = DateFormat.getDateInstance()
        val formattedDate = dateFormat.format(Date(java.lang.Long.valueOf(note?.dateAdded!!)).time)
        holder.dateAdded.text = formattedDate
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    inner class ViewHolder(itemView: View, var ctx: Context) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var body: TextView
        var dateAdded: TextView
        private val cardView: View
        var userId: String?

        init {

            title = itemView.findViewById(R.id.titleNoteRow)
            body = itemView.findViewById(R.id.descNoteRow)
            dateAdded = itemView.findViewById(R.id.dateAdded)
            cardView = itemView.findViewById(R.id.cardViewRow)

            val androidColors = itemView.resources.getIntArray(R.array.androidcolors)
            val randomAndroidColor = androidColors[Random().nextInt(androidColors.size)]
            cardView.setBackgroundColor(randomAndroidColor)
            userId = null
            itemView.setOnClickListener {
                val selectedItem = noteList[adapterPosition]
                val selectedKey = selectedItem?.key

//                    TODO: Goto detailsActivity with details
////                val intent = Intent(context, NoteDetailsActivity::class.java)
//                intent.putExtra("title", title.text.toString())
//                intent.putExtra("body", body.text.toString())
//                intent.putExtra("userKey", selectedKey) //Sends the selected key
//                ctx.startActivity(intent)
            }
        }
    }

}