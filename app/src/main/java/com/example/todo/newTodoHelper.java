package com.example.todo;

public class newTodoHelper {
    String TITLE,DESCRIPTION;

    public String getTITLE() {
        return TITLE;
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

    public newTodoHelper() {
    }

    public newTodoHelper(String TITLE, String DESCRIPTION) {
        this.TITLE = TITLE;
        this.DESCRIPTION = DESCRIPTION;
    }
}
