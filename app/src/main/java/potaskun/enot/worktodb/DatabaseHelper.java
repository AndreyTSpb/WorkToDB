package potaskun.enot.worktodb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "user_bd.db"; // название бд
    private static final int SCHEMA = 2; // версия базы данных
    static final String TABLE = "users"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_PHONE = "phone";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users ("+ COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
                + " TEXT," + COLUMN_YEAR + " INTEGER," + COLUMN_PHONE + " INTEGER );");
        // добавление начальных данных
        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME
                + ", " + COLUMN_YEAR  + ", " + COLUMN_PHONE + ") VALUES ('Sten Smith', 1981, 8595555555);");
        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME
                + ", " + COLUMN_YEAR  + ", " + COLUMN_PHONE + ") VALUES ('Роджер Смит', 1881, 4545);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
