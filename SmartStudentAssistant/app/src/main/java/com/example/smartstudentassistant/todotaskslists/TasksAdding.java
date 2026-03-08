package com.example.smartstudentassistant.todotaskslists;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smartstudentassistant.HomeActivity;
import com.example.smartstudentassistant.MyTaskHelper;
import com.example.smartstudentassistant.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class TasksAdding extends AppCompatActivity {

    /* Creating variable of database helper class for call */
    MyTaskHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adding);

        /* This is done to enable and initialize object of our SQLite DatabaseHelper class */
        databaseHelper = new MyTaskHelper(TasksAdding.this);

        /* Linking layout elements for setting its data with a content */
        TextInputLayout layoutTitle = findViewById(R.id.layout_task_title);
        TextInputLayout layoutDetail = findViewById(R.id.layout_task_detail);

        /* Linking edit text elements for setting its data with a content */
        TextInputEditText etTitle = findViewById(R.id.edit_task_title);
        TextInputEditText etDetail = findViewById(R.id.edit_task_detail);

        /* Linking button elements for setting its data with a content */
        Button btnAddTask = findViewById(R.id.btn_adding_task);

        /* Setting click listener to add task details in database via button */
        btnAddTask.setOnClickListener(v -> {
            /* Here we are obtaining text of values entered in edit text */
            String taskTitle = String.valueOf(etTitle.getText());
            String taskDetail = String.valueOf(etDetail.getText());

            if (taskTitle.isEmpty()) {

                layoutTitle.setError("Task Name cannot be empty");

            }

            if (taskDetail.isEmpty()) {

                layoutDetail.setError("Task detail cannot be empty");
                return;

            }

            if (taskTitle.length() < 6) {
                layoutTitle.setError("Task Name cannot be below 6");
            }

            if (taskDetail.length() < 12) {
                layoutDetail.setError("Task detail cannot be below 12");
                return;
            }

            if (taskTitle.length() > 22) {
                layoutTitle.setError("Task Name cannot be above 22");
            }
            if (taskDetail.length() > 55) {
                layoutDetail.setError("Task detail cannot be above 55");
                return;
            }

            /* Here we will insert a new task to database in tasks lists table after each of data checking is done */
            Boolean insertTask = databaseHelper.addingActivity(taskTitle, taskDetail, "Pending");

            if (insertTask){ Toast.makeText(TasksAdding.this, "New task is added", Toast.LENGTH_LONG).show(); }

            else { Toast.makeText(TasksAdding.this, "Error! Kindly try again", Toast.LENGTH_LONG).show(); }

        });

        /* This is done to hide cursor, shift focus, and hide keyboard when user login input is done */
        etDetail.setOnEditorActionListener((view, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_DONE) {

                /* obtaining input method service for our keyboard */
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if (imm != null) {

                    /* hiding keyboard from our login interface */
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                }

                /* clearing basic focus from our pass edittext */
                view.clearFocus(); return true;

            }

            /* letting our system to handle this by itself */
            return false;

        });

        /* Setting text Watcher to remove error on text change from Title layout */
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /* No need to use this function in this case */
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /* removing error from our layout after text is changed */
                layoutTitle.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {
                /* No need to use this function in this case */
            }
        });

        /* Setting text Watcher to remove error on text change from Detail layout */
        etDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /* No need to use this function in this case */
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /* removing error from our layout after text is changed */
                layoutDetail.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                /* No need to use this function in this case */
            }
        });

        /* This is done to enable back pressed on User Tasks Screen to move back to previous screen */
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent AdminHome = new Intent(TasksAdding.this, HomeActivity.class);
                startActivity(AdminHome);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(TasksAdding.this, callback);

        hideSystemBars(); /* This is a function calling to hide system screens bars */

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tasks_adding_screen), (v, insets) -> {
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