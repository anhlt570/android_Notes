package tuananh.com.notes;

import android.app.Activity;

/**
 * Created by anh.letuan2 on 12/22/2016.
 */

public class RootActivity extends Activity{
    public static final int MAIN_LAYOUT     = 0;
    public static final int EDITOR_LAYOUT   = 1;
    public static final String TAG ="Anhlt2";
    public Database myDatabase = new Database(this) ;

}
