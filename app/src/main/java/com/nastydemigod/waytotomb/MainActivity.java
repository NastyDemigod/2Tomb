package com.nastydemigod.waytotomb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText surname, name, otch, birthday, death, grave, area;
    private Spinner cemetery;
    protected String cemeteryItem;
    protected Integer cementeryPosithion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getId();
        this.setSpinner();



    }
    protected void getId (){
        //Получить id
        surname = findViewById(R.id.surname);
        name = findViewById(R.id.name);
        otch = findViewById(R.id.otch);
        birthday = findViewById(R.id.birthday);
        death = findViewById(R.id.death);
        cemetery = findViewById(R.id.cemetery);
        grave = findViewById(R.id.grave);
        area = findViewById(R.id.area);

    }

    public void birthday_click(View view) {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date     = day +"."+month+"."+year;
                birthday.setText(date);
            }
        },year, month, day);


        //Разобраться с цветом
        //datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.RED));


        datePickerDialog.show();
    }

    public void death_click(View view) {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date     = day +"."+month+"."+year;
                death.setText(date);
            }
        },year, month, day);
        datePickerDialog.show();
    }


    protected void setSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spCementery, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cemetery.setAdapter(adapter);

        cemetery.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Выбранный элемент спинера (кладбище)
        cemeteryItem = cemetery.getItemAtPosition(position).toString();
        cementeryPosithion = position;
        Log.d("Hello", "Элемент: "+ cemeteryItem+", позиция: "+cementeryPosithion);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    //Кнопка поиска
    public void find(View view) {
        Log.d("Hello", "нажаите на кнопку");
        if(surname.getText().toString().isEmpty()&&
            name.getText().toString().isEmpty()&&
            otch.getText().toString().isEmpty()&&
            birthday.getText().toString().isEmpty()&&
            death.getText().toString().isEmpty()&&
            cementeryPosithion == 0 &&
            grave.getText().toString().isEmpty()&&
            area.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Введите данные", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Ваши данные", Toast.LENGTH_SHORT).show();
        }
    }


}