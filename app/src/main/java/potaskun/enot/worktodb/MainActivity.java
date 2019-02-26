package potaskun.enot.worktodb;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ListView userList;
    TextView header;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    /**
     * создание объекта SQLiteOpenHelper
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        header = findViewById(R.id.header);
        userList = findViewById(R.id.list);

        databaseHelper = new DatabaseHelper(getApplicationContext());
    }

    /**
     * инициализация объектов для работы с базой
     */
    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = databaseHelper.getReadableDatabase();

        //получаем данные из бд в виде курсора
        userCursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[] {
                DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_YEAR, DatabaseHelper.COLUMN_PHONE
        };
        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(
                this, //Первым параметром выступает контекст, с которым ассоциируется адаптер
                R.layout.tree_line_list_item,//ресурс разметки интерфейса, который будет использоваться для отображения результатов выборки
                userCursor,//курсор
                headers, // список столбцов из выборки, которые будут отображаться в разметке интерфейса
                new int[]{
                        //элементы внутри ресурса разметки, которые будут отображать значения столбцов из четвертого параметра
                            R.id.text1,
                            R.id.text2,
                            R.id.text3
                    },
                0 //флаги, задающие поведения адаптера
                );
        header.setText("Найдено элементов: " + String.valueOf(userCursor.getCount()));
        userList.setAdapter(userAdapter); //отправляем собраный адаптер в список

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }
    // по нажатию на кнопку запускаем UserActivity для добавления данных
    public void add(View view) {
        Intent intent = new Intent(MainActivity.this, UserActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }
}
