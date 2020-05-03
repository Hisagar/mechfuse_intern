package com.example.interntask.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interntask.DatabaseClient;
import com.example.interntask.FavMovies;
import com.example.interntask.MainActivity;
import com.example.interntask.Movie;
import com.example.interntask.Profile;
import com.example.interntask.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.zip.Inflater;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {
    ArrayList<Movie> movies;
    Context context;

    public MovieAdapter(ArrayList<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MovieHolder movieHolder=new MovieHolder(LayoutInflater.from(context).inflate(R.layout.movie_layout, parent, false));
        return movieHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieHolder holder, final int position) {
        holder.favBtn.setTag("false");
        int f=-1;
        for(int j=0;j<MainActivity.favList.size();j++)
        {
            if(MainActivity.favList.get(j).getId()==movies.get(position).getId()) {
                Picasso.with(context).load(R.drawable.fav_on).resize(52, 52).into(holder.favBtn);
                holder.favBtn.setTag("true");
                f=j;
            }
        }
    holder.title.setText(movies.get(position).getOriginal_title());
    Picasso.with(context).load("https://image.tmdb.org/t/p/w500"+movies.get(position).getPoster_path()).into(holder.imgPoster);
    holder.favBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(holder.favBtn.getTag().toString().equals("false"))
            {
                MainActivity.favList.add(movies.get(position));
                Picasso.with(context).load(R.drawable.fav_on).resize(52, 52).into(holder.favBtn);
            }else
            {
                for(int j=0;j<MainActivity.favList.size();j++)
                {
                    if(MainActivity.favList.get(j).getId()==movies.get(position).getId()) {
                        Picasso.with(context).load(R.drawable.fav_off).resize(52, 52).into(holder.favBtn);
                        MainActivity.favList.remove(j);
                    }
                }

            }
            Set<Movie> s = new LinkedHashSet<Movie>(MainActivity.favList);
            MainActivity.favList.clear();
            MainActivity.favList.addAll(s);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(MainActivity.favList);
            editor.putString("fav", json);
            editor.apply();


        }
    });
        JSONArray arr=movies.get(position).getGenre_ids();
        String genres="";
        if(arr!=null)
        {

            for(int k=0;k<arr.length();k++)
            {
                try {
                    genres+=MainActivity.mapGen.get(arr.get(k))+", ";
                   // Toast.makeText(context, MainActivity.mapGen.get(arr.get(k)) +" ", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            holder.genres.setText(genres);
        }

        holder.over.setText(movies.get(position).getOverview());
        holder.voteAvg.setText(movies.get(position).getVote_average().toString());


    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder {
        TextView title,genres,voteAvg,over;
        ImageView imgPoster;
        ImageButton favBtn;
        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            title=(TextView) itemView.findViewById(R.id.title);
            genres=(TextView) itemView.findViewById(R.id.genText);
            over=(TextView) itemView.findViewById(R.id.over);
            voteAvg=(TextView) itemView.findViewById(R.id.voteText);
            imgPoster=(ImageView) itemView.findViewById(R.id.imgPoster);
            favBtn=(ImageButton)itemView.findViewById(R.id.favBtn);
        }
    }


    private void addFav(final int id, final String title, final String path) {



        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                FavMovies favMovies = new FavMovies();
                favMovies.setId(id);
                favMovies.setTitle(title);
                favMovies.setPoster(path);

                //adding to database
                DatabaseClient.getInstance(context).getFavListDb()
                        .taskDao()
                        .insert(favMovies);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //finish();
                // startActivity(new Intent(getActivity(), MainActivity.class));
                //  Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_LONG).show();
                //getTasks();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();

    }
}
