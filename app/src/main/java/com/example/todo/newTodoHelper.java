package com.example.todo;

public class newTodoHelper {
    String TITLE,DESCRIPTION,DATE;

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

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

    public newTodoHelper(String TITLE, String DESCRIPTION,String DATE) {
        this.TITLE = TITLE;
        this.DESCRIPTION = DESCRIPTION;
        this.DATE=DATE;
    }
}
