package example.com.fielthyapps.Feature.Medcheck;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import example.com.fielthyapps.HomeActivity;
import example.com.fielthyapps.R;

public class MedCheckActivity extends AppCompatActivity {
private Button btn_lanjut;
private FirebaseAuth firebaseAuth;

private FirebaseFirestore fStore;
private FirebaseUser currentUser;
private ProgressDialog mLoading;
private EditText eT_berat,eT_tinggi,et_lingkarperut,eT_sistolik,eT_diastolik,eT_guladarah,eT_lemakk;
private String berat,tinggi,lingkar_perut,sistolik,diastolik,guladarah,lemak,indikator,gender;
private ImageView iV_back;
    String formattedDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_check);
        btn_lanjut = findViewById(R.id.btn_lanjut_hasil);
        eT_berat = findViewById(R.id.eT_beratBadan);
        eT_tinggi = findViewById(R.id.eT_tinggiBadan);
        et_lingkarperut = findViewById(R.id.eT_lingkar_perut);
        eT_sistolik = findViewById(R.id.eT_sistolik);
        eT_diastolik = findViewById(R.id.eT_diastolik);
        eT_guladarah = findViewById(R.id.eT_gula);
        eT_lemakk = findViewById(R.id.eT_lemak);
        iV_back = findViewById(R.id.iV_kembali);
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mLoading = new ProgressDialog(this);
        mLoading.setMessage("Please Wait..");

         currentUser = firebaseAuth.getCurrentUser();
        checkUserData();

        iV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedCheckActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        btn_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inputMedcheck();
            }
        });
    }

    private void inputMedcheck() {
        berat = eT_berat.getText().toString();
        tinggi = eT_tinggi.getText().toString();
        lingkar_perut = et_lingkarperut.getText().toString();
        sistolik = eT_sistolik.getText().toString();
        diastolik = eT_diastolik.getText().toString();
        guladarah = eT_guladarah.getText().toString();
        lemak = eT_lemakk.getText().toString();


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

        if (berat.isEmpty()) {
            eT_berat.setError("Masukan Berat Badan Terlebih Dahulu");
            eT_berat.setFocusable(true);
        }else if (tinggi.isEmpty()){
            eT_tinggi.setError("Masukan Tinggi Badan Terlebih Dahulu");
            eT_tinggi.setFocusable(true);
        } else if (lingkar_perut.isEmpty()) {
            et_lingkarperut.setError("Masukan Lingkar Perut Terlebih Dahulu");
            et_lingkarperut.setFocusable(true);
        } else if (sistolik.isEmpty()) {
            eT_sistolik.setError("Masukan Sistolik Terlebih Dahulu");
            eT_sistolik.setFocusable(true);
        } else if (diastolik.isEmpty()) {
            eT_diastolik.setError("Masukan Diastolik Terlebih Dahulu");
            eT_diastolik.setFocusable(true);
        } else if (guladarah.isEmpty()) {
            eT_guladarah.setError("Masukan Gula Darah Terlebih Dahulu");
            eT_guladarah.setFocusable(true);
        } else if (lemak.isEmpty()) {
            eT_lemakk.setError("Masukan Lemak Terlebih Dahulu");
            eT_lemakk.setFocusable(true);
        }else{
            int pangkat = 2;
            float tinggiKuadrat = (float) Math.pow(Float.parseFloat(tinggi), pangkat);
            float beratAsli = (float) Float.parseFloat(berat);
            Log.d("Coba Tinggi", "pushDataUmum: " + beratAsli);
            float hasilbmi = (beratAsli / tinggiKuadrat) * 10000;
            String hasil = String.format("%.2f", hasilbmi);


            if (hasilbmi< 18.5){
                indikator = ("Berat Badan Kurang(Underwieght)");
            }else if (hasilbmi >= 18.5 && hasilbmi <= 22.9){
                indikator = ("Berat badan normal");
            }else if (hasilbmi >= 23 && hasilbmi <=24.5){
                indikator = ("Kelebihan Berat badan(Overweight) dengan resiko");
            }else if (hasilbmi >= 25 && hasilbmi <= 29.9){
                indikator = ("Obesitas I");
            }else if (hasilbmi >=30){
                indikator = ("Obesitas II");
            }
            DocumentReference documentReference = fStore.collection("medcheck").document();
            HashMap hashMap = new HashMap();
            String uid = currentUser.getUid();
            hashMap.put("uid", uid);
            hashMap.put("id", documentReference.getId());
            hashMap.put("gender", gender);
            hashMap.put("berat", berat);
            hashMap.put("tinggi", tinggi);
            hashMap.put("lingkarperut", lingkar_perut);
            hashMap.put("sistolik", sistolik);
            hashMap.put("diastolik", diastolik);
            hashMap.put("guladarah", guladarah);
            hashMap.put("lemak", lemak);
            hashMap.put("hasilbmi", hasil);
            hashMap.put("date", formattedDate);


            documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(MedCheckActivity.this, "Berhasil input data Medcheck", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MedCheckActivity.this, HasilMedCheckActivity.class);
                    intent.putExtra("id", documentReference.getId());
                    intent.putExtra("uid", uid);
                    intent.putExtra("gender", gender);
                    intent.putExtra("berat", berat);
                    intent.putExtra("tinggi", tinggi);
                    intent.putExtra("lingkarperut", lingkar_perut);
                    intent.putExtra("sistolik", sistolik);
                    intent.putExtra("diastolik", diastolik);
                    intent.putExtra("guladarah", guladarah);
                    intent.putExtra("lemak", lemak);
                    intent.putExtra("hasilbmi", hasil);
                    intent.putExtra("status", "testmedcheck");
                    startActivity(intent);
                    finish();
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
                gender = value.getString("gender");


            }
        });

    }

}