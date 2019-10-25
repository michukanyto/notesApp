package com.appsmontreal.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayNoteActivity extends AppCompatActivity {
    private String note;
    private Intent intent;
    private TextView noteTextView;
    private Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);
        intent = getIntent();
        note = intent.getStringExtra("Note");
        Log.i("display --------------------> ", note);
        noteTextView = findViewById(R.id.noteTextView);
        getNoteText();
        closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void getNoteText() {
        noteTextView.setText(note);
    }
}