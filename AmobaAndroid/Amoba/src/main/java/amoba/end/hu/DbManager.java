package amoba.end.hu;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Map;
import java.util.TreeMap;

public class DbManager {
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private boolean ignoreOpen = false;
    private static DbManager inst = null;

    public static final int ERR_SUCCESS = 0;
    public static final int ERR_ALREADY_EXISTS = -1;
    public static final int ERR_FAILED_TO_INSERT = -2;

    public static DbManager getInstance(){
        if(inst == null){
            inst = new DbManager();
        }

        return inst;
    }

    private DbManager() {
    }

    public void open(){
        dbHelper = DbHelper.getInstance();
        db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);
    }

    public void close(){
        dbHelper.close();
    }

    public void beginTransAction(){
        ignoreOpen = true;
        open();
        db.execSQL("BEGIN TRANSACTION;");
    }

    public void endTransAction(){
        db.execSQL("END TRANSACTION;");
        close();
        ignoreOpen = false;
    }

    public void reCreateDb(){
        open();
        dbHelper.onUpgrade(db,0,1);
        close();
    }

    public TreeMap<String, String> loadSettings(){
        if(!ignoreOpen){
            open();
        }

        Cursor c = db.query(DbHelper.DBSettings.TABLE_NAME, new String[]{
                DbHelper.DBSettings.COL_KEY,
                DbHelper.DBSettings.COL_VALUE,
        },null, null, null, null, DbHelper.DBSettings.COL_KEY + " ASC");

        TreeMap<String, String> settings = new TreeMap<String, String>();

        if(c.moveToFirst()){
            do{
                Pair<String, String> item = getSettingsItemByCursor(c);

                if(!settings.containsKey(item.getKey())){
                    settings.put(item.getKey(), item.getValue());
                }
            }
            while(c.moveToNext());
        }

        if(!ignoreOpen){
            close();
        }

        return settings;
    }

    public int saveItem(Map.Entry<String, String> item){
        if(!ignoreOpen){
            open();
        }

        ContentValues values = new ContentValues();

        values.put(DbHelper.DBSettings.COL_KEY, item.getKey());
        values.put(DbHelper.DBSettings.COL_VALUE, item.getValue());

        try{
            db.insertOrThrow(DbHelper.DBSettings.TABLE_NAME, null, values);
        }
        catch(Exception e){
            try{
                db.update(DbHelper.DBSettings.TABLE_NAME, values, DbHelper.DBSettings.COL_KEY + " = ?", new String[]{item.getKey()});
            }
            catch(Exception ex){
                return ERR_FAILED_TO_INSERT;
            }
            return ERR_FAILED_TO_INSERT;
        }

        if(!ignoreOpen){
            close();
        }

        return ERR_SUCCESS;
    }

    public void saveSettings(TreeMap<String, String> data){
        beginTransAction();

        for(Map.Entry<String, String> item: data.entrySet()){
            saveItem(item);
        }

        endTransAction();
    }

    private static Pair<String, String> getSettingsItemByCursor(final Cursor c){
        return new Pair<String, String>(
                c.getString(c.getColumnIndex(DbHelper.DBSettings.COL_KEY)),
                c.getString(c.getColumnIndex(DbHelper.DBSettings.COL_VALUE))
        );
    }
}
