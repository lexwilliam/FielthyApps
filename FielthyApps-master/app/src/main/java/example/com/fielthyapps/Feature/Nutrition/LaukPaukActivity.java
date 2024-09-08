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
import android.widget.AutoCompleteTextView;
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

public class LaukPaukActivity extends AppCompatActivity {
    private Spinner lauksatu,laukdua,lauktiga;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore fStore;
    private EditText eT_lauksatu,eT_laukdua,eT_lauktiga;
    private Button btn_submit;
    private String get_lauksatu,get_laukdua,get_lauktiga;
    private  String selectedValuesatu,selectedvaluedua,selecetedvaluetiga;
    private String id;
    String formattedDate;
    private ImageView iV_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lauk_pauk);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        eT_lauksatu = findViewById(R.id.eT_laukpertama);
        eT_laukdua = findViewById(R.id.eT_laukkedua);
        eT_lauktiga = findViewById(R.id.eT_laukketiga);
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
        lauksatu = findViewById(R.id.spinner_lauksatu);
        laukdua = findViewById(R.id.spinner_laukdua);
        lauktiga = findViewById(R.id.spinner_lauktiga);

        // Mengatur adapter ke Spinner
        lauksatu.setAdapter(arrayAdapter);
        laukdua.setAdapter(arrayAdapter);
        lauktiga.setAdapter(arrayAdapter);

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
                Intent intent = new Intent(LaukPaukActivity.this, NutritionActivity.class);
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

        lauksatu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        laukdua.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        lauktiga.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        get_lauksatu = eT_lauksatu.getText().toString();
        get_laukdua = eT_laukdua.getText().toString();
        get_lauktiga = eT_lauktiga.getText().toString();

        if (get_lauksatu.isEmpty()){
            eT_lauksatu.setError("Masukan lauk pertama lebih dulu");
            eT_lauksatu.setFocusable(true);
        }else if (get_laukdua.isEmpty()){
            eT_laukdua.setError("Masukan lauk kedua lebih dulu");
            eT_laukdua.setFocusable(true);
        }else if (get_lauktiga.isEmpty()){
            eT_lauktiga.setError("Masukan lauk Ketiga lebih dulu");
            eT_lauktiga.setFocusable(true);
        }else{
            DocumentReference documentReference = fStore.collection("lauk").document(id);
            HashMap hashMap = new HashMap();
            String uid = firebaseUser.getUid();
            hashMap.put("uid", uid);
            hashMap.put("id", documentReference.getId());
            hashMap.put("idnutritiontest", id);
            hashMap.put("date", formattedDate);
            hashMap.put("lauksatu", get_lauksatu);
            hashMap.put("porsisatu", selectedValuesatu);
            hashMap.put("laukdua", get_laukdua);
            hashMap.put("porsidua", selectedvaluedua);
            hashMap.put("lauktiga", get_lauktiga);
            hashMap.put("porsitiga", selecetedvaluetiga);


            documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    DocumentReference updatenutrition = fStore.collection("nutritiontest").document(id);
                    Map<String, Object> data = new HashMap<>();
                    data.put("laukpauk", "1");
                    updatenutrition.update(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(LaukPaukActivity.this, "Berhasil input data Lauk", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LaukPaukActivity.this, NutritionActivity.class);
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
                    Toast.makeText(LaukPaukActivity.this, "Gagal memperbarui dokumen nutritiontest", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    }
