package com.appsmontreal.notes.model;

import com.appsmontreal.notes.model.Note;

import java.util.ArrayList;
import java.util.List;

public interface IModelListener {
    default void onGetAllNotes(List<Note> notes) {
        // Do nothing
    }

    default void onGetNoteById(Note note) {
        // Do nothing
    }
}
