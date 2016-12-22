package tuananh.com.notes;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends RootActivity {

    public int m_currentLayout;
    protected final static String MESSAGE_TO_EDITOR ="MessageToEditor";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_currentLayout =0;
        if(myDatabase ==null) myDatabase = new Database(getApplicationContext());
        Button buttonAddNote = (Button) findViewById(R.id.bt_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                goToEditor(null);
            }
        });

    }
    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinearLayout listNotes = (LinearLayout) findViewById(R.id.list_notes);
        listNotes.removeAllViews();
        Cursor cursor = myDatabase.readDatabase();
        if(cursor.moveToFirst())
        {
            do{
                String text, id;
                text = cursor.getString(cursor.getColumnIndex(Database.Entries.CONTENT));
//                id = cursor.getString(cursor.getColumnIndex(Database.Entries._ID));
                Log.d(TAG, "onResume: "+text);
//                Log.d(TAG, "onResume: "+id);
                addNoteView(text);
                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }
    }

    void addNoteView(final String newNote)
    {
        LinearLayout listNotes = (LinearLayout) findViewById(R.id.list_notes);
        TextView note = new TextView(this);
        note.setBackgroundResource(R.drawable.note_background);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout .LayoutParams.WRAP_CONTENT);
        layoutParam.setMargins(20,7,20,7);
        note.setLayoutParams(layoutParam);
        note.setText(newNote);
        note.setPadding(10,20,0,20);
        note.setTextSize(TypedValue.COMPLEX_UNIT_PT,10);
        note.setTextColor(Color.parseColor("#000000"));
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEditor(newNote);
            }
        });
        listNotes.addView(note);
    }

   public void goToEditor(String currentNote)
   {
       Intent intent = new Intent(this,EditorActivity.class);
       EditText editNote = (EditText) findViewById(R.id.et_note);
       if(currentNote!=null) intent.putExtra(MESSAGE_TO_EDITOR,currentNote);
       startActivity(intent);
   }

}
