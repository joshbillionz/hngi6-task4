package com.project.pontusgoaltracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import  com.project.pontusgoaltracker.database.DbSchema.*;
import com.project.pontusgoaltracker.models.Task;


public class GoalBaseHelper extends SQLiteOpenHelper{
    private static final int VERSION= 1;
    private static final String DATABASE_NAME = "goalBase.Db";

    public GoalBaseHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //table for goals
        db.execSQL("create table " + DbSchema.GoalTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                GoalTable.Cols.UUID + ", " +
                GoalTable.Cols.TITLE + ", " +
                GoalTable.Cols.DESCRIPTION + ", " +
                GoalTable.Cols.TYPE + ", " +
                GoalTable.Cols.DATE_CREATED + ", " +
                GoalTable.Cols.DEADLINE + ", " +
                GoalTable.Cols.COMPLETED + ", " +
                GoalTable.Cols.COMPLETED_TASK_COUNT +
                ")"
        );
        //table for tasks
        db.execSQL("create table " + DbSchema.TaskTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                TaskTable.Cols.TITLE +", " +
                TaskTable.Cols.COMPLETED +
                ")"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}

