package com.project.pontusgoaltracker.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.project.pontusgoaltracker.database.GoalBaseHelper;
import com.project.pontusgoaltracker.database.GoalCursorWrapper;
import  com.project.pontusgoaltracker.database.DbSchema.GoalTable;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//THIS CLASS IS A SINGLETON
public class GoalLab {

    // A singleton class
    private static GoalLab sGoalLab;

    //i made the goal list and getGoal(UUID) STATIC

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static GoalLab get(Context context){
        if(sGoalLab==null){
            sGoalLab = new GoalLab(context);
        }

        return sGoalLab;
    }

    private GoalLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new GoalBaseHelper(mContext)
                .getWritableDatabase();


    }

    public void addGoal(Goal goal){
        ContentValues values = getContentValues(goal);
        mDatabase.insert(GoalTable.NAME,null,values);
    }

    public void deleteGoal(Goal goal){
        mDatabase.delete(
                GoalTable.NAME,
                GoalTable.Cols.UUID + " = ?",
                new String[]{goal.getGoalIdString()}
        );
    }

    public List<Goal> getGoals(){
        List<Goal> goalList = new ArrayList<>();
        GoalCursorWrapper cursor = queryGoals(null,null);
        // mCOUNTForCURSOR=cursor.getCount();
        try{
            cursor.moveToFirst();
            //mCOUNTForCURSOR=cursor.getCount();
            while(!cursor.isAfterLast()){
                goalList.add(cursor.getGoal());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return goalList;
    }

    public  Goal getGoal(UUID id){
        GoalCursorWrapper cursor = queryGoals(
                GoalTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try{
            if(cursor.getCount()==0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getGoal();
        }finally{
            cursor.close();
        }
    }

    public  void updateGoal(Goal goal){
        String uuidString = goal.getGoalIdString();
        ContentValues values = getContentValues(goal);

        mDatabase.update(GoalTable.NAME,values,GoalTable.Cols.UUID + " = ?"
                ,new String[] {uuidString});
    }

    public void deleteAllGoals(){
        mDatabase.execSQL("delete from " + GoalTable.NAME);
    }

    //a method that shuttle goals into ContentValues
    private static ContentValues getContentValues(Goal goal){
        //todo :get content values for task;
        ContentValues values = new ContentValues();
        values.put(GoalTable.Cols.UUID, goal.getGoalIdString());
        values.put(GoalTable.Cols.TITLE,goal.getTitle());
        values.put(GoalTable.Cols.DESCRIPTION,goal.getDescription());
        values.put(GoalTable.Cols.TYPE,goal.getType());
        values.put(GoalTable.Cols.DEADLINE, goal.getDeadline());
        values.put(GoalTable.Cols.COMPLETED, goal.isCompleted()? 1 : 0);
        values.put(GoalTable.Cols.COMPLETED_TASK_COUNT,goal.getCompletedTaskCount());

        return values;
    }

    private  GoalCursorWrapper queryGoals(String where, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                GoalTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null,null

        );

        return new GoalCursorWrapper(cursor);
    }
}