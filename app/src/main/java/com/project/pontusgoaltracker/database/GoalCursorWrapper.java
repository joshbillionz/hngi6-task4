package com.project.pontusgoaltracker.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.project.pontusgoaltracker.models.Goal;
import com.project.pontusgoaltracker.database.DbSchema.GoalTable;


import java.util.Date;
import java.util.UUID;

public class GoalCursorWrapper extends CursorWrapper {

    public GoalCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    //the goal is in this cursorWrapper
    //get it out

    public Goal getGoal() {
        String uuidString = getString(getColumnIndex(DbSchema.GoalTable.Cols.UUID));
        String title = getString(getColumnIndex(GoalTable.Cols.TITLE));
        String description = getString(getColumnIndex(GoalTable.Cols.DESCRIPTION));
        String type = getString(getColumnIndex(GoalTable.Cols.TYPE));
        Long deadline = getLong(getColumnIndex(GoalTable.Cols.DEADLINE));
        int isCompleted = getInt(getColumnIndex(GoalTable.Cols.COMPLETED));
        int completedTaskCount = getInt(getColumnIndex(GoalTable.Cols.COMPLETED_TASK_COUNT));


        Goal goal = new Goal(UUID.fromString(uuidString));
        goal.setTitle(title);
        goal.setDescription(description);
        goal.setDeadline(new Date(deadline).toString());
        goal.setCompleted(isCompleted != 0);
        goal.setType(type);
        goal.setCompletedTaskCount(completedTaskCount);

        return goal ;

    }
}
