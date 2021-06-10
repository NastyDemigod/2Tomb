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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        String sSurname, sName, sOtch, sBirthday, sDeath, sCemetery, sGrave, sArea;
        sSurname = surname.getText().toString();
        sName = name.getText().toString();
        sOtch = otch.getText().toString();
        sBirthday = birthday.getText().toString();
        sDeath = death.getText().toString();
        sCemetery = cemeteryItem;
        sGrave =  grave.getText().toString();
        sArea = area.getText().toString();

        if(sSurname.isEmpty()&&
            sName.isEmpty()&&
            sOtch.isEmpty()&&
            sBirthday.isEmpty()&&
            sDeath.isEmpty()&&
            cementeryPosithion == 0 &&
            sGrave.isEmpty()&&
            sArea.isEmpty())
        {
            Toast.makeText(this, "Введите данные", Toast.LENGTH_SHORT).show();
        }
        else
        {
            this.postRequest();
        }
    }

    //POST запрос
    private void postRequest(){
        Log.d("POST", "Метод post запроса");


        String sSurname, sName, sOtch, sBirthday, sDeath, sCemetery, sGrave, sArea;
        sSurname = surname.getText().toString();
        sName = name.getText().toString();
        sOtch = otch.getText().toString();
        sBirthday = birthday.getText().toString();
        sDeath = death.getText().toString();
        sCemetery = cemeteryItem;
        sGrave =  grave.getText().toString();
        sArea = area.getText().toString();





        OkHttpClient client = new OkHttpClient();

        String url = "http://185.117.152.68:3999/defunct/";
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject actulData = new JSONObject();
        try {
            Log.d("POST", "try полей");
            if(!sSurname.isEmpty()){
                Log.d("POST", "Фамилия");
                actulData.put("surename",sSurname);
            }
            if(!sName.isEmpty()){
                Log.d("POST", "Имя");
                actulData.put("name",sName);
            }
            if(!sOtch.isEmpty()){
                Log.d("POST", "Отчество");
                actulData.put("otch",sOtch);
            }
            if(!sBirthday.isEmpty()){
                Log.d("POST", "Дата рождения");
                actulData.put("birthday",sBirthday);
            }
            if(!sDeath.isEmpty()){
                Log.d("POST", "Дата смерти");
                actulData.put("death",sDeath);
            }
            if(!sCemetery.isEmpty()){
                Log.d("POST", "Кладбище "+sCemetery);
                actulData.put("cemetery",sCemetery);
            }
            if(!sGrave.isEmpty()){
                Log.d("POST", "Захоронение");
                actulData.put("grave",sGrave);
            }
            if(!sArea.isEmpty()){
                Log.d("POST", "Участок");
                actulData.put("area",sArea);
            }

        }
        catch (JSONException e)
        {
            Log.d("POST", "catch полей");
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, actulData.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Log.d("POST", "Запрос был отправлен");
            Response response = client.newCall(request).execute();
            this.parsResponseJSON(response.body().string());
            Log.d("POST", "Ответ: "+ response.body().string());
        }
        catch (IOException e)
        {
            Log.d("POST", "Произошло исключение");
            e.printStackTrace();
        }
    }

    private void parsResponseJSON(String responseBody){
        Log.d("POST", "Парсинг ответа");
        try {
            JSONArray defuncts = new JSONArray(responseBody);
            for(int i=0; i<defuncts.length();i++){
                Log.d("POST", "Цикл с i = "+i);
                try {
                    JSONObject defunct = defuncts.getJSONObject(i);
                    String fname = defunct.getString("surename");
                    surname.setText(fname);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}