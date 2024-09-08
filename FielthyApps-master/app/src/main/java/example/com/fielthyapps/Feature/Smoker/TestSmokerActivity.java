package example.com.fielthyapps.Feature.Smoker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import example.com.fielthyapps.Feature.Medcheck.HasilMedCheckActivity;
import example.com.fielthyapps.Feature.Medcheck.MedCheckActivity;
import example.com.fielthyapps.Feature.Nutrition.NutritionActivity;
import example.com.fielthyapps.Feature.Nutrition.SayurSayuranActivity;
import example.com.fielthyapps.R;

public class TestSmokerActivity extends AppCompatActivity {
private Button btn_submit;
private EditText eT_batang,eT_tahun,eT_bungkus,eT_rupiah;
private String batang,tahun,bungkus,rupiah;
    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore fStore;
    private FirebaseUser currentUser;
    private ProgressDialog mLoading;
    private String merokok;
    private ImageView iV_back;
    private String formattedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_smoker);
        eT_batang = findViewById(R.id.eT_batang);
        eT_tahun = findViewById(R.id.eT_tahun);
        eT_bungkus = findViewById(R.id.eT_bungkus);
        eT_rupiah = findViewById(R.id.eT_rupiah);
        btn_submit = findViewById(R.id.btn_lanjut_hasil);
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mLoading = new ProgressDialog(this);
        mLoading.setMessage("Please Wait..");
        iV_back = findViewById(R.id.iV_kembali);

        currentUser = firebaseAuth.getCurrentUser();
        Intent iin = getIntent();
        final Bundle b = iin.getExtras();

        if (b != null) {
            merokok = (String) b.get("merokok");
        }

        iV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestSmokerActivity.this, SmokerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitTest();

            }
        });
    }

    private void submitTest() {
        batang = eT_batang.getText().toString();
        tahun = eT_tahun.getText().toString();
        bungkus = eT_bungkus.getText().toString();
        rupiah = eT_rupiah.getText().toString();
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


        if (batang.isEmpty()) {
            eT_batang.setError("Masukan Berapa Batang Rokok Terlebih Dahulu");
            eT_batang.setFocusable(true);
        }else if (tahun.isEmpty()){
            eT_tahun.setError("Masukan Berapa Tahun Terlebih Dahulu");
            eT_tahun.setFocusable(true);
        } else if (bungkus.isEmpty()) {
            eT_bungkus.setError("Masukan Berapa Bungkus Terlebih Dahulu");
            eT_bungkus.setFocusable(true);
        } else if (rupiah.isEmpty()) {
            eT_rupiah.setError("Masukan Banyak Uang Terlebih Dahulu");
            eT_rupiah.setFocusable(true);
        }else{
            DocumentReference documentReference = fStore.collection("smoker").document();
            HashMap hashMap = new HashMap();
            String uid = currentUser.getUid();
            hashMap.put("uid", uid);
            hashMap.put("id", documentReference.getId());
            hashMap.put("batang", batang);
            hashMap.put("tahun", tahun);
            hashMap.put("bungkus", bungkus);
            hashMap.put("rupiah", rupiah);
            hashMap.put("merokok", merokok);
            hashMap.put("date", formattedDate);


            documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(TestSmokerActivity.this, "Berhasil input data Smoker", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TestSmokerActivity.this, HasilSmokerActivity.class);
                    intent.putExtra("id", documentReference.getId());
                    intent.putExtra("uid", uid);
                    intent.putExtra("batang", batang);
                    intent.putExtra("tahun", tahun);
                    intent.putExtra("bungkus", bungkus);
                    intent.putExtra("rupiah", rupiah);
                    intent.putExtra("status", "testsmoker");
                    startActivity(intent);
                    finish();
                }
            });
        }

    }
}