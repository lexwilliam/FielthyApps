package example.com.fielthyapps.Feature.Nutrition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import example.com.fielthyapps.HomeActivity;
import example.com.fielthyapps.R;
import example.com.fielthyapps.databinding.ActivityNutritionBinding;

public class NutritionActivity extends AppCompatActivity {
    private String id;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore fStore;
    private ActivityNutritionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNutritionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        Intent iin = getIntent();
        final Bundle b = iin.getExtras();

        if (b != null) {
            id = (String) b.get("id");
        }
        checktest();

        binding.iVKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NutritionActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        binding.tVDiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NutritionActivity.this,DietSehatActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        binding.tVMakananSehat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NutritionActivity.this,MakananSehatActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        binding.LLMakanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NutritionActivity.this,MakananPokokActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        binding.LLSayuran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NutritionActivity.this,SayurSayuranActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        binding.LLBuah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NutritionActivity.this,BuahBuahanActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        binding.LLLaukpauk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NutritionActivity.this,LaukPaukActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        binding.tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NutritionActivity.this, FoodRecognitionActivity.class);
                startActivity(intent);
            }
        });

        binding.btnHasil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NutritionActivity.this, HasilNutritionActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("status","testnutrition");
                startActivity(intent);
            }
        });

    }

    private void checktest(){
        DocumentReference checkdata = fStore.collection("nutritiontest").document(id);
        checkdata.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Dokumen ditemukan, Anda dapat mengakses nilai-nilainya di sini
                    String laukpauk = documentSnapshot.getString("laukpauk");
                    String makananstring = documentSnapshot.getString("makanan");
                    String sayuranstring = documentSnapshot.getString("sayuran");
                    String buahstring = documentSnapshot.getString("buah");
                    binding.btnHasil.setVisibility(View.INVISIBLE);

                    if (laukpauk.equals("1")){
                        binding.LLLaukpauk.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
                    }

                    if (makananstring.equals("1")){
                        binding.LLMakanan.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
                    }

                    if (sayuranstring.equals("1")){
                        binding.LLSayuran.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
                    }

                    if (buahstring.equals("1")){
                        binding.LLBuah.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
                    }

                    if (laukpauk.equals("1") && makananstring.equals("1") && sayuranstring.equals("1") && buahstring.equals("1")) {
                        binding.LLLaukpauk.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
                        binding.LLMakanan.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
                        binding.LLSayuran.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
                        binding.LLBuah.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
                        binding.btnHasil.setVisibility(View.VISIBLE);

                    }


//                    if (laukpauk.equals("1") && makananstring.equals("0") && sayuranstring.equals("0") && buahstring.equals("0")){
//                        lauk.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
//                    }else if (laukpauk.equals("1") && makananstring.equals("1") && sayuranstring.equals("0") && buahstring.equals("0")){
//                        lauk.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
//                        makanan.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
//                    }else if (laukpauk.equals("1") && makananstring.equals("1") && sayuranstring.equals("1") && buahstring.equals("0")){
//                        lauk.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
//                        makanan.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
//                        sayuran.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
////                        buah.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
//                    } else if (laukpauk.equals("1") && makananstring.equals("1") && sayuranstring.equals("1") && buahstring.equals("1")) {
//                        lauk.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
//                        makanan.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
//                        sayuran.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
//                        buah.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
//                        btn_hasil.setVisibility(View.VISIBLE);
//
//                    }


                    // Lakukan apa pun yang perlu Anda lakukan dengan nilai-nilai tersebut
                } else {
                    // Dokumen tidak ditemukan
                    Log.d("Firestore", "Dokumen tidak ditemukan.");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Penanganan kesalahan jika gagal mengambil data dari Firestore
                Log.e("Firestore", "Gagal mengambil data: " + e.getMessage());
            }
        });
    }
}