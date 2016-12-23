package tuananh.com.notes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends RootActivity {

    protected final static String NOTE_T0_EDIT_CONTENT = "NoteToEditContent";
    protected final static String NOTE_T0_EDIT_ID = "NoteToEditId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (myDatabase == null) myDatabase = new Database(getApplicationContext());
        Button buttonAddNote = (Button) findViewById(R.id.bt_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                goToEditor();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        loadListNotes();
    }

    void loadListNotes() {
        LinearLayout listNotes = (LinearLayout) findViewById(R.id.list_notes);
        listNotes.removeAllViews();
        Cursor cursor = myDatabase.readDatabase();
        if (cursor.moveToFirst()) {
            do {
                String text;
                int id;
                text = cursor.getString(cursor.getColumnIndex(Database.Entries.CONTENT));
                id = cursor.getInt(cursor.getColumnIndex(Database.Entries.ID));
                addNoteView(text, id);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
    }


    void addNoteView(final String noteContent, final int noteId) {
        LinearLayout listNotes = (LinearLayout) findViewById(R.id.list_notes);
        final TextView note = new TextView(this);
        note.setId(noteId);
        note.setBackgroundResource(R.drawable.note_background);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParam.setMargins(20, 7, 20, 7);
        note.setLayoutParams(layoutParam);
        note.setText(noteContent);
        note.setPadding(10, 20, 0, 20);
        note.setTextSize(TypedValue.COMPLEX_UNIT_PT, 11);
        note.setTextColor(Color.parseColor("#000000"));
        note.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d(TAG, "onLongClick: ");
                showListOptionsDialog(noteId, noteContent);
                return true;
            }
        });
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                goToEditor(note.getText().toString(), note.getId());
            }
        });
        listNotes.addView(note);
    }

    public void goToEditor() {
        Intent intent = new Intent(this, EditorActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToEditor(String currentNote, int noteId) {
        Intent intent = new Intent(this, EditorActivity.class);
        if (currentNote != null) intent.putExtra(NOTE_T0_EDIT_CONTENT, currentNote);
        intent.putExtra(NOTE_T0_EDIT_ID, noteId);
        startActivity(intent);
        finish();
    }

    private void showListOptionsDialog(final int noteId, final String noteContent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] listOptions = {"Copy", "Remove", "Edit"};
        builder.setItems(listOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0://copy
                    {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        clipboard.setText(noteContent);
                        Log.d(TAG, "onClick item: copy");

                        break;
                    }
                    case 1://remove
                    {
                        if (noteId != -1)
                            myDatabase.removeNote(noteId);
                        loadListNotes();
                        Log.d(TAG, "onClick item: remove");
                        break;
                    }
                    case 2://edit
                    {
                        goToEditor(noteContent, noteId);
                        Log.d(TAG, "onClick item: edit");
                        break;
                    }
                    default:
                        break;
                }
            }
        });
        builder.show();
    }
}
