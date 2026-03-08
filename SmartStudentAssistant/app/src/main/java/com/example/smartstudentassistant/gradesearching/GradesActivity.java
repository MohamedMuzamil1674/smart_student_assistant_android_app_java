package com.example.smartstudentassistant.gradesearching;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smartstudentassistant.HomeActivity;
import com.example.smartstudentassistant.R;
import com.example.smartstudentassistant.contentsdatabase.MyDatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class GradesActivity extends AppCompatActivity {

    TextInputLayout layoutStdMarks; TextInputEditText editStudentMarks;
    Button btnSearch; MyDatabaseHelper MyDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grades);

        /* Initializing database helper class object by passing activity context to it */
        MyDbHelper = new MyDatabaseHelper(GradesActivity.this);

        /* Initializing our interface views to perform required action on them easily */
        layoutStdMarks = findViewById(R.id.Layout_obtain_marks);
        editStudentMarks = findViewById(R.id.et_obtain_marks);
        btnSearch = findViewById(R.id.btn_search_grade);

        /* This is done to hide cursor, clear focus, and hide keyboard when user login input is done */
        editStudentMarks.setOnEditorActionListener((view, actionId, event) -> {

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

        btnSearch.setOnClickListener(v -> {
            /* This is done to obtain value of student obtained marks for search */
            String stdMarks = String.valueOf(editStudentMarks.getText());

            if (stdMarks.isEmpty()){
                layoutStdMarks.setError("Obtained Marks cannot be empty");
                return;
            }
            if (stdMarks.length() < 2){
                layoutStdMarks.setError("Marks input cannot be below 2");
                return;
            }

            if (stdMarks.length() > 3){
                layoutStdMarks.setError("Marks input cannot be above 3");
                return;
            }

            /* This is done to check marks of student */
            int marks = Integer.parseInt(stdMarks);
            if (marks >= 50 && marks <= 100){

                showGradeDialog(stdMarks);

            }

            if (marks < 50 || marks > 100){

                Toast.makeText(GradesActivity.this, "Marks must be in range of 50 - 100",
                        Toast.LENGTH_SHORT).show();

            }
        });

        /* This is done to enable back pressed on our User Grades screen to move back to previous screen */
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                Intent userHome = new Intent(GradesActivity.this, HomeActivity.class);
                startActivity(userHome);

            }
        };
        getOnBackPressedDispatcher().addCallback(GradesActivity.this, callback);

        hideSystemBars(); /* This is our function calling that hides system screens bars */

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.grades_screen), (v, insets) -> {

            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;

        });
    }

    /* This is used to show one custom dialog to user after success of query */
    private void showGradeDialog(String stdMarks){

        /* Setting content of our custom dialog box in activity */
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.std_grade_dialog);

        /* Linking element of custom dialog for setting content */
        TextView txtObtGrade = dialog.findViewById(R.id.txt_obtained_gpa);
        TextView txtgdStatus = dialog.findViewById(R.id.txt_grade_value);

        /* Getting required data from grades table by array list */
        ArrayList<GradeModel> stdGradeList = MyDbHelper.getStudentGrade(stdMarks);
        GradeModel stdModel = stdGradeList.get(0);

        /* Setting fetched values from our list into text views */
        txtObtGrade.setText(stdModel.getGradeScore());
        txtgdStatus.setText(stdModel.getGradeState());

        /* Showing our custom dialog in grade search screen */
        dialog.show();

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