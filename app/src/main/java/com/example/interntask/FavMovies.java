package com.example.interntask;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class FavMovies implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int no;

    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "poster")
    private String poster;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
