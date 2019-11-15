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
    private List<Note> notes;

    public FireBaseNoteDAO() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceNote = mDatabase.getReference("Notes");
    }


    @Override
    public Note getNoteById(int id) {
        for (Note n : notes) {
            if (n.getId() == id) {
                return n;
            }
        }

        return null;
    }

    @Override
    public void getAllNotes(IModelListener daoListener) {
        mReferenceNote.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notes = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Note aNote = keyNode.getValue(Note.class);
                    notes.add(aNote);
                    Log.i("===============> readNote ", aNote.getText() + ", " + aNote.getId());

                }
                daoListener.onGetAllNotes(notes);//Charge ArrayList got it previously
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean deleteNoteById(int id) {

        final boolean[] isItSuccess = {true};
        mReferenceNote.child(String.valueOf(id)).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                isItSuccess[0] = false;

            }
        });
        return isItSuccess[0];
    }


    @Override
    public boolean updateNote(Note note) {
        final boolean[] isItSuccess = {true};
        mReferenceNote.child(String.valueOf(note.getId())).setValue(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                isItSuccess[0] = false;
            }
        });

        return isItSuccess[0];
    }

    @Override
    public boolean insertNote(Note note) {
        final boolean[] isItSuccess = {true};
        int id = notes.size() + 1 ;
        Note updateNote = new Note(id,note.getText()); // FIXME: Use IDs the right way
        String key = mReferenceNote.push().getKey();
        // Log.i("==========> insert ", Integer.toString(id));
        mReferenceNote.child(String.valueOf(id)).setValue(updateNote).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                isItSuccess[0] = false;
            }
        });
        return isItSuccess[0];
    }
}
