package com.example.interntask;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FavMovies.class}, version = 1)

    public abstract class FavListDB extends RoomDatabase {
        public abstract FavDao taskDao();
    }

