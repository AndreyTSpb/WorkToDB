package potaskun.enot.worktodb;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserActivity extends AppCompatActivity {

    EditText nameBox;
    EditText yearBox;
    Button delButton;
    Button saveButton;

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long userId = 0; //Если из MainActivity не было передано id, то устанавливаем его значение 0, следовательно, у нас будет добавление, а не редактирование/удаление

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        nameBox = findViewById(R.id.name);
        yearBox = findViewById(R.id.year);
        delButton = findViewById(R.id.deleteButt);
        saveButton = findViewById(R.id.saveButton);

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase(); //открыли на запись

        Bundle extras = getIntent().getExtras(); // Получаем данные с MainActivity

        if(extras != null ){
            userId = extras.getLong("id"); //если выборка не пуста то ищем id и присваемваем его userId
        }
        // если userId больше 0 то это редактирование иначе добавление
        if(userId > 0) {
            // получаем элемент по id из бд
            userCursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE + " WHERE "
                    + DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
            userCursor.moveToFirst();
            nameBox.setText(userCursor.getString(1));
            yearBox.setText(String.valueOf(userCursor.getString(2)));
            userCursor.close();
        }else{
            // скрываем кнопку удаления
            delButton.setVisibility(View.GONE);
        }

    }

    public void save(View view) {
        ContentValues cv = new ContentValues();//Первый параметр метода - это ключ, а второй - значение
        cv.put(DatabaseHelper.COLUMN_NAME, nameBox.getText().toString());
        cv.put(DatabaseHelper.COLUMN_YEAR, Integer.parseInt(yearBox.getText().toString()));
        if(userId > 0 ){
            //передается название таблицы, объект ContentValues и критерий, по которому происходит обновление (в данном случае столбец id)
            db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(userId), null);
        }else {
            //принимает название таблицы, объект ContentValues с добавляемыми значениями. Второй параметр является необязательным: он передает столбец, в который надо добавить значение NULL
            db.insert(DatabaseHelper.TABLE, null, cv);
        }
        goHome();
    }

    public void delete(View view) {
        /*передается название таблицы, а также столбец, по которому происходит удаление, и его значение*/
        db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(userId)});
        goHome();
    }

    private void goHome() {
        // закрываем подключение
        db.close();
        // переход к главной activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
