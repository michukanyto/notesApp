package com.appsmontreal.notes.dao;

import android.app.Application;
import android.content.Context;

import com.appsmontreal.notes.view.MainActivity;

public class DAOFactory {

   public static final String SQLITE = "sqlite";
    public static final String FIRE_BASE = "fire_base";


    public static INoteDAO getNoteDAO(String dataSource, Context context) {
        switch (dataSource) {
            case SQLITE:
                return new SQLiteNote1DAO(context);
            case FIRE_BASE:
                return  new FireBaseNoteDAO();
            default:
                throw new UnsupportedOperationException();
        }
    }
}
