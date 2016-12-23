package tuananh.com.notes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by anh.letuan2 on 12/22/2016.
 */

public class EditorActivity extends RootActivity
{
    public static final int STATE_NEW_NOTE = 0;
    public static final int STATE_EDIT_NOTE= 1;
    protected int m_State=-1;
    protected int m_noteId=-1;
    public EditText editNote ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_editor);
        editNote = (EditText) findViewById(R.id.et_note);
        final NoteAlertDialog noteAlertDialog = new NoteAlertDialog();
        Button buttonDone = (Button) findViewById(R.id.bt_done_edit);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentNote = editNote.getText().toString();
                if(isEmptyNote(currentNote)) noteAlertDialog.show(getFragmentManager(),"");
                else
                {
                    if (m_State == STATE_NEW_NOTE)
                        myDatabase.addNote(currentNote);
                    else {
                        myDatabase.updateNote(m_noteId, currentNote);
                    }
                    goToMain();
                }
            }
        });

        //check if we are editing a note or create a new one
        Intent intent = getIntent();
        String currentNote = intent.getStringExtra(MainActivity.NOTE_T0_EDIT_CONTENT);
        if(currentNote!=null)
        {
            m_State= STATE_EDIT_NOTE;
            editNote.append(currentNote);
            m_noteId = intent.getIntExtra(MainActivity.NOTE_T0_EDIT_ID,-1);
        }
        else m_State= STATE_NEW_NOTE;
    }
    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMain();
    }

    private boolean isEmptyNote(String note)
    {
        Log.d(TAG, "isEmptyNote: ["+note+"]");
        if(note.isEmpty()) return true;

        for (char c: note.toCharArray()) {

            if((c!='\n')&&(c!=' ')&&(c!='\t'))
            {
                Log.d(TAG, "isEmptyNote: ["+c+"]");
                return false;
            }
        }

        return true;
    }


    //go to Home screen where have all the notes
    public void goToMain()
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private class NoteAlertDialog extends DialogFragment{
        public boolean m_willSave=true;
        public boolean m_willBackToMain = true;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
            //press Yes to save anyway, No to back to Main without saving and Cancel to continue edit
            builder.setMessage("Your note is totally meaningless.Save it??")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //save note to database and go to main screen
                            EditText noteEditor = (EditText) findViewById(R.id.et_note);
                            if (m_State == STATE_NEW_NOTE)
                                myDatabase.addNote(noteEditor.getText().toString());
                            else {
                                myDatabase.updateNote(m_noteId, noteEditor.getText().toString());
                            }
                            goToMain();
                        }
                    })
                    .setNeutralButton("Hell no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //go to main without saving
                            goToMain();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog m_willSave=false;
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
