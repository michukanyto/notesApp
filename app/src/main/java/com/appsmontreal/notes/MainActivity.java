package com.appsmontreal.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.appsmontreal.notes.Serializer.ObjectSerializer;

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
    private ListView notesListView;
    private Intent intentCreateNote;
    private Intent intentDisplayNote;
    private SharedPreferences sharedPreferences;
    private ObjectSerializer objectSerializer;
    private SharedPreferences.Editor editor;
    private String note;
    private ArrayList<String> notes;
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
        intentCreateNote = new Intent(this,CreateNoteActivity.class);
        intentDisplayNote = new Intent(this,DisplayNoteActivity.class);
        sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        notes = new ArrayList<>();
        titles = new ArrayList<>();
        dateFormat = new SimpleDateFormat(FORMAT_PATTERN);
        reloadNotes();
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
                startActivityForResult(intentCreateNote,911);//Create Note
                break;

            case R.id.exit:
                finish();
                break;

            default:
                break;
        }

        return true;
    }

    //Handling the note that's coming from CreateNoteActivity and DisplayActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 911){//Create Note
            if (resultCode == RESULT_OK) {
                note = data.getStringExtra(NAME_NOTE);
                Log.i("----------------->", note);
                newNameNote = dateFormat.format(Calendar.getInstance().getTime());
                Log.i("----------------->", newNameNote);
                notes.add(note);
            }
        }else if(requestCode == 912) {//Display Note
            if (resultCode == RESULT_OK) {
                String noteUpdated = data.getStringExtra(NOTE_UPDATED);
                Log.i("----------------->", noteUpdated);
                modifyNote(noteUpdated);
            }
        }
        saveNotes();
    }


    private void getTitles() {//to handle a list with just one line
        String[] nameTitles;
        for (String s : notes) {
            nameTitles = s.split("\n");
            titles.add(nameTitles[0]);
        }
    }


    private void reloadNotes() {
        notes.clear();
        titles.clear();
        try {
            notes = (ArrayList<String>) objectSerializer.deserialize(sharedPreferences.getString(KEY_NOTE,objectSerializer.serialize(new ArrayList<String>())));
            getTitles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("------------>Array", notes.toString());
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,titles);
        notesListView.setAdapter(arrayAdapter);

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                index = i;
                intentDisplayNote.putExtra("Note",notes.get(i));
                Log.i("------------>item pushed ", notes.get(i));
//                startActivity(intentDisplayNote);
                startActivityForResult(intentDisplayNote,912);//Display Note
            }
        });

        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                index = i;
                launchDialog();
                return true;
            }
        });
    }


    private void saveNotes() {
        try {
            editor.putString(KEY_NOTE, objectSerializer.serialize(notes)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
        reloadNotes();
    }


    private void modifyNote(String update) {
        notes.set(index,update);
//        arrayAdapter.notifyDataSetChanged();
        saveNotes();
    }


    private void deleteNote() {
        notes.remove(index);
        arrayAdapter.remove(arrayAdapter.getItem(index));
        arrayAdapter.notifyDataSetChanged();
//        saveNotes();
    }


    private void launchDialog() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Alert")
                .setMessage("Do you want to delete this note?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("---------------->","Yes pressed");
                        deleteNote();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("---------------->","No pressed");
                    }
                })
                .show();
    }

}
