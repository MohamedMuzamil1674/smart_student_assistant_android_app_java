package com.example.smartstudentassistant.gradesearching;

public class GradeModel {
    String gradeScore;
    String gradeState;

    public GradeModel(){
        /* Default constructor required for calling this class */
    }

    public String getGradeScore()
    {
        return gradeScore;
    }

    public void setGradeScore(String gradeScore)
    {
        this.gradeScore = gradeScore;
    }

    public String getGradeState()
    {
        return gradeState;
    }

    public void setGradeState(String gradeState)
    {
        this.gradeState = gradeState;
    }
}
