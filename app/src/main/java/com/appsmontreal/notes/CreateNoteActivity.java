package com.appsmontreal.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appsmontreal.notes.Serializer.ObjectSerializer;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText noteEditText;
    private Intent returnIntent;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        noteEditText = findViewById(R.id.noteEditText);
        saveButton = findViewById(R.id.saveButton);
        saveNote();

    }

    private void saveNote() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnIntent = new Intent();
                returnIntent.putExtra("NOTE",noteEditText.getText().toString());
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });
    }


}
