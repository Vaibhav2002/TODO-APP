package com.example.todo;

public class myDoes {
    String TITLE,DESCRIPTION;

    public String getTITLE() {
        return TITLE;
    }

    public myDoes() {
    }

    public myDoes(String TITLE, String DESCRIPTION) {
        this.TITLE = TITLE;
        this.DESCRIPTION = DESCRIPTION;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }
}
