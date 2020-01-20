package com.moaz.notesapp;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM myTable")
    List<TableItem> getAll();

    @Insert
    void insert(TableItem task);

    @Delete
    void delete(TableItem task);

    @Update
    void update(TableItem task);

}