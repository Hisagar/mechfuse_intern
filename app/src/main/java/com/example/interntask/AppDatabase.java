package com.example.interntask;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Profile.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProfileDao taskDao();
}