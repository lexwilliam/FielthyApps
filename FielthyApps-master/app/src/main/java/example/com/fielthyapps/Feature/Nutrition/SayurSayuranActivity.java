package example.com.fielthyapps.Feature.Nutrition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.Map;

import example.com.fielthyapps.R;

public class SayurSayuranActivity extends AppCompatActivity {
    private Spinner sayursatu,sayurdua,sayurtiga;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore fStore;
    private EditText eT_sayursatu,eT_sayurdua,eT_sayurtiga;
    private Button btn_submit;
    private String get_sayursatu,get_sayurdua,get_sayurtiga;
    private  String selectedValuesatu,selectedvaluedua,selecetedvaluetiga;
    private String id;
    String formattedDate;
    private ImageView iV_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sayur_sayuran);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        eT_sayursatu = findViewById(R.id.eT_sayursatu);
        eT_sayurdua = findViewById(R.id.eT_sayurdua);
        eT_sayurtiga = findViewById(R.id.eT_sayurketiga);
        btn_submit = findViewById(R.id.btn_submit);
        iV_back = findViewById(R.id.iV_kembali);
        // Mendapatkan referensi ke array string yang baru saja dibuat
        Resources resources = getResources();
        String[] piring = resources.getStringArray(R.array.porsi);

        // Membuat ArrayAdapter dan mengisi parameter yang dibutuhkan
        // dalam kasus kita, mengisi konteks, layout dropdown, dan array
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdownitem, piring);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Mendapatkan referensi ke Spinner
        sayursatu = findViewById(R.id.spinner_sayursatu);
        sayurdua = findViewById(R.id.spinner_sayurdua);
        sayurtiga = findViewById(R.id.spinner_sayurtiga);

        // Mengatur adapter ke Spinner
        sayursatu.setAdapter(arrayAdapter);
        sayurdua.setAdapter(arrayAdapter);
        sayurtiga.setAdapter(arrayAdapter);
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

        Intent iin = getIntent();
        final Bundle b = iin.getExtras();

        if (b != null) {
            id = (String) b.get("id");
        }

        iV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SayurSayuranActivity.this,NutritionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputtoDB();
            }
        });

        sayursatu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Mengambil nilai yang dipilih
                selectedValuesatu= parent.getItemAtPosition(position).toString();
                // Menampilkan nilai yang dipilih
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        sayurdua.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Mengambil nilai yang dipilih
                selectedvaluedua= parent.getItemAtPosition(position).toString();
                // Menampilkan nilai yang dipilih
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        sayurtiga.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Mengambil nilai yang dipilih
                selecetedvaluetiga= parent.getItemAtPosition(position).toString();
                // Menampilkan nilai yang dipilih
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void inputtoDB(){
        get_sayursatu = eT_sayursatu.getText().toString();
        get_sayurdua = eT_sayurdua.getText().toString();
        get_sayurtiga = eT_sayurtiga.getText().toString();

        if (get_sayursatu.isEmpty()){
            eT_sayursatu.setError("Masukan sayur pertama lebih dulu");
            eT_sayursatu.setFocusable(true);
        }else if (get_sayurdua.isEmpty()){
            eT_sayurdua.setError("Masukan sayur kedua lebih dulu");
            eT_sayurdua.setFocusable(true);
        }else if (get_sayurtiga.isEmpty()){
            eT_sayurtiga.setError("Masukan sayur Ketiga lebih dulu");
            eT_sayurtiga.setFocusable(true);
        }else{
            DocumentReference documentReference = fStore.collection("sayur").document(id);
            HashMap hashMap = new HashMap();
            String uid = firebaseUser.getUid();
            hashMap.put("uid", uid);
            hashMap.put("id", documentReference.getId());
            hashMap.put("idnutritiontest", id);
            hashMap.put("date", formattedDate);
            hashMap.put("sayursatu", get_sayursatu);
            hashMap.put("porsisatu", selectedValuesatu);
            hashMap.put("sayurdua", get_sayurdua);
            hashMap.put("porsidua", selectedvaluedua);
            hashMap.put("sayurtiga", get_sayurtiga);
            hashMap.put("porsitiga", selecetedvaluetiga);


            documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    DocumentReference updatenutrition = fStore.collection("nutritiontest").document(id);
                    Map<String, Object> data = new HashMap<>();
                    data.put("sayuran", "1");
                    updatenutrition.update(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(SayurSayuranActivity.this, "Berhasil input data sayur", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SayurSayuranActivity.this, NutritionActivity.class);
                                    intent.putExtra("id",id);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("gagal", "Gagal memperbarui dokumen nutritiontest", e);
                    Toast.makeText(SayurSayuranActivity.this, "Gagal memperbarui dokumen nutritiontest", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
}