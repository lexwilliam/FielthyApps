package example.com.fielthyapps.Feature.Stress;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Map;

import example.com.fielthyapps.Feature.History.HistoryActivity;
import example.com.fielthyapps.Feature.Smoker.HasilSmokerActivity;
import example.com.fielthyapps.Feature.Smoker.InformasiMenjauhiRokokAdapter;
import example.com.fielthyapps.Feature.Smoker.SmokerTipsList;
import example.com.fielthyapps.HomeActivity;
import example.com.fielthyapps.R;

public class HasilStressActivity extends AppCompatActivity {
    private TextView tV_hasil, tV_angka;
    private RecyclerView rV_tips;
    private Button btn_back;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore fStore;
    private String id, status,type;
    private ImageView iV_status;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_stress);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        tV_hasil = findViewById(R.id.tV_status_stress);
        tV_angka = findViewById(R.id.tV_angka_stress);
        rV_tips = findViewById(R.id.rV_tips);
        btn_back = findViewById(R.id.btn_back);
        iV_status = findViewById(R.id.iV_status);
        Intent iin = getIntent();
        final Bundle b = iin.getExtras();

        if (b != null) {
            id = (String) b.get("id");
            status = (String) b.get("status");
            type = (String) b.get("type");

        }

        get_data();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equals("history")){
                    Intent intent = new Intent(HasilStressActivity.this, HistoryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else if (type.equals("test")){
                    Intent intent = new Intent(HasilStressActivity.this, StressActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("id",id);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void get_data() {


        if (status.equals("stress")) {
            DocumentReference checkdata = fStore.collection("stress").document(id);

          checkdata.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
              @Override
              public void onSuccess(DocumentSnapshot documentSnapshot) {
                  if (documentSnapshot != null){
                      if (documentSnapshot.exists()) {
                          Map<String, Object> data = documentSnapshot.getData();
                          if (data != null) {
                              int totalHasil = calculateTotalHasil(data);
                              Log.d("Score", "Score " + totalHasil);
                              if (totalHasil < 15) {
                                  tV_angka.setText("Score " + totalHasil);
                                  tV_hasil.setText("Normal");
                                  iV_status.setImageResource(R.drawable.status_normal);
                              } else if (totalHasil < 19) {
                                  tV_angka.setText("Score " + totalHasil);
                                  tV_hasil.setText("Ringan");
                                  iV_status.setImageResource(R.drawable.status_ringan);
                              } else if (totalHasil < 26) {
                                  tV_angka.setText("Score " + totalHasil);
                                  tV_hasil.setText("Sedang");
                                  iV_status.setImageResource(R.drawable.status_sedang);
                              } else if (totalHasil < 34) {
                                  tV_angka.setText("Score " + totalHasil);
                                  tV_hasil.setText("Berat");
                                  iV_status.setImageResource(R.drawable.status_besar);
                              } else {
                                  tV_angka.setText("Score " + totalHasil);
                                  tV_hasil.setText("Sangat Berat");
                                  iV_status.setImageResource(R.drawable.status_sangat);
                              }

                              SmokerTipsList[] myListData = new SmokerTipsList[] {
                                      new SmokerTipsList("Bicarakan keluhan dengan seseorang yang dapat dipercaya",R.drawable.nmbr_one,R.drawable.stress_satu),
                                      new SmokerTipsList("Melakukan kegiatan yang sesuai dengan minat dan kemampuan",R.drawable.nmbr_two,R.drawable.stress_dua),
                                      new SmokerTipsList("Kembangkan hobi yang bermanfaat",R.drawable.nmbr_three,R.drawable.stress_tiga),
                                      new SmokerTipsList("Meningkatkan ibadah dan mendekatkan diri pada tuhan",R.drawable.nmbr_four,R.drawable.stress_empat),
                                      new SmokerTipsList("Bepikir positif",R.drawable.nmbr_five,R.drawable.stress_lima),
                                      new SmokerTipsList("Tenangkan pikiran dengan relaksasi",R.drawable.nmbr_six,R.drawable.stress_enam),
                                      new SmokerTipsList("Jagalah Kesehatan dengan olahraga aktivitas fisik secara teratur, tidak cukup,\n" +
                                              "makan bergizi seimbang, serta terapkan perilaku bersih dan sehat",R.drawable.nmbr_seven,R.drawable.stress_tujuh),

                              };
                              InformasiMenjauhiRokokAdapter adapter = new InformasiMenjauhiRokokAdapter(myListData);
                              rV_tips.setHasFixedSize(true);
                              rV_tips.setLayoutManager(new LinearLayoutManager(HasilStressActivity.this));
                              rV_tips.setAdapter(adapter);

                          }
                      }
                  }
              }
          });
        }else if (status.equals("cemas")){
            DocumentReference checkdata = fStore.collection("cemas").document(id);

            checkdata.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot != null){
                        if (documentSnapshot.exists()) {
                            Map<String, Object> data = documentSnapshot.getData();
                            if (data != null) {
                                int totalHasil = calculateTotalHasil(data);
                                // Gunakan totalHasil sesuai kebutuhan Anda, misalnya menampilkan di UI
                                Log.d("Score", "Score " + totalHasil);

                                if (totalHasil < 8) {
                                    tV_angka.setText("Score " + totalHasil);
                                    tV_hasil.setText("Normal");
                                    iV_status.setImageResource(R.drawable.status_normal);
                                } else if (totalHasil < 10) {
                                    tV_angka.setText("Score " + totalHasil);
                                    tV_hasil.setText("Ringan");
                                    iV_status.setImageResource(R.drawable.status_ringan);
                                } else if (totalHasil < 15) {
                                    tV_angka.setText("Score " + totalHasil);
                                    tV_hasil.setText("Sedang");
                                    iV_status.setImageResource(R.drawable.status_sedang);
                                } else if (totalHasil < 20) {
                                    tV_angka.setText("Score " + totalHasil);
                                    tV_hasil.setText("Berat");
                                    iV_status.setImageResource(R.drawable.status_besar);
                                } else {
                                    tV_angka.setText("Score " + totalHasil);
                                    tV_hasil.setText("Sangat Berat");
                                    iV_status.setImageResource(R.drawable.status_sangat);
                                }
                                SmokerTipsList[] myListData = new SmokerTipsList[] {
                                        new SmokerTipsList("Menulis Jurnal",R.drawable.nmbr_one,R.drawable.cemas_satu),
                                        new SmokerTipsList("Mencari Dukungan",R.drawable.nmbr_two,R.drawable.cemas_dua),
                                        new SmokerTipsList("Melakukan Relaksasi",R.drawable.nmbr_three,R.drawable.cemas_tiga),
                                        new SmokerTipsList("Membuat Prioritas",R.drawable.nmbr_four,R.drawable.cemas_empat),
                                        new SmokerTipsList("Melakukan Olahraga",R.drawable.nmbr_five,R.drawable.cemas_lima),
                                        new SmokerTipsList("Menanamkan Pikiran Positif",R.drawable.nmbr_six,R.drawable.cemas_enam),};
                                InformasiMenjauhiRokokAdapter adapter = new InformasiMenjauhiRokokAdapter(myListData);
                                rV_tips.setHasFixedSize(true);
                                rV_tips.setLayoutManager(new LinearLayoutManager(HasilStressActivity.this));
                                rV_tips.setAdapter(adapter);

                            }
                        }
                    }
                }
            });
        }else if (status.equals("depresi")){
            DocumentReference checkdata = fStore.collection("depresi").document(id);

            checkdata.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot != null){
                        if (documentSnapshot.exists()) {
                            Map<String, Object> data = documentSnapshot.getData();
                            if (data != null) {
                                int totalHasil = calculateTotalHasil(data);
                                // Gunakan totalHasil sesuai kebutuhan Anda, misalnya menampilkan di UI
                                Log.d("Score", "Score " + totalHasil);

                                if (totalHasil < 10) {
                                    tV_angka.setText("Score " + totalHasil);
                                    tV_hasil.setText("Normal");
                                    iV_status.setImageResource(R.drawable.status_normal);
                                } else if (totalHasil < 14) {
                                    tV_angka.setText("Score " + totalHasil);
                                    tV_hasil.setText("Ringan");
                                    iV_status.setImageResource(R.drawable.status_ringan);
                                } else if (totalHasil < 21) {
                                    tV_angka.setText("Score " + totalHasil);
                                    tV_hasil.setText("Sedang");
                                    iV_status.setImageResource(R.drawable.status_sedang);
                                } else if (totalHasil < 28) {
                                    tV_angka.setText("Score " + totalHasil);
                                    tV_hasil.setText("Berat");
                                    iV_status.setImageResource(R.drawable.status_besar);
                                } else {
                                    tV_angka.setText("Score " + totalHasil);
                                    tV_hasil.setText("Sangat Berat");
                                    iV_status.setImageResource(R.drawable.status_sangat);
                                }

                                SmokerTipsList[] myListData = new SmokerTipsList[] {
                                        new SmokerTipsList("Lakukan relaksasi untuk mengatasi stres, misalnya yoga atau pilates.",R.drawable.nmbr_one,R.drawable.depresi_satu),
                                        new SmokerTipsList("Cukupi kebutuhan tidur, minimal selama 8 jam per hari.",R.drawable.nmbr_two,R.drawable.depresi_dua),
                                        new SmokerTipsList("Hindari konsumsi minuman beralkohol.",R.drawable.nmbr_three,R.drawable.depresi_tiga),
                                        new SmokerTipsList("Lakukan olahraga secara teratur.",R.drawable.nmbr_four,R.drawable.depresi_empat),
                                        new SmokerTipsList("Pastikan untuk berkumpul dengan teman atau keluarga pada waktu luang.",R.drawable.nmbr_five,R.drawable.depresi_lima),
                                        new SmokerTipsList("Batasi penggunaan sosial media jika dirasa mengganggu.",R.drawable.nmbr_six,R.drawable.depresi_enam),
                                        new SmokerTipsList("Jauhi orang yang membawa pengaruh buruk.",R.drawable.nmbr_seven,R.drawable.depresi_tujuh),
                                        new SmokerTipsList("Lakukan pengobatan dan kontrol rutin terhadap penyakit kronis yang berisiko menyebabkan depresi",R.drawable.nmbr_eight,R.drawable.depresi_delapan),
                                        new SmokerTipsList("Konsultasikan dengan dokter jika merasakan sedih yang berkepanjangan,terutama setelah mengalami kejadian yang tidak menyenangkan.",R.drawable.nmbrnine,R.drawable.depresi_sembilan)

                                };
                                InformasiMenjauhiRokokAdapter adapter = new InformasiMenjauhiRokokAdapter(myListData);
                                rV_tips.setHasFixedSize(true);
                                rV_tips.setLayoutManager(new LinearLayoutManager(HasilStressActivity.this));
                                rV_tips.setAdapter(adapter);

                            }
                        }
                    }
                }
            });
        }




    }

    // Fungsi untuk menghitung total hasil
    private int calculateTotalHasil(Map<String, Object> data) {
        int totalHasil = 0;

        for (int i = 1; i <= 14; i++) {
            String questKey = "quest" + i;
            String answer = (String) data.get(questKey);

            if (answer != null) {
                int hasil = 0;
                switch (answer) {
                    case "Tidak sesuai/tidak pernah":
                        hasil = 0;
                        break;
                    case "Perilaku muncul sesekali/jarang":
                        hasil = 1;
                        break;
                    case "Sesuai/cukup sering muncul":
                        hasil = 2;
                        break;
                    case "Sangat sesuai/sangat sering muncul":
                        hasil = 3;
                        break;
                    default:
                        hasil = 0; // Jika jawaban tidak dikenali, anggap nilai 0
                        break;
                }
                totalHasil += hasil;
            }
        }
        return totalHasil;

    }
}