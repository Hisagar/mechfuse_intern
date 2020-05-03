package com.example.interntask.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.interntask.MainActivity;
import com.example.interntask.Movie;
import com.example.interntask.R;
import com.example.interntask.adapter.MovieAdapter;
import com.example.interntask.adapter.TabsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        setData(root);

        return root;
    }

    private void setData(final View root) {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.themoviedb.org/3/genre/movie/list?api_key=e16f9ec421f01f05db45a6d069d84d56&language="+
                getActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("My_Lang","en"),
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
                            JSONArray movieArray = obj.getJSONArray("genres");

                            //now looping through all the elements of the json array
                            int i;
                            for (i = 0; i < movieArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject movieObject = movieArray.getJSONObject(i);
                                MainActivity.mapGen.put(movieObject.getInt("id"),movieObject.getString("name"));

                            }
                            if(i==movieArray.length()) {

                                TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(getActivity(), getActivity().getSupportFragmentManager());
                                ViewPager viewPager = root.findViewById(R.id.view_pager);
                                viewPager.setAdapter(tabsPagerAdapter);
                                TabLayout tabs = root.findViewById(R.id.tabs);
                                tabs.setupWithViewPager(viewPager);
                            }



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





    }
}
