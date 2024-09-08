package example.com.fielthyapps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import example.com.fielthyapps.Auth.ProfileActivity;
import example.com.fielthyapps.Feature.History.HistoryActivity;
import example.com.fielthyapps.Feature.Medcheck.MedCheckActivity;
import example.com.fielthyapps.Feature.Nutrition.NutritionActivity;
import example.com.fielthyapps.Feature.Physical.PhysicalActivity;
import example.com.fielthyapps.Feature.RestPattern.RestPatternActivity;
import example.com.fielthyapps.Feature.Smoker.HasilSmokerActivity;
import example.com.fielthyapps.Feature.Smoker.SmokerActivity;
import example.com.fielthyapps.Feature.Smoker.TestSmokerActivity;
import example.com.fielthyapps.Feature.Stress.StressActivity;

public class HomeActivity extends AppCompatActivity {
    private LinearLayout medcheck,nutrition,physical,restpattern,smoker,stress;
    private TextView tV_profile;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore fStore;
    private CircleImageView image_profile;
    String formattedDate;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigate);
        medcheck = findViewById(R.id.LL_medcheck);
        nutrition = findViewById(R.id.LL_nutrition);
        physical = findViewById(R.id.LL_physical);
        restpattern = findViewById(R.id.LL_rest);
        smoker = findViewById(R.id.LL_smoker);
        stress = findViewById(R.id.LL_stress);
        image_profile = findViewById(R.id.profile_image);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
       tV_profile= findViewById(R.id.tV_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        checkUserLogin();
// Mendapatkan tanggal saat ini
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }

        // Mengatur format tanggal
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        }

        // Mengonversi LocalDate ke String
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDate= currentDate.format(formatter);
        }


        //Get the time of day


        medcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, MedCheckActivity.class));
            }
        });

        nutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = firebaseUser.getUid();

                // Dokumen belum ada, buat dokumen baru
                DocumentReference documentReference = fStore.collection("nutritiontest").document();
                HashMap hashMap = new HashMap();

                hashMap.put("uid", uid);
                hashMap.put("id", documentReference.getId());
                hashMap.put("date", formattedDate);
                hashMap.put("laukpauk", "0");
                hashMap.put("makanan", "0");
                hashMap.put("sayuran", "0");
                hashMap.put("buah", "0");


                documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent intent = new Intent(HomeActivity.this, NutritionActivity.class);
                        intent.putExtra("id",documentReference.getId());
                        startActivity(intent);
                    }
                });


//                fStore.collection("nutritiontest")
//                        .whereEqualTo("uid", uid)
//                        .whereEqualTo("date", formattedDate)
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    QuerySnapshot querySnapshot = task.getResult();
//                                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
//                                        // Dokumen sudah ada, tampilkan pesan
//                                        Toast.makeText(HomeActivity.this, "Anda sudah melakukan tes hari ini.", Toast.LENGTH_SHORT).show();
//                                    } else {
//
//                                    }
//                                } else {
//                                    // Gagal melakukan pengecekan
//                                    Toast.makeText(HomeActivity.this, "Gagal memeriksa status tes: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });




            }
        });

        physical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, PhysicalActivity.class));
            }
        });

        restpattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, RestPatternActivity.class));
            }
        });

        smoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, SmokerActivity.class));
            }
        });

        stress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = firebaseUser.getUid();
                // Dokumen belum ada, buat dokumen baru
                DocumentReference documentReference = fStore.collection("stresstest").document();
                HashMap hashMap = new HashMap();

                hashMap.put("uid", uid);
                hashMap.put("id", documentReference.getId());
                hashMap.put("date", formattedDate);
                hashMap.put("stress", "0");
                hashMap.put("depresi", "0");
                hashMap.put("cemas", "0");


                documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent intent = new Intent(HomeActivity.this, StressActivity.class);
                        intent.putExtra("id",documentReference.getId());
                        startActivity(intent);
                    }
                });






            }
        });


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.bottom_home) {
                    return true;
                } else if (item.getItemId() == R.id.bottom_history) {
                    startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.bottom_profile) {
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    private void checkUserLogin() {
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        DocumentReference documentReference = fStore.collection("user").document(user.getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Date date = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int hour = cal.get(Calendar.HOUR_OF_DAY);

                //Set greeting
                String greeting = null;
                if(hour>= 12 && hour < 17){
                    greeting = "Good Afternoon";
                } else if(hour >= 17 && hour < 21){
                    greeting = "Good Evening";
                } else if(hour >= 21 && hour < 24){
                    greeting = "Good Night";
                } else {
                    greeting = "Good Morning";
                }

                tV_profile.setText(greeting + " " + value.getString("nama"));

                if (user.getPhotoUrl() == null){
                    Glide.with(HomeActivity.this)
                            .load(R.drawable.default_profile)
                            .into(image_profile);
                }else{
                    Glide.with(HomeActivity.this)
                            .load(user.getPhotoUrl())
                            .into(image_profile);

                }


            }
        });

    }
}
