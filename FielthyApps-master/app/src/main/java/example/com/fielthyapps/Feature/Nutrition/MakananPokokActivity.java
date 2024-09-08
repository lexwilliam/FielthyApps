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

import example.com.fielthyapps.HomeActivity;
import example.com.fielthyapps.R;

public class MakananPokokActivity extends AppCompatActivity {
private Spinner makansatu,makandua,makantiga;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore fStore;
    private EditText eT_makanansatu,eT_makanandua,eT_makanantiga;
    private Button btn_submit;
    private String get_makanansatu,get_makanandua,get_makanantiga;
    private  String selectedValuesatu,selectedvaluedua,selecetedvaluetiga;
    private String id;
    String formattedDate;
    private ImageView iV_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makanan_pokok);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        eT_makanansatu = findViewById(R.id.eT_makanansatu);
        eT_makanandua = findViewById(R.id.eT_makanandua);
        eT_makanantiga = findViewById(R.id.eT_makananketiga);
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
        makansatu = findViewById(R.id.spinner_makansatu);
        makandua = findViewById(R.id.spinner_makandua);
        makantiga = findViewById(R.id.spinner_makantiga);

        // Mengatur adapter ke Spinner
        makansatu.setAdapter(arrayAdapter);
        makandua.setAdapter(arrayAdapter);
        makantiga.setAdapter(arrayAdapter);

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
                Intent intent = new Intent(MakananPokokActivity.this, NutritionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputtoDB();
            }
        });

        makansatu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        makandua.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        makantiga.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        get_makanansatu = eT_makanansatu.getText().toString();
        get_makanandua = eT_makanandua.getText().toString();
        get_makanantiga = eT_makanantiga.getText().toString();

        if (get_makanansatu.isEmpty()){
            eT_makanansatu.setError("Masukan makanan pertama lebih dulu");
            eT_makanansatu.setFocusable(true);
        }else if (get_makanandua.isEmpty()){
            eT_makanandua.setError("Masukan makanan kedua lebih dulu");
            eT_makanandua.setFocusable(true);
        }else if (get_makanantiga.isEmpty()){
            eT_makanantiga.setError("Masukan makanan Ketiga lebih dulu");
            eT_makanantiga.setFocusable(true);
        }else{
            DocumentReference documentReference = fStore.collection("makanan").document(id);
            HashMap hashMap = new HashMap();
            String uid = firebaseUser.getUid();
            hashMap.put("uid", uid);
            hashMap.put("id", documentReference.getId());
            hashMap.put("idnutritiontest", id);
            hashMap.put("date", formattedDate);
            hashMap.put("makanansatu", get_makanansatu);
            hashMap.put("porsisatu", selectedValuesatu);
            hashMap.put("makanandua", get_makanandua);
            hashMap.put("porsidua", selectedvaluedua);
            hashMap.put("makanantiga", get_makanantiga);
            hashMap.put("porsitiga", selecetedvaluetiga);


            documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    DocumentReference updatenutrition = fStore.collection("nutritiontest").document(id);
                    Map<String, Object> data = new HashMap<>();
                    data.put("makanan", "1");
                    updatenutrition.update(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(MakananPokokActivity.this, "Berhasil input data makanan", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MakananPokokActivity.this, NutritionActivity.class);
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
                    Toast.makeText(MakananPokokActivity.this, "Gagal memperbarui dokumen nutritiontest", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
}