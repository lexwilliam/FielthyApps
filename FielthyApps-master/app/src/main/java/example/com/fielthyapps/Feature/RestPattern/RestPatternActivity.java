package example.com.fielthyapps.Feature.RestPattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import example.com.fielthyapps.Feature.RestPattern.HasilTestRestActivity;
import example.com.fielthyapps.HomeActivity;
import example.com.fielthyapps.R;

public class RestPatternActivity extends AppCompatActivity {
    private Button btn_tracking_sleep, btn_keterangan_rest;
    private int status = 0;
    private TextView tV_duration, tV_dayname, tV_tanggal, tV_quality, tV_desc_quality, tV_duration_sleep;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore fStore;
    private String formattedTime;
    private String formattedDate;
    private String dayName, type_ket;
    private long hours;
    private ImageView iV_back;
    private String uid, id, date, timesleep, day, status_ket;
    private BroadcastReceiver broadcastReceiver; // Deklarasi broadcastReceiver di tingkat kelas
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_pattern);

        btn_tracking_sleep = findViewById(R.id.btn_tracking_sleep);
        tV_duration = findViewById(R.id.tV_duration_sleep);
        tV_dayname = findViewById(R.id.tV_dayname);
        tV_tanggal = findViewById(R.id.tV_tanggal);
        tV_quality = findViewById(R.id.tV_kualitas_tidur);
        tV_duration_sleep = findViewById(R.id.tV_hasil_duration);
        iV_back = findViewById(R.id.iV_kembali);
        btn_keterangan_rest = findViewById(R.id.btn_keterangan_rest);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formattedDate = currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dayName = currentDate.format(DateTimeFormatter.ofPattern("EEEE", Locale.getDefault()));
        }
        tV_dayname.setText(dayName);
        tV_tanggal.setText(formattedDate);


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long elapsedTime = intent.getLongExtra("elapsed_time", 0);
                Log.d("RestPatternActivity", "Broadcast received: elapsed_time = " + elapsedTime);
                updateElapsedTime(elapsedTime);
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("stopwatch_update"));

        iV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestPatternActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            id = b.getString("id");
            date = b.getString("date");
            uid = b.getString("uid");
            day = b.getString("day");
            timesleep = b.getString("timesleep");
            status_ket = b.getString("status");

            tV_dayname.setText(day);

            int hours = getHours(timesleep);

            tV_duration_sleep.setText(timesleep);
            if (hours < 7) {
                type_ket = "Bad Sleep";
                tV_quality.setText(type_ket);
            } else if (hours >= 7 && hours <= 9) {
                type_ket = "Good Sleep";
                tV_quality.setText(type_ket);
            } else if (hours > 9) {
                type_ket = "Over Sleep";
                tV_quality.setText(type_ket);
            }

            if ("historyrest".equals(status_ket)) {
                btn_tracking_sleep.setVisibility(View.INVISIBLE);
            }
        }

        btn_tracking_sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status == 0) {
                    startStopwatchService();
                    btn_tracking_sleep.setText("Stop Tracking");
                } else if (status == 1) {
                    stopStopwatchService();
                    btn_tracking_sleep.setText("Start Tracking");

                    DocumentReference documentReference = fStore.collection("restpattern").document();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    String uid = firebaseUser.getUid();
                    hashMap.put("uid", uid);
                    hashMap.put("id", documentReference.getId());
                    hashMap.put("date", formattedDate);
                    hashMap.put("day", dayName);
                    hashMap.put("timesleep", formattedTime);
                    documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            tV_duration_sleep.setText(formattedTime);
                            if (hours < 7) {
                                type_ket = "Bad Sleep";
                                tV_quality.setText(type_ket);
                            } else if (hours >= 7 && hours <= 9) {
                                type_ket = "Good Sleep";
                                tV_quality.setText(type_ket);
                            } else if (hours > 9) {
                                type_ket = "Over Sleep";
                                tV_quality.setText(type_ket);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RestPatternActivity.this, "Gagal Menambahkan Data physical", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btn_keterangan_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestPatternActivity.this, HasilTestRestActivity.class);
                intent.putExtra("type", type_ket);
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission();
        }
    }

    private void requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin notifikasi diberikan
                Toast.makeText(this, "Notifikasi diizinkan", Toast.LENGTH_SHORT).show();
            } else {
                // Izin notifikasi ditolak
                Toast.makeText(this, "Izin notifikasi ditolak", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("stopwatch_update"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    public void startStopwatchService() {
        Intent intent = new Intent(this, StopwatchService.class);
        status = 1;
        startService(intent);
    }

    private void updateElapsedTime(long elapsedTime) {
        hours = TimeUnit.MILLISECONDS.toHours(elapsedTime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime) - TimeUnit.HOURS.toMinutes(hours);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - TimeUnit.MINUTES.toSeconds(minutes);

        formattedTime = String.format(Locale.getDefault(), "%d jam %d menit %d detik", hours, minutes, seconds);
        Log.d("RestPatternActivity", "updateElapsedTime: " + formattedTime);
        tV_duration.setText(formattedTime);
    }

    private void stopStopwatchService() {
        Intent intent = new Intent(this, StopwatchService.class);
        status = 0;
        stopService(intent);
    }

    public static int getHours(String time) {
        String[] parts = time.split(" ");
        int hours = 0;

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equalsIgnoreCase("jam")) {
                hours = Integer.parseInt(parts[i - 1]);
                break;
            }
        }

        return hours;
    }
}
