package com.example.interntask.ui.gallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interntask.DatabaseClient;
import com.example.interntask.MainActivity;
import com.example.interntask.Profile;
import com.example.interntask.R;
import com.example.interntask.adapter.MovieAdapter;

import java.util.List;

public class FavMovieFragment extends Fragment {

    private EditText editTextTask, editTextDesc, editTextFinishBy;
    public RecyclerView favRecyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_gallery, container, false);

        //  loadMovieList();
        favRecyclerView = v.findViewById(R.id.favRecyclerView);
        MovieAdapter adapter = new MovieAdapter(MainActivity.favList, getActivity());
        favRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        favRecyclerView.setAdapter(adapter);
        return  v;
    }



}
