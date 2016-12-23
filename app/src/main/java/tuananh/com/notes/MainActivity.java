package tuananh.com.notes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends RootActivity {

    protected final static String NOTE_T0_EDIT_CONTENT ="NoteToEditContent";
    protected final static String NOTE_T0_EDIT_ID ="NoteToEditId";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(myDatabase ==null) myDatabase = new Database(getApplicationContext());
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
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: ");
        System.exit(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        loadListNotes();
    }

    void loadListNotes()
    {
        LinearLayout listNotes = (LinearLayout) findViewById(R.id.list_notes);
        listNotes.removeAllViews();
        Cursor cursor = myDatabase.readDatabase();
        if(cursor.moveToFirst())
        {
            do{
                String text;
                int id;
                text = cursor.getString(cursor.getColumnIndex(Database.Entries.CONTENT));
                id = cursor.getInt(cursor.getColumnIndex(Database.Entries.ID));
                addNoteView(text,id);
                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }
    }


    void addNoteView(String newNote, final int noteId)
    {
        LinearLayout listNotes = (LinearLayout) findViewById(R.id.list_notes);
        final TextView note = new TextView(this);
        note.setId(noteId);
        note.setBackgroundResource(R.drawable.note_background);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout .LayoutParams.WRAP_CONTENT);
        layoutParam.setMargins(20,7,20,7);
        note.setLayoutParams(layoutParam);
        note.setText(newNote);
        note.setPadding(10,20,0,20);
        note.setTextSize(TypedValue.COMPLEX_UNIT_PT,10);
        note.setTextColor(Color.parseColor("#000000"));
        note.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d(TAG, "onLongClick: ");
                RemoveNoteDialog removeNoteDialog= new RemoveNoteDialog();
                removeNoteDialog.noteId =noteId;
                removeNoteDialog.show(getFragmentManager(),"");
                return true;
            }
        });
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                goToEditor(note.getText().toString(),note.getId());
            }
        });
        listNotes.addView(note);
    }

   public void goToEditor()
   {
       Intent intent = new Intent(this,EditorActivity.class);
       startActivity(intent);
   }

    public void goToEditor(String currentNote,int noteId)
    {
        Intent intent = new Intent(this,EditorActivity.class);
        if(currentNote!=null) intent.putExtra(NOTE_T0_EDIT_CONTENT,currentNote);
        intent.putExtra(NOTE_T0_EDIT_ID,noteId);
        startActivity(intent);
    }

    public class RemoveNoteDialog extends DialogFragment {
        public int noteId=-1;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Delete?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(noteId!=-1)
                            myDatabase.removeNote(noteId);
                            loadListNotes();
                        }
                    })
                    .setNegativeButton("Hell no", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
