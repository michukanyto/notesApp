package com.appsmontreal.notes.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.appsmontreal.notes.R;
import com.appsmontreal.notes.controller.NoteController;
import com.appsmontreal.notes.dao.DAOFactory;
import com.appsmontreal.notes.dao.INoteDAO;
import com.appsmontreal.notes.foundation.ObjectSerializer;
import com.appsmontreal.notes.model.Note;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final String FILE_NAME = "DATA";
    public static final String NAME_NOTE = "NOTE" ;
    public static final String KEY_NOTE = "KEY_NOTE" ;
    public static final String FORMAT_PATTERN = "yy-mm-dd hh:mm";
    public static final String NOTE_UPDATED = "NOTE_UPDATED" ;
    public static final String DATA_SOURCE_NAME = "fire_base";
    private NoteController noteController;
    private ListView notesListView;
    private EditText filterEditText;
    private Intent intentCreateNote;
    private Intent intentDisplayNote;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ArrayList<Note> notes;
    private ArrayList<String> titles;
    private String newNameNote;
    private  DateFormat dateFormat;
    private ArrayAdapter<String> arrayAdapter;
    private int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notesListView = findViewById(R.id.notesListView);
        intentCreateNote = new Intent(this, CreateNoteActivity.class);
        intentDisplayNote = new Intent(this, DisplayNoteActivity.class);
        sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        notes = new ArrayList<>();
        titles = new ArrayList<>();
        dateFormat = new SimpleDateFormat(FORMAT_PATTERN);

        noteController =  NoteController.getInstance(DATA_SOURCE_NAME,this);

        prepareFilter();
        reloadNotes();
    }

   // Filter
    private void prepareFilter() {
        filterEditText = findViewById(R.id.filterEditText);
        filterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (MainActivity.this).arrayAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    //Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.addNote:
                startActivity(intentCreateNote);
//                startActivityForResult(intentCreateNote,911);//Create Note
                break;

            case R.id.exit:
                finish();
                break;

            default:
                break;
        }

        return true;
    }


    private void getTitles() {//to handle a list with just one line
        try {
            String[] nameTitles;
            for (Note n : notes) {
                nameTitles = n.getText().split("\n");
                titles.add(nameTitles[0]);
                Log.i("============> Titles",n.getText());
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reloadNotes() {
        notes.clear();
        titles.clear();
        notes = noteController.readAllNotes();
        getTitles();
        Log.i("------------>Array", notes.toString());
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        notesListView.setAdapter(arrayAdapter);

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                Note displayNote = notes.get(index);
                Log.i("FROM ======== MAIN =>", displayNote.getText() + " " + displayNote.getId());
                intentDisplayNote.putExtra(NAME_NOTE,displayNote);
                startActivity(intentDisplayNote);
            }
        });

        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                launchDialog();
                return true;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reloadNotes();
    }



    private void deleteNote() {
        Log.i("===================> ", Integer.toString(notes.get(index).getId()));
        boolean isItDeleted = noteController.deleteNote(notes.get(index).getId());

        if (isItDeleted) {
            notes.remove(index);
            Toast.makeText(this,"Note deleted successfully", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,"Note was no deleted", Toast.LENGTH_LONG).show();
        }

        arrayAdapter.notifyDataSetChanged();
        reloadNotes();
    }


    private void launchDialog() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_delete)
                .setTitle("Delete")
                .setMessage("Do you want to delete this note?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("---------------->","Yes pressed");
                        deleteNote();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}
