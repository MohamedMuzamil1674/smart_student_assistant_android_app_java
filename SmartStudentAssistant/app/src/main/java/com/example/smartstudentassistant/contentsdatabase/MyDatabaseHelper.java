package com.example.smartstudentassistant.contentsdatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.smartstudentassistant.booksearchings.BooksModel;
import com.example.smartstudentassistant.gradesearching.GradeModel;
import com.example.smartstudentassistant.notesdrivelink.NotesModel;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    /* Declaring key Name for our SQlite Database system */
    private static final String DATABASE_PATH = "data/data/com.example.smartstudentassistant.contentsdatabase/databases";

    /* Declaring key Name for our SQlite Database system */
    private static final String DATABASE_NAME = "DigitalStudyAssist.db";

    /* Declaring Version of our SQlite Database system */
    private static final int DATABASE_VERSION = 6;

    public MyDatabaseHelper(@Nullable Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        copyDatabaseFile(context);

    }

    private void copyDatabaseFile(Context context) {

        try {

            InputStream inputStream = context.getAssets().open(DATABASE_NAME);

            String outputFileName = DATABASE_PATH + DATABASE_NAME;

            OutputStream outputStream = new FileOutputStream(outputFileName);

            byte[] buffer = new byte[1024];

            int length;

            while ((length = inputStream.read(buffer)) > 0){

                outputStream.write(buffer, 0, length);

            }

            outputStream.flush();

            outputStream.close();

            inputStream.close();

        } catch (Exception e) {

            Log.e("loading_error", "Error in setting database", e);
            /* This prints full details of exception in a logcat section */

        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /* This is used to obtain book name, writer name, and edition from our books table */
    public ArrayList<BooksModel> getStudyBooks(){

        /* Creating an array list to obtain grades of student */
        ArrayList<BooksModel> booksList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor curBooks = db.rawQuery("SELECT * FROM Books ORDER BY SubjectName ASC", null);

        if (curBooks.moveToFirst()){

            do { /* Fetching values from our books table by column index */
                String subjectName = curBooks.getString(curBooks.getColumnIndexOrThrow("SubjectName"));
                String booksName = curBooks.getString(curBooks.getColumnIndexOrThrow("BookName"));
                String writers = curBooks.getString(curBooks.getColumnIndexOrThrow("Author"));
                String editions = curBooks.getString(curBooks.getColumnIndexOrThrow("Edition"));

                /* Passing our fetched values to our book model class */
                BooksModel booksModel = new BooksModel();
                booksModel.setSubjectName(subjectName);
                booksModel.setBooksName(booksName);
                booksModel.setWriters(writers);
                booksModel.setEditions(editions);

                /* Setting our values in array list of studies books */
                booksList.add(booksModel);

            } while (curBooks.moveToNext());
            /* This keeps fetching books till cursor moves to next */
        }

        /* Closing cursor after getting values from books table */
        curBooks.close();

        /* Returning our book list after obtaining our values */
        return booksList;
    }

    /* This is used to obtain gradeScore and gradeStatus from grades table after user marks query is done */
    public ArrayList<GradeModel> getStudentGrade(String stdMarks){
        /* Creating an array list to obtain grades of student */
        ArrayList<GradeModel> gradeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Grades WHERE Exam_Marks = ?",
                new String[]{stdMarks});

        if (cursor.moveToFirst()){
            /* Fetching values from grade table by column index */
            String gdStatus = cursor.getString(cursor.getColumnIndexOrThrow("Grade_Status"));
            Double stdGrade = cursor.getDouble(cursor.getColumnIndexOrThrow("Total_GPA"));

            /* Passing our fetched values to grade model class */
            GradeModel gradeModel = new GradeModel();
            gradeModel.setGradeScore(String.valueOf(stdGrade));
            gradeModel.setGradeState(gdStatus);

            /* Setting our values in array list of std grades */
            gradeList.add(gradeModel);
        }

        /* Closing cursor after getting values from grades table */
        cursor.close();

        /* Returning our grade list after obtaining our values */
        return gradeList;
    }

    /* Here we are creating an array list to obtain notes data from database */
    public ArrayList<NotesModel> getStudyDrives(){
        /* Creating an array list of notes model class for calling table */
        ArrayList<NotesModel> notesModelArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        /* Here we are using one cursor query to fetch data from database */
        Cursor curNotes = db.rawQuery("SELECT * FROM Notes_Drive ORDER BY Subject ASC", null);

        if (curNotes.moveToFirst()){
            /* Getting data from our notes table when count is not zero */
            do {
                /* Fetching notes data by column index from our table */
                String notesTitle = curNotes.getString(curNotes.getColumnIndexOrThrow("Subject"));
                String notesLinks = curNotes.getString(curNotes.getColumnIndexOrThrow("NotesLink"));

                /* Sharing fetched data with our notes model data class */
                NotesModel notesModel = new NotesModel();
                notesModel.setNotesTitle(notesTitle);
                notesModel.setNotesLinks(notesLinks);

                /* Setting data with our notes array list for querying */
                notesModelArrayList.add(notesModel);

            } while (curNotes.moveToNext());
            /* This keeps getting values from table till very end */
        }
        /* Closing cursor after getting values from notes table */
        curNotes.close();

        /* Returning our notes list after obtaining our values */
        return notesModelArrayList;
    }
}
