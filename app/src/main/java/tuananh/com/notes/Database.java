package tuananh.com.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by anh.letuan2 on 11/14/2016.
 */

public class Database extends SQLiteOpenHelper{
    public static class Entries{
        public static final String TABLE_NOTES="notes";
        public static final String CONTENT ="content";
        public static final String ID ="noteId";

    }

    public static final String SQL_CREATE_TABLE_NOTES       = "Create table "+Entries.TABLE_NOTES+" (" +
            Entries.ID+ " integer primary key autoincrement,"+
            Entries.CONTENT+" varchar "+
            " );";
    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS" +Entries.TABLE_NOTES+" ;";


    public static final int DATABASE_VERSION    = 1;
    public static final String DATABASE_NAME    = "Notes";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_TABLE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(SQL_DELETE_TABLE);
        onCreate(database);
    }

    public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        onUpgrade(database, oldVersion, newVersion);
    }

    public Cursor readDatabase()
    {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "select * from "+ Entries.TABLE_NOTES;
        Cursor cursor =  database.rawQuery(query,null);
        cursor.moveToFirst();
        return cursor;
    }

    public void removeNote(int noteId)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        String selection = Entries.ID +" LIKE ?";
        String id = ""+noteId;
        String[] selectionArgs = {id};
        database.delete(Entries.TABLE_NOTES,selection,selectionArgs);
    }

    public void addNote(String newNote)
    {
        if(newNote.isEmpty())return;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Entries.CONTENT,newNote);
        database.insert(Entries.TABLE_NOTES,null,values);
    }

    public void updateNote(int noteId, String newNote)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(newNote.isEmpty())
        {
            removeNote(noteId);
            return;
        }
        values.put(Entries.CONTENT,newNote);
        String selection = Entries.ID + " like ?";
        String[] selcectionArgs = {""+noteId};
        database.update(Entries.TABLE_NOTES,values,selection,selcectionArgs);
    }

}
