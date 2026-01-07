package com.example.expensemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.RowNotesBinding;
import com.example.expensemanager.models.Notes;
import com.example.expensemanager.utils.Helper;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NotesViewHolder> {

    Context context;
    ArrayList<Notes> notesArrayList;

    public NoteAdapter(Context context, ArrayList<Notes> notesArrayList){
        this.context= context;
        this.notesArrayList= notesArrayList;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.row_notes,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Notes notes= notesArrayList.get(position);
        holder.binding.datetxt.setText(Helper.formatDate(notes.getDateField()));
        holder.binding.noteTxt.setText(notes.getNoteField());
    }

    @Override
    public int getItemCount() {
        return notesArrayList.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder{

        RowNotesBinding binding;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= RowNotesBinding.bind(itemView);
        }
    }

}
