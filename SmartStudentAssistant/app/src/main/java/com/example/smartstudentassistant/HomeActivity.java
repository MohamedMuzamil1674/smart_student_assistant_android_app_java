package com.example.smartstudentassistant;

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

import com.example.smartstudentassistant.booksearchings.BooksActivity;
import com.example.smartstudentassistant.filesscannings.FilesScanner;
import com.example.smartstudentassistant.gradesearching.GradesActivity;
import com.example.smartstudentassistant.imagescontents.ContentActivity;
import com.example.smartstudentassistant.notesdrivelink.NotesActivity;
import com.example.smartstudentassistant.todotaskslists.TasksActivity;
import com.google.android.material.card.MaterialCardView;

public class HomeActivity extends AppCompatActivity {

    MaterialCardView cardBooksLib, cardNotesLib, cardStdGrade, cardTaskList, cardTextOcr, cardScanning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        cardBooksLib = findViewById(R.id.card_books_icon);
        cardNotesLib = findViewById(R.id.notes_cards_icon);
        cardStdGrade = findViewById(R.id.card_grades_icon);
        cardTextOcr = findViewById(R.id.encode_cards_icon);
        cardTaskList = findViewById(R.id.card_tasks_icon);
        cardScanning = findViewById(R.id.scan_cards_icon);

        cardBooksLib.setOnClickListener(v -> {

            Intent intent = new Intent(HomeActivity.this, BooksActivity.class);
            startActivity(intent);
        });

        cardNotesLib.setOnClickListener(v -> {

            Intent routine = new Intent(HomeActivity.this, NotesActivity.class);
            startActivity(routine);

        });


        cardTaskList.setOnClickListener(v -> {

            Intent intent = new Intent(HomeActivity.this, TasksActivity.class);
            startActivity(intent);

        });

        cardTextOcr.setOnClickListener(v -> {

            Intent intent = new Intent(HomeActivity.this, ContentActivity.class);
            startActivity(intent);

        });

        cardStdGrade.setOnClickListener(v -> {

            Intent intent = new Intent(HomeActivity.this, GradesActivity.class);
            startActivity(intent);

        });

        cardScanning.setOnClickListener(v -> {

            Intent intent = new Intent(HomeActivity.this, FilesScanner.class);
            startActivity(intent);

        });

        hideSystemBars(); /* This is our function calling that hides both of a system screens bars */

        /* This is used to enable back button press if user press it then we minimize application */
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() { /* This is main function to handle back pressed */

                finishAfterTransition(); /* This closes application with transitions animation */

            }
            /* Here we are registering back press call back on its clicks so we can enable it */
        }; getOnBackPressedDispatcher().addCallback(this, callback);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.notes_drive_screen), (v, insets) -> {
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