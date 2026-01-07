package com.example.expensemanager.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.expensemanager.adapters.NoteAdapter;
import com.example.expensemanager.databinding.FragmentNotesBinding;
import com.example.expensemanager.models.Notes;
import com.example.expensemanager.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.Calendar;

public class NotesFragment extends Fragment {

    Calendar calendar;
    ArrayList<Notes> notesArrayList= new ArrayList<>();
    FragmentNotesBinding binding;
    public MainViewModel viewModel;
    public NotesFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentNotesBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        Toast.makeText(getActivity(), "NotesFragment", Toast.LENGTH_SHORT).show();
        calendar= Calendar.getInstance();
        binding.noteList.setLayoutManager(new LinearLayoutManager(getActivity()));
        notesArrayList= viewModel.getNotes();
        NoteAdapter adapter= new NoteAdapter(getActivity(), notesArrayList);
        binding.noteList.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        binding.noteList.setAdapter(adapter);

        return binding.getRoot();
    }
}