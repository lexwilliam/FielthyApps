package example.com.fielthyapps.Feature.Stress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import example.com.fielthyapps.R;

public class SectionTigaActivity extends AppCompatActivity {
    private RecyclerView rV_ketiga;
    private Button btn_lanjut;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore fStore;
    String formattedDate;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_tiga);
        rV_ketiga = findViewById(R.id.rV_quest_ketiga);
        btn_lanjut = findViewById(R.id.btn_lanjut_hasil_sec_three);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
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

        List<QuestList> questions = new ArrayList<>();
        questions.add(new QuestList("Saya kesulitan untuk berpikir positif", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya merasa berat untuk memulai sesuatu", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya merasa tidak ada yang dapat diharapkan", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya merasa sedih dan tertekan", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya merasa kehilangan minat apapun", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya merasa tidak berarti/bernilai", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya merasa hidup saya tidak berguna", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya sulit menikmati kegiatan saya", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya sering merasa murung", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya tidak dapat antusias atas apapun", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya merasa tidak berharga", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya merasa masa depan saya suram", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya merasa hidup saya tak bermakna", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya merasa kesulitan untuk berinisiatif", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));

        QuestSectionsatuAdapter adapter = new QuestSectionsatuAdapter(questions);
        rV_ketiga.setHasFixedSize(true);
        rV_ketiga.setLayoutManager(new LinearLayoutManager(this));
        rV_ketiga.setAdapter(adapter);

        Intent iin = getIntent();
        final Bundle b = iin.getExtras();

        if (b != null) {
            id = (String) b.get("id");
        }
        btn_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = firebaseUser.getUid();
                DocumentReference documentReference = fStore.collection("depresi").document(id);
                Map<String, Object> answers = new HashMap<>();
                String id_doc = documentReference.getId();
                answers.put("uid", uid);
                answers.put("id", id_doc);
                answers.put("date", formattedDate);
                for (int i = 0; i < questions.size(); i++) {
                    QuestList question = questions.get(i);
                    int selectedOption = question.getSelectedOption();
                    if (selectedOption != -1) {
                        String selectedOptionText = question.getOptions().get(selectedOption);
                        answers.put("quest" + (i + 1), selectedOptionText);
                    } else {
                        answers.put("quest" + (i + 1), "Tidak ada jawaban dipilih");
                    }
                }

                documentReference.set(answers)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                DocumentReference updatestress = fStore.collection("stresstest").document(id);
                                Map<String, Object> data = new HashMap<>();
                                data.put("cemas", "1");
                                updatestress.update(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(SectionTigaActivity.this, "Berhasil input data Cemas", Toast.LENGTH_SHORT).show();
                                                Log.d("Firestore", "Data berhasil disimpan.");
                                                Intent intent = new Intent(SectionTigaActivity.this,HasilStressActivity.class);
                                                intent.putExtra("status","depresi");
                                                intent.putExtra("id",id);
                                                intent.putExtra("type","test");
                                                startActivity(intent);
                                            }
                                        });

                                // Tampilkan pesan berhasil atau lakukan sesuatu
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Firestore", "Gagal menyimpan data", e);
                                // Tampilkan pesan gagal atau lakukan sesuatu
                            }
                        });


//                for (int i = 0; i < myListData.length; i++) {
//                    QuestList quest = myListData[i];
//                    int selectedOption = quest.getSelectedOption();
//                    if (selectedOption != -1) {
//                        // Jawaban dipilih untuk pertanyaan ke-i
//                        // Lakukan sesuatu dengan nilai selectedOption
//                        Log.d("Jawaban dipilih", "Pertanyaan ke-" + i + ": Option " + selectedOption);
//                    } else {
//                        // Tidak ada jawaban dipilih untuk pertanyaan ke-i
//                        Log.d("Jawaban dipilih", "Pertanyaan ke-" + i + ": Tidak ada jawaban dipilih");
//                    }
//                }
            }
        });
    }
}