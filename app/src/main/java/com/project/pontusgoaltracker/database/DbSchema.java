package com.project.pontusgoaltracker.database;

public class DbSchema {

    public static final class GoalTable{
        //there could be another table in this schema class eg Tasks Table
        //hence the need for the NAME variable;
        public static final String NAME = "goals";
//          lesson : A goal table will have columns which you can
//              distinguish by Cols.TITLE, or using any other property

        //the schema carries tables ..
        //the table can have as many columns as possible


        //but each must have  necessary properties;
        public static final class Cols{
            public static final String UUID ="uuid";
            public static final String TITLE ="title";
            public static final String DESCRIPTION ="description";
            public static final String TYPE ="type";
            public static final String DATE_CREATED ="dateCreated";
            public static final String DEADLINE ="deadline";
            public static final String COMPLETED ="completed";
            public static final String COMPLETED_TASK_COUNT = "completedTaskCount";
        }
    }

    public static final class TaskTable{
        public static final String NAME = "tasks";

        public static final class Cols{
            public static final String TITLE ="title";
            public static final String COMPLETED ="completed";
        }
    }

}
