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

public class NutritionActivity extends AppCompatActivity {
    private LinearLayout makanan,sayuran,buah,lauk;
    private TextView tV_makananSehat,tV_dietSehat;
    private Button btn_hasil;
    private String id;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore fStore;
    private ImageView iV_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
        makanan = findViewById(R.id.LL_makanan);
        sayuran = findViewById(R.id.LL_sayuran);
        buah =  findViewById(R.id.LL_buah);
        lauk = findViewById(R.id.LL_laukpauk);
        tV_makananSehat = findViewById(R.id.tV_makanan_sehat);
        tV_dietSehat = findViewById(R.id.tV_diet);
        btn_hasil = findViewById(R.id.btn_hasil);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        iV_back = findViewById(R.id.iV_kembali);

        Intent iin = getIntent();
        final Bundle b = iin.getExtras();

        if (b != null) {
            id = (String) b.get("id");
        }
        checktest();

        iV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NutritionActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        tV_dietSehat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NutritionActivity.this,DietSehatActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        tV_makananSehat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NutritionActivity.this,MakananSehatActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        makanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NutritionActivity.this,MakananPokokActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        sayuran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NutritionActivity.this,SayurSayuranActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        buah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NutritionActivity.this,BuahBuahanActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        lauk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NutritionActivity.this,LaukPaukActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        btn_hasil.setOnClickListener(new View.OnClickListener() {
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
                    btn_hasil.setVisibility(View.INVISIBLE);

                    if (laukpauk.equals("1")){
                        lauk.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
                    }

                    if (makananstring.equals("1")){
                        makanan.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
                    }

                    if (sayuranstring.equals("1")){
                        sayuran.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
                    }

                    if (buahstring.equals("1")){
                        buah.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
                    }

                    if (laukpauk.equals("1") && makananstring.equals("1") && sayuranstring.equals("1") && buahstring.equals("1")) {
                        lauk.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
                        makanan.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
                        sayuran.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
                        buah.setBackground(ContextCompat.getDrawable(NutritionActivity.this, R.drawable.bg_nutrition));
                        btn_hasil.setVisibility(View.VISIBLE);

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