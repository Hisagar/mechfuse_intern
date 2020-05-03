package com.example.interntask;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.interntask.adapter.MovieAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static ImageView profileImage;
    public static TextView textViewName,textViewEmail;
    public static HashMap<Integer,String> mapGen=new HashMap<>();
    public static ArrayList<Movie> favList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        Gson gson = new Gson();
        String json = prefs.getString("fav", null);
        Type type = new TypeToken<ArrayList<Movie>>() {}.getType();
        MainActivity.favList= gson.fromJson(json, type);
        if(favList==null)
        {
            favList=new ArrayList<>();
        }
        loadLocalLang();
        getTasks();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);



        NavigationView navigationView = findViewById(R.id.nav_view);
        profileImage=navigationView.getHeaderView(0).findViewById(R.id.imageViewProfile);
        textViewName=navigationView.getHeaderView(0).findViewById(R.id.txtName);
        textViewEmail=navigationView.getHeaderView(0).findViewById(R.id.txtEmail);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        setProfileData();
    }




    public void setProfileData() {

        class GetTasks extends AsyncTask<Void, Void, List<Profile>> {

            @Override
            protected List<Profile> doInBackground(Void... voids) {
                List<Profile> profileList = DatabaseClient
                        .getInstance(MainActivity.this)
                        .getAppDatabase()
                        .taskDao()
                        .getAll();
                return profileList;
            }

            @Override
            protected void onPostExecute(List<Profile> tasks) {
                super.onPostExecute(tasks);

                textViewName.setText(tasks.get(0).getName());
                textViewEmail.setText(tasks.get(0).getEmail());
                Bitmap bmp = BitmapFactory.decodeByteArray(tasks.get(0).getImage(), 0, tasks.get(0).getImage().length);
                profileImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200,
                        200, false));

            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.changeLang)
        {
            showChangeLangDialog();
        }
        return super.onOptionsItemSelected(item);

    }

    public void showChangeLangDialog()
    {
        final String[] lan={"English","Deutsche"};
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose Language..");
        builder.setSingleChoiceItems(lan, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            if(which==0)
            {
                setLocal("en");
                recreate();
            }else
            {
                setLocal("de");
                recreate();
            }
            dialog.dismiss();
            }
        });

        AlertDialog dialog=builder.create();
        dialog.show();

    }

    public void setLocal(String lan) {
        Locale locale=new Locale(lan);
        Locale.setDefault(locale);
        Configuration configuration=new Configuration();
        configuration.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lan);
        editor.apply();
    }
    public void loadLocalLang()
    {
        SharedPreferences sharedPreferences=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=sharedPreferences.getString("My_Lang","en");
        setLocal(language);
    }

    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<Profile>> {

            @Override
            protected List<Profile> doInBackground(Void... voids) {
                List<Profile> profileList = DatabaseClient
                        .getInstance(MainActivity.this)
                        .getAppDatabase()
                        .taskDao()
                        .getAll();
                return profileList;
            }

            @Override
            protected void onPostExecute(List<Profile> tasks) {
                super.onPostExecute(tasks);
//                TasksAdapter adapter = new TasksAdapter(MainActivity.this, tasks);
//                recyclerView.setAdapter(adapter);
                if(tasks.size()==0)
                {

                    Drawable drawable = getResources().getDrawable(R.drawable.user);
                    Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] bitmapdata = stream.toByteArray();
                    saveTask("Sagar Wankhade","sagar.wankhade18@vit.edu",bitmapdata);
                }


            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void saveTask(final String name, final String email, final byte[] image) {



        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                Profile profile = new Profile();
                profile.setName(name);
                profile.setEmail(email);
                profile.setImage(image);
                //  task.setImage(sFinishBy);
                profile.setFinished(false);

                //adding to database
                DatabaseClient.getInstance(MainActivity.this).getAppDatabase()
                        .taskDao()
                        .insert(profile);
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
