package com.example.interntask;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavDao {
    @Query("SELECT * FROM FavMovies")
    List<FavMovies> getAll();

    @Insert
    void insert(FavMovies favMovies);

    @Delete
    void delete(FavMovies favMovies);



}
