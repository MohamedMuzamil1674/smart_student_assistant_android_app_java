package com.example.smartstudentassistant.onboardingsui;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartstudentassistant.R;

public class IntroActivity1 extends AppCompatActivity {

    /* Declaring of our four Text View variables */
    TextView txtBook, txtGrade;

    /* Declaring of our ImageView variables */
    ImageView imgBooks, imgMarks;

    /* Declaring of our Button variables */
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro1);

        /* Hooking Views of Login layout in our java code */
        txtBook = findViewById(R.id.study_books_txt);
        txtGrade = findViewById(R.id.grade_txt);
        imgBooks = findViewById(R.id.study_book_img);
        imgMarks = findViewById(R.id.cal_grade_img);
        btnNext = findViewById(R.id.btn_next_one);

        /* Setting onClick Listener on our nextButton */
        btnNext.setOnClickListener(v -> {

            Intent iNext = new Intent(IntroActivity1.this, IntroActivity2.class);

            startActivity(iNext);

        });

        /* This is used to enable back button press if user press it then we minimize application */
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() { /* This is main function to handle back pressed */

                finishAfterTransition(); /* This closes application with transitions animation */

            }
            /* Here we are registering back press call back on its clicks so we can enable it */
        }; getOnBackPressedDispatcher().addCallback(this, callback);
        hideSystemBars(); /* This is a function calling to hide system screens bars */


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
