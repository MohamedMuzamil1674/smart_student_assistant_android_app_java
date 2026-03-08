package com.example.smartstudentassistant.booksearchings;

public class BooksModel {

    String subjectName;

    String booksName;
    String writers;
    String editions;

    public BooksModel(){
        /* Default constructor required for calling this class */
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getBooksName() {
        return booksName;
    }

    public void setBooksName(String booksName) {
        this.booksName = booksName;
    }

    public String getWriters() {
        return writers;
    }

    public void setWriters(String writers) {
        this.writers = writers;
    }

    public String getEditions() {
        return editions;
    }

    public void setEditions(String editions) {
        this.editions = editions;
    }
}
