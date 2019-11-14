package com.appsmontreal.notes.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.appsmontreal.notes.model.IModelListener;
import com.appsmontreal.notes.model.Note;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FireBaseNoteDAO implements INoteDAO {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceNote;

    public FireBaseNoteDAO() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceNote = mDatabase.getReference("Notes");

    }
    

    @Override
    public Note getNoteById(int id) {
        return null;
    }

    @Override
    public void getAllNotes(IModelListener daoListener) {
        mReferenceNote.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Note> notes = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Note aNote = keyNode.getValue(Note.class);
                    notes.add(aNote);
                    Log.i("===============> readNote ", aNote.getText() + ", " + aNote.getId());

                }
                daoListener.onGetAllNotes(notes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean deleteNoteById(int id) {
        return false;
    }

    @Override
    public boolean updateNote(Note note) {
        return false;
    }

    @Override
    public boolean insertNote(Note note) {
        final boolean[] isItSuccess = {false};
        Note updateNote = new Note(0,note.getText()); // FIXME: Use IDs the right way
        String key = mReferenceNote.push().getKey();
        // Log.i("==========> insert ", Integer.toString(id));
        mReferenceNote.child(key).setValue(updateNote).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                isItSuccess[0] = true;
            }
        });
        return isItSuccess[0];
    }
}
