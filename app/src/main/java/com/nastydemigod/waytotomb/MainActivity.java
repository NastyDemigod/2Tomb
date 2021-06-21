package com.nastydemigod.waytotomb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static final int ERROR_DIALOG_REQUEST = 9001;

    private EditText surname, name, otch, birthday, death, grave, area;
    private Spinner cemetery;
    protected String cemeteryItem;
    protected Integer cementeryPosithion;
    private ListView list_defunct;
    private DefunctAdapter defunctAdapter;
    private TextView location;

    private List<Defunct> defunctList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("post", "onCreate");
        this.getId();
        this.setSpinner();
        this.setOnClickItem();


        //В строке Фамиия только буквы
        surname.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(source.equals("")){ // для пробела
                    return source;
                }
                if(source.toString().matches("[a-zA-Zа-яА-Я]+")){
                    return source;
                }
                return "";
            }
        }});

        //В строке Имя только буквы
        name.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(source.equals("")){ // для пробела
                    return source;
                }
                if(source.toString().matches("[a-zA-Zа-яА-Я]+")){
                    return source;
                }
                return "";
            }
        }});

        //В строке Отчество только буквы
        otch.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(source.equals("")){ // для пробела
                    return source;
                }
                if(source.toString().matches("[a-zA-Zа-яА-Я]+")){
                    return source;
                }
                return "";
            }
        }});


        //Создание маски для поля Захоронение
        Slot[] slotsGrave = new UnderscoreDigitSlotsParser().parseSlots("#__-____-______");
        FormatWatcher formatWatcherGrave = new MaskFormatWatcher(
                MaskImpl.createTerminated(slotsGrave)
        );
        formatWatcherGrave.installOn(grave);

        //Создание маски для поля Участка
        Slot[] slotsArea = new UnderscoreDigitSlotsParser().parseSlots("&__-____-______");
        FormatWatcher formatWatcherArea= new MaskFormatWatcher(
                MaskImpl.createTerminated(slotsArea)
        );
        formatWatcherArea.installOn(area);

    }

    //Получаем объекты о Id
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

        list_defunct = findViewById(R.id.list_defunct);

        location = findViewById(R.id.location);

    }

    //Календарь для воода даты рождения
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

        datePickerDialog.show();
    }

    //Каленадрь для ввода даты смерти
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

    //Выпадающий список кладбищ
    protected void setSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spCementery, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cemetery.setAdapter(adapter);

        cemetery.setOnItemSelectedListener(this);
    }

    //Выбор элемента выпадающего списка
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
        String sSurname, sName, sOtch, sBirthday, sDeath, iCementery,  sGrave, sArea;
        Integer pCementery;
        sSurname = surname.getText().toString();
        sName = name.getText().toString();
        sOtch = otch.getText().toString();
        sBirthday = birthday.getText().toString();
        sDeath = death.getText().toString();
        pCementery = cementeryPosithion;
        iCementery = cemeteryItem;
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
            defunctList = new ArrayList<>();


            //        запуск побочного потока
            Thread thread = new Thread(new Runnable() {
                public void run() {

                    postRequest(sSurname, sName, sOtch, sBirthday,sDeath,iCementery,pCementery,sGrave,sArea);
                }
            });
//        запуск побочного потока
            thread.start();

        }


    }

    //POST запрос
    private void postRequest(String sSurname, String sName, String sOtch, String sBirthday, String sDeath, String iCementery,Integer pCementery, String sGrave, String sArea){
        Log.d("POST", "Метод post запроса");

        OkHttpClient client = new OkHttpClient();

        String url = "http://185.117.152.68:3999/defunct";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject actulData = new JSONObject();
        try {
            Log.d("POST", "try полей");
            if(!sSurname.isEmpty()){
                Log.d("POST", "Фамилия");
                actulData.put("surname",sSurname);
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
            if(pCementery!=0){
                Log.d("POST", "Кладбище "+iCementery);
                actulData.put("cemetery",iCementery);
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
        Log.d("POST", "Запрос: "+actulData.toString());
        RequestBody body = RequestBody.create(JSON, actulData.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try {

            Log.d("POST", "Запрос был отправлен");
            Response response = client.newCall(request).execute();
            if(response.code()==200){
                // Реализовать проверку на статус кода и только тогда запускать
                parsResponseJSON(response.body().string());
            }else{
                //Сообшение о проблеме
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Неполадки на сервере", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        }
        catch (IOException e)
        {
            Log.d("POST", "Произошло исключение");
            e.printStackTrace();
            Log.d("POST", "Ошибка: "+ e.getMessage());
        }

    }

    //Парсинг ответа
    private void parsResponseJSON(String responseBody){
        Log.d("POST", "Парсинг ответа");
        try {
            JSONArray defuncts = new JSONArray(responseBody);

            for(int i=0; i<defuncts.length();i++){
                Log.d("POST", "Цикл с i = "+i);
                try {

                    JSONObject defunct = defuncts.getJSONObject(i);


                    String fname = defunct.getString("surname");
                    Log.d("POST", "Фамилия: "+ fname);

                    String name = defunct.getString("name");
                    Log.d("POST", "Имя: "+ name);

                    String otch = defunct.getString("otch");
                    Log.d("POST", "Отчество: "+ otch);

                    String FNO = fname +" "+ name + " "+ otch;

                    String birthday = defunct.getString("birthday");
                    Log.d("POST", "Дата рождения: "+ birthday);

                    String death = defunct.getString("death");
                    Log.d("POST", "Дата смерти: "+ death);

                    String dates = birthday + "-"+ death;

                    String grave = defunct.getString("grave");
                    Log.d("POST", "Захоронение: "+ grave);

                    String area = defunct.getString("area");
                    Log.d("POST", "Участок: "+ area);

                    String cemetery = defunct.getString("cemetery");
                    Log.d("POST", "Кладбище: "+ cemetery);

                    String location = defunct.getString("location");
                    Log.d("POST", "Координаты: "+ location);


                    Defunct defunctEl = new Defunct(FNO,dates,cemetery,grave,area,location);

                    defunctList.add(defunctEl);
                    Log.d("post", "Лист: "+ defunctList.toString());



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("post", "Адаптер");
                    defunctAdapter = new DefunctAdapter(getApplicationContext(), R.layout.list_defunct, defunctList);
                    list_defunct.setAdapter(defunctAdapter);
                }
            });



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    //Нажатие на элемент, запуск карты
    private void setOnClickItem(){
        Log.d("mapME", "Нажатие на элемент");
        if(isServiceOK()){
            Log.d("mapME", "Нажали, проверили, что с сервисами все в пордяке");
            list_defunct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("mapME", "Нажали!!!!");
                    Defunct def = defunctList.get(position);
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("location",def.getLocahion());
                    startActivity(intent);


                }
            });
        }
    }


    //Проверка что с сервисами Google все в порядке
    public boolean isServiceOK() {
        Log.d("mapME", "isServicesOK: checking google service version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available== ConnectionResult.SUCCESS) {
            //все хорошо
            Log.d("mapME", "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //получили ошибку но знаем какую
            Log.d("mapMe", "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this, "Вы не можете вызвать карту", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}