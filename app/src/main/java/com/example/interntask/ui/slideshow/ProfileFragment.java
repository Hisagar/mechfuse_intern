package com.example.interntask.ui.slideshow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.interntask.DatabaseClient;
import com.example.interntask.MainActivity;
import com.example.interntask.Profile;
import com.example.interntask.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private EditText editTextTask, editTextDesc, editTextFinishBy;

    private Button btnSelect;
    private ImageView image;
    public Profile profile;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        image = (ImageView) root.findViewById(R.id.img);
        editTextTask = root.findViewById(R.id.editTextTask);
        editTextDesc = root.findViewById(R.id.editTextDesc);
        btnSelect=root.findViewById(R.id.imageSelect);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        getTasks();

        root.findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();
                updateTask(profile);
            }
        });



        return root;
    }


    private void loadTask(Profile profile) {
        editTextTask.setText(profile.getName());
        editTextDesc.setText(profile.getEmail());
        //byteArray=task.getImage();
        Bitmap bmp = BitmapFactory.decodeByteArray(profile.getImage(), 0, profile.getImage().length);
        image.setImageBitmap(Bitmap.createScaledBitmap(bmp, image.getWidth(),
                image.getHeight(), false));
    }

    private void updateTask(final Profile profile) {
        final String name = editTextTask.getText().toString().trim();
        final String email = editTextDesc.getText().toString().trim();
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] imageInByte = baos.toByteArray();

        if (name.isEmpty()) {
            editTextTask.setError("Name required");
            editTextTask.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextDesc.setError("Email required");
            editTextDesc.requestFocus();
            return;
        }


        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                profile.setName(name);
                profile.setEmail(email);
                profile.setImage(imageInByte);
                DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .taskDao()
                        .update(profile);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                MainActivity.textViewName.setText(name);
                MainActivity.textViewEmail.setText(email);
                Bitmap bmp = BitmapFactory.decodeByteArray(imageInByte, 0, imageInByte.length);
                MainActivity.profileImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200,
                        200, false));

                Toast.makeText(getActivity(), "Updated", Toast.LENGTH_LONG).show();
                //finish();
                //startActivity(new Intent(UpdateTaskActivity.this, MainActivity.class));
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }


    private void deleteTask(final Profile profile) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .taskDao()
                        .delete(profile);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_LONG).show();
                //finish();
                //startActivity(new Intent(UpdateTaskActivity.this, MainActivity.class));
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }

    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<Profile>> {

            @Override
            protected List<Profile> doInBackground(Void... voids) {
                List<Profile> profileList = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .taskDao()
                        .getAll();
                return profileList;
            }

            @Override
            protected void onPostExecute(List<Profile> tasks) {
                super.onPostExecute(tasks);
                profile =tasks.get(0);
                loadTask(profile);

            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }


}
