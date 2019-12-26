package com.rupam.notes.Data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rupam.notes.Activities.NoteDetailsActivity;
import com.rupam.notes.Model.Note;
import com.rupam.notes.R;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private Context context;
    private List<Note> noteList;


    public NoteAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }
    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {

        Note note = noteList.get(position);

        holder.title.setText(note.getTitle().toUpperCase());
        holder.body.setText(note.getNoteBody());

        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(Long.valueOf(note.getDateAdded())).getTime());

        holder.dateAdded.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView body;
        public TextView dateAdded;
        private View cardView;
        String userId;

        public ViewHolder(@NonNull final View itemView, final Context ctx) {
            super(itemView);

            context = ctx;

            title = itemView.findViewById(R.id.titleNoteRow);
            body = itemView.findViewById(R.id.descNoteRow);
            dateAdded = itemView.findViewById(R.id.dateAdded);
            cardView = itemView.findViewById(R.id.cardViewRow);

            int[] androidColors = itemView.getResources().getIntArray(R.array.androidcolors);
            int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];

            cardView.setBackgroundColor(randomAndroidColor);
           
            userId = null;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Note selectedItem = noteList.get(getAdapterPosition());
                    String selectedKey = selectedItem.getKey();

//                    TODO: Goto detailsActivity with details
                    Intent intent = new Intent(context, NoteDetailsActivity.class);
                    intent.putExtra("title", title.getText().toString());
                    intent.putExtra("body", body.getText().toString());
                    intent.putExtra("userKey", selectedKey);  //Sends the selected key

                    ctx.startActivity(intent);
                }
            });
        }
    }
}
