package tuananh.com.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by anh.letuan2 on 12/22/2016.
 */

public class EditorActivity extends RootActivity
{
    public EditText editNote ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_editor);
        editNote = (EditText) findViewById(R.id.et_note);

        Button buttonDone = (Button) findViewById(R.id.bt_done_edit);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDatabase.addNote(editNote.getText().toString());
                goToMain();
            }
        });

        Intent intent = getIntent();
        String messageFromMain = intent.getStringExtra(MainActivity.MESSAGE_TO_EDITOR);
        if(messageFromMain!=null) editNote.setText(messageFromMain);
    }

    public void goToMain()
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


}
