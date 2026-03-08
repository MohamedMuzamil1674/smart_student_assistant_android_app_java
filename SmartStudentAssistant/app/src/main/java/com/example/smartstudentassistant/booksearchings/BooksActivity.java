package com.example.smartstudentassistant.booksearchings;

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
import com.example.smartstudentassistant.R;
import com.example.smartstudentassistant.contentsdatabase.MyDatabaseHelper;

import java.util.ArrayList;

public class BooksActivity extends AppCompatActivity {

    /* Creating variable of our books array list for calling */
    ArrayList<BooksModel> booksModelArrayList;

    /* Creating variable of books adapter class for calling */
    BooksRVAdapter booksRVAdapter;

    /* Creating variable of database helper class for call */
    MyDatabaseHelper databaseHelper;

    /* Creating variable of our recycler view for calling */
    RecyclerView booksRView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_books);

        /* Initializing object of our database helper class to execute data queries */
        databaseHelper = new MyDatabaseHelper(BooksActivity.this);

        /* Initializing object of our array list in order to execute data queries */
        booksModelArrayList = new ArrayList<>();

        /* Getting data of our books details array list from database handler class */
        booksModelArrayList = databaseHelper.getStudyBooks();

        /* Here we are passing our books array list values to to our adapter class */
        booksRVAdapter = new BooksRVAdapter(booksModelArrayList, BooksActivity.this);
        booksRView = findViewById(R.id.rview_books);

        /* Here we are setting a Layout Manager for alignment of our recycler view */
        LinearLayoutManager layoutManager = new LinearLayoutManager(BooksActivity.this,
                RecyclerView.VERTICAL, false);

        /* Setting Layout Manager on our recycler view for setting vertical display */
        booksRView.setLayoutManager(layoutManager);

        /* Setting Books RV Adapter onto our recycler view for display of data items */
        booksRView.setAdapter(booksRVAdapter);

        /* This is done to enable back pressed on our User Grades screen to move back to previous screen */
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                Intent userHome = new Intent(BooksActivity.this, HomeActivity.class);

                startActivity(userHome);

            }
        }; getOnBackPressedDispatcher().addCallback(BooksActivity.this, callback);

        hideSystemBars(); /* This is our function calling that hides system screens bars */

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.books_screen), (v, insets) -> {
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