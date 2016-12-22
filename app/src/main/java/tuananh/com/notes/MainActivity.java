package tuananh.com.notes;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.camera2.params.BlackLevelPattern;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends RootActivity {

    public int m_currentLayout;

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
    protected void onResume() {
        super.onResume();
        Cursor cursor = myDatabase.readDatabase();
        if(cursor.moveToFirst())
        {
            do{
                String text, id;
                text = cursor.getString(cursor.getColumnIndex(Database.Entries.CONTENT));
//                id = cursor.getString(cursor.getColumnIndex(Database.Entries._ID));
                Log.d(TAG, "onResume: "+text);
//                Log.d(TAG, "onResume: "+id);
                addNewNote(text);
                cursor.moveToNext();
            }while (cursor.isAfterLast());
        }
    }

    void addNewNote(String newNote)
    {
        LinearLayout listNotes = (LinearLayout) findViewById(R.id.list_notes);
        TextView note = new TextView(this);
        ViewGroup.LayoutParams layoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        note.setLayoutParams(layoutParam);
        note.setText(newNote);
        note.setPadding(7,7,7,7);
        note.setTextSize(TypedValue.COMPLEX_UNIT_PT,11);
        note.setTextColor(Color.parseColor("#000000"));
        note.setBackgroundColor(Color.parseColor("#f9e954"));
        listNotes.addView(note);
    }

   public void goToEditor()
   {
       Intent intent = new Intent(this,EditorActivity.class);
       startActivity(intent);
   }

}
