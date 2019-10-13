package com.appsmontreal.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import com.appsmontreal.notes.Serializer.ObjectSerializer;

public class CreateNoteActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private EditText noteEditText;
    private ObjectSerializer objectSerializer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        sharedPreferences = getSharedPreferences("NOTES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        noteEditText = findViewById(R.id.noteEditText);

    }
}
