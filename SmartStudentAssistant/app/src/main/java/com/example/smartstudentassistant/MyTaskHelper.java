package com.example.smartstudentassistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smartstudentassistant.todotaskslists.TasksModel;

import java.util.ArrayList;

public class MyTaskHelper extends SQLiteOpenHelper {

    /* Creating Table of Todo_Tasks in our SQlite Database */
    private static final String TABLE_ACTIVITY = "TasksList";

    /* Creating Columns in Table Todo_Tasks of SQlite DB*/
    private static final String TODO_ID = "Activity_Id";
    private static final String TASK_NAME = "TaskTitle";
    private static final String TASK_DETAIL = "TaskDetail";
    private static final String TASK_STATUS = "Status";

    /* This is our main default constructor required for effective execution of queries inside our SQlite Database system */
    public MyTaskHelper(Context context) {

        super(context, "digital_tasks", null, 2);

    }


    /* This function is required to add new tables or values in our database when system is created for very first time */
    @Override
    public void onCreate(SQLiteDatabase db) {

         /* Once below query is executed then it will create table with Todo_Id, Task_Name,TaskDetails, and Task_Status */
        db.execSQL("CREATE TABLE " + TABLE_ACTIVITY + "(" + TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TASK_NAME +
                " TEXT NOT NULL," + TASK_DETAIL + " TEXT NOT NULL," + TASK_STATUS + " TEXT NOT NULL" + ")");


    }

    /* This function is required to change or add new tables in our database system on database version increment */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

    /* This is done to enable a user to add one activity into tasks table for his or her tasks list */
    public Boolean addingActivity(String taskTitle, String taskDetail, String taskStatus){
        /* This is done to enable new data insert inside our sqlite database */
        SQLiteDatabase db = this.getWritableDatabase();

        /* Here we are using contentValues to load values into our columns */
        ContentValues values = new ContentValues();
        values.put(TASK_NAME, taskTitle);
        values.put(TASK_DETAIL, taskDetail);
        values.put(TASK_STATUS, taskStatus);

        /* This is done to place values in activity table after true result*/
        long result = db.insert(TABLE_ACTIVITY, null, values);
        if (result != -1) {
            return true;
        }
        else {
            return false;
        }
    }

    /* This is used to obtain task details from tasks table by user identity */
    public ArrayList<TasksModel> getTasksByUser(){
        /* Creating an array list to obtain tasks of student */
        ArrayList<TasksModel> tasksList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        /* Selecting all todo_tasks from tasks table by using user identity */
        Cursor curTasks = db.rawQuery("SELECT * FROM " + TABLE_ACTIVITY + " ORDER BY " + TASK_NAME + " ASC ", null);

        if (curTasks.moveToFirst()){ /* when values are there we get them */

            do {/* Fetching values from our tasks table by column index */
                int taskId = curTasks.getInt(0);
                String taskTitle = curTasks.getString(2);
                String taskDetail = curTasks.getString(3);
                String taskStatus = curTasks.getString(4);

                /* Passing our fetched values to our tasks model class */
                TasksModel tasksModel = new TasksModel();
                tasksModel.setTaskId(taskId);
                tasksModel.setTaskTitle(taskTitle);
                tasksModel.setTaskDetail(taskDetail);
                tasksModel.setTaskStatus(taskStatus);

                /* Setting our values in array list of studies tasks */
                tasksList.add(tasksModel);

            } while (curTasks.moveToNext());
            /* This keeps fetching tasks till cursor moves to next */
        }
        /* Closing cursor after getting values from tasks table */
        curTasks.close();

        /* Returning our task list after obtaining our values */
        return tasksList;
    }

    /* This is used to change status of one task once it is checked by its user */
    public void changeTaskStatus(int taskId, String taskStatus){
        /* This is used to enable new data changes in our tasks database table */
        SQLiteDatabase db = this.getWritableDatabase();

        /* Here we are using contentValues to load values into our columns */
        ContentValues values = new ContentValues();
        values.put(TASK_STATUS, taskStatus);

        /* This is done to place new status in tasks table after true result */
        db.update(TABLE_ACTIVITY, values, TODO_ID + " = ? ", new String[] {String.valueOf(taskId)} );
    }

    /* This is used to change details of one task once it is checked by its user */
    public Boolean changeTaskData(int taskId, String taskTitle, String taskDetail){

        SQLiteDatabase db = this.getWritableDatabase();

        /* Here we are using contentValues to load values into our columns */
        ContentValues values = new ContentValues();
        values.put(TASK_NAME, taskTitle);
        values.put(TASK_DETAIL, taskDetail);

        /* This is done to place new status in tasks table after true result */
        long result = db.update(TABLE_ACTIVITY, values, TODO_ID + " = ? ", new String[] {String.valueOf(taskId)} );
        if (result != -1) {
            return true;
        }
        else {
            return false;
        }
    }

    /* This is used to enable one task delete from tasks table by system user */
    public Boolean deleteTask(int taskId){
        /* This is done to enable new data change inside our sqlite database */
        SQLiteDatabase db = this.getWritableDatabase();

        /* This is done to delete values in users table after true result*/
        long result = db.delete(TABLE_ACTIVITY, TODO_ID + " = ? ",
                new String[] {String.valueOf(taskId)});

        if (result != -1) {
            return true;
        }
        else {
            return false;
        }
    }

}
