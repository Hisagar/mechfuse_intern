package com.example.interntask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.interntask.adapter.MovieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UpcomingFragment extends Fragment {
    private String JSON_URL = "https://api.themoviedb.org/3/movie/upcoming?api_key=e16f9ec421f01f05db45a6d069d84d56";
    public ArrayList<Movie> upcomingList=new ArrayList<>();
    public RecyclerView upcomingRecyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v= inflater.inflate(R.layout.upcoming_frag_layout,container,false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        // progressBar.setVisibility(View.INVISIBLE);


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray movieArray = obj.getJSONArray("results");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < movieArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject movieObject = movieArray.getJSONObject(i);
                                Movie movie=new Movie();
                                movie.setPopularity(movieObject.getDouble("popularity"));
                                movie.setVote_count(movieObject.getInt("vote_count"));
                                movie.setPoster_path(movieObject.getString("poster_path"));
                                movie.setId(movieObject.getInt("id"));
                                movie.setAdult(movieObject.getBoolean("adult"));
                                movie.setOriginal_language(movieObject.getString("original_language"));
                                movie.setOriginal_title(movieObject.getString("original_title"));
                                movie.setVote_average(movieObject.getDouble("vote_average"));
                                movie.setOverview(movieObject.getString("overview"));
                                movie.setRelese_date(movieObject.getString("release_date"));
                                upcomingList.add(movie);
                            }

                            upcomingRecyclerView=v.findViewById(R.id.upRecyclerView);
                            MovieAdapter adapter=new MovieAdapter(upcomingList,getActivity());
                            upcomingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            upcomingRecyclerView.setAdapter(adapter);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);



        return v;
    }
}
