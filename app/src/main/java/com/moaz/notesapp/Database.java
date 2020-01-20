package com.moaz.notesapp;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {TableItem.class}, version = 1)
public abstract class Database extends RoomDatabase {

    public abstract TaskDao taskDao();

    private static volatile Database INSTANCE;

    public static Database getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, Database.class, "task_database").build();
        }
        return INSTANCE;
    }


}