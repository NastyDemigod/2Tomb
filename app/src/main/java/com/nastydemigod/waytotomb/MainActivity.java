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
            this.postRequest(sSurname, sName, sOtch, sBirthday,sDeath,sCemetery,sGrave,sArea);
        }
    }

    //POST запрос
    private void postRequest(String surname, String name, String otch, String birthday, String death, String cemetery, String grave, String area){

        OkHttpClient client = new OkHttpClient();

        String url = "http://185.117.152.68:3999/defunct/";
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject actulData = new JSONObject();
        try {
            if(!surname.isEmpty()){
                actulData.put("surename",surname);
            }
            if(!name.isEmpty()){
                actulData.put("name",name);
            }
            if(!otch.isEmpty()){
                actulData.put("otch",otch);
            }
            if(!birthday.isEmpty()){
                actulData.put("birthday",birthday);
            }
            if(!death.isEmpty()){
                actulData.put("death",death);
            }
            if(!cemetery.isEmpty()){
                actulData.put("cemetery",cemetery);
            }
            if(!grave.isEmpty()){
                actulData.put("grave",grave);
            }
            if(!area.isEmpty()){
                actulData.put("area",area);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, actulData.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}