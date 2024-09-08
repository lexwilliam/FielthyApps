package example.com.fielthyapps.Feature.Physical;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import example.com.fielthyapps.Feature.Smoker.SmokerActivity;
import example.com.fielthyapps.HomeActivity;
import example.com.fielthyapps.R;

public class BalkeActivity extends AppCompatActivity implements SensorEventListener, OnMapReadyCallback {
    private TextView tV_time, hari, tanggal;
    private EditText eT_tahun,eT_gender,eT_km,eT_beratbadan,eT_tinggibadan,eT_waktu;
    private String age,gender,beratbadan,tinggibadan,jaraktempuh,waktu;
    private Button btn_track,btn_test;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore fStore;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private int stepCount =0;
    String formattedDate;
    private long startTime;
    private float stepLengthMeters = 0.762f;
    private ImageView iV_back;
    private boolean isRunning = false;
    private CountDownTimer countDownTimer;

    private GoogleMap gMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private List<LatLng> pathPoints = new ArrayList<>();
    private Polyline polyline;
    private float totalDistance = 0;
    private Location lastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balke);
        tV_time = findViewById(R.id.tV_time);
        eT_tahun = findViewById(R.id.eT_balke_umur);
        eT_gender = findViewById(R.id.eT_balke_gender);
        eT_beratbadan = findViewById(R.id.eT_balke_berat);
        eT_tinggibadan = findViewById(R.id.eT_balke_tinggi);
        eT_waktu = findViewById(R.id.eT_balke_time);
        eT_km = findViewById(R.id.eT_balke_km);
        btn_track = findViewById(R.id.btn_track_balke);
        btn_test = findViewById(R.id.btn_lanjut_hasil_balke);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        long duration = TimeUnit.MINUTES.toMillis(15);
        startTime = System.currentTimeMillis();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        hari = findViewById(R.id.hari);
        tanggal = findViewById(R.id.tanggal);
        iV_back = findViewById(R.id.iV_kembali);

        iV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BalkeActivity.this, PhysicalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        tV_time.setText("15.00");
        eT_waktu.setText("15.00");
        checkUserData();
        disableEditText(eT_tahun);
        disableEditText(eT_gender);
        disableEditText(eT_km);
        disableEditText(eT_waktu);

        LocalDate currentDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formattedDate = currentDate.format(formatter);
        }

        DateTimeFormatter dayFormatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dayFormatter = DateTimeFormatter.ofPattern("EEEE", new Locale("id", "ID"));
        }
        String dayOfWeek = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dayOfWeek = currentDate.format(dayFormatter);
        }
        String dateString = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateString = currentDate.format(formatter);
        }

        hari.setText(dayOfWeek);
        tanggal.setText(dateString);

        if (stepCounterSensor == null){
            Toast.makeText(this, "Step counter not available", Toast.LENGTH_SHORT).show();
        }

        btn_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRunning) {

                    pathPoints.clear();
                    totalDistance = 0;
                    if (polyline != null) {
                        polyline.remove();
                    }
                    eT_km.setText(null);

                    btn_track.setText("Stop Tracking");
                    isRunning = true;
                    startLocationUpdates();
                    countDownTimer = new CountDownTimer(duration, 1000) {

                        @Override
                        public void onTick(long l) {
                            String sDuration = String.format(Locale.ENGLISH, "%02d:%02d",
                                    TimeUnit.MILLISECONDS.toMinutes(l),
                                    TimeUnit.MILLISECONDS.toSeconds(l) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
                            tV_time.setText(sDuration);
                        }

                        @Override
                        public void onFinish() {
                            stopLocationUpdates();
                            btn_track.setText("Start Tracking");
                            isRunning = false;
                        }
                    }.start();
                } else {
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    btn_track.setText("Start Tracking");
                    stopLocationUpdates();
                    isRunning = false;
                }
            }
        });

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inputData();

            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            createLocationRequest();
            startLocationUpdates();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.id_map);
        mapFragment.getMapAsync(this);

        createLocationRequest();

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateDistanceMeters(10)
                .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                .setWaitForAccurateLocation(true)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        if (!pathPoints.isEmpty()) {
                            LatLng lastPoint = pathPoints.get(pathPoints.size() - 1);
                            float distance = calculateDistance(lastPoint, latLng);
                            totalDistance += distance;
                            DecimalFormat decimalFormat = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.getDefault()));
                            eT_km.setText(decimalFormat.format(totalDistance));
                        }
                        pathPoints.add(latLng);
                        drawPolyline();
                    }
                }
            }

        };


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("GPS tidak aktif, aktifkan sekarang?")
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        createLocationRequest();
    }

    private void inputData() {
        age = eT_tahun.getText().toString();
        gender = eT_gender.getText().toString();
        beratbadan = eT_beratbadan.getText().toString();
        tinggibadan = eT_tinggibadan.getText().toString();
        jaraktempuh = eT_km.getText().toString().replace(",", ".");
        waktu = eT_waktu.getText().toString();

        try {
            float jarakTempuhValue = Float.parseFloat(jaraktempuh);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Format angka tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        if (age.isEmpty()) {
            eT_tahun.setError("Masukan Umur Terlebih Dahulu");
            eT_tahun.setFocusable(true);
        } else if (gender.isEmpty()) {
            eT_gender.setError("Masukan Jenis Kelamin Terlebih Dahulu");
            eT_gender.setFocusable(true);
        }else if (beratbadan.isEmpty()){
            eT_beratbadan.setError("Masukan Berat Badan Terlebih Dahulu");
            eT_beratbadan.setFocusable(true);
        }else if (tinggibadan.isEmpty()){
            eT_tinggibadan.setError("Masukan Tinggi badan Terlebih Dahulu");
            eT_tinggibadan.setFocusable(true);
        }else if (jaraktempuh.isEmpty()){
            eT_km.setError("Masukan Jarak Tempuh Terlebih Dahulu");
            eT_km.setFocusable(true);
        }else if (waktu.isEmpty()){
            eT_waktu.setError("Masukan Waktu Terlebih Dahulu");
            eT_waktu.setFocusable(true);
        }else {
            DocumentReference documentReference = fStore.collection("balke").document();
            HashMap hashMap = new HashMap();
            String uid = firebaseUser.getUid();
            hashMap.put("uid", uid);
            hashMap.put("id", documentReference.getId());
            hashMap.put("date", formattedDate);
            hashMap.put("age", age);
            hashMap.put("gender", gender);
            hashMap.put("beratbadan", beratbadan);
            hashMap.put("tinggibadan", tinggibadan);
            hashMap.put("jaraktempuh", jaraktempuh);
            hashMap.put("waktu", waktu);
            documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(BalkeActivity.this, "Berhasil input data balke", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BalkeActivity.this, HasilTestActivity.class);
                    intent.putExtra("type", "1");
                    intent.putExtra("age", age);
                    intent.putExtra("gender", gender);
                    intent.putExtra("beratbadan", beratbadan);
                    intent.putExtra("tinggibadan", tinggibadan);
                    intent.putExtra("jaraktempuh", jaraktempuh);
                    intent.putExtra("waktu", waktu);
                    startActivity(intent);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BalkeActivity.this, "Gagal Menambahkan Data physical", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void checkUserData() {
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        DocumentReference documentReference = fStore.collection("user").document(user.getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String umur = String.valueOf(value.getLong("umur"));
                eT_tahun.setText(umur);
                eT_gender.setText(value.getString("gender"));


            }
        });

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(isRunning) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
                stepCount = (int) sensorEvent.values[0];
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void startSensorRegistration() {
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d("Coba Sensor", "startSensorRegistration: " + sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL));
        }
    }

    private void stopSensorRegistration(){
        if (stepCounterSensor != null){
            sensorManager.unregisterListener(this);
            float distanceInKm = stepCount * stepLengthMeters/1000;
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
            symbols.setDecimalSeparator('.');

            DecimalFormat decimalFormat = new DecimalFormat("#.##", symbols);

            String formattedDistance = decimalFormat.format(distanceInKm);
            eT_km.setText(formattedDistance);
        }
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
    }

    private float calculateDistance(LatLng start, LatLng end) {
        float[] results = new float[1];
        Location.distanceBetween(start.latitude, start.longitude, end.latitude, end.longitude, results);
        return results[0];
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        gMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));
                            pathPoints.add(currentLocation);
                        }
                    }
                });

        polyline = gMap.addPolyline(new PolylineOptions().color(ContextCompat.getColor(this, R.color.red)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createLocationRequest();
                startLocationUpdates();
            }
        }
    }


    private void createLocationRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(5000);
            this.locationRequest = locationRequest;
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        if(isRunning) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            if (lastLocation != null) {
                                float dist = lastLocation.distanceTo(location);
                                if (dist > 5) {
                                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                    if (!pathPoints.isEmpty()) {
                                        LatLng lastPoint = pathPoints.get(pathPoints.size() - 1);
                                        float distance = calculateDistance(lastPoint, latLng);
                                        totalDistance += distance;
                                        DecimalFormat decimalFormat = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.getDefault()));
                                        eT_km.setText(decimalFormat.format(totalDistance));
                                    }
                                    pathPoints.add(latLng);
                                    drawPolyline();
                                    lastLocation = location;
                                }
                            } else {
                                lastLocation = location;
                            }
                        }
                    }
                }
            }, Looper.getMainLooper());
        }
    }

    private void drawPolyline() {
        if (gMap != null) {
            if (polyline != null) {
                polyline.remove();
            }
            PolylineOptions polylineOptions = new PolylineOptions()
                    .color(ContextCompat.getColor(this, R.color.red))
                    .width(5)
                    .addAll(pathPoints);
            polyline = gMap.addPolyline(polylineOptions);
            if (!pathPoints.isEmpty()) {
                LatLng lastPoint = pathPoints.get(pathPoints.size() - 1);
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastPoint, 15f));
            }
        }
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}