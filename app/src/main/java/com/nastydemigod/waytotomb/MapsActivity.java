package com.nastydemigod.waytotomb;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private GoogleMap mMap;
    private String location;
    private Float latitude, longitude;
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        //Получить координаты
        Bundle arguments = getIntent().getExtras();
        location = arguments.get("location").toString();
        Log.d("mapME", "передача данных через активити: " + location);


        //Преобразование строки в число с плавающей точкой
        String[] split = location.split(", ");

        Log.d("mapME", "первый: " + split[0] + "\n второй: " + split[1]);

        latitude = Float.parseFloat(split[0]);
        longitude = Float.parseFloat(split[1]);


        //Получить разрешение на местоположение
        getLocationPermission();
    }



    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);

        mapFragment.getMapAsync(MapsActivity.this);
    }

    //Получить разрешение на местоположение
    private void getLocationPermission() {
        Log.d("mapME", "getLocationPermission: getting location permissions");
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    //По результату запроса разрешений
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("mapME", "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d("mapMe", "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d("mapME", "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    initMap();
                }
            }
        }
    }

    //Анимация Google камеры
    private void animateCamera(LatLng latLng, Float zoom){
        Log.d("mapME", "Камера animateCamera");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }

    //Инициализация карты
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("mapME", "onMapReady");

        //Если разрешение на местоположение есть
        if (mLocationPermissionsGranted) {
            Log.d("mapME", "Разрешение есть");

            //Маркер на локации выбранной могилы
            LatLng tomb = new LatLng(latitude, longitude);
            Log.d("mapME", "Новый маркер");
            mMap.addMarker(new MarkerOptions()
                    .position(tomb)
                    .title("Могила")
                    .icon(BitmapDescriptorFactory.defaultMarker(210))
            );
            animateCamera(tomb,DEFAULT_ZOOM);


            //Получение текущего местоположения
            Log.d("mapME", "getDeviceLocation");
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            try{
                //Если разрешение на местоположение есть
                if(mLocationPermissionsGranted){
                    Task location  = fusedLocationClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Log.d("mapME", "onComplete нашли локацию");
                                Location curerentLocation  = (Location) task.getResult();
                                if(curerentLocation!=null){
                                    //Маркер на месте пользователя
                                    LatLng user = new LatLng(curerentLocation.getLatitude(), curerentLocation.getLongitude());
                                    mMap.addMarker(new MarkerOptions()
                                            .position(user)
                                            .title("Пользователь")
                                            .icon(BitmapDescriptorFactory.defaultMarker(48)));
                                    animateCamera(user,DEFAULT_ZOOM);

                                    mMap.setMyLocationEnabled(true);


                                    //Построение маршрута от пользователя до могилы
                                    Log.d("mapME","Ломанные линии");
                                    Polyline polyline = googleMap.addPolyline(new PolylineOptions()
                                            .clickable(true)
                                            .add(
                                                    user,
                                                    tomb));
                                }else{
                                    Log.d("mapME", "Текущая локация null");
                                    Toast.makeText(MapsActivity.this, "Невозможно получить текущее местоположение", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                Log.d("mapME", "onComplete локация нулевая");
                                Toast.makeText(MapsActivity.this, "Невозможно получить текущее местоположение", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }catch (SecurityException e) {
                Log.d("mapME", "SecurityException: "+e.getMessage());
            }

        }
    }
}