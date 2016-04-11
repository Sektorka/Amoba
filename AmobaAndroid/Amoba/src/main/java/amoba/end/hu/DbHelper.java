package amoba.end.hu;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "amoba.dat";
    private static final int DB_VERSION = 3;
    private static DbHelper inst = null;

    public static class DBSettings{
        public static final String TABLE_NAME = "settings";

        public static final String COL_KEY = "key";
        public static final String COL_VALUE = "value";

        private static final String DB_CREATE_TABLE_SETTINGS = "CREATE TABLE IF NOT EXISTS \"" + TABLE_NAME + "\" (\n" +
                "\"" + COL_KEY + "\"  TEXT PRIMARY KEY NOT NULL,\n" +
                "\"" + COL_VALUE + "\"  TEXT DEFAULT NULL\n" +
                ");";

        private static final String DB_DROP_TABLE_SETTINGS = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public static DbHelper getInstance(){
        if(inst == null){
            inst = new DbHelper();
        }

        return inst;
    }

    private DbHelper() {
        super(StartActivity.instance(), DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBSettings.DB_CREATE_TABLE_SETTINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBSettings.DB_DROP_TABLE_SETTINGS);
        db.execSQL(DBSettings.DB_CREATE_TABLE_SETTINGS);
    }
}
