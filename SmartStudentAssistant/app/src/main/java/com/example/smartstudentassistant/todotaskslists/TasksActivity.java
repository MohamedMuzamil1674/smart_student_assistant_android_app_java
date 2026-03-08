package com.example.smartstudentassistant.todotaskslists;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentassistant.HomeActivity;
import com.example.smartstudentassistant.MyTaskHelper;
import com.example.smartstudentassistant.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TasksActivity extends AppCompatActivity {

    /* Creating variable of our tasks array list for calling */
    ArrayList<TasksModel> tasksArrayList;

    /* Creating variable of tasks adapter class for calling */
    TasksAdapter tasksRVAdapter;

    /* Creating variable of floating action button adding */
    FloatingActionButton btnAddTask;

    /* Creating variable of database helper class for call */
    MyTaskHelper databaseHelper;

    /* Creating variable of our recycler view for calling */
    RecyclerView tasksRView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tasks);

        /* Hooking layout items of our tasks list screen to get all things done nicely */
        tasksRView = findViewById(R.id.rview_tasks_list);

        /* Here we are using Floating action button to show dialog for adding tasks */
        btnAddTask = findViewById(R.id.fab_add_task);

        /* This is done to enable and initialize object of our SQLite DatabaseHelper class */
        databaseHelper = new MyTaskHelper(TasksActivity.this);

        /* Initializing object of our array list in order to execute data queries */
        tasksArrayList = new ArrayList<>();

        /* Getting data of our tasks array list from database class by a user id*/
        tasksArrayList = databaseHelper.getTasksByUser();

        /* Here we are passing our tasks array list values to to our adapter class */
        tasksRVAdapter = new TasksAdapter(tasksArrayList, this);

        /* Here we are setting a Layout Manager for alignment of our recycler view */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false);

        /* Setting Layout Manager on our recycler view for setting vertical display */
        tasksRView.setLayoutManager(layoutManager);

        /* Setting Tasks RV Adapter onto our recycler view for display of data items */
        tasksRView.setAdapter(tasksRVAdapter);

        /* Here we will use listener on tasks adapter for changes on check box status */
        tasksRVAdapter.setOnTaskStatusChangeListener(tasksModel -> {
            /* Changing status of checkbox in activity */
            databaseHelper.changeTaskStatus(tasksModel.getTaskId(), tasksModel.getTaskStatus());
        });

        /* Here we will use listener on tasks adapter for enabling clicks on task items */
        tasksRVAdapter.setOnTaskItemSelectedListener(tasksModel -> {

            /* Here we will share data to task change screen for further queries */
            Intent taskIntent = new Intent(TasksActivity.this, TaskChange.class);

            /* Placing data in intent to share it with our task change activity */
            taskIntent.putExtra("task_id", tasksModel.getTaskId());
            taskIntent.putExtra("task_title", tasksModel.getTaskTitle());
            taskIntent.putExtra("task_detail", tasksModel.getTaskDetail());
            startActivity(taskIntent);
        });

        /* Here we are enabling click on floating button to show task add screen */
        btnAddTask.setOnClickListener(v -> {

            Intent intent = new Intent(TasksActivity.this, TasksAdding.class);
            startActivity(intent);

        });

        /* This is done to enable back pressed on User Tasks Screen to move back to previous screen */
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                Intent AdminHome = new Intent(TasksActivity.this, HomeActivity.class);
                startActivity(AdminHome);

            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        hideSystemBars(); /* This is a function calling to hide system screens bars */

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tasks_list_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() { /* This is used when activity is resumed by system users */

        super.onResume(); /* This is object of class that is called with resume function */

        hideSystemBars(); /* This is our function calling that hides system screens bars */

    }

    /* This is done to hide status bar and navigations bar in our Splash Screen activity */
    private void hideSystemBars(){

        /* Getting instance of insets controller for hiding of system bars for screen */
        final WindowInsetsController insetsController = getWindow().getInsetsController();

        if (insetsController != null) { /* First we hide system bars on screen creation */

            /* This is used to hide system bars from screen until swipe is not occurred */
            insetsController.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());

            /* Then we re show them when user swipes navigation area or status bar area */
            insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) { /* This is a focus change function */

        super.onWindowFocusChanged(hasFocus); /* This is required key only for few devices */

        if (hasFocus){ /* So that system bars can be hided on them for immersive display */

            hideSystemBars(); /* This is a function calling to hide system screens bars */

        }
    }

}